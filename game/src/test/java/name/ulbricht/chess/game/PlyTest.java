package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

final class PlyTest {

    @Test
    void move() {
        Ply ply = Ply.move(Piece.WHITE_PAWN, Coordinate.d4, Coordinate.d5);

        assertEquals(Piece.WHITE_PAWN, ply.piece);
        assertEquals(Coordinate.d4, ply.source);
        assertEquals(Coordinate.d5, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }

    @Test
    void moveAndCaptures() {
        Ply ply = Ply.moveAndCaptures(Piece.WHITE_QUEEN, Coordinate.d4, Coordinate.d5, Piece.BLACK_PAWN);

        assertEquals(Piece.WHITE_QUEEN, ply.piece);
        assertEquals(Coordinate.d4, ply.source);
        assertEquals(Coordinate.d5, ply.target);
        assertEquals(Coordinate.d5, ply.captures);
        assertEquals(Piece.BLACK_PAWN, ply.capturedPiece);
    }

    @Test
    void pawnDoubleAdvance() {
        Ply ply = Ply.pawnDoubleAdvance(Piece.WHITE_PAWN, Coordinate.d2);

        assertEquals(Piece.WHITE_PAWN, ply.piece);
        assertEquals(Coordinate.d2, ply.source);
        assertEquals(Coordinate.d4, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }

    @Test
    void whiteKingSideCastling() {
        Ply ply = Ply.kingSideCastling(Piece.WHITE_KING);

        assertEquals(Piece.WHITE_KING, ply.piece);
        assertEquals(Coordinate.e1, ply.source);
        assertEquals(Coordinate.g1, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }

    @Test
    void whiteQueenSideCastling() {
        Ply ply = Ply.queenSideCastling(Piece.WHITE_KING);

        assertEquals(Piece.WHITE_KING, ply.piece);
        assertEquals(Coordinate.e1, ply.source);
        assertEquals(Coordinate.b1, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }

    @Test
    void blackKingSideCastling() {
        Ply ply = Ply.kingSideCastling(Piece.BLACK_KING);

        assertEquals(Piece.BLACK_KING, ply.piece);
        assertEquals(Coordinate.e8, ply.source);
        assertEquals(Coordinate.g8, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }

    @Test
    void blackQueenSideCastling() {
        Ply ply = Ply.queenSideCastling(Piece.BLACK_KING);

        assertEquals(Piece.BLACK_KING, ply.piece);
        assertEquals(Coordinate.e8, ply.source);
        assertEquals(Coordinate.b8, ply.target);
        assertNull(ply.captures);
        assertNull(ply.capturedPiece);
    }
}
