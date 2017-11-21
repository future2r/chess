package name.ulbricht.chessfx.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public final class PGNGameReadTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Arrays.asList(
                new Object[]{"Kasparov.pgn", 10},
                new Object[]{"Wikipedia (de).pgn", 0},
                new Object[]{"Wikipedia (en).pgn", 0}
        );
    }

    private final Path fileName;
    private final int gameIndex;

    public PGNGameReadTest(String fileName, int gameIndex) {
        this.fileName = Paths.get(System.getProperty("user.dir"), "files").resolve(fileName);
        this.gameIndex = gameIndex;
    }

    @Test
    public void read() throws IOException {
        PGNGame game = PGN.readGame(this.fileName, this.gameIndex);

        assertNotNull(game);
    }
}
