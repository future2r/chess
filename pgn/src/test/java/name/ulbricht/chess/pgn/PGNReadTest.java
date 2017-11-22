package name.ulbricht.chess.pgn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

final class PGNReadTest {

    private static Stream<Arguments> createReadGameInfosArguments() {
        return Stream.of(
                Arguments.of("Kasparov.pgn", 2083),
                Arguments.of("Wikipedia (de).pgn", 1),
                Arguments.of("Wikipedia (en).pgn", 1)
        );
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createReadGameInfosArguments")
    void readGameInfos(String fileName, int gameCount) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
        List<PGNGameInfo> gameInfos = PGN.readGameInfos(filePath);

        assertEquals(gameCount, gameInfos.size());

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

    private static Stream<Arguments> createReadSingleGameArguments() {
        return Stream.of(
                Arguments.of("Kasparov.pgn", 10),
                Arguments.of("Wikipedia (de).pgn", 0),
                Arguments.of("Wikipedia (en).pgn", 0)
        );
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createReadSingleGameArguments")
    void readSingleGame(String fileName, int index) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
        PGNGame game = PGN.readGame(filePath, index);

        assertNotNull(game);
    }

    @ParameterizedTest(name = "{index}: {0}")
    @ValueSource(strings = {"Kasparov.pgn", "Wikipedia (de).pgn", "Wikipedia (en).pgn"})
    void readAllGames(String fileName) throws IOException {
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
