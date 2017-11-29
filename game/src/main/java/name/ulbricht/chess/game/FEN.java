package name.ulbricht.chess.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Implemenation of the Forsyth-Edwards Notation.
 * <p>
 * https://www.chessclub.com/user/help/PGN-spec (chapter 16.1)
 */
public final class FEN {

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
        sb.append(setup.getActivePlayer().san);
        sb.append(FIELD_SEPARATOR);

        // castling
        boolean available = false;
        if (setup.isWhiteKingSideCastlingAvailable()) {
            sb.append(Piece.WHITE_KING.san);
            available = true;
        }
        if (setup.isWhiteQueenSideCastlingAvailable()) {
            sb.append(Piece.WHITE_QUEEN.san);
            available = true;
        }
        if (setup.isBlackKingSideCastlingAvailable()) {
            sb.append(Piece.BLACK_KING.san);
            available = true;
        }
        if (setup.isBlackQueenSideCastlingAvailable()) {
            sb.append(Piece.BLACK_QUEEN.san);
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
                piece = Piece.ofSan(c);
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
            setup.setActivePlayer(Player.ofSan(s.charAt(0)));
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
                if (c == Piece.WHITE_KING.san) setup.setWhiteKingSideCastlingAvailable(true);
                else if (c == Piece.WHITE_QUEEN.san) setup.setWhiteQueenSideCastlingAvailable(true);
                else if (c == Piece.BLACK_KING.san) setup.setBlackKingSideCastlingAvailable(true);
                else if (c == Piece.BLACK_QUEEN.san) setup.setBlackQueenSideCastlingAvailable(true);
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

    private FEN() {
        // hidden
    }
}
