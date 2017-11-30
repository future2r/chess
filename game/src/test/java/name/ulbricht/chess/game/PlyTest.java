package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class PlyTest {

    @Test
    void move() {
        Ply ply = Ply.move(Piece.WHITE_PAWN, Coordinate.d4, Coordinate.d5);

        assertEquals(Piece.WHITE_PAWN, ply.getPiece());
        assertEquals(Coordinate.d4, ply.getSource());
        assertEquals(Coordinate.d5, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void moveAndCaptures() {
        Ply ply = Ply.moveAndCaptures(Piece.WHITE_QUEEN, Coordinate.d4, Coordinate.d5, Piece.BLACK_PAWN);

        assertEquals(Piece.WHITE_QUEEN, ply.getPiece());
        assertEquals(Coordinate.d4, ply.getSource());
        assertEquals(Coordinate.d5, ply.getTarget());
        assertEquals(Coordinate.d5, ply.getCaptures());
        assertEquals(Piece.BLACK_PAWN, ply.getCapturedPiece());
    }

    @Test
    void pawnDoubleAdvance() {
        Ply ply = Ply.pawnDoubleAdvance(Piece.WHITE_PAWN, Coordinate.d2);

        assertEquals(Piece.WHITE_PAWN, ply.getPiece());
        assertEquals(Coordinate.d2, ply.getSource());
        assertEquals(Coordinate.d4, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void whiteKingSideCastling() {
        Ply ply = Ply.kingSideCastling(Piece.WHITE_KING);

        assertEquals(Piece.WHITE_KING, ply.getPiece());
        assertEquals(Coordinate.e1, ply.getSource());
        assertEquals(Coordinate.g1, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void whiteQueenSideCastling() {
        Ply ply = Ply.queenSideCastling(Piece.WHITE_KING);

        assertEquals(Piece.WHITE_KING, ply.getPiece());
        assertEquals(Coordinate.e1, ply.getSource());
        assertEquals(Coordinate.b1, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void blackKingSideCastling() {
        Ply ply = Ply.kingSideCastling(Piece.BLACK_KING);

        assertEquals(Piece.BLACK_KING, ply.getPiece());
        assertEquals(Coordinate.e8, ply.getSource());
        assertEquals(Coordinate.g8, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }

    @Test
    void blackQueenSideCastling() {
        Ply ply = Ply.queenSideCastling(Piece.BLACK_KING);

        assertEquals(Piece.BLACK_KING, ply.getPiece());
        assertEquals(Coordinate.e8, ply.getSource());
        assertEquals(Coordinate.b8, ply.getTarget());
        assertNull(ply.getCaptures());
        assertNull(ply.getCapturedPiece());
    }
}
