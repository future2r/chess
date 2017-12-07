package name.ulbricht.chess.pgn;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

final class PGNReadTest {

    private static Stream<Arguments> createReadAllGamesArguments() throws IOException {
        Path filesPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files");
        return Files.find(filesPath, 1, (file, attr) -> file.getFileName().toString().endsWith(".pgn"))
                .map(file -> Arguments.of(file));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("createReadAllGamesArguments")
    void readAllGames(Path file) throws IOException {
        PGN.readGames(file);
    }
}
