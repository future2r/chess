package name.ulbricht.chessfx.io;

import name.ulbricht.chessfx.core.PieceType;
import org.junit.Test;

import static org.junit.Assert.*;

public final class SANMoveTest {

    @Test
    public void pawn() {
        SANMove move = SANMove.of("e4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.PAWN, move.getPieceType());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void pawnCheck() {
        SANMove move = SANMove.of("e4+");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.PAWN, move.getPieceType());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e4", move.getTo().toString());
        assertNull(move.getPromotion());
        assertTrue(move.isCheck());
    }

    @Test
    public void pawnCaptures() {
        SANMove move = SANMove.of("dxe4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.PAWN, move.getPieceType());
        assertEquals("d", move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void bishopCaptures() {
        SANMove move = SANMove.of("Bxe4");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.BISHOP, move.getPieceType());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("e4", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousColumn() {
        SANMove move = SANMove.of("Ngf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.KNIGHT, move.getPieceType());
        assertEquals("g", move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousRow() {
        SANMove move = SANMove.of("N5f3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.KNIGHT, move.getPieceType());
        assertNull(move.getFromColumn());
        assertEquals("5", move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousFull() {
        SANMove move = SANMove.of("Qe2f3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.QUEEN, move.getPieceType());
        assertEquals("e", move.getFromColumn());
        assertEquals("2", move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousColumnCaptures() {
        SANMove move = SANMove.of("Ngxf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.KNIGHT, move.getPieceType());
        assertEquals("g", move.getFromColumn());
        assertNull(move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousRowCaptures() {
        SANMove move = SANMove.of("N5xf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.KNIGHT, move.getPieceType());
        assertNull(move.getFromColumn());
        assertEquals("5", move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void disambiguousFullCaptures() {
        SANMove move = SANMove.of("Qe2xf3");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.QUEEN, move.getPieceType());
        assertEquals("e", move.getFromColumn());
        assertEquals("2", move.getFromRow());
        assertTrue(move.isCapture());
        assertEquals("f3", move.getTo().toString());
        assertNull(move.getPromotion());
        assertFalse(move.isCheck());
    }


    @Test
    public void pawnPromotion() {
        SANMove move = SANMove.of("e8=Q");

        assertEquals(SANMove.Type.DEFAULT, move.getType());
        assertEquals(PieceType.PAWN, move.getPieceType());
        assertNull(move.getFromColumn());
        assertNull(move.getFromRow());
        assertFalse(move.isCapture());
        assertEquals("e8", move.getTo().toString());
        assertEquals(PieceType.QUEEN, move.getPromotion());
        assertFalse(move.isCheck());
    }

    @Test
    public void kignsideCastling() {
        SANMove move = SANMove.of("O-O");

        assertEquals(SANMove.Type.KINGSIDE_CASTLING, move.getType());
        assertEquals(PieceType.KING, move.getPieceType());
        assertNull(move.getTo());
        assertFalse(move.isCheck());
    }

    @Test
    public void kignsideCastlingCheck() {
        SANMove move = SANMove.of("O-O+");

        assertEquals(SANMove.Type.KINGSIDE_CASTLING, move.getType());
        assertEquals(PieceType.KING, move.getPieceType());
        assertNull(move.getTo());
        assertTrue(move.isCheck());
    }

    @Test
    public void queensideCastling() {
        SANMove move = SANMove.of("O-O-O");

        assertEquals(SANMove.Type.QUEENSIDE_CASTLING, move.getType());
        assertEquals(PieceType.KING, move.getPieceType());
        assertNull(move.getTo());
        assertFalse(move.isCheck());
    }

    @Test
    public void queensideCastlingCheck() {
        SANMove move = SANMove.of("O-O-O+");

        assertEquals(SANMove.Type.QUEENSIDE_CASTLING, move.getType());
        assertEquals(PieceType.KING, move.getPieceType());
        assertNull(move.getTo());
        assertTrue(move.isCheck());
    }
}