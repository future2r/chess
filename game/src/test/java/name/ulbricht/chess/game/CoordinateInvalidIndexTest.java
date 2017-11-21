package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class CoordinateInvalidIndexTest {

    @Test
    public void testInvalidIndices() {

        int[] indices = new int[]{-10, -1, 64, 100};

        for (int index : indices) {
            try {
                Coordinate.valueOf(index);
                fail("IndexOutOfBoundsException expected");
            } catch (IndexOutOfBoundsException ex) {
                assertEquals(Integer.toString(index), ex.getMessage());
            }
        }
    }

    @Test
    public void testNullNames() {
        try {
            Coordinate.valueOf(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("Name is null", ex.getMessage());
        }
    }

    @Test
    public void testInvalidNames() {

        String[] names = {"", "a", "1", "a 1", "1a", "a9", "x1", "x9"};

        for (String name : names) {
            try {
                Coordinate.valueOf(name);
                fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException ex) {
                assertEquals("No enum constant name.ulbricht.chess.game.Coordinate." + name, ex.getMessage());
            }
        }
    }
}
