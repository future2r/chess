package name.ulbricht.chess.game;

import java.util.Objects;

public final class Setup {

    public static Setup empty() {
        return new Setup();
    }

    public static Setup standard() {
        return FEN.createSetup(FEN.STANDARD);
    }

    private final Piece[] board = new Piece[Coordinate.ROWS * Coordinate.COLUMNS];
    private Player activePlayer = Player.WHITE;
    private boolean whiteKingSideCastlingAvailable = true;
    private boolean whiteQueenSideCastlingAvailable = true;
    private boolean blackKingSideCastlingAvailable = true;
    private boolean blackQueenSideCastlingAvailable = true;
    private Coordinate enPassantTarget;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    private Setup() {
        // hidden
    }

    public Piece getPiece(Coordinate coordinate) {
        return this.board[coordinate.ordinal()];
    }

    public void setPiece(Coordinate coordinate, Piece piece) {
        this.board[coordinate.ordinal()] = piece;
    }

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = Objects.requireNonNull(activePlayer);
    }

    public boolean isWhiteKingSideCastlingAvailable() {
        return this.whiteKingSideCastlingAvailable;
    }

    public void setWhiteKingSideCastlingAvailable(boolean whiteKingSideCastlingAvailable) {
        this.whiteKingSideCastlingAvailable = whiteKingSideCastlingAvailable;
    }

    public boolean isWhiteQueenSideCastlingAvailable() {
        return this.whiteQueenSideCastlingAvailable;
    }

    public void setWhiteQueenSideCastlingAvailable(boolean whiteQueenSideCastlingAvailable) {
        this.whiteQueenSideCastlingAvailable = whiteQueenSideCastlingAvailable;
    }

    public boolean isBlackKingSideCastlingAvailable() {
        return this.blackKingSideCastlingAvailable;
    }

    public void setBlackKingSideCastlingAvailable(boolean blackKingSideCastlingAvailable) {
        this.blackKingSideCastlingAvailable = blackKingSideCastlingAvailable;
    }

    public boolean isBlackQueenSideCastlingAvailable() {
        return this.blackQueenSideCastlingAvailable;
    }

    public void setBlackQueenSideCastlingAvailable(boolean blackQueenSideCastlingAvailable) {
        this.blackQueenSideCastlingAvailable = blackQueenSideCastlingAvailable;
    }

    public Coordinate getEnPassantTarget() {
        return this.enPassantTarget;
    }

    public void setEnPassantTarget(Coordinate enPassantTarget) {
        this.enPassantTarget = enPassantTarget;
    }

    public int getHalfMoveClock() {
        return this.halfMoveClock;
    }

    public void setHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
    }

    public int getFullMoveNumber() {
        return this.fullMoveNumber;
    }

    public void setFullMoveNumber(int fullMoveNumber) {
        this.fullMoveNumber = fullMoveNumber;
    }
}
