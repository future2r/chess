package name.ulbricht.chess.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class Rules {

    static Coordinate king(Piece[] board, Player player) {
        Piece king = player == Player.WHITE ? Piece.WHITE_KING : Piece.BLACK_KING;
        for (Coordinate coordinate : Coordinate.values()) {
            if (board[coordinate.ordinal()] == king) return coordinate;
        }
        throw new IllegalStateException("Piece not found: " + king);
    }

    static List<Coordinate> attacks(Piece[] board, Player attacker) {
        List<Coordinate> squares = new ArrayList<>();

        for (Coordinate source : Coordinate.values()) {
            Piece piece = board[source.ordinal()];
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

    private static List<Coordinate> attacks(Piece[] board, Coordinate source, int steps, Direction... directions) {
        List<Coordinate> squares = new ArrayList<>();
        Piece piece = Objects.requireNonNull(board[Objects.requireNonNull(source).ordinal()]);
        for (Direction direction : directions) {
            for (int step = 1; step <= steps; step++) {
                Coordinate target = source.go(direction, step);
                if (target == null) break;
                Piece targetPiece = board[target.ordinal()];
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

    static List<Ply> plies(Piece[] board, Coordinate source, int steps, Direction... directions) {
        List<Ply> plies = new ArrayList<>();
        Piece piece = Objects.requireNonNull(board[Objects.requireNonNull(source).ordinal()]);
        for (Direction direction : directions) {
            for (int step = 1; step <= steps; step++) {
                Coordinate target = source.go(direction, step);
                if (target == null) break;
                Piece targetPiece = board[target.ordinal()];
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

    static List<Ply> pawnPlies(Piece[] board, Coordinate source, Coordinate enPassantTarget) {
        List<Ply> plies = new ArrayList<>();
        Piece piece = Objects.requireNonNull(board[Objects.requireNonNull(source).ordinal()]);

        Direction direction = MoveDirection.forward(piece.player);
        int startRow = piece.player == Player.WHITE ? 1 : 6;
        int promotionRow = piece.player == Player.WHITE ? 7 : 0;

        // first field in move direction
        Coordinate target = source.go(direction);
        if (target != null && board[target.ordinal()] == null) {
            if (target.rowIndex == promotionRow) {
                plies.add(Ply.pawnPromotion(piece, source, target));
            } else {
                plies.add(Ply.move(piece, source, target));
            }

            // double advance in move direction
            if (source.rowIndex == startRow) {
                target = source.go(direction, 2);
                if (target != null && board[target.ordinal()] == null) {
                    plies.add(Ply.pawnDoubleAdvance(piece, source));
                }
            }
        }

        // capturing forward left and forward right in move direction
        for (Direction captureDirection : new Direction[]{
                MoveDirection.forwardLeft(piece.player), MoveDirection.forwardRight(piece.player)}) {
            target = source.go(captureDirection);
            if (target != null) {
                Piece capturedPiece = board[target.ordinal()];
                if (capturedPiece != null && capturedPiece.player.isOpponent(piece.player)) {
                    if (target.rowIndex == promotionRow) {
                        plies.add(Ply.pawnPromotionAndCaptures(piece, source, target, capturedPiece));
                    } else {
                        plies.add(Ply.moveAndCaptures(piece, source, target, capturedPiece));
                    }
                }
                if (target == enPassantTarget) {
                    Coordinate captures = Coordinate.valueOf(target.columnIndex, piece.player == Player.WHITE ? 4 : 3);
                    capturedPiece = board[captures.ordinal()];
                    if (capturedPiece.type == PieceType.PAWN && capturedPiece.player.isOpponent(piece.player)) {
                        plies.add(Ply.pawnEnPassant(piece, source, target));
                    }
                }
            }
        }

        return plies;
    }

    static Ply kingSideCastlingPly(Piece[] board, Coordinate source, List<Coordinate> attacked) {
        Piece piece = Objects.requireNonNull(board[Objects.requireNonNull(source).ordinal()]);

        // king cannot be in check
        if (attacked.contains(source)) return null;

        // the row and rook depends on the player
        int row = piece.player == Player.WHITE ? 0 : 7;
        Piece rook = piece.player == Player.WHITE ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;

        // king side (none attacked)
        Coordinate empty = Coordinate.valueOf(5, row);
        if (board[empty.ordinal()] != null || attacked.contains(empty)) return null;
        Coordinate target = Coordinate.valueOf(6, row);
        if (board[target.ordinal()] != null || attacked.contains(target)) return null;
        Coordinate rookSource = Coordinate.valueOf(7, row);
        if (board[rookSource.ordinal()] != rook || attacked.contains(rookSource)) return null;
        return Ply.kingSideCastling(piece);
    }

    static Ply queenSideCastlingPly(Piece[] board, Coordinate source, List<Coordinate> attacked) {
        Piece piece = Objects.requireNonNull(board[Objects.requireNonNull(source).ordinal()]);

        // king cannot be in check
        if (attacked.contains(source)) return null;

        // the row and rook depends on the player
        int row = piece.player == Player.WHITE ? 0 : 7;
        Piece rook = piece.player == Player.WHITE ? Piece.WHITE_ROOK : Piece.BLACK_ROOK;

        // queen side (none attacked)
        Coordinate empty = Coordinate.valueOf(3, row);
        if (board[empty.ordinal()] != null || attacked.contains(empty)) return null;
        empty = Coordinate.valueOf(2, row);
        if (board[empty.ordinal()] != null || attacked.contains(empty)) return null;
        Coordinate target = Coordinate.valueOf(1, row);
        if (board[target.ordinal()] != null || attacked.contains(target)) return null;
        Coordinate rookSource = Coordinate.valueOf(0, row);
        if (board[rookSource.ordinal()] != rook || attacked.contains(rookSource)) return null;
        return Ply.queenSideCastling(piece);
    }

    static void performPly(Piece[] board, Ply ply) {
        switch (ply.type) {
            case MOVE:
                if (ply.captures != null) board[ply.captures.ordinal()] = null;
                move(board, ply.source, ply.target);
                break;
            case PAWN_DOUBLE_ADVANCE:
                move(board, ply.source, ply.target);
                break;
            case PAWN_EN_PASSANT:
                board[ply.captures.ordinal()] = null;
                move(board, ply.source, ply.target);
                break;
            case PAWN_PROMOTION:
                if (ply.captures != null) board[ply.captures.ordinal()] = null;
                board[ply.source.ordinal()] = null;
                board[ply.target.ordinal()] = Piece.valueOf(
                        ply.promotion != null ? ply.promotion : PieceType.QUEEN, ply.piece.player);
                break;
            case KING_SIDE_CASTLING: {
                move(board, ply.source, ply.target);
                int row = ply.piece.player == Player.WHITE ? 0 : 7;
                move(board, Coordinate.valueOf(7, row), Coordinate.valueOf(5, row));
            }
            break;
            case QUEEN_SIDE_CASTLING: {
                move(board, ply.source, ply.target);
                int row = ply.piece.player == Player.WHITE ? 0 : 7;
                move(board, Coordinate.valueOf(0, row), Coordinate.valueOf(2, row));
            }
            break;
            default:
                throw new IllegalArgumentException("Unsupported ply type: " + ply.type);
        }
    }

    private static void move(Piece[] board, Coordinate source, Coordinate target) {
        Piece piece = board[source.ordinal()];
        board[source.ordinal()] = null;
        board[target.ordinal()] = piece;
    }
}
