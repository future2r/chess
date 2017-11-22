package name.ulbricht.chess.pgn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class SANMoveTest {

    @Test
    void pawn() {
        SANMove move = SANMove.of("e4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void pawnCheck() {
        SANMove move = SANMove.of("e4+");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTo());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }

    @Test
    void pawnCaptures() {
        SANMove move = SANMove.of("dxe4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertEquals("d", move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void bishopCaptures() {
        SANMove move = SANMove.of("Bxe4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.BISHOP, move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousColumn() {
        SANMove move = SANMove.of("Ngf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.KNIGHT, move.getPiece());
        assertEquals("g", move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousRow() {
        SANMove move = SANMove.of("N5f3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.KNIGHT, move.getPiece());
        assertNull(move.getFromColumn());
        assertEquals("5", move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousFull() {
        SANMove move = SANMove.of("Qe2f3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.QUEEN, move.getPiece());
        assertEquals("e", move.getFromColumn());
        assertEquals("2", move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousColumnCaptures() {
        SANMove move = SANMove.of("Ngxf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.KNIGHT, move.getPiece());
        assertEquals("g", move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousRowCaptures() {
        SANMove move = SANMove.of("N5xf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.KNIGHT, move.getPiece());
        assertNull(move.getFromColumn());
        assertEquals("5", move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void disambiguousFullCaptures() {
        SANMove move = SANMove.of("Qe2xf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(SANMove.QUEEN, move.getPiece());
        assertEquals("e", move.getFromColumn());
        assertEquals("2", move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    void pawnPromotion() {
        SANMove move = SANMove.of("e8=Q");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertNull(move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e8", move.getTo());
        assertEquals(SANMove.QUEEN, move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void kignsideCastling() {
        SANMove move = SANMove.of("O-O");

        assertEquals(SANMove.Type.KINGSIDE_CASTLING, move.getType());
        assertEquals(SANMove.KING, move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertNull(move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void kignsideCastlingCheck() {
        SANMove move = SANMove.of("O-O+");

        assertEquals(SANMove.Type.KINGSIDE_CASTLING, move.getType());
        assertEquals(SANMove.KING, move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertNull(move.getTo());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }

    @Test
    void queensideCastling() {
        SANMove move = SANMove.of("O-O-O");

        assertEquals(SANMove.Type.QUEENSIDE_CASTLING, move.getType());
        assertEquals(SANMove.KING, move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertNull(move.getTo());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    void queensideCastlingCheck() {
        SANMove move = SANMove.of("O-O-O+");

        assertEquals(SANMove.Type.QUEENSIDE_CASTLING, move.getType());
        assertEquals(SANMove.KING, move.getPiece());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertNull(move.getTo());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }
}