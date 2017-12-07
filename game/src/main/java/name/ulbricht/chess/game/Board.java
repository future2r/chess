package name.ulbricht.chess.game;

import java.util.Objects;

public final class Board implements Cloneable {

    public static Board initial() {
        return FEN.createBoard(FEN.INITIAL);
    }

    private Piece[] pieces = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
    private Player activePlayer = Player.WHITE;
    private boolean whiteKingSideCastlingAvailable = true;
    private boolean whiteQueenSideCastlingAvailable = true;
    private boolean blackKingSideCastlingAvailable = true;
    private boolean blackQueenSideCastlingAvailable = true;
    private Coordinate enPassantTarget;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;

    Piece getPiece(Coordinate coordinate) {
        return this.pieces[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()];
    }

    boolean isEmpty(Coordinate coordinate) {
        return getPiece(coordinate) == null;
    }

    void setPiece(Coordinate coordinate, Piece piece) {
        this.pieces[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()] = piece;
    }

    Coordinate king(Player player) {
        return findFirst(Piece.valueOf(PieceType.KING, Objects.requireNonNull(player, "player cannot be null")));
    }

    private Coordinate findFirst(Piece piece) {
        for (Coordinate coordinate : Coordinate.values()) {
            if (getPiece(coordinate) == piece) return coordinate;
        }
        return null;
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

    @Override
    protected Board clone() {
        try {
            Board copy = (Board) super.clone();
            copy.pieces = this.pieces.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    @Override
    public String toString() {


        StringBuilder sb = new StringBuilder("{pieces=");
        for (int row = 0; row < Coordinate.ROWS; row++) {
            sb.append('\n');
            for (int column = Coordinate.COLUMNS - 1; column >= 0; column--) {
                Piece piece = getPiece(Coordinate.valueOf(column, row));
                if (piece != null) sb.append(FEN.symbol(piece));
                else sb.append(' ');
            }
        }
        sb.append('}');
        sb.append("\nactivePlayer=");
        sb.append(this.activePlayer);
        sb.append("\nwhiteKingSideCastlingAvailable=");
        sb.append(this.whiteKingSideCastlingAvailable);
        sb.append("\nwhiteQueenSideCastlingAvailable=");
        sb.append(this.whiteQueenSideCastlingAvailable);
        sb.append("\nblackKingSideCastlingAvailable=");
        sb.append(this.blackKingSideCastlingAvailable);
        sb.append("\nblackQueenSideCastlingAvailable=");
        sb.append(this.blackQueenSideCastlingAvailable);
        sb.append("\nenpassantTarget=");
        sb.append(this.enPassantTarget);
        sb.append("\nhalfMoveClock");
        sb.append(this.halfMoveClock);
        sb.append("\nfullMoveNumber");
        sb.append(this.fullMoveNumber);

        return sb.toString();
    }
}
