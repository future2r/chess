package name.ulbricht.chess.game;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Implemenation of the Standard Algebraic Notation.
 */
public final class SAN {

    public static final char ROOK = 'R';
    public static final char KNIGHT = 'N';
    public static final char BISHOP = 'B';
    public static final char QUEEN = 'Q';
    public static final char KING = 'K';

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

    public static SANPly ply(String s) {
        log.fine(() -> "Parsing " + s);

        Matcher matcher = pattern.matcher(s);
        if (!matcher.matches()) throw new IllegalArgumentException("Illegal SAN ply: " + s);

        log.fine(() -> createLog(matcher));

        if (matcher.group(GROUP_MOVE) != null) {

            String sPiece = matcher.group(GROUP_PIECE);
            PieceType pieceType = !sPiece.isEmpty() ? pieceType(sPiece.charAt(0)) : PieceType.PAWN;

            String sSourceColumn = matcher.group(GROUP_SOURCE_COLUMN);
            char srcColumnIdx = !sSourceColumn.isEmpty() ? sSourceColumn.charAt(0) : 0;

            String sSourceRow = matcher.group(GROUP_SOURCE_ROW);
            char srcRowIdx = !sSourceRow.isEmpty() ? sSourceRow.charAt(0) : 0;

            boolean captures = !matcher.group(GROUP_CAPTURES).isEmpty();

            Coordinate target = Coordinate.valueOf(matcher.group(GROUP_TARGET));

            String sPromotion = matcher.group(GROUP_PROMOTION);
            PieceType promotion = (sPromotion != null && s.length() > 0) ? pieceType(sPromotion.charAt(0)) : null;

            boolean check = !matcher.group(GROUP_CHECK).isEmpty();

            return new SANPly(SANPlyType.MOVE, pieceType, srcColumnIdx, srcRowIdx,
                    captures, target, promotion, check);

        } else if (matcher.group(GROUP_KING_SIDE_CASTLING) != null) {
            boolean check = !matcher.group(GROUP_KING_SIDE_CASTLING_CHECK).isEmpty();

            return new SANPly(SANPlyType.KING_SIDE_CASTLING, PieceType.KING, (char) 0, (char) 0,
                    false, null, null, check);

        } else if (matcher.group(GROUP_QUEEN_SIDE_CASTLING) != null) {
            boolean check = !matcher.group(GROUP_QUEEN_SIDE_CASTLING_CHECK).isEmpty();

            return new SANPly(SANPlyType.QUEEN_SIDE_CASTLING, PieceType.KING, (char) 0, (char) 0,
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

    /**
     * Finds a valid ply for the active player that matches the noted ply.
     *
     * @param game   the current game
     * @param sanPly the noted SAN ply
     * @return a matching ply the is valid for the active player in the current game, otherwise {@code null}
     */
    public static Ply findPly(Game game, SANPly sanPly) {
        Player player = game.getActivePlayer();
        List<name.ulbricht.chess.game.Ply> plies = game.getValidPlies();
        if (plies.isEmpty()) return null;

        switch (sanPly.type) {
            case MOVE: {
                Piece piece = Piece.valueOf(sanPly.piece, player);
                List<Ply> filteredPlies = game.getValidPlies().stream()
                        .filter(p -> p.piece == piece)
                        .filter(p -> p.target == sanPly.target)
                        .collect(Collectors.toList());
                if (filteredPlies.isEmpty()) return null;

                Ply ply = null;

                if (piece.type == PieceType.PAWN) {
                    if (sanPly.captures) {
                        // find the right pawn ply
                        for (Ply candidatePly : filteredPlies) {
                            if (sourceMatchs(sanPly, candidatePly)) {
                                if (ply != null) throw new IllegalStateException("To many plies found");
                                ply = candidatePly;
                            }
                        }
                    } else {
                        // there should be only one ply left
                        if (filteredPlies.size() != 1) throw new IllegalStateException("To many plies found");
                        ply = filteredPlies.get(0);
                    }

                    if (ply == null) return null;

                    // set the promotion
                    if (sanPly.promotion != null) {
                        if (ply.type != PlyType.PAWN_PROMOTION)
                            throw new IllegalStateException("found ply is not a pawn promotion");
                        ply.promotion = sanPly.promotion;
                    }
                } else {
                    // if there is only one ply for this piece and target, we're done
                    if (filteredPlies.size() == 1) {
                        ply = filteredPlies.get(0);
                    } else {
                        // find the correct ply
                        for (Ply candidatePly : filteredPlies) {
                            if (sourceMatchs(sanPly, candidatePly)) {
                                if (ply != null) throw new IllegalStateException("To many plies found");
                                ply = candidatePly;
                            }
                        }
                    }
                }

                if (ply == null) return null;

                // check capturing
                if (sanPly.captures) {
                    if (ply.captures == null || ply.capturedPiece == null)
                        throw new IllegalStateException("Found ply does not capture");
                } else {
                    if (ply.captures != null || ply.capturedPiece != null)
                        throw new IllegalStateException("Found ply does capture");
                }

                return ply;
            }
            case KING_SIDE_CASTLING:
                return game.getValidPlies().stream()
                        .filter(p -> p.type == PlyType.KING_SIDE_CASTLING)
                        .findFirst().orElse(null);
            case QUEEN_SIDE_CASTLING:
                return game.getValidPlies().stream()
                        .filter(p -> p.type == PlyType.QUEEN_SIDE_CASTLING)
                        .findFirst().orElse(null);
            default:
                throw new IllegalArgumentException("Illegal SAN ply type: " + sanPly.type);
        }
    }

    private static boolean sourceMatchs(SANPly sanPly, Ply ply) {
        return (sanPly.sourceColumn == 0 || sanPly.sourceColumn == ply.source.columnName) &&
                (sanPly.sourceRow == 0 || sanPly.sourceRow == ply.source.rowName);
    }

    private SAN() {
        // hidden
    }
}
