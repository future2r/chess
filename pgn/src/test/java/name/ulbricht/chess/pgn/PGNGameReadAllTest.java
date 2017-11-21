package name.ulbricht.chess.pgn;

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
        this.fileName = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
    }

    @Test
    public void read() throws IOException {
        List<PGNGameInfo> gameInfos = PGN.readGameInfos(this.fileName);
        List<PGNGame> games = PGN.readGames(this.fileName, 0, gameInfos.size() - 1);

        assertEquals(gameInfos.size(), games.size());

        for (int i = 0; i < gameInfos.size(); i++) {
            PGNGameInfo info = gameInfos.get(i);
            PGNGame game = games.get(i);

            assertEquals(info.getEvent(), game.getInfo().getEvent());
            assertEquals(info.getSite(), game.getInfo().getSite());
            assertEquals(info.getDate(), game.getInfo().getDate());
            assertEquals(info.getRound(), game.getInfo().getRound());
            assertEquals(info.getWhite(), game.getInfo().getWhite());
            assertEquals(info.getBlack(), game.getInfo().getBlack());
            assertEquals(info.getResult(), game.getInfo().getResult());
        }
    }
}
