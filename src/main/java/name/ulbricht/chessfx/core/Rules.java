package name.ulbricht.chessfx.core;

import java.util.*;
import java.util.stream.Collectors;

final class Rules {

    private final Board board;
    private final Player player;
    private final Map<Coordinate, List<Move>> legalMoves;

    Rules(Board board, Player player) {
        this.board = board;
        this.player = player;

        this.legalMoves = findLegalMoves();
    }

    Map<Coordinate, List<Move>> getLegalMoves() {
        return this.legalMoves;
    }

    private Map<Coordinate, List<Move>> findLegalMoves() {
        Map<Coordinate, List<Move>> legalMoves = new TreeMap<>();

        List<Square> legalSquares = this.board.squares()
                .filter(square -> !square.isEmpty())
                .filter(square -> square.getPiece().getPlayer() == this.player)
                .collect(Collectors.toList());

        for (Square fromSquare : legalSquares) {
            List<Move> moves = findLegalMoves(fromSquare);
            if (!moves.isEmpty()) {
                legalMoves.put(fromSquare.getCoordinate(), moves);
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

    private List<Move> findLegalMoves(Square fromSquare) {
        switch (fromSquare.getPiece().getType()) {
            case PAWN:
                return findPawnMoves(fromSquare);
            case ROOK:
                return findDirectionalMoves(fromSquare, Integer.MAX_VALUE,
                        Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
            case KNIGHT:
                return findKnightMoves(fromSquare);
            case BISHOP:
                return findDirectionalMoves(fromSquare, Integer.MAX_VALUE,
                        Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT);
            case QUEEN:
                return findDirectionalMoves(fromSquare, Integer.MAX_VALUE, Direction.values());
            case KING:
                return findDirectionalMoves(fromSquare, 1, Direction.values());
            default:
                throw new IllegalArgumentException("Unexpected piece type " + fromSquare.getPiece().getType());
        }
    }

    private List<Move> findPawnMoves(Square fromSquare) {
        checkValidPiece(fromSquare, Piece.Type.PAWN);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();
        int direction = this.player == Player.WHITE ? 1 : -1;

        // one step forward
        Optional<Coordinate> to = from.moveTo(0, direction);
        if (to.isPresent()) {
            Square toSquare = this.board.getSquare(to.get());
            if (toSquare.isEmpty()) moves.add(new Move(from, to.get(), null));

            // two steps forward (if not yet moved)
            if (fromSquare.getPiece().getMoveCount() == 0 && toSquare.isEmpty()) {
                to = from.moveTo(0, 2 * direction);
                if (to.isPresent()) {
                    toSquare = this.board.getSquare(to.get());
                    if (toSquare.isEmpty()) moves.add(new Move(from, to.get(), null));
                }
            }
        }

        return moves;
    }

    private List<Move> findKnightMoves(Square fromSquare) {
        checkValidPiece(fromSquare, Piece.Type.KNIGHT);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();

        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Optional<Coordinate> to = from.moveTo(jump[0], jump[1]);
            if (to.isPresent()) {
                Square toSquare = this.board.getSquare(to.get());
                if (toSquare.isEmpty() || toSquare.getPiece().getPlayer() != this.player)
                    moves.add(new Move(from, to.get(), toSquare.isEmpty() ? null : to.get()));
            }
        }

        return moves;
    }

    private List<Move> findDirectionalMoves(Square fromSquare, int maxSteps, Direction... directions) {
        checkValidPiece(fromSquare, Piece.Type.ROOK, Piece.Type.BISHOP, Piece.Type.QUEEN, Piece.Type.KING);

        List<Move> moves = new ArrayList<>();
        Coordinate from = fromSquare.getCoordinate();

        for (Direction direction : directions) {
            Optional<Coordinate> to;
            int step = 1;
            do {
                to = from.moveTo(step * direction.getColumnOffset(), step * direction.getRowOffset());
                if (to.isPresent()) {
                    Square toSquare = this.board.getSquare(to.get());
                    Piece piece = toSquare.getPiece();
                    if (piece == null) moves.add(new Move(from, to.get(), null));
                    else if (piece.getPlayer() != this.player) {
                        moves.add(new Move(from, to.get(), to.get()));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && to.isPresent());
        }

        return moves;
    }

    private void checkValidPiece(Square square, Piece.Type... pieceTypes) {
        if (square.isEmpty()) throw new IllegalArgumentException("square cannot be empty");
        Piece piece = square.getPiece();

        if (piece.getPlayer() != this.player) throw new IllegalArgumentException("player mismatch");

        if (!Arrays.asList(pieceTypes).contains(piece.getType()))
            throw new IllegalArgumentException("Cannot handle piece type " + piece.getType());
    }
}
