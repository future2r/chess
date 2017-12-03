package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class SANTest {

    @Test
    void pawnMove() {
        SAN.Ply ply = SAN.ply("e4");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.e4, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pawnMove_Check() {
        SAN.Ply ply = SAN.ply("c6+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.c6, ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void pawnMove_Promotion() {
        SAN.Ply ply = SAN.ply("b8=Q");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.b8, ply.target);
        assertEquals(PieceType.QUEEN, ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pawnMove_Promotion_Check() {
        SAN.Ply ply = SAN.ply("a1=Q+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.a1, ply.target);
        assertEquals(PieceType.QUEEN, ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void pawnCapture() {
        SAN.Ply ply = SAN.ply("dxe4");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals('d', ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.e4, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pawnCapture_Check() {
        SAN.Ply ply = SAN.ply("cxf8+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals('c', ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.f8, ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void pawnCapture_Promotion() {
        SAN.Ply ply = SAN.ply("axb1=R");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals('a', ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.b1, ply.target);
        assertEquals(PieceType.ROOK, ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pawnCapture_Promotion_Check() {
        SAN.Ply ply = SAN.ply("fxg8=N+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertNull(ply.piece);
        assertEquals('f', ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.g8, ply.target);
        assertEquals(PieceType.KNIGHT, ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void pieceMove() {
        SAN.Ply ply = SAN.ply("Nd2");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.KNIGHT, ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.d2, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pieceMove_Check() {
        SAN.Ply ply = SAN.ply("Bc2+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.BISHOP, ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.c2, ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void pieceMove_Column() {
        SAN.Ply ply = SAN.ply("Rae1");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.ROOK, ply.piece);
        assertEquals('a', ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.e1, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pieceMove_Row() {
        SAN.Ply ply = SAN.ply("N5f3");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.KNIGHT, ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals('5', ply.sourceRow);
        assertFalse(ply.captures);
        assertEquals(Coordinate.f3, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pieceCapture() {
        SAN.Ply ply = SAN.ply("Qxe7");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.QUEEN, ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.e7, ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void pieceCapture_Check() {
        SAN.Ply ply = SAN.ply("Rxd5+");

        assertEquals(SAN.PlyType.MOVE, ply.type);
        assertEquals(PieceType.ROOK, ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertTrue(ply.captures);
        assertEquals(Coordinate.d5, ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void kingSideCastling() {
        SAN.Ply ply = SAN.ply("O-O");

        assertEquals(SAN.PlyType.KING_SIDE_CASTLING, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertNull(ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void kingSideCastling_Check() {
        SAN.Ply ply = SAN.ply("O-O+");

        assertEquals(SAN.PlyType.KING_SIDE_CASTLING, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertNull(ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }

    @Test
    void queenSideCastling() {
        SAN.Ply ply = SAN.ply("O-O-O");

        assertEquals(SAN.PlyType.QUEEN_SIDE_CASTLING, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertNull(ply.target);
        assertNull(ply.promotion);
        assertFalse(ply.check);
    }

    @Test
    void queenSideCastling_Check() {
        SAN.Ply ply = SAN.ply("O-O-O+");

        assertEquals(SAN.PlyType.QUEEN_SIDE_CASTLING, ply.type);
        assertNull(ply.piece);
        assertEquals(0, ply.sourceColumn);
        assertEquals(0, ply.sourceRow);
        assertFalse(ply.captures);
        assertNull(ply.target);
        assertNull(ply.promotion);
        assertTrue(ply.check);
    }
}
