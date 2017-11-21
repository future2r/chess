package name.ulbricht.chess.pgn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class PGNGameReadAllTest {

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"Kasparov.pgn", "Wikipedia (de).pgn", "Wikipedia (en).pgn"})
    public void read(String fileName) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
        List<PGNGameInfo> gameInfos = PGN.readGameInfos(filePath);
        List<PGNGame> games = PGN.readGames(filePath, 0, gameInfos.size() - 1);

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
