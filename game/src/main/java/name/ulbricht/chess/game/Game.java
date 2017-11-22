package name.ulbricht.chess.game;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a game on the board.
 */
public final class Game {

    private final Piece[] board;
    private Player currentPlayer;
    private final Map<Coordinate, List<Move>> legalMoves = new TreeMap<>();

    /**
     * Creates a new game. This game will have a new board with the initial positions of the pieces.
     */
    public Game() {
        this.board = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
        start();
    }

    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Starts a new game. Everything will be reset to the initial values.
     */
    public void start() {
        setup();
        this.currentPlayer = Player.WHITE;
        findLegalMoves();
    }

    private void setup() {
        clear();

        PieceType[] baseLinePieces = new PieceType[]{
                PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
                PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK};

        Coordinate whiteCoordinate = Coordinate.a1;
        Coordinate blackCoordinate = Coordinate.a8;
        for (int i = 0; i < Coordinate.COLUMNS; i++) {
            setPiece(whiteCoordinate, new Piece(baseLinePieces[i], Player.WHITE));
            setPiece(whiteCoordinate.moveUp(), new Piece(PieceType.PAWN, Player.WHITE));
            whiteCoordinate = whiteCoordinate.moveRight();

            setPiece(blackCoordinate, new Piece(baseLinePieces[i], Player.BLACK));
            setPiece(blackCoordinate.moveDown(), new Piece(PieceType.PAWN, Player.BLACK));
            blackCoordinate = blackCoordinate.moveRight();
        }
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
     * Sets a piece to the square defined by the given coordinate. If the piece is {@code null} then the square will be
     * cleared. Any current piece will be replaced and returned.
     *
     * @param coordinate the coordinate
     * @param piece      the piece or {@code null}
     * @return the replaced piece or {@code null}
     * @see #getPiece(Coordinate)
     */
    void setPiece(Coordinate coordinate, Piece piece) {
        this.board[coordinate.ordinal()] = piece;
    }

    /**
     * Returns the piece at the given coordinate. If the square is empty the returned value will be {@code null}.
     *
     * @param coordinate the coordinate
     * @return the piece or {@code null}
     * @see #setPiece(Coordinate, Piece)
     */
    public Piece getPiece(Coordinate coordinate) {
        return this.board[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()];
    }

    void movePiece(Coordinate source, Coordinate target) {
        Piece piece = getPiece(source);
        if (piece == null) throw new IllegalStateException("No piece to move");
        setPiece(target, piece);
        piece.incrementMoveCount();
    }

    void removePiece(Coordinate coordinate) {
        setPiece(coordinate, null);
    }

    void clear() {
        Arrays.fill(this.board, null);
    }

    /**
     * Returns a map with legal move for the current player. The keys are the coordinates of the piece to move. The
     * values are lists with legal moves for this piece.
     *
     * @return a map with pieces and their legal moves
     */
    public Map<Coordinate, List<Move>> getLegalMoves() {
        return Collections.unmodifiableMap(this.legalMoves);
    }

    private void findLegalMoves() {
        this.legalMoves.clear();
        this.legalMoves.putAll(Rules.findLegalMoves(this));
    }

    /**
     * Performs the specified move in this game. The piece will be moved the the current player is switched.
     *
     * @param move the move to perform
     */
    public void performMove(Move move) {
        if (this.legalMoves.values().stream().flatMap(l -> l.stream()).noneMatch(m -> m == move))
            throw new IllegalArgumentException("Not a legal move");

        switch (move.getType()) {
            case SIMPLE:
                if (move.getCaptures() != null) removePiece(move.getCaptures());
                movePiece(move.getSource(), move.getTarget());
                break;
            default:
                throw new IllegalArgumentException("Unsupported move type: " + move.getType());
        }

        this.currentPlayer = this.currentPlayer == Player.WHITE ? Player.BLACK : Player.WHITE;
        findLegalMoves();
    }
}
