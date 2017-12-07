package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

final class PGNSimulationTest {

    @TestFactory
    Stream<DynamicTest> createTests() throws IOException {
        Path filesPath = Paths.get(System.getProperty("user.dir")).getParent().resolve("files");

        return Files.find(filesPath, 1, (file, attr) -> file.getFileName().toString().endsWith(".pgn"))
                .flatMap(file -> {
                    try {
                        return PGN.readGames(file).stream();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }).map(game -> DynamicTest.dynamicTest(
                        game.getEvent() +
                                ", " + game.getSite() +
                                ", " + game.getDate() +
                                ", round " + game.getRound(),
                        () -> simulateGame(game)));
    }

    /**
     * Simulates a complete game. A game is successfully simulated if all noted plies can be found and performed as
     * valid plies.
     *
     * @param pgnGame a game from the database
     */
    private void simulateGame(PGNGame pgnGame) {
        Game game = new Game();

        for (SANPly sanPly : pgnGame.getPlies()) {
            Ply ply = SAN.findPly(game, sanPly);
            assertNotNull(ply, "SAN ply not found in valid game plies");

            game.performPly(ply);

            // check for check (that seems to be optional in PGN)
            if ((sanPly.check && game.getCheckState() == CheckState.NONE) ||
                    (!sanPly.check && game.getCheckState() != CheckState.NONE)) {
                System.out.println("WARNING: check state mismatch");
            }
        }
    }
}
