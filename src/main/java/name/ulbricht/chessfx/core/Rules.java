package name.ulbricht.chessfx.core;

import java.util.*;

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

        for (Map.Entry<Coordinate, Piece> entry : this.board.pieces().entrySet()) {
            Coordinate coordinate = entry.getKey();
            Piece piece = entry.getValue();

            if (piece.getPlayer() == this.player) {
                List<Move> moves = findLegalMoves(coordinate);
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

    private List<Move> findLegalMoves(Coordinate from) {
        Optional<Piece> piece = this.board.getPiece(from);
        if (piece.isPresent()) {
            switch (piece.get().getType()) {
                case PAWN:
                    return findPawnMoves(from);
                case ROOK:
                    return findDirectionalMoves(from, Integer.MAX_VALUE,
                            Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
                case KNIGHT:
                    return findKnightMoves(from);
                case BISHOP:
                    return findDirectionalMoves(from, Integer.MAX_VALUE,
                            Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT);
                case QUEEN:
                    return findDirectionalMoves(from, Integer.MAX_VALUE, Direction.values());
                case KING:
                    return findDirectionalMoves(from, 1, Direction.values());
            }
        }
        return Collections.emptyList();
    }

    private List<Move> findPawnMoves(Coordinate from) {
        checkValidPiece(from, Piece.Type.PAWN);
        List<Move> moves = new ArrayList<>();
        int direction = this.player == Player.WHITE ? 1 : -1;

        // one step forward
        Optional<Coordinate> to = from.moveTo(0, direction);
        if (to.isPresent()) {
            if (!this.board.getPiece(to.get()).isPresent()) moves.add(Move.simple(this.board, from, to.get()));

            // two steps forward (if not yet moved)
            Piece me = this.board.getPiece(from).orElseThrow(() -> new IllegalStateException("piece expected"));
            if (me.getMoveCount() == 0 && !this.board.getPiece(to.get()).isPresent()) {
                to = from.moveTo(0, 2 * direction);
                if (to.isPresent()) {
                    if (!this.board.getPiece(to.get()).isPresent())
                        moves.add(Move.simple(this.board, from, to.get()));
                }
            }
        }

        return moves;
    }

    private List<Move> findKnightMoves(Coordinate from) {
        checkValidPiece(from, Piece.Type.KNIGHT);
        List<Move> moves = new ArrayList<>();

        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Optional<Coordinate> to = from.moveTo(jump[0], jump[1]);
            if (to.isPresent()) {
                Optional<Piece> piece = this.board.getPiece(to.get());
                if (!piece.isPresent()) moves.add(Move.simple(this.board, from, to.get()));
                else if (piece.get().getPlayer().isOpponent(this.player))
                    moves.add(Move.simple(this.board, from, to.get()));
            }
        }
        return moves;
    }

    private List<Move> findDirectionalMoves(Coordinate from, int maxSteps, Direction... directions) {
        checkValidPiece(from, Piece.Type.ROOK, Piece.Type.BISHOP, Piece.Type.QUEEN, Piece.Type.KING);
        List<Move> moves = new ArrayList<>();

        for (Direction direction : directions) {
            Optional<Coordinate> to;
            int step = 1;
            do {
                to = from.moveTo(step * direction.getColumnOffset(), step * direction.getRowOffset());
                if (to.isPresent()) {
                    Optional<Piece> piece = this.board.getPiece(to.get());
                    if (!piece.isPresent()) moves.add(Move.simple(this.board, from, to.get()));
                    else if (piece.get().getPlayer().isOpponent(this.player)) {
                        moves.add(Move.simple(this.board, from, to.get()));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && to.isPresent());
        }

        return moves;
    }

    private void checkValidPiece(Coordinate coordinate, Piece.Type... pieceTypes) {
        Piece piece = this.board.getPiece(coordinate).orElseThrow(() -> new IllegalArgumentException("square cannot be empty"));
        if (piece.getPlayer().isOpponent(this.player)) throw new IllegalArgumentException("player mismatch");

        if (!Arrays.asList(pieceTypes).contains(piece.getType()))
            throw new IllegalArgumentException("Cannot handle piece type " + piece.getType());
    }
}
