package name.ulbricht.chess.game;

import java.util.*;

final class Rules {

    static Map<Coordinate, List<Move>> findLegalMoves(Game game) {
        Map<Coordinate, List<Move>> legalMoves = new TreeMap<>();

        for (Map.Entry<Coordinate, Piece> entry : game.pieces().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Piece piece = entry.getValue();

            if (piece.getPlayer() == game.getCurrentPlayer()) {
                List<Move> moves = findLegalMoves(game, coordinate);
                if (!moves.isEmpty()) {
                    legalMoves.put(coordinate, moves);
                }
            }
        }
        return legalMoves;
    }

    private enum Direction {
        UP(0, 1), UP_RIGHT(1, 1), RIGHT(1, 0), DOWN_RIGHT(1, -1), DOWN(0, -1), DOWN_LEFT(-1, -1), LEFT(-1, 0), UP_LEFT(-1, 1);

        private final int columnOffset;
        private final int rowOffset;

        Direction(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }

        private int getColumnOffset() {
            return this.columnOffset;
        }

        private int getRowOffset() {
            return this.rowOffset;
        }
    }

    private static List<Move> findLegalMoves(Game game, Coordinate source) {
        Piece piece = game.getPiece(source);
        if (piece != null) {
            switch (piece.getType()) {
                case PAWN:
                    return findPawnMoves(game, source);
                case ROOK:
                    return findDirectionalMoves(game, source, Integer.MAX_VALUE,
                            Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
                case KNIGHT:
                    return findKnightMoves(game, source);
                case BISHOP:
                    return findDirectionalMoves(game, source, Integer.MAX_VALUE,
                            Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT);
                case QUEEN:
                    return findDirectionalMoves(game, source, Integer.MAX_VALUE, Direction.values());
                case KING:
                    return findDirectionalMoves(game, source, 1, Direction.values());
            }
        }
        return Collections.emptyList();
    }

    private static List<Move> findPawnMoves(Game game, Coordinate source) {
        checkValidPiece(game, source, PieceType.PAWN);
        List<Move> moves = new ArrayList<>();
        int direction = game.getCurrentPlayer() == Player.WHITE ? 1 : -1;

        // one step forward
        Coordinate target = source.moveTo(0, direction);
        if (target != null) {
            if (game.getPiece(target) == null) moves.add(Move.simple(game, source, target));

            // two steps forward (if not yet moved)
            Piece me = game.getPiece(source);
            if (!me.isMoved() && game.getPiece(target) == null) {
                target = source.moveTo(0, 2 * direction);
                if (target != null) {
                    if (game.getPiece(target) == null)
                        moves.add(Move.simple(game, source, target));
                }
            }
        }

        // check capture
        int[][] captures = new int[][]{{-1, direction}, {1, direction}};
        for (int[] capture : captures) {
            target = source.moveTo(capture[0], capture[1]);
            if (target != null) {
                Piece piece = game.getPiece(target);
                if (piece != null && piece.getPlayer().isOpponent(game.getCurrentPlayer()))
                    moves.add(Move.simple(game, source, target));
            }
        }

        return moves;
    }

    private static List<Move> findKnightMoves(Game game, Coordinate source) {
        checkValidPiece(game, source, PieceType.KNIGHT);
        List<Move> moves = new ArrayList<>();

        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Coordinate target = source.moveTo(jump[0], jump[1]);
            if (target != null) {
                Piece piece = game.getPiece(target);
                if (piece == null || piece.getPlayer().isOpponent(game.getCurrentPlayer()))
                    moves.add(Move.simple(game, source, target));
            }
        }
        return moves;
    }

    private static List<Move> findDirectionalMoves(Game game, Coordinate source, int maxSteps, Direction... directions) {
        checkValidPiece(game, source, PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING);
        List<Move> moves = new ArrayList<>();

        for (Direction direction : directions) {
            Coordinate target;
            int step = 1;
            do {
                target = source.moveTo(step * direction.getColumnOffset(), step * direction.getRowOffset());
                if (target != null) {
                    Piece piece = game.getPiece(target);
                    if (piece == null) moves.add(Move.simple(game, source, target));
                    else if (piece.getPlayer().isOpponent(game.getCurrentPlayer())) {
                        moves.add(Move.simple(game, source, target));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && target != null);
        }

        return moves;
    }

    private static void checkValidPiece(Game game, Coordinate coordinate, PieceType... pieceTypes) {
        Piece piece = game.getPiece(coordinate);
        if (piece.getPlayer().isOpponent(game.getCurrentPlayer()))
            throw new IllegalArgumentException("player mismatch");

        if (!Arrays.asList(pieceTypes).contains(piece.getType()))
            throw new IllegalArgumentException("Cannot handle piece type " + piece.getType());
    }
}
