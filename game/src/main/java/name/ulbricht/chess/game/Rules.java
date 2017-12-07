package name.ulbricht.chess.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Rules {

    public static final List<PieceType> promotionPieceTypes = List.of(PieceType.QUEEN, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK);

    public static int baseRowIndex(Player player) {
        return Objects.requireNonNull(player, "player cannot be null") == Player.WHITE
                ? 0
                : Coordinate.ROWS - 1;
    }

    public static Coordinate initialKingCoordinate(Player player) {
        return Objects.requireNonNull(player, "player cannot be null") == Player.WHITE
                ? Coordinate.e1
                : Coordinate.e8;
    }

    public static Coordinate initialKingSideRookCoordinate(Player player) {
        return Objects.requireNonNull(player, "player cannot be null") == Player.WHITE
                ? Coordinate.h1
                : Coordinate.h8;
    }

    public static Coordinate initialQueenSideRookCoordinate(Player player) {
        return Objects.requireNonNull(player, "player cannot be null") == Player.WHITE
                ? Coordinate.a1
                : Coordinate.a8;
    }

    static List<Coordinate> attacks(Board board, Player attacker) {
        List<Coordinate> squares = new ArrayList<>();

        for (Coordinate source : Coordinate.values()) {
            Piece piece = board.getPiece(source);
            if (piece != null && piece.player == attacker) {
                switch (piece.type) {
                    case QUEEN:
                        squares.addAll(attacks(board, source, Integer.MAX_VALUE, MoveDirection.values()));
                        break;
                    case KING:
                        squares.addAll(attacks(board, source, 1, MoveDirection.values()));
                        break;
                    case ROOK:
                        squares.addAll(attacks(board, source, Integer.MAX_VALUE,
                                MoveDirection.UP, MoveDirection.RIGHT, MoveDirection.DOWN, MoveDirection.LEFT));
                        break;
                    case BISHOP:
                        squares.addAll(attacks(board, source, Integer.MAX_VALUE,
                                MoveDirection.UP_LEFT, MoveDirection.UP_RIGHT, MoveDirection.DOWN_RIGHT, MoveDirection.DOWN_LEFT));
                        break;
                    case KNIGHT:
                        squares.addAll(attacks(board, source, 1, KnightJump.values()));
                        break;
                    case PAWN:
                        squares.addAll(attacks(board, source, 1, MoveDirection.forwardLeft(piece.player), MoveDirection.forwardRight(piece.player)));
                        break;
                }
            }
        }

        return squares;
    }

    private static List<Coordinate> attacks(Board board, Coordinate source, int steps, Direction... directions) {
        Piece piece = board.getPiece(source);
        if (piece == null) return Collections.emptyList();

        List<Coordinate> squares = new ArrayList<>();
        for (Direction direction : directions) {
            for (int step = 1; step <= steps; step++) {
                Coordinate target = source.go(direction, step);
                if (target == null) break;
                Piece targetPiece = board.getPiece(target);
                if (targetPiece == null) {
                    squares.add(target);
                } else if (targetPiece.player.isOpponent(piece.player)) {
                    squares.add(target);
                    break;
                } else {
                    break;
                }
            }
        }
        return squares;
    }

    static List<Ply> plies(Board board, Coordinate source, int steps, Direction... directions) {
        Piece piece = board.getPiece(source);
        if (piece == null) return Collections.emptyList();

        List<Ply> plies = new ArrayList<>();
        for (Direction direction : directions) {
            for (int step = 1; step <= steps; step++) {
                Coordinate target = source.go(direction, step);
                if (target == null) break;
                Piece targetPiece = board.getPiece(target);
                if (targetPiece == null) {
                    plies.add(Ply.move(piece, source, target));
                } else if (targetPiece.player.isOpponent(piece.player)) {
                    plies.add(Ply.moveAndCaptures(piece, source, target, targetPiece));
                    break;
                } else {
                    break;
                }
            }
        }
        return plies;
    }

    static List<Ply> pawnPlies(Board board, Coordinate source, Coordinate enPassantTarget) {
        Piece piece = board.getPiece(source);
        if (piece == null) return Collections.emptyList();

        List<Ply> plies = new ArrayList<>();
        Direction direction = MoveDirection.forward(piece.player);
        int startRow = piece.player == Player.WHITE ? 1 : 6;
        int promotionRow = baseRowIndex(piece.player.opponent());

        // first field in move direction
        Coordinate target = source.go(direction);
        if (target != null && board.isEmpty(target)) {
            if (target.rowIndex == promotionRow) {
                plies.add(Ply.pawnPromotion(piece, source, target));
            } else {
                plies.add(Ply.move(piece, source, target));
            }

            // double advance in move direction
            if (source.rowIndex == startRow) {
                target = source.go(direction, 2);
                if (target != null && board.isEmpty(target)) {
                    plies.add(Ply.pawnDoubleAdvance(piece, source));
                }
            }
        }

        // capturing forward left and forward right in move direction
        for (Direction captureDirection : new Direction[]{
                MoveDirection.forwardLeft(piece.player), MoveDirection.forwardRight(piece.player)}) {
            target = source.go(captureDirection);
            if (target != null) {
                Piece capturedPiece = board.getPiece(target);
                if (capturedPiece != null && capturedPiece.player.isOpponent(piece.player)) {
                    if (target.rowIndex == promotionRow) {
                        plies.add(Ply.pawnPromotionAndCaptures(piece, source, target, capturedPiece));
                    } else {
                        plies.add(Ply.moveAndCaptures(piece, source, target, capturedPiece));
                    }
                }
                if (target == enPassantTarget) {
                    Coordinate captures = Coordinate.valueOf(target.columnIndex, piece.player == Player.WHITE ? 4 : 3);
                    capturedPiece = board.getPiece(captures);
                    if (capturedPiece.type == PieceType.PAWN && capturedPiece.player.isOpponent(piece.player)) {
                        plies.add(Ply.pawnEnPassant(piece, source, target));
                    }
                }
            }
        }

        return plies;
    }

    static Ply kingSideCastlingPly(Board board, Coordinate source, List<Coordinate> attacked) {
        Piece piece = board.getPiece(source);
        if (piece == null) return null;

        // piece must be a king
        if (piece.type != PieceType.KING)
            throw new IllegalArgumentException("Castling not allowed for " + piece.type);

        // king must be on his initial position
        if (source != initialKingCoordinate(piece.player))
            throw new IllegalArgumentException("Not a valid source " + source);

        // king cannot be in check
        if (attacked.contains(source)) return null;

        // rook must be on its initial position
        Coordinate rookSource = initialKingSideRookCoordinate(piece.player);
        if (board.getPiece(rookSource) != Piece.valueOf(PieceType.ROOK, piece.player)) return null;

        // no piece between king and rook
        Coordinate square = source.go(MoveDirection.RIGHT);
        while (square != rookSource) {
            if (board.getPiece(square) != null) return null;
            square = square.go(MoveDirection.RIGHT);
        }

        // way and target of the king cannot be in check
        if (attacked.contains(source.go(MoveDirection.RIGHT))) return null;
        if (attacked.contains(source.go(MoveDirection.RIGHT, 2))) return null;

        return Ply.kingSideCastling(piece);
    }

    static Ply queenSideCastlingPly(Board board, Coordinate source, List<Coordinate> attacked) {
        Piece piece = board.getPiece(source);
        if (piece == null) return null;

        // piece must be a king
        if (piece.type != PieceType.KING)
            throw new IllegalArgumentException("Castling not allowed for " + piece.type);

        // king must be on his initial position
        if (source != initialKingCoordinate(piece.player))
            throw new IllegalArgumentException("Not a valid source " + source);

        // king cannot be in check
        if (attacked.contains(source)) return null;

        // rook must be on its initial position
        Coordinate rookSource = initialQueenSideRookCoordinate(piece.player);
        if (board.getPiece(rookSource) != Piece.valueOf(PieceType.ROOK, piece.player)) return null;

        // no piece between king and rook
        Coordinate square = source.go(MoveDirection.LEFT);
        while (square != rookSource) {
            if (board.getPiece(square) != null) return null;
            square = square.go(MoveDirection.LEFT);
        }

        // way and target of the king cannot be in check
        if (attacked.contains(source.go(MoveDirection.LEFT))) return null;
        if (attacked.contains(source.go(MoveDirection.LEFT, 2))) return null;

        return Ply.queenSideCastling(piece);
    }

    static void performPly(Board board, Ply ply) {
        switch (ply.type) {
            case MOVE:
                if (ply.captures != null) board.setPiece(ply.captures, null);
                move(board, ply.source, ply.target);
                break;
            case PAWN_DOUBLE_ADVANCE:
                move(board, ply.source, ply.target);
                break;
            case PAWN_EN_PASSANT:
                board.setPiece(ply.captures, null);
                move(board, ply.source, ply.target);
                break;
            case PAWN_PROMOTION:
                Piece promotionPiece = Piece.valueOf(
                        ply.promotion != null ? ply.promotion : PieceType.QUEEN, ply.piece.player);
                if (!promotionPieceTypes.contains(promotionPiece.type))
                    throw new IllegalStateException("Not a valid promotion piece: " + promotionPiece);

                if (ply.captures != null) board.setPiece(ply.captures, null);
                board.setPiece(ply.source, null);
                board.setPiece(ply.target, promotionPiece);
                break;
            case KING_SIDE_CASTLING: {
                move(board, ply.source, ply.target);
                int row = baseRowIndex(ply.piece.player);
                move(board,
                        Coordinate.valueOf(Coordinate.COLUMNS - 1, row),
                        ply.source.go(MoveDirection.RIGHT));
            }
            break;
            case QUEEN_SIDE_CASTLING: {
                move(board, ply.source, ply.target);
                int row = baseRowIndex(ply.piece.player);
                move(board,
                        Coordinate.valueOf(0, row),
                        ply.source.go(MoveDirection.LEFT));
            }
            break;
            default:
                throw new IllegalArgumentException("Unsupported ply type: " + ply.type);
        }
    }

    private static void move(Board board, Coordinate source, Coordinate target) {
        Piece piece = board.getPiece(source);
        board.setPiece(source, null);
        board.setPiece(target, piece);
    }
}
