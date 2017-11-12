package name.ulbricht.chessfx.core.io;

import name.ulbricht.chessfx.core.Game;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a game in the Portable Game Notation (PGN). Although this notation permits multiple games in one file,
 * this implementation only supports writing of one game and reading of the first game in the file.
 */
public final class PGNFile {

    private static final Charset encoding = StandardCharsets.ISO_8859_1;
    private static final int MAX_LINE_LENGHT = 255;
    private static final int PREFERRED_LINE_LENGHT = 80;
    private static final char INLINE_COMMENT_START = ';';
    private static final char BRACE_COMMENT_START = '{';
    private static final char BRACHE_COMMENT_END = '}';
    private static final char LINE_COMMENT_START = '%';
    private static final char STRING_QUOTE = '"';
    private static final char STRING_ESCAPE = '\\';

    private static final char TAG_PAIR_START = '[';
    private static final char TAG_PAIR_END = ']';
    private static final char TAG_PAIR_SEPARATOR = ' ';

    private static final String LINE_BREAK = System.getProperty("line.separator");

    enum DefaultTag {
        EVENT("Event"),
        SITE("Site"),
        GAME_START_DATE("Date"),
        ROUND("Round"),
        WHITE_PLAYER_NAME("White"),
        BLACK_PLAYER_NAME("Black"),
        GAME_RESULT("Result");

        private final String tagName;

        DefaultTag(String tagName) {
            this.tagName = tagName;
        }

        public String getTagName() {
            return this.tagName;
        }
    }

    public enum GameResult {
        WHITE_WINS("1-0"),
        BLACK_WINS("0-1"),
        DRAWN("1/2-1/2"),
        IN_PROGRESS("*");

        private final String value;

        GameResult(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private final Map<String, Object> tags = new HashMap<>();

    public PGNFile() {
        // init default values for default tags
        setEvent("Unknown");
        setSite("Unknown");
        setGameStartDate("????.??.??");
        setRound("1");
        setWhitePlayerName("Unknown White Player");
        setBlackPlayerName("Unknown Black Player");
        setGameResult(GameResult.IN_PROGRESS);
    }

    public <T> T getTag(String key) {
        return (T) this.tags.get(key);
    }

    public void setTag(String key, Object value) {
        this.tags.put(key, value);
    }

    public String getEvent() {
        return this.getTag(DefaultTag.EVENT.getTagName());
    }

    public void setEvent(String event) {
        setTag(DefaultTag.EVENT.getTagName(), event);
    }

    public String getSite() {
        return this.getTag(DefaultTag.SITE.getTagName());
    }

    public void setSite(String site) {
        setTag(DefaultTag.SITE.getTagName(), site);
    }

    public String getGameStartDate() {
        return this.getTag(DefaultTag.GAME_START_DATE.getTagName());
    }

    public void setGameStartDate(String gameStartDate) {
        setTag(DefaultTag.GAME_START_DATE.getTagName(), gameStartDate);
    }

    public String getRound() {
        return this.getTag(DefaultTag.ROUND.getTagName());
    }

    public void setRound(String round) {
        setTag(DefaultTag.ROUND.getTagName(), round);
    }

    public String getWhitePlayerName() {
        return this.getTag(DefaultTag.WHITE_PLAYER_NAME.getTagName());
    }

    public void setWhitePlayerName(String whitePlayerName) {
        setTag(DefaultTag.WHITE_PLAYER_NAME.getTagName(), whitePlayerName);
    }

    public String getBlackPlayerName() {
        return this.getTag(DefaultTag.BLACK_PLAYER_NAME.getTagName());
    }

    public void setBlackPlayerName(String blackPlayerName) {
        setTag(DefaultTag.BLACK_PLAYER_NAME.getTagName(), blackPlayerName);
    }

    public GameResult getGameResult() {
        return getTag(DefaultTag.GAME_RESULT.getTagName());
    }

    public void setGameResult(GameResult gameResult) {
        setTag(DefaultTag.GAME_RESULT.getTagName(), Objects.requireNonNull(gameResult, "gameResult cannot be null"));
    }

    public void write(Path fileName, Game game) throws IOException {
        try (OutputStream out = Files.newOutputStream(fileName)) {
            write(out, game);
        }
    }

    public void write(OutputStream out, Game game) throws IOException {
        try (Writer writer = new OutputStreamWriter(out)) {
            // write standard tags
            writeTag(writer, DefaultTag.EVENT.getTagName(), getEvent());
            writeTag(writer, DefaultTag.SITE.getTagName(), getSite());
            writeTag(writer, DefaultTag.GAME_START_DATE.getTagName(), getGameStartDate());
            writeTag(writer, DefaultTag.ROUND.getTagName(), getRound());
            writeTag(writer, DefaultTag.WHITE_PLAYER_NAME.getTagName(), getWhitePlayerName());
            writeTag(writer, DefaultTag.BLACK_PLAYER_NAME.getTagName(), getBlackPlayerName());
            writeTag(writer, DefaultTag.GAME_RESULT.getTagName(), getGameResult().getValue());

            // write other tags
            List<String> defaultTagNames = Stream.of(DefaultTag.values())
                    .map(t -> t.getTagName())
                    .collect(Collectors.toList());
            this.tags.entrySet().stream()
                    .filter(e -> !defaultTagNames.contains(e.getKey()))
                    .forEach(e -> writeTag(writer, e.getKey(), e.getValue().toString()));
        }
    }

    private void writeTag(Writer writer, String key, String value) {
        try {
            writer.write(TAG_PAIR_START);
            writer.write(key);
            writer.write(TAG_PAIR_SEPARATOR);
            writer.write(STRING_QUOTE);
            writer.write(quoteString(value));
            writer.write(STRING_QUOTE);
            writer.write(TAG_PAIR_END);
            writer.write(LINE_BREAK);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String quoteString(String s) {
        return s.replaceAll("\"", "\\\"");
    }
}
