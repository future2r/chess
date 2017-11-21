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

import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public final class PGNGameReadAllTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Arrays.asList(
                new Object[]{"Kasparov.pgn"},
                new Object[]{"Wikipedia (de).pgn"},
                new Object[]{"Wikipedia (en).pgn"}
        );
    }

    private final Path fileName;

    public PGNGameReadAllTest(String fileName) {
        this.fileName = Paths.get(System.getProperty("user.dir"), "files").resolve(fileName);
    }

    @Test
    public void read() throws IOException {
        List<PGNGameInfo> gameInfos = PGN.readGameInfos(this.fileName);

        for (int i = 0; i < gameInfos.size(); i++) {
            PGNGame game = PGN.readGame(this.fileName, i);
            assertNotNull(game);
        }
    }
}
