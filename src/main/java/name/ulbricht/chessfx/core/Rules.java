package name.ulbricht.chessfx.core;

import java.util.*;
import java.util.stream.Collectors;

final class Rules {

    static Map<Square, List<Move>> findLegalMoves(Board board, Player player) {
        Map<Square, List<Move>> legalMoves = new TreeMap<>();

        List<Square> legalSquares = board.squares()
                .filter(square -> !square.isEmpty())
                .filter(square -> square.getPiece().getPlayer() == player)
                .collect(Collectors.toList());

        for (Square fromSquare : legalSquares) {
            List<Move> moves = findLegalMoves(board, player, fromSquare);
            if (!moves.isEmpty()) {
                legalMoves.put(fromSquare, moves);
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

    private static List<Move> findLegalMoves(Board board, Player player, Square fromSquare) {
        switch (fromSquare.getPiece().getType()) {
            case PAWN:
                return findPawnMoves(board, player, fromSquare);
            case ROOK:
                return findDirectionalMoves(board, player, fromSquare, Integer.MAX_VALUE,
                        Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
            case KNIGHT:
                return findKnightMoves(board, player, fromSquare);
            case BISHOP:
                return findDirectionalMoves(board, player, fromSquare, Integer.MAX_VALUE,
                        Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT);
            case QUEEN:
                return findDirectionalMoves(board, player, fromSquare, Integer.MAX_VALUE, Direction.values());
            case KING:
                return findDirectionalMoves(board, player, fromSquare, 1, Direction.values());
            default:
                throw new IllegalArgumentException("Unexpected piece type " + fromSquare.getPiece().getType());
        }
    }

    private static List<Move> findPawnMoves(Board board, Player player, Square fromSquare) {
        checkValidPiece(fromSquare, player, Piece.Type.PAWN);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();
        int direction = player == Player.WHITE ? 1 : -1;

        // one step forward
        Optional<Coordinate> to = from.moveTo(0, direction);
        if (to.isPresent()) {
            Square toSquare = board.getSquare(to.get());
            if (toSquare.isEmpty()) moves.add(new Move(fromSquare, toSquare));
        }

        // two steps forward (if not yet moved)
        if (fromSquare.getPiece().getMoveCount() == 0) {
            to = from.moveTo(0, 2 * direction);
            if (to.isPresent()) {
                Square toSquare = board.getSquare(to.get());
                if (toSquare.isEmpty()) moves.add(new Move(fromSquare, toSquare));
            }
        }

        return moves;
    }

    private static List<Move> findKnightMoves(Board board, Player player, Square fromSquare) {
        checkValidPiece(fromSquare, player, Piece.Type.KNIGHT);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();

        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Optional<Coordinate> to = from.moveTo(jump[0], jump[1]);
            if (to.isPresent()) {
                Square toSquare = board.getSquare(to.get());
                if (toSquare.isEmpty() || toSquare.getPiece().getPlayer() != player)
                    moves.add(new Move(fromSquare, toSquare));
            }
        }

        return moves;
    }

    private static List<Move> findDirectionalMoves(Board board, Player player, Square fromSquare, int maxSteps, Direction... directions) {
        checkValidPiece(fromSquare, player, Piece.Type.ROOK, Piece.Type.BISHOP, Piece.Type.QUEEN, Piece.Type.KING);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();

        for (Direction direction : directions) {
            Optional<Coordinate> to;
            int step = 1;
            do {
                to = from.moveTo(step * direction.getColumnOffset(), step * direction.getRowOffset());
                if (to.isPresent()) {
                    Square toSquare = board.getSquare(to.get());
                    Piece piece = toSquare.getPiece();
                    if (piece == null) moves.add(new Move(fromSquare, toSquare));
                    else if (piece.getPlayer() != player){
                        moves.add(new Move(fromSquare, toSquare));
                        break;
                    }
                    else break;
                }
                step++;
            } while (step <= maxSteps && to.isPresent());
        }

        return moves;
    }

    private static void checkValidPiece(Square square, Player player, Piece.Type... pieceTypes) {
        if (square.isEmpty()) throw new IllegalArgumentException("square cannot be empty");
        Piece piece = square.getPiece();

        if (piece.getPlayer() != player) throw new IllegalArgumentException("player mismatch");

        if (!Arrays.asList(pieceTypes).contains(piece.getType()))
            throw new IllegalArgumentException("Cannot handle piece type " + piece.getType());
    }
}
