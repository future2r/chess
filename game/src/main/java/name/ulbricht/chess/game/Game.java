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
    private final List<Ply> legalPlies = new ArrayList<>();

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
        if (piece == null) throw new IllegalStateException("No piece to go");
        removePiece(source);
        setPiece(target, piece);
    }

    private void removePiece(Coordinate coordinate) {
        setPiece(coordinate, null);
    }

    /**
     * Returns a list with legal go for the current player.
     *
     * @return a list of legal moves
     */
    public List<Ply> getLegalPlies() {
        return Collections.unmodifiableList(this.legalPlies);
    }

    public List<Ply> getLegalMoves(Coordinate source) {
        return this.legalPlies.stream().filter(m -> m.getSource() == source).collect(Collectors.toList());
    }

    private void findLegalMoves() {
        this.legalPlies.clear();

        for (Coordinate coordinate : Coordinate.values()) {
            Piece piece = getPiece(coordinate);
            if (piece != null && piece.player == this.activePlayer) {
                this.legalPlies.addAll(findLegalMoves(coordinate));
            }
        }
    }

    /**
     * Performs the specified ply in this game. The piece will be moved and the the current player is switched.
     *
     * @param ply the ply to perform
     */
    public void performMove(Ply ply) {
        if (!this.legalPlies.contains(ply))
            throw new IllegalArgumentException("Not a legal ply");

        switch (ply.getType()) {
            case SIMPLE:
                if (ply.getCaptures() != null) removePiece(ply.getCaptures());
                movePiece(ply.getSource(), ply.getTarget());
                break;
            case PAWN_DOUBLE_ADVANCE:
                movePiece(ply.getSource(), ply.getTarget());
                // TODO set en passant target
                break;
            default:
                throw new IllegalArgumentException("Unsupported ply type: " + ply.getType());
        }

        this.activePlayer = this.activePlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }

    private List<Ply> findLegalMoves(Coordinate source) {
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

    private List<Ply> findPawnMoves(Coordinate source) {
        expectPiece(source, this.activePlayer, PieceType.PAWN);
        List<Ply> plies = new ArrayList<>();
        Direction direction = Direction.forward(this.activePlayer);
        int startRow = this.activePlayer == Player.WHITE ? 1 : 6;

        // one step forward
        Coordinate target = source.go(direction);
        if (target != null) {
            if (getPiece(target) == null) plies.add(Ply.simple(this.activePlayer, source, target));

            // two steps forward (if not yet moved)
            if (source.rowIndex == startRow && getPiece(target) == null) {
                target = source.go(direction, 2);
                if (target != null && getPiece(target) == null)
                    plies.add(Ply.pawnDoubleAdvance(this.activePlayer, source));
            }
        }

        // check captures
        for (Direction captures : new Direction[]{Direction.forwardLeft(this.activePlayer), Direction.forwardRight(this.activePlayer)}) {
            target = source.go(captures);
            if (target != null) {
                Piece piece = getPiece(target);
                if (piece != null && piece.player.isOpponent(getActivePlayer()))
                    plies.add(Ply.simpleCaptures(this.activePlayer, source, target));
            }
        }

        return plies;
    }

    private List<Ply> findKnightMoves(Coordinate source) {
        expectPiece(source, this.activePlayer, PieceType.KNIGHT);
        List<Ply> plies = new ArrayList<>();
        int[][] jumps = new int[][]{{-1, 2}, {1, 2}, {-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {-1, -2}, {1, -2}};
        for (int[] jump : jumps) {
            Coordinate target = source.go(jump[0], jump[1]);
            if (target != null) {
                Piece piece = getPiece(target);
                if (piece == null)
                    plies.add(Ply.simple(this.activePlayer, source, target));
                else if (piece.player.isOpponent(this.activePlayer))
                    plies.add(Ply.simpleCaptures(this.activePlayer, source, target));
            }
        }
        return plies;
    }

    private List<Ply> findDirectionalMoves(Coordinate source, int maxSteps, Direction... directions) {
        expectPiece(source, this.activePlayer, PieceType.ROOK, PieceType.BISHOP, PieceType.QUEEN, PieceType.KING);
        List<Ply> plies = new ArrayList<>();
        for (Direction direction : directions) {
            Coordinate target;
            int step = 1;
            do {
                target = source.go(direction, step);
                if (target != null) {
                    Piece piece = getPiece(target);
                    if (piece == null) plies.add(Ply.simple(this.activePlayer, source, target));
                    else if (piece.player.isOpponent(this.activePlayer)) {
                        plies.add(Ply.simpleCaptures(this.activePlayer, source, target));
                        break;
                    } else break;
                }
                step++;
            } while (step <= maxSteps && target != null);
        }
        return plies;
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
