package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.*;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class PGNSimulationTest {

    @TestFactory
    Stream<DynamicTest> createTests() {
        String[] fileNames = {/*"Wikipedia (de).pgn", "Wikipedia (en).pgn",*/ "Kasparov.pgn"};

        return Stream.of(fileNames).flatMap(fileName -> {
            try {
                Path file = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
                return PGN.readGames(file).stream();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(game -> DynamicTest.dynamicTest(
                game.getEvent() + ", " + game.getSite() + ", " + game.getDate() + ", round " + game.getRound(),
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

            // check for check
            if (sanPly.check)
                assertTrue(game.getCheckState() == CheckState.CHECK || game.getCheckState() == CheckState.CHECKMATE, sanPly.toString());
            else assertEquals(CheckState.NONE, game.getCheckState(), sanPly.toString());
        }
    }
}
