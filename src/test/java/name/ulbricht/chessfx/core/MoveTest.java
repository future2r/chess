package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MoveTest {

    @Test
    public void testMove() {
        Square fromSquare = new Square(Coordinate.valueOf("d4"));
        fromSquare.setPiece(new Piece(Piece.Type.QUEEN, Player.WHITE));

        Square toSquare = new Square(Coordinate.valueOf("d5"));

        Move move = new Move(fromSquare, toSquare);

        assertEquals(fromSquare, move.getFromSquare());
        assertEquals(toSquare, move.getToSquare());
        assertNull(move.getCapturedSquare());
    }

    @Test
    public void testCaptureMove() {
        Square fromSquare = new Square(Coordinate.valueOf("d4"));
        fromSquare.setPiece(new Piece(Piece.Type.QUEEN, Player.WHITE));

        Square toSquare = new Square(Coordinate.valueOf("d5"));
        toSquare.setPiece(new Piece(Piece.Type.PAWN, Player.BLACK));

        Move move = new Move(fromSquare, toSquare);

        assertEquals(fromSquare, move.getFromSquare());
        assertEquals(toSquare, move.getToSquare());
        assertEquals(toSquare, move.getCapturedSquare());
    }
}
