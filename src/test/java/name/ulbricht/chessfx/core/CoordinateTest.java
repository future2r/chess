package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class CoordinateTest {

    @Test
    public void testMoveLeft() {
        assertEquals(Coordinate.valueOf("g1"), Coordinate.valueOf("h1").moveLeft());
        assertEquals(Coordinate.valueOf("d5"), Coordinate.valueOf("e5").moveLeft());
        assertEquals(Coordinate.valueOf("a8"), Coordinate.valueOf("b8").moveLeft());
    }

    @Test
    public void testMoveLeft_Fail() {
        try {
            Coordinate.valueOf("a5").moveLeft();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
            assertEquals("Cannot move left from a5", ex.getMessage());
        }
    }

    @Test
    public void testMoveRight() {
        assertEquals(Coordinate.valueOf("b1"), Coordinate.valueOf("a1").moveRight());
        assertEquals(Coordinate.valueOf("f5"), Coordinate.valueOf("e5").moveRight());
        assertEquals(Coordinate.valueOf("h8"), Coordinate.valueOf("g8").moveRight());
    }

    @Test
    public void testMoveRight_Fail() {
        try {
            Coordinate.valueOf("h5").moveRight();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
            assertEquals("Cannot move right from h5", ex.getMessage());
        }
    }

    @Test
    public void testMoveUp() {
        assertEquals(Coordinate.valueOf("a2"), Coordinate.valueOf("a1").moveUp());
        assertEquals(Coordinate.valueOf("e6"), Coordinate.valueOf("e5").moveUp());
        assertEquals(Coordinate.valueOf("h8"), Coordinate.valueOf("h7").moveUp());
    }

    @Test
    public void testMoveUp_Fail() {
        try {
            Coordinate.valueOf("d8").moveUp();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
            assertEquals("Cannot move up from d8", ex.getMessage());
        }
    }

    @Test
    public void testMoveDown() {
        assertEquals(Coordinate.valueOf("a7"), Coordinate.valueOf("a8").moveDown());
        assertEquals(Coordinate.valueOf("e4"), Coordinate.valueOf("e5").moveDown());
        assertEquals(Coordinate.valueOf("h1"), Coordinate.valueOf("h2").moveDown());
    }

    @Test
    public void testMoveDown_Fail() {
        try {
            Coordinate.valueOf("e1").moveDown();
            fail("IllegalStateException expected");
        } catch (IllegalStateException ex) {
            assertEquals("Cannot move down from e1", ex.getMessage());
        }
    }
}
