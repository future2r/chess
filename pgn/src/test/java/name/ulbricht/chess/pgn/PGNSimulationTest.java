package name.ulbricht.chess.pgn;

import name.ulbricht.chess.game.CheckState;
import name.ulbricht.chess.game.Game;
import name.ulbricht.chess.game.Ply;
import name.ulbricht.chess.game.SAN;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class PGNSimulationTest {

    @TestFactory
    public Stream<DynamicTest> createTests() {
        String[] fileNames = {"Wikipedia (de).pgn", "Wikipedia (en).pgn"};

        return Stream.of(fileNames).flatMap(fileName -> {
            try {
                Path file = Paths.get(System.getProperty("user.dir")).getParent().resolve("files").resolve(fileName);
                return PGN.readGames(file).stream();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).map(game -> DynamicTest.dynamicTest(game.getEvent(), () -> simulateGame(game)));
    }

    /**
     * Simulates a complete game. A game is successfully simulated if all noted plies can be found and performed as
     * valid plies.
     *
     * @param pgnGame a game from the database
     */
    private void simulateGame(PGNGame pgnGame) {
        Game game = new Game();

        for (SAN.Ply sanPly : pgnGame.getPlies()) {
            Ply ply = findPly(game, sanPly);

            game.performPly(ply);

            if (sanPly.check) assertEquals(CheckState.CHECK, game.getCheckState());
            else assertEquals(CheckState.NONE, game.getCheckState());
        }
    }

    /**
     * Find a valid ply for the active player that matches the noted ply from the database.
     *
     * @param game   the current game
     * @param sanPly the noted SAN ply
     * @return a matching ply the is valid for the active player in the current game.
     */
    private Ply findPly(Game game, SAN.Ply sanPly) {
        List<Ply> validPlies = game.getValidPlies();
        assertFalse(validPlies.isEmpty(), "No more valid moves in game.");


        fail("No valid ply found for SAN ply: " + sanPly);
        return null;
    }
}
