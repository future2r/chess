package name.ulbricht.chess.pgn;

public enum PGNResult {

    WHITE_WINS("1-0"),

    BLACK_WINS("0-1"),

    DRAWN_GAME("1/2-1/2"),

    NOT_FINISHED("*");

    private final String text;

    PGNResult(String text) {
        this.text = text;
    }

    String getText() {
        return this.text;
    }
}
