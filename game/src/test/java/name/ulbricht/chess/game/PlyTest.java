package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class PlyTest {

    @Test
    void testMove() {
        Ply ply = Ply.move(Piece.WHITE_PAWN, Coordinate.d4, Coordinate.d5);

        assertEquals(Piece.WHITE_PAWN, ply.getPiece());
        assertEquals(Coordinate.d4, ply.getSource());
        assertEquals(Coordinate.d5, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void testSimpleCaptures() {
        Ply ply = Ply.captures(Piece.WHITE_QUEEN, Coordinate.d4, Coordinate.d5, Piece.BLACK_PAWN);

        assertEquals(Piece.WHITE_QUEEN, ply.getPiece());
        assertEquals(Coordinate.d4, ply.getSource());
        assertEquals(Coordinate.d5, ply.getTarget());
        assertEquals(Coordinate.d5, ply.getCaptures());
        assertEquals(Piece.BLACK_PAWN, ply.getCapturedPiece());
    }

    @Test
    void testPawnDoubleAdvance() {
        Game game = new Game(FENSetup.of("8/8/8/8/8/8/3P4/8 w KQkq - 0 1"));

        Ply ply = Ply.pawnDoubleAdvance(Piece.WHITE_PAWN, Coordinate.d2);

        assertEquals(Piece.WHITE_PAWN, ply.getPiece());
        assertEquals(Coordinate.d2, ply.getSource());
        assertEquals(Coordinate.d4, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }
}
