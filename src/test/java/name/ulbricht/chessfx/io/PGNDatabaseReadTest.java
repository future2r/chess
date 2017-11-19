package name.ulbricht.chessfx.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public final class PGNDatabaseReadTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Arrays.asList(
                new Object[]{"Kasparov.pgn", (Supplier) KasparovVerifier::new},
                new Object[]{"Wikipedia (de).pgn", (Supplier) Wikipedia_de::new},
                new Object[]{"Wikipedia (en).pgn", (Supplier) Wikipedia_en::new}
        );
    }

    private final Path fileName;
    private final Consumer<PGNDatabase> verifier;

    public PGNDatabaseReadTest(String fileName, Supplier<Consumer<PGNDatabase>> verifierFactory) {
        this.fileName = Paths.get(System.getProperty("user.dir"), "files").resolve(fileName);
        this.verifier = verifierFactory.get();
    }

    @Test
    public void read() throws IOException {
        PGNDatabase database = PGNDatabase.read(this.fileName);
        this.verifier.accept(database);
    }

    private static final class KasparovVerifier implements Consumer<PGNDatabase> {

        @Override
        public void accept(PGNDatabase db) {
            assertEquals(2083, db.getGames().size());

            PGNGame game = db.getGames().get(0);
            assertEquals("Wch U16", game.getTag(PGNGame.EVENT_TAG));
            assertEquals("Wattignies", game.getTag(PGNGame.SITE_TAG));
            assertEquals("1976.08.27", game.getTag(PGNGame.DATE_TAG));
            assertEquals("?", game.getTag(PGNGame.ROUND_TAG));
            assertEquals("Chandler, Murray G", game.getTag(PGNGame.WHITE_TAG));
            assertEquals("Kasparov, Gary", game.getTag(PGNGame.BLACK_TAG));
            assertEquals("1-0", game.getTag(PGNGame.RESULT_TAG));
        }
    }

    private static final class Wikipedia_de implements Consumer<PGNDatabase> {

        @Override
        public void accept(PGNDatabase db) {
            assertEquals(1, db.getGames().size());

            PGNGame game = db.getGames().get(0);
            assertEquals("IBM Kasparov vs. Deep Blue Rematch", game.getTag(PGNGame.EVENT_TAG));
            assertEquals("New York, NY USA", game.getTag(PGNGame.SITE_TAG));
            assertEquals("1997.05.11", game.getTag(PGNGame.DATE_TAG));
            assertEquals("6", game.getTag(PGNGame.ROUND_TAG));
            assertEquals("Deep Blue", game.getTag(PGNGame.WHITE_TAG));
            assertEquals("Kasparov, Garry", game.getTag(PGNGame.BLACK_TAG));
            assertEquals("1-0", game.getTag(PGNGame.RESULT_TAG));
        }
    }

    private static final class Wikipedia_en implements Consumer<PGNDatabase> {

        @Override
        public void accept(PGNDatabase db) {
            assertEquals(1, db.getGames().size());

            PGNGame game = db.getGames().get(0);
            assertEquals("F/S Return Match", game.getTag(PGNGame.EVENT_TAG));
            assertEquals("Belgrade, Serbia JUG", game.getTag(PGNGame.SITE_TAG));
            assertEquals("1992.11.04", game.getTag(PGNGame.DATE_TAG));
            assertEquals("29", game.getTag(PGNGame.ROUND_TAG));
            assertEquals("Fischer, Robert J.", game.getTag(PGNGame.WHITE_TAG));
            assertEquals("Spassky, Boris V.", game.getTag(PGNGame.BLACK_TAG));
            assertEquals("1/2-1/2", game.getTag(PGNGame.RESULT_TAG));
        }
    }
}
