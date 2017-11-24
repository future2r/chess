package name.ulbricht.chess.pgn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class SANMoveTest {

    @Test
    void pawn() {
        SANPly move = SANPly.of("e4");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void pawnCheck() {
        SANPly move = SANPly.of("e4+");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTarget());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }

    @Test
    void pawnCaptures() {
        SANPly move = SANPly.of("dxe4");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertEquals("d", move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void bishopCaptures() {
        SANPly move = SANPly.of("Bxe4");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.BISHOP, move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousColumn() {
        SANPly move = SANPly.of("Ngf3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.KNIGHT, move.getPiece());
        assertEquals("g", move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousRow() {
        SANPly move = SANPly.of("N5f3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.KNIGHT, move.getPiece());
        assertNull(move.getSourceColumn());
        assertEquals("5", move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousFull() {
        SANPly move = SANPly.of("Qe2f3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.QUEEN, move.getPiece());
        assertEquals("e", move.getSourceColumn());
        assertEquals("2", move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousColumnCaptures() {
        SANPly move = SANPly.of("Ngxf3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.KNIGHT, move.getPiece());
        assertEquals("g", move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousRowCaptures() {
        SANPly move = SANPly.of("N5xf3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.KNIGHT, move.getPiece());
        assertNull(move.getSourceColumn());
        assertEquals("5", move.getSourceRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousFullCaptures() {
        SANPly move = SANPly.of("Qe2xf3");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertEquals(SANPly.QUEEN, move.getPiece());
        assertEquals("e", move.getSourceColumn());
        assertEquals("2", move.getSourceRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void pawnPromotion() {
        SANPly move = SANPly.of("e8=Q");

        assertEquals(SANPly.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertEquals("e8", move.getTarget());
        assertEquals(SANPly.QUEEN, move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void kignsideCastling() {
        SANPly move = SANPly.of("O-O");

        assertEquals(SANPly.Type.KING_SIDE_CASTLING, move.getType());
        assertEquals(SANPly.KING, move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertNull(move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void kignsideCastlingCheck() {
        SANPly move = SANPly.of("O-O+");

        assertEquals(SANPly.Type.KING_SIDE_CASTLING, move.getType());
        assertEquals(SANPly.KING, move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertNull(move.getTarget());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }

    @Test
    void queensideCastling() {
        SANPly move = SANPly.of("O-O-O");

        assertEquals(SANPly.Type.QUEEN_SIDE_CASTLING, move.getType());
        assertEquals(SANPly.KING, move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertNull(move.getTarget());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void queensideCastlingCheck() {
        SANPly move = SANPly.of("O-O-O+");

        assertEquals(SANPly.Type.QUEEN_SIDE_CASTLING, move.getType());
        assertEquals(SANPly.KING, move.getPiece());
        assertNull(move.getSourceColumn());
        assertNull(move.getSourceRow());
        assertFalse(move.isCapture());
        assertNull(move.getTarget());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }
}