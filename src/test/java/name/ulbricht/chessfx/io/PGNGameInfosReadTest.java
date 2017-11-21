package name.ulbricht.chessfx.io;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public final class PGNGameInfosReadTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Arrays.asList(
                new Object[]{"Kasparov.pgn", 2083},
                new Object[]{"Wikipedia (de).pgn", 1},
                new Object[]{"Wikipedia (en).pgn", 1}
        );
    }

    private final Path fileName;
    private final int gameCount;

    public PGNGameInfosReadTest(String fileName, int gameCount) {
        this.fileName = Paths.get(System.getProperty("user.dir"), "files").resolve(fileName);
        this.gameCount = gameCount;
    }

    @Test
    public void read() throws IOException {
        List<PGNGameInfo> gameInfos = PGN.readGameInfos(this.fileName);

        assertEquals(this.gameCount, gameInfos.size());

        for (PGNGameInfo gameInfo : gameInfos) {
            assertNotNull(gameInfo.getEvent());
            assertNotNull(gameInfo.getSite());
            assertNotNull(gameInfo.getDate());
            assertNotNull(gameInfo.getRound());
            assertNotNull(gameInfo.getWhite());
            assertNotNull(gameInfo.getBlack());
            assertNotNull(gameInfo.getResult());
        }
    }
}
