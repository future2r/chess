package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class MoveTest {

    @Test
    void testSimpleNoCapture() {
        Game game = new Game();
        game.setup(FENPositions.of("8/8/8/8/3P4/8/8/8 w KQkq - 0 1"));

        Move move = Move.simple(Player.WHITE, Coordinate.d4, Coordinate.d5);

        assertEquals(Coordinate.d4, move.getSource());
        assertEquals(Coordinate.d5, move.getTarget());
        assertNull(move.getCaptures());
    }

    @Test
    void testSimpleCaptures() {
        Game game = new Game();
        game.setup(FENPositions.of("8/8/8/3p4/3Q4/8/8/8 w KQkq - 0 1"));

        Move move = Move.simpleCaptures(Player.WHITE, Coordinate.d4, Coordinate.d5);

        assertEquals(Coordinate.d4, move.getSource());
        assertEquals(Coordinate.d5, move.getTarget());
        assertEquals(Coordinate.d5, move.getCaptures());
    }

    @Test
    void testpawnDoubleAdvance() {
        Game game = new Game();
        game.setup(FENPositions.of("8/8/8/8/8/8/3P4/8 w KQkq - 0 1"));

        Move move = Move.pawnDoubleAdvance(Player.WHITE, Coordinate.d2);

        assertEquals(Coordinate.d2, move.getSource());
        assertEquals(Coordinate.d4, move.getTarget());
        assertNull(move.getCaptures());
    }
}
