package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class MoveTest {

    @Test
    public void testMove() {
        Coordinate from = Coordinate.valueOf("d4");
        Coordinate to = Coordinate.valueOf("d5");

        Move move = new Move(from, to, null);

        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertFalse(move.getCaptured().isPresent());
    }

    @Test
    public void testCaptureMove() {
        Coordinate from = Coordinate.valueOf("d4");
        Coordinate to = Coordinate.valueOf("d5");

        Move move = new Move(from, to, to);

        assertEquals(from, move.getFrom());
        assertEquals(to, move.getTo());
        assertEquals(to, move.getCaptured().get());
    }
}
