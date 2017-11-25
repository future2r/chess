package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Implemenation of the Forsyth-Edwards Notation.
 * <p>
 * https://www.chessclub.com/user/help/PGN-spec (chapter 16.1)
 */
public final class FENSetup {

    public static FENSetup of(String s) {
        return new FENSetup(Objects.requireNonNull(s));
    }

    public static final String STANDARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static FENSetup standard() {
        return of(STANDARD);
    }

    public static final String EMPTY = "8/8/8/8/8/8/8/8 w KQkq - 0 1";

    public static FENSetup empty() {
        return of(EMPTY);
    }

    private static final char ROW_SEPARATOR = '/';
    private static final char FIELD_SEPARATOR = ' ';
    private static final String EMPTY_FIELD = "-";

    private final Piece[] board = new Piece[Coordinate.ROWS * Coordinate.COLUMNS];
    private Player activePlayer = Player.WHITE;
    private boolean whiteKingSideCastlingAvailable = true;
    private boolean whiteQueenSideCastlingAvailable = true;
    private boolean blackKingSideCastlingAvailable = true;
    private boolean blackQueenSideCastlingAvailable = true;
    private Coordinate enPassantTarget;
    private int halfMoveClock;
    private int fullMoveNumber;

    private FENSetup(String s) {
        String[] fields = s.split("" + FIELD_SEPARATOR);
        if (fields.length != 6) throw new IllegalArgumentException("Invalid number of data fields");

        parsePositions(checkField(fields[0]));
        parseActivePlayer(checkField(fields[1]));
        parseCastling(checkField(fields[2]));
        parseEnPassantTarget(checkField(fields[3]));
        parseHalfMoveClock(checkField(fields[4]));
        parseFullMoveNumber(checkField(fields[5]));
    }

    private String checkField(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("Invalid field: " + s);
        return s;
    }

    private void parsePositions(String s) {
        int rowIndex = 7;
        int columnIndex = -1;
        for (char c : s.toCharArray()) {

            Piece piece;
            try {
                piece = Piece.ofSan(c);
            } catch (IllegalArgumentException ex) {
                piece = null;
            }
            if (piece != null) {
                columnIndex++;
                if (columnIndex > Coordinate.COLUMNS)
                    throw new IllegalArgumentException("To many pieces in row " + Coordinate.toRowName(rowIndex));
                setPiece(Coordinate.valueOf(columnIndex, rowIndex), piece);
            } else if (c >= '1' && c <= '8') {
                columnIndex += (c - '0');
                if (columnIndex > Coordinate.COLUMNS)
                    throw new IllegalArgumentException("To many pieces in row " + Coordinate.toRowName(rowIndex));
            } else if (c == ROW_SEPARATOR) {
                rowIndex--;
                if (rowIndex > Coordinate.ROWS) throw new IllegalArgumentException("To many rows");
                columnIndex = -1;
            } else {
                throw new IllegalArgumentException("Illegal character: " + c);
            }
        }
    }

    private void parseActivePlayer(String s) {
        if (s.length() == 1) {
            setActivePlayer(Player.ofSan(s.charAt(0)));
        } else
            throw new IllegalArgumentException("Invalid active player field: " + s);
    }

    private void parseCastling(String s) {
        this.whiteKingSideCastlingAvailable = false;
        this.whiteQueenSideCastlingAvailable = false;
        this.blackKingSideCastlingAvailable = false;
        this.blackQueenSideCastlingAvailable = false;

        if (!s.equals(EMPTY_FIELD)) {
            for (char c : s.toCharArray()) {
                if (c == Piece.WHITE_KING.san) this.whiteKingSideCastlingAvailable = true;
                else if (c == Piece.WHITE_QUEEN.san) this.whiteQueenSideCastlingAvailable = true;
                else if (c == Piece.BLACK_KING.san) this.blackKingSideCastlingAvailable = true;
                else if (c == Piece.BLACK_QUEEN.san) this.blackQueenSideCastlingAvailable = true;
                else throw new IllegalArgumentException("Illegal character: " + c);
            }
        }
    }

    private void parseEnPassantTarget(String s) {
        this.enPassantTarget = null;

        if (!s.equals(EMPTY_FIELD)) {
            Coordinate coordinate = Coordinate.valueOf(s);
            int expectedRowIndex = this.activePlayer == Player.WHITE ? 5 : 2;
            if (coordinate.rowIndex != expectedRowIndex)
                throw new IllegalArgumentException("Illegal row for active player.");
            this.enPassantTarget = coordinate;
        }
    }

    private void parseHalfMoveClock(String s) {
        try {
            int value = Integer.parseInt(s);
            if (value < 0) throw new IllegalArgumentException("Illegal value for half go clock: " + value);
            this.halfMoveClock = value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal value for half go clock: " + s);
        }
    }

    private void parseFullMoveNumber(String s) {
        try {
            int value = Integer.parseInt(s);
            if (value < 1) throw new IllegalArgumentException("Illegal value for full go number: " + value);
            this.fullMoveNumber = value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal value for full go number: " + s);
        }
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

    public int getFullMoveNumber() {
        return this.fullMoveNumber;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // positions
        for (int row = 7; row >= 0; row--) {
            int emptyCounter = 0;
            for (int column = 0; column <= 7; column++) {
                Piece piece = this.board[Coordinate.valueOf(column, row).ordinal()];
                if (piece != null) {
                    if (emptyCounter > 0) {
                        sb.append(emptyCounter);
                        emptyCounter = 0;
                    }
                    sb.append(piece.san);
                } else {
                    emptyCounter++;
                }
            }
            if (emptyCounter > 0) {
                sb.append(emptyCounter);
            }
            if (row > 0) sb.append(ROW_SEPARATOR);
        }
        sb.append(FIELD_SEPARATOR);

        // player
        sb.append(this.activePlayer.san);
        sb.append(FIELD_SEPARATOR);

        // castling
        boolean available = false;
        if (this.whiteKingSideCastlingAvailable) {
            sb.append(Piece.WHITE_KING.san);
            available = true;
        }
        if (this.whiteQueenSideCastlingAvailable) {
            sb.append(Piece.WHITE_QUEEN.san);
            available = true;
        }
        if (this.blackKingSideCastlingAvailable) {
            sb.append(Piece.BLACK_KING.san);
            available = true;
        }
        if (this.blackQueenSideCastlingAvailable) {
            sb.append(Piece.BLACK_QUEEN.san);
            available = true;
        }
        if (!available) sb.append(EMPTY_FIELD);
        sb.append(FIELD_SEPARATOR);

        // en-passant target
        if (this.enPassantTarget != null) sb.append(this.enPassantTarget.name());
        else sb.append(EMPTY_FIELD);
        sb.append(FIELD_SEPARATOR);

        // half move clock
        sb.append(this.halfMoveClock);
        sb.append(FIELD_SEPARATOR);

        // full move number
        sb.append(this.fullMoveNumber);

        return sb.toString();
    }
}
