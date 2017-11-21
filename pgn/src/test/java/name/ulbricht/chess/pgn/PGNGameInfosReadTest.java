package name.ulbricht.chess.pgn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class PGNGameInfosReadTest {

    private static Stream<Arguments> createArguments() {
        return Stream.of(
                Arguments.of("Kasparov.pgn", 2083),
                Arguments.of("Wikipedia (de).pgn", 1),
                Arguments.of("Wikipedia (en).pgn", 1)
        );
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createArguments")
    public void read(String fileName, int gameCount) throws IOException {
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
}
