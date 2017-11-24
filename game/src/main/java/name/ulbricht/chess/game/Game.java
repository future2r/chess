package name.ulbricht.chess.game;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Piece[] board;
    private Player activePlayer;
    private final List<Move> legalMoves = new ArrayList<>();

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this.board = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
        setupDefault();
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setup(FENPositions positions) {
        Arrays.fill(this.board, null);
        for (Coordinate coordinate : Coordinate.values()) {
            setPiece(coordinate, positions.getPiece(coordinate));
        }
        this.activePlayer = positions.getActivePlayer();
        findLegalMoves();
    }

    public void setupDefault() {
        setup(FENPositions.ofDefault());
    }

    /**
     * Returns a map with all pieces on the board. The keys are the coordinates and the values are the pieces. Empty
     * square will not be returned.
     *
     * @return a map with coordinates and pieces
     */
    public Map<Coordinate, Piece> pieces() {
        return Stream.of(Coordinate.values())
                .filter(c -> this.board[c.ordinal()] != null)
                .collect(Collectors.toMap(c -> c, c -> this.board[c.ordinal()]));
    }

    /**
     * Returns the piece at the given coordinate. If the square is empty the returned value will be {@code null}.
     *
     * @param coordinate the coordinate
     * @return the piece or {@code null}
     */
    public Piece getPiece(Coordinate coordinate) {
        return this.board[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()];
    }

    private void setPiece(Coordinate coordinate, Piece piece) {
        this.board[coordinate.ordinal()] = piece;
    }

    private void movePiece(Coordinate source, Coordinate target) {
        Piece piece = getPiece(source);
        if (piece == null) throw new IllegalStateException("No piece to move");
        removePiece(source);
        setPiece(target, piece);
    }

    private void removePiece(Coordinate coordinate) {
        setPiece(coordinate, null);
    }

    /**
     * Returns a list with legal move for the current player.
     *
     * @return a list of legal moves
     */
    public List<Move> getLegalMoves() {
        return Collections.unmodifiableList(this.legalMoves);
    }

    public List<Move> getLegalMoves(Coordinate source) {
        return this.legalMoves.stream().filter(m -> m.getSource() == source).collect(Collectors.toList());
    }

    private void findLegalMoves() {
        this.legalMoves.clear();

        for (Coordinate coordinate : Coordinate.values()) {
            Piece piece = getPiece(coordinate);
            if (piece != null && piece.player == this.activePlayer) {
                this.legalMoves.addAll(findLegalMoves(coordinate));
            }
        }
    }

    /**
     * Performs the specified move in this game. The piece will be moved and the the current player is switched.
     *
     * @param move the move to perform
     */
    public void performMove(Move move) {
        if (!this.legalMoves.contains(move))
            throw new IllegalArgumentException("Not a legal move");

        switch (move.getType()) {
            case SIMPLE:
                if (move.getCaptures() != null) removePiece(move.getCaptures());
                movePiece(move.getSource(), move.getTarget());
                break;
            default:
                throw new IllegalArgumentException("Unsupported move type: " + move.getType());
        }

        this.activePlayer = this.activePlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }

    private enum Direction {
        UP(0, 1), UP_RIGHT(1, 1), RIGHT(1, 0), DOWN_RIGHT(1, -1), DOWN(0, -1), DOWN_LEFT(-1, -1), LEFT(-1, 0), UP_LEFT(-1, 1);

        final int columnOffset;
        final int rowOffset;

        Direction(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }
    }

    private List<Move> findLegalMoves(Coordinate source) {
        Piece piece = getPiece(source);
        if (piece != null) {
            switch (piece.type) {
                case PAWN:
                    return findPawnMoves(source);
                case ROOK:
                    return findDirectionalMoves(source, Integer.MAX_VALUE,
                            Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT);
                case KNIGHT:
                    return findKnightMoves(source);
                case BISHOP:
                    return findDirectionalMoves(source, Integer.MAX_VALUE,
                            Direction.UP_LEFT, Direction.UP_RIGHT, Direction.DOWN_RIGHT, Direction.DOWN_LEFT);
                case QUEEN:
                    return findDirectionalMoves(source, Integer.MAX_VALUE, Direction.values());
                case KING:
                    return findDirectionalMoves(source, 1, Direction.values());
            }
        }
        return Collections.emptyList();
    }

    private List<Move> findPawnMoves(Coordinate source) {
        expectPiece(source, this.activePlayer, PieceType.PAWN);
        List<Move> moves = new ArrayList<>();
        int direction = this.activePlayer == Player.WHITE ? 1 : -1;
        int startRow = this.activePlayer == Player.WHITE ? 1 : 6;

        // one step forward
        Coordinate target = source.moveTo(0, direction);
        if (target != null) {
            if (getPiece(target) == null) moves.add(Move.simple(source, target, null));

            // two steps forward (if not yet moved)
            if (source.getRowIndex() == startRow && getPiece(target) == null) {
                target = source.moveTo(0, 2 * direction);
                if (target != null && getPiece(target) == null)
                    moves.add(Move.simple(source, target, null));
            }
        }

        // check capture
        int[][] captures = new int[][]{{-1, direction}, {1, direction}};
        for (
                int[] capture : captures) {
            target = source.moveTo(capture[0], capture[1]);
            if (target != null) {
                Piece piece = getPiece(target);
                if (piece != null && piece.player.isOpponent(getActivePlayer()))
                    moves.add(Move.simple(source, target, target));
            }
        }

        return moves;
    }

    private List<Move> findKnightMoves(Coordinate source) {
        expectPiece(source, this.activePlayer, PieceType.KNIGHT);
        List<Move> moves = new ArrayList<>();
        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Coordinate target = source.moveTo(jump[0], jump[1]);
            if (target != null) {
                Piece piece = getPiece(target);
                if (piece == null)
                    moves.add(Move.simple(source, target, null));
                else if (piece.player.isOpponent(this.activePlayer))
                    moves.add(Move.simple(source, target, target));
            }
        }
        return moves;
    }

    private List<Move> findDirectionalMoves(Coordinate source, int maxSteps, Direction... directions) {
        expectPiece(source, this.activePlayer, PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING);
        List<Move> moves = new ArrayList<>();
        for (Direction direction : directions) {
            Coordinate target;
            int step = 1;
            do {
                target = source.moveTo(step * direction.columnOffset, step * direction.rowOffset);
                if (target != null) {
                    Piece piece = getPiece(target);
                    if (piece == null) moves.add(Move.simple(source, target, null));
                    else if (piece.player.isOpponent(this.activePlayer)) {
                        moves.add(Move.simple(source, target, target));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && target != null);
        }
        return moves;
    }

    private void expectPiece(Coordinate coordinate, Player player, PieceType... pieceTypes) {
        Piece piece = getPiece(coordinate);
        if (piece == null) throw new IllegalStateException("Piece expected");
        if (piece.player != player)
            throw new IllegalStateException("Expected piece of player: " + player);
        if (!Arrays.asList(pieceTypes).contains(piece.type))
            throw new IllegalArgumentException("Unexpected piece type: " + piece.type);
    }
}
