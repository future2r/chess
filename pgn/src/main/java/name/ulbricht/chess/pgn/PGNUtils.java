package name.ulbricht.chess.pgn;

final class PGNUtils {

    static PGNResult toResult(String s) {
        for (PGNResult result : PGNResult.values()) {
            if (result.getText().equals(s)) return result;
        }
        throw new IllegalArgumentException("Illegal value for result: " + s);
    }

    static String dequote(String s) {
        return s.replaceAll("^\"", "").replaceAll("\"$", "");
    }

    private PGNUtils() {
        // hidden
    }
}
