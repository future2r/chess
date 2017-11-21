package name.ulbricht.chess.game;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class CoordinateTest {

    @Test
    public void testMoveLeft() {
        assertEquals(Coordinate.valueOf("g1"), Coordinate.valueOf("h1").moveLeft().get());
        assertEquals(Coordinate.valueOf("d5"), Coordinate.valueOf("e5").moveLeft().get());
        assertEquals(Coordinate.valueOf("a8"), Coordinate.valueOf("b8").moveLeft().get());
    }

    @Test
    public void testMoveLeft_Fail() {
        assertFalse(Coordinate.valueOf("a5").moveLeft().isPresent());
    }

    @Test
    public void testMoveRight() {
        assertEquals(Coordinate.valueOf("b1"), Coordinate.valueOf("a1").moveRight().get());
        assertEquals(Coordinate.valueOf("f5"), Coordinate.valueOf("e5").moveRight().get());
        assertEquals(Coordinate.valueOf("h8"), Coordinate.valueOf("g8").moveRight().get());
    }

    @Test
    public void testMoveRight_Fail() {
        assertFalse(Coordinate.valueOf("h5").moveRight().isPresent());
    }

    @Test
    public void testMoveUp() {
        assertEquals(Coordinate.valueOf("a2"), Coordinate.valueOf("a1").moveUp().get());
        assertEquals(Coordinate.valueOf("e6"), Coordinate.valueOf("e5").moveUp().get());
        assertEquals(Coordinate.valueOf("h8"), Coordinate.valueOf("h7").moveUp().get());
    }

    @Test
    public void testMoveUp_Fail() {
        assertFalse(Coordinate.valueOf("d8").moveUp().isPresent());
    }

    @Test
    public void testMoveDown() {
        assertEquals(Coordinate.valueOf("a7"), Coordinate.valueOf("a8").moveDown().get());
        assertEquals(Coordinate.valueOf("e4"), Coordinate.valueOf("e5").moveDown().get());
        assertEquals(Coordinate.valueOf("h1"), Coordinate.valueOf("h2").moveDown().get());
    }

    @Test
    public void testMoveDown_Fail() {
        assertFalse(Coordinate.valueOf("e1").moveDown().isPresent());
    }
}
