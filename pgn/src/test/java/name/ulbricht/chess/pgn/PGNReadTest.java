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

final class PGNReadTest {

    private static Stream<Arguments> createReadAllGamesArguments() {
        return Stream.of(
                Arguments.of("Kasparov.pgn", 2083),
                Arguments.of("Wikipedia (de).pgn", 1),
                Arguments.of("Wikipedia (en).pgn", 1)
        );
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createReadAllGamesArguments")
    void readAllGames(String fileName, int gameNumber) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
        List<PGNGame> games = PGN.readGames(filePath);

        assertEquals(gameNumber, games.size());
    }
}
