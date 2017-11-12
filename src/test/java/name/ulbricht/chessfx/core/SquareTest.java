package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SquareTest {

    @Test
    public void testClone() {
        Square square = new Square(Coordinate.valueOf("a1"));
        square.setPiece(new Piece(Piece.Type.PAWN, Player.WHITE));

        Square clone = square.clone();

        assertFalse(square == clone);
        assertEquals(square, clone);

        assertEquals(square.getCoordinate(), clone.getCoordinate());
        assertEquals(square.getPiece(), clone.getPiece());
    }
}
