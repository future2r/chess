package name.ulbricht.chess.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

/**
 * Implemenation of the Forsyth-Edwards Notation.
 * <p>
 * https://www.chessclub.com/user/help/PGN-spec (chapter 16.1)
 */
public final class FEN {

    private static final char WHITE_PLAYER = 'w';
    private static final char BLACK_PLAYER = 'b';

    private static final char WHITE_PAWN = 'P';
    private static final char WHITE_ROOK = SAN.ROOK;
    private static final char WHITE_KNIGHT = SAN.KNIGHT;
    private static final char WHITE_BISHOP = SAN.BISHOP;
    private static final char WHITE_QUEEN = SAN.QUEEN;
    private static final char WHITE_KING = SAN.KING;

    private static final char BLACK_PAWN = 'p';
    private static final char BLACK_ROOK = 'r';
    private static final char BLACK_KNIGHT = 'n';
    private static final char BLACK_BISHOP = 'b';
    private static final char BLACK_QUEEN = 'q';
    private static final char BLACK_KING = 'k';

    public static final String STANDARD = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    private static final char ROW_SEPARATOR = '/';
    private static final char FIELD_SEPARATOR = ' ';
    private static final String EMPTY_FIELD = "-";

    public static Setup createSetup(String s) {
        Setup setup = Setup.empty();

        String[] fields = s.split("" + FIELD_SEPARATOR);
        if (fields.length != 6) throw new IllegalArgumentException("Invalid number of data fields");

        parsePositions(setup, checkField(fields[0]));
        parseActivePlayer(setup, checkField(fields[1]));
        parseCastling(setup, checkField(fields[2]));
        parseEnPassantTarget(setup, checkField(fields[3]));
        parseHalfMoveClock(setup, checkField(fields[4]));
        parseFullMoveNumber(setup, checkField(fields[5]));

        return setup;
    }

    public static String toString(Setup setup) {
        StringBuilder sb = new StringBuilder();

        // positions
        for (int row = 7; row >= 0; row--) {
            int emptyCounter = 0;
            for (int column = 0; column <= 7; column++) {
                Piece piece = setup.getPiece(Coordinate.valueOf(column, row));
                if (piece != null) {
                    if (emptyCounter > 0) {
                        sb.append(emptyCounter);
                        emptyCounter = 0;
                    }
                    sb.append(symbol(piece));
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
        sb.append(symbol(setup.getActivePlayer()));
        sb.append(FIELD_SEPARATOR);

        // castling
        boolean available = false;
        if (setup.isWhiteKingSideCastlingAvailable()) {
            sb.append(WHITE_KING);
            available = true;
        }
        if (setup.isWhiteQueenSideCastlingAvailable()) {
            sb.append(WHITE_QUEEN);
            available = true;
        }
        if (setup.isBlackKingSideCastlingAvailable()) {
            sb.append(BLACK_KING);
            available = true;
        }
        if (setup.isBlackQueenSideCastlingAvailable()) {
            sb.append(BLACK_QUEEN);
            available = true;
        }
        if (!available) sb.append(EMPTY_FIELD);
        sb.append(FIELD_SEPARATOR);

        // en-passant target
        Coordinate enPassantTarget = setup.getEnPassantTarget();
        if (enPassantTarget != null) sb.append(enPassantTarget.name());
        else sb.append(EMPTY_FIELD);
        sb.append(FIELD_SEPARATOR);

        // half move clock
        sb.append(setup.getHalfMoveClock());
        sb.append(FIELD_SEPARATOR);

        // full move number
        sb.append(setup.getFullMoveNumber());

        return sb.toString();
    }

    public static Setup fromFile(Path file) throws IOException {
        return FEN.createSetup(Files.lines(file)
                .limit(1)
                .findFirst()
                .orElseThrow(() -> new IOException("Cannot read empty file")));
    }

    public static void toFile(Path file, Setup setup) throws IOException {
        Files.write(file, Arrays.asList(toString(setup)));
    }

    private static String checkField(String s) {
        if (s == null || s.isEmpty()) throw new IllegalArgumentException("Invalid field: " + s);
        return s;
    }

    private static void parsePositions(Setup setup, String s) {
        int rowIndex = 7;
        int columnIndex = -1;
        for (char c : s.toCharArray()) {

            Piece piece;
            try {
                piece = piece(c);
            } catch (IllegalArgumentException ex) {
                piece = null;
            }
            if (piece != null) {
                columnIndex++;
                if (columnIndex > Coordinate.COLUMNS)
                    throw new IllegalArgumentException("To many pieces in row " + Coordinate.toRowName(rowIndex));
                setup.setPiece(Coordinate.valueOf(columnIndex, rowIndex), piece);
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

    private static void parseActivePlayer(Setup setup, String s) {
        if (s.length() == 1) {
            setup.setActivePlayer(player(s.charAt(0)));
        } else
            throw new IllegalArgumentException("Invalid active player field: " + s);
    }

    private static void parseCastling(Setup setup, String s) {
        setup.setWhiteKingSideCastlingAvailable(false);
        setup.setWhiteQueenSideCastlingAvailable(false);
        setup.setBlackKingSideCastlingAvailable(false);
        setup.setBlackQueenSideCastlingAvailable(false);

        if (!s.equals(EMPTY_FIELD)) {
            for (char c : s.toCharArray()) {
                if (c == WHITE_KING) setup.setWhiteKingSideCastlingAvailable(true);
                else if (c == WHITE_QUEEN) setup.setWhiteQueenSideCastlingAvailable(true);
                else if (c == BLACK_KING) setup.setBlackKingSideCastlingAvailable(true);
                else if (c == BLACK_QUEEN) setup.setBlackQueenSideCastlingAvailable(true);
                else throw new IllegalArgumentException("Illegal character: " + c);
            }
        }
    }

    private static void parseEnPassantTarget(Setup setup, String s) {
        if (!s.equals(EMPTY_FIELD)) {
            Coordinate coordinate = Coordinate.valueOf(s);
            int expectedRowIndex = setup.getActivePlayer() == Player.WHITE ? 5 : 2;
            if (coordinate.rowIndex != expectedRowIndex)
                throw new IllegalArgumentException("Illegal row for active player.");
            setup.setEnPassantTarget(coordinate);
        }
    }

    private static void parseHalfMoveClock(Setup setup, String s) {
        try {
            int value = Integer.parseInt(s);
            if (value < 0) throw new IllegalArgumentException("Illegal value for half go clock: " + value);
            setup.setHalfMoveClock(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal value for half go clock: " + s);
        }
    }

    private static void parseFullMoveNumber(Setup setup, String s) {
        try {
            int value = Integer.parseInt(s);
            if (value < 1) throw new IllegalArgumentException("Illegal value for full go number: " + value);
            setup.setFullMoveNumber(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Illegal value for full go number: " + s);
        }
    }

    public static char symbol(Player player) {
        switch (Objects.requireNonNull(player, "player cannot be null")) {
            case WHITE:
                return WHITE_PLAYER;
            case BLACK:
                return BLACK_PLAYER;
            default:
                throw new InternalError("Unexpected player enum value" + player);
        }
    }

    public static char symbol(Piece piece) {
        switch (Objects.requireNonNull(piece, "piece cannot be null")) {
            case WHITE_PAWN:
                return WHITE_PAWN;
            case WHITE_ROOK:
                return WHITE_ROOK;
            case WHITE_KNIGHT:
                return WHITE_KNIGHT;
            case WHITE_BISHOP:
                return WHITE_BISHOP;
            case WHITE_QUEEN:
                return WHITE_QUEEN;
            case WHITE_KING:
                return WHITE_KING;
            case BLACK_PAWN:
                return BLACK_PAWN;
            case BLACK_ROOK:
                return BLACK_ROOK;
            case BLACK_KNIGHT:
                return BLACK_KNIGHT;
            case BLACK_BISHOP:
                return BLACK_BISHOP;
            case BLACK_QUEEN:
                return BLACK_QUEEN;
            case BLACK_KING:
                return BLACK_KING;
            default:
                throw new InternalError("Unexpected piece enum value" + piece);
        }
    }

    public static Player player(char symbol) {
        switch (symbol) {
            case WHITE_PLAYER:
                return Player.WHITE;
            case BLACK_PLAYER:
                return Player.BLACK;
            default:
                throw new IllegalArgumentException("Unknown player symbol " + symbol);
        }
    }

    public static Piece piece(char symbol) {
        switch (symbol) {
            case WHITE_PAWN:
                return Piece.WHITE_PAWN;
            case WHITE_ROOK:
                return Piece.WHITE_ROOK;
            case WHITE_KNIGHT:
                return Piece.WHITE_KNIGHT;
            case WHITE_BISHOP:
                return Piece.WHITE_BISHOP;
            case WHITE_QUEEN:
                return Piece.WHITE_QUEEN;
            case WHITE_KING:
                return Piece.WHITE_KING;
            case BLACK_PAWN:
                return Piece.BLACK_PAWN;
            case BLACK_ROOK:
                return Piece.BLACK_ROOK;
            case BLACK_KNIGHT:
                return Piece.BLACK_KNIGHT;
            case BLACK_BISHOP:
                return Piece.BLACK_BISHOP;
            case BLACK_QUEEN:
                return Piece.BLACK_QUEEN;
            case BLACK_KING:
                return Piece.BLACK_KING;
            default:
                throw new IllegalArgumentException("Unknown piece symbol " + symbol);
        }
    }

    private FEN() {
        // hidden
    }
}
