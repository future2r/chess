package name.ulbricht.chess.pgn;

import name.ulbricht.chess.pgn.antlr.PGNLexer;
import name.ulbricht.chess.pgn.antlr.PGNParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public final class PGN {

    static final String EVENT_TAG = "Event";
    static final String SITE_TAG = "Site";
    static final String DATE_TAG = "Date";
    static final String ROUND_TAG = "Round";
    static final String WHITE_TAG = "White";
    static final String BLACK_TAG = "Black";
    static final String RESULT_TAG = "Result";

    private static final Charset encoding = StandardCharsets.ISO_8859_1;

    public static List<PGNGameInfo> readGameInfos(Path file) throws IOException {
        return read(file, new PGNGameInfoListener());
    }

    public static PGNGame readGame(Path file, int index) throws IOException {
        List<PGNGame> games = readGames(file, index, index);
        if (games.isEmpty()) throw new IOException("No game found for index: " + index);
        return games.get(0);
    }

    public static List<PGNGame> readGames(Path file, int fromIndex, int toIndex) throws IOException {
        return read(file, new PGNGameListener(fromIndex, toIndex));
    }

    private static <T> T read(Path file, PGNDatabaseListener<T> listener) throws IOException {
        CharStream cs = CharStreams.fromPath(file, encoding);
        PGNLexer lexer = new PGNLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PGNParser parser = new PGNParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(listener, parser.parse());

        return listener.getData();
    }
}
