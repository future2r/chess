package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Implemenation of the Forsyth-Edwards Notation.
 * <p>
 * https://www.chessclub.com/user/help/PGN-spec (chapter 16.1)
 */
public final class FENPositions {

    public static FENPositions of(String s) {
        return new FENPositions(Objects.requireNonNull(s));
    }

    public static final String DEFAULT = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    public static FENPositions ofDefault() {
        return of(DEFAULT);
    }

    private static final char WHITE_PAWN = 'P';
    private static final char WHITE_ROOK = 'R';
    private static final char WHITE_KNIGHT = 'N';
    private static final char WHITE_BISHOP = 'B';
    private static final char WHITE_QUEEN = 'Q';
    private static final char WHITE_KING = 'K';

    private static final char BLACK_PAWN = 'p';
    private static final char BLACK_ROOK = 'r';
    private static final char BLACK_KNIGHT = 'n';
    private static final char BLACK_BISHOP = 'b';
    private static final char BLACK_QUEEN = 'q';
    private static final char BLACK_KING = 'k';

    private static final char WHITE = 'w';
    private static final char BLACK = 'b';

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

    private FENPositions(String s) {
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
            switch (c) {
                case WHITE_PAWN:
                case WHITE_ROOK:
                case WHITE_KNIGHT:
                case WHITE_BISHOP:
                case WHITE_QUEEN:
                case WHITE_KING:
                case BLACK_PAWN:
                case BLACK_ROOK:
                case BLACK_KNIGHT:
                case BLACK_BISHOP:
                case BLACK_QUEEN:
                case BLACK_KING:
                    columnIndex++;
                    if (columnIndex > Coordinate.COLUMNS)
                        throw new IllegalArgumentException("To many pieces in row " + Coordinate.toRowName(rowIndex));
                    Piece piece = Piece.ofSan(c);
                    this.board[Coordinate.valueOf(columnIndex, rowIndex).ordinal()] = piece;
                    break;
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                    columnIndex += (c - '0');
                    if (columnIndex > Coordinate.COLUMNS)
                        throw new IllegalArgumentException("To many pieces in row " + Coordinate.toRowName(rowIndex));
                    break;
                case ROW_SEPARATOR:
                    rowIndex--;
                    if (rowIndex > Coordinate.ROWS) throw new IllegalArgumentException("To many rows");
                    columnIndex = -1;
                    break;
                default:
                    throw new IllegalArgumentException("Illegal character: " + c);
            }
        }
    }

    private void parseActivePlayer(String s) {
        if (s.length() == 1) {
            int c = s.charAt(0);
            switch (c) {
                case WHITE:
                    this.activePlayer = Player.WHITE;
                    break;
                case BLACK:
                    this.activePlayer = Player.BLACK;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid active player field: " + s);
            }
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
                switch (c) {
                    case WHITE_KING:
                        this.whiteKingSideCastlingAvailable = true;
                        break;
                    case WHITE_QUEEN:
                        this.whiteQueenSideCastlingAvailable = true;
                        break;
                    case BLACK_KING:
                        this.blackKingSideCastlingAvailable = true;
                        break;
                    case BLACK_QUEEN:
                        this.blackQueenSideCastlingAvailable = true;
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal character: " + c);
                }
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

    public Player getActivePlayer() {
        return this.activePlayer;
    }

    public boolean isWhiteKingSideCastlingAvailable() {
        return this.whiteKingSideCastlingAvailable;
    }

    public boolean isWhiteQueenSideCastlingAvailable() {
        return this.whiteQueenSideCastlingAvailable;
    }

    public boolean isBlackKingSideCastlingAvailable() {
        return this.blackKingSideCastlingAvailable;
    }

    public boolean isBlackQueenSideCastlingAvailable() {
        return this.blackQueenSideCastlingAvailable;
    }

    public Coordinate getEnPassantTarget() {
        return this.enPassantTarget;
    }

    public int getHalfMoveClock() {
        return this.halfMoveClock;
    }

    public int getFullMoveNumber() {
        return this.fullMoveNumber;
    }
}
