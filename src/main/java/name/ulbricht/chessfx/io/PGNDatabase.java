package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.io.antlr.PGNLexer;
import name.ulbricht.chessfx.io.antlr.PGNParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a PGN file to store games.
 */
public final class PGNDatabase {

    private static final Charset encoding = StandardCharsets.ISO_8859_1;

    public static PGNDatabase read(Path file) throws IOException {
        CharStream cs = CharStreams.fromPath(file, encoding);
        PGNLexer lexer = new PGNLexer(cs);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PGNParser parser = new PGNParser(tokens);

        ParseTreeWalker walker = new ParseTreeWalker();
        PGNListener listener = new PGNListener();
        walker.walk(listener, parser.parse());

        return listener.getDatabase();
    }

    private final List<PGNGame> games = new ArrayList<>();

    public List<PGNGame> getGames() {
        return this.games;
    }
}
