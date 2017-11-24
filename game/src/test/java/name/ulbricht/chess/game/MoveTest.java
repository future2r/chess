package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class MoveTest {

    @Test
    void testSimpleNoCapture() {
        Game game = new Game();

        Coordinate source = Coordinate.d4;
        game.setup(FENPositions.of("8/8/8/8/3P4/8/8/8 w KQkq - 0 1"));

        Coordinate target = Coordinate.d5;

        Move move = Move.simple(source, target, null);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertNull(move.getCaptures());
    }

    @Test
    void testSimpleCaptures() {
        Game game = new Game();

        Coordinate source = Coordinate.d4;
        Coordinate target = Coordinate.d5;

        game.setup(FENPositions.of("8/8/8/3p4/3Q4/8/8/8 w KQkq - 0 1"));

        Move move = Move.simple(source, target, target);

        assertEquals(source, move.getSource());
        assertEquals(target, move.getTarget());
        assertEquals(target, move.getCaptures());
    }
}
