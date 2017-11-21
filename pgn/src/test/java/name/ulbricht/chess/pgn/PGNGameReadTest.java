package name.ulbricht.chess.pgn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class PGNGameReadTest {

    private static Stream<Arguments> createArguments() {
        return Stream.of(
                Arguments.of("Kasparov.pgn", 10),
                Arguments.of("Wikipedia (de).pgn", 0),
                Arguments.of("Wikipedia (en).pgn", 0)
        );
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createArguments")
    public void read(String fileName, int index) throws IOException {
        Path filePath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
        PGNGame game = PGN.readGame(filePath, index);

        assertNotNull(game);
    }
}
