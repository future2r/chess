package name.ulbricht.chess.game;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implemenation of the Standard Algebraic Notation.
 */
public final class SAN {

    public static final char ROOK = 'R';
    public static final char KNIGHT = 'N';
    public static final char BISHOP = 'B';
    public static final char QUEEN = 'Q';
    public static final char KING = 'K';

    public enum PlyType {
        MOVE, KING_SIDE_CASTLING, QUEEN_SIDE_CASTLING
    }

    public static final class Ply {

        public final PlyType type;
        public final PieceType piece;
        public final char sourceColumn;
        public final char sourceRow;
        public final boolean captures;
        public final Coordinate target;
        public final PieceType promotion;
        public final boolean check;

        private Ply(PlyType type, PieceType piece, char sourceColumn, char sourceRow,
                    boolean captures, Coordinate target, PieceType promotion, boolean check) {
            this.type = type;
            this.piece = piece;
            this.sourceColumn = sourceColumn;
            this.sourceRow = sourceRow;
            this.captures = captures;
            this.target = target;
            this.promotion = promotion;
            this.check = check;
        }

    }

    private static final Logger log = Logger.getLogger(SAN.class.getPackage().getName());

    private static final String GROUP_MOVE = "mv";
    private static final String GROUP_PIECE = "pc";
    private static final String GROUP_SOURCE_COLUMN = "sc";
    private static final String GROUP_SOURCE_ROW = "sr";
    private static final String GROUP_CAPTURES = "cap";
    private static final String GROUP_TARGET = "tgt";
    private static final String GROUP_PROMOTION = "prm";
    private static final String GROUP_CHECK = "ch";
    private static final String GROUP_KING_SIDE_CASTLING = "kc";
    private static final String GROUP_KING_SIDE_CASTLING_CHECK = "kcc";
    private static final String GROUP_QUEEN_SIDE_CASTLING = "qc";
    private static final String GROUP_QUEEN_SIDE_CASTLING_CHECK = "qcc";

    private static final Pattern pattern = Pattern.compile(
            "(?<mv>(?<pc>[RNBQK]?)(?<sc>[a-h]?)(?<sr>[1-8]?)(?<cap>x?)(?<tgt>[a-h][1-8])(=(?<prm>[RNBQ]))?(?<ch>\\+?))" +
                    "|(?<qc>(O-O-O)(?<qcc>\\+?))" +
                    "|(?<kc>(O-O)(?<kcc>\\+?))");

    public static Ply ply(String s) {
        log.fine(() -> "Parsing " + s);

        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) throw new IllegalArgumentException("Illegal SAN ply: " + s);

        log.fine(() -> createLog(matcher));

        if (matcher.group(GROUP_MOVE) != null) {

            String sPiece = matcher.group(GROUP_PIECE);
            PieceType pieceType = !sPiece.isEmpty() ? pieceType(sPiece.charAt(0)) : null;

            String sSourceColumn = matcher.group(GROUP_SOURCE_COLUMN);
            char srcColumnIdx = !sSourceColumn.isEmpty() ? sSourceColumn.charAt(0) : 0;

            String sSourceRow = matcher.group(GROUP_SOURCE_ROW);
            char srcRowIdx = !sSourceRow.isEmpty() ? sSourceRow.charAt(0) : 0;

            boolean captures = !matcher.group(GROUP_CAPTURES).isEmpty();

            Coordinate target = Coordinate.valueOf(matcher.group(GROUP_TARGET));

            String sPromotion = matcher.group(GROUP_PROMOTION);
            PieceType promotion = (sPromotion != null && s.length() > 0) ? pieceType(sPromotion.charAt(0)) : null;

            boolean check = !matcher.group(GROUP_CHECK).isEmpty();

            return new Ply(PlyType.MOVE, pieceType, srcColumnIdx, srcRowIdx,
                    captures, target, promotion, check);

        } else if (matcher.group(GROUP_KING_SIDE_CASTLING) != null) {
            boolean check = !matcher.group(GROUP_KING_SIDE_CASTLING_CHECK).isEmpty();

            return new Ply(PlyType.KING_SIDE_CASTLING, null, (char) 0, (char) 0,
                    false, null, null, check);

        } else if (matcher.group(GROUP_QUEEN_SIDE_CASTLING) != null) {
            boolean check = !matcher.group(GROUP_QUEEN_SIDE_CASTLING_CHECK).isEmpty();

            return new Ply(PlyType.QUEEN_SIDE_CASTLING, null, (char) 0, (char) 0,
                    false, null, null, check);

        } else {
            throw new IllegalArgumentException("Illegal SAN ply: " + s);
        }
    }

    private static String createLog(Matcher matcher) {
        StringBuilder sb = new StringBuilder();
        sb.append("Parsed ");
        sb.append(matcher.group(0));
        sb.append('\n');
        for (String group : new String[]{
                GROUP_MOVE, GROUP_PIECE, GROUP_SOURCE_COLUMN, GROUP_SOURCE_ROW, GROUP_CAPTURES, GROUP_TARGET,
                GROUP_PROMOTION, GROUP_CHECK,
                GROUP_KING_SIDE_CASTLING, GROUP_KING_SIDE_CASTLING_CHECK,
                GROUP_QUEEN_SIDE_CASTLING, GROUP_QUEEN_SIDE_CASTLING_CHECK}) {
            sb.append(group);
            sb.append(':');
            sb.append(matcher.group(group));
            sb.append('\n');
        }
        return sb.toString();
    }

    private static PieceType pieceType(char symbol) {
        switch (symbol) {
            case ROOK:
                return PieceType.ROOK;
            case KNIGHT:
                return PieceType.KNIGHT;
            case BISHOP:
                return PieceType.BISHOP;
            case QUEEN:
                return PieceType.QUEEN;
            case KING:
                return PieceType.KING;
            default:
                throw new IllegalArgumentException("Illegal piece type symbol: " + symbol);
        }
    }

    private SAN() {
        // hidden
    }
}
