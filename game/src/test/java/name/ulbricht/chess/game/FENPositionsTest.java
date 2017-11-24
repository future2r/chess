package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FENPositionsTest {

    @Test
    public void testDefault() {
        FENPositions fen = FENPositions.ofDefault();

        assertEquals(Piece.BLACK_ROOK, fen.getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, fen.getPiece(Coordinate.b8));
        assertEquals(Piece.BLACK_BISHOP, fen.getPiece(Coordinate.c8));
        assertEquals(Piece.BLACK_QUEEN, fen.getPiece(Coordinate.d8));
        assertEquals(Piece.BLACK_KING, fen.getPiece(Coordinate.e8));
        assertEquals(Piece.BLACK_BISHOP, fen.getPiece(Coordinate.f8));
        assertEquals(Piece.BLACK_KNIGHT, fen.getPiece(Coordinate.g8));
        assertEquals(Piece.BLACK_ROOK, fen.getPiece(Coordinate.h8));

        assertPiece(fen, Piece.BLACK_PAWN, Coordinate.a7, Coordinate.h7);

        assertEmpty(fen, Coordinate.a6, Coordinate.h3);

        assertPiece(fen, Piece.WHITE_PAWN, Coordinate.a2, Coordinate.h2);

        assertEquals(Piece.WHITE_ROOK, fen.getPiece(Coordinate.a1));
        assertEquals(Piece.WHITE_KNIGHT, fen.getPiece(Coordinate.b1));
        assertEquals(Piece.WHITE_BISHOP, fen.getPiece(Coordinate.c1));
        assertEquals(Piece.WHITE_QUEEN, fen.getPiece(Coordinate.d1));
        assertEquals(Piece.WHITE_KING, fen.getPiece(Coordinate.e1));
        assertEquals(Piece.WHITE_BISHOP, fen.getPiece(Coordinate.f1));
        assertEquals(Piece.WHITE_KNIGHT, fen.getPiece(Coordinate.g1));
        assertEquals(Piece.WHITE_ROOK, fen.getPiece(Coordinate.h1));

        assertEquals(Player.WHITE, fen.getActivePlayer());

        assertTrue(fen.isWhiteKingSideCastlingAvailable());
        assertTrue(fen.isWhiteQueenSideCastlingAvailable());
        assertTrue(fen.isBlackKingSideCastlingAvailable());
        assertTrue(fen.isBlackQueenSideCastlingAvailable());

        assertNull(fen.getEnPassantTarget());

        assertEquals(0, fen.getHalfMoveClock());
        assertEquals(1, fen.getFullMoveNumber());
    }

    @Test
    public void testSpecial() {
        FENPositions fen = FENPositions.of("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");

        assertEmpty(fen, Coordinate.a8, Coordinate.d8);
        assertEquals(Piece.BLACK_KING, fen.getPiece(Coordinate.e8));
        assertEmpty(fen, Coordinate.f8, Coordinate.d2);
        assertEquals(Piece.WHITE_PAWN, fen.getPiece(Coordinate.e2));
        assertEmpty(fen, Coordinate.f2, Coordinate.d1);
        assertEquals(Piece.WHITE_KING, fen.getPiece(Coordinate.e1));
        assertEmpty(fen, Coordinate.f1, Coordinate.h1);

        assertEquals(Player.WHITE, fen.getActivePlayer());

        assertFalse(fen.isWhiteKingSideCastlingAvailable());
        assertFalse(fen.isWhiteQueenSideCastlingAvailable());
        assertFalse(fen.isBlackKingSideCastlingAvailable());
        assertFalse(fen.isBlackQueenSideCastlingAvailable());

        assertNull(fen.getEnPassantTarget());

        assertEquals(5, fen.getHalfMoveClock());
        assertEquals(39, fen.getFullMoveNumber());
    }

    @Test
    public void testPieces() {
        assertEquals(Piece.WHITE_PAWN, FENPositions.of("P7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_ROOK, FENPositions.of("R7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KNIGHT, FENPositions.of("N7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_BISHOP, FENPositions.of("B7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_QUEEN, FENPositions.of("Q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KING, FENPositions.of("K7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));

        assertEquals(Piece.BLACK_PAWN, FENPositions.of("p7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_ROOK, FENPositions.of("r7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, FENPositions.of("n7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_BISHOP, FENPositions.of("b7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_QUEEN, FENPositions.of("q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KING, FENPositions.of("k7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
    }

    @Test
    public void testPlayer() {
        assertEquals(Player.WHITE, FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 1").getActivePlayer());
        assertEquals(Player.BLACK, FENPositions.of("8/8/8/8/8/8/8/8 b - - 0 1").getActivePlayer());
    }

    @Test
    public void testCastling() {
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 1"), false, false, false, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w KQkq - 0 1"), true, true, true, true);

        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w KQ - 0 1"), true, true, false, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w kq - 0 1"), false, false, true, true);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w Kk - 0 1"), true, false, true, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w Qq - 0 1"), false, true, false, true);

        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w K - 0 1"), true, false, false, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w Q - 0 1"), false, true, false, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w k - 0 1"), false, false, true, false);
        assertCastling(FENPositions.of("8/8/8/8/8/8/8/8 w q - 0 1"), false, false, false, true);
    }

    @Test
    public void testEnPassant() {
        assertNull(FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 1").getEnPassantTarget());

        assertEquals(Coordinate.c3, FENPositions.of("8/8/8/8/8/8/8/8 b - c3 0 1").getEnPassantTarget());
        assertEquals(Coordinate.d6, FENPositions.of("8/8/8/8/8/8/8/8 w - d6 0 1").getEnPassantTarget());

        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - c4 0 1"));
        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - c3 0 1"));
    }

    @Test
    public void testHalfMoveClock() {
        assertEquals(0, FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 1").getHalfMoveClock());
        assertEquals(42, FENPositions.of("8/8/8/8/8/8/8/8 w - - 42 1").getHalfMoveClock());

        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - - -1 1"));
        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - - x 1"));
    }

    @Test
    public void testFullMoveNumber() {
        assertEquals(1, FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 1").getFullMoveNumber());
        assertEquals(42, FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 42").getFullMoveNumber());

        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 0"));
        assertThrows(IllegalArgumentException.class, () -> FENPositions.of("8/8/8/8/8/8/8/8 w - - 0 x"));
    }

    private static void assertPiece(FENPositions fen, Piece piece, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertEquals(piece, fen.getPiece(c)));
    }

    private static void assertEmpty(FENPositions fen, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertNull(fen.getPiece(c)));
    }

    private static void assertCastling(FENPositions fen, boolean whiteKingSide, boolean whiteQueenSide,
                                       boolean blackKingSide, boolean blackQueenSide) {
        assertEquals(whiteKingSide, fen.isWhiteKingSideCastlingAvailable());
        assertEquals(whiteQueenSide, fen.isWhiteQueenSideCastlingAvailable());
        assertEquals(blackKingSide, fen.isBlackKingSideCastlingAvailable());
        assertEquals(blackQueenSide, fen.isBlackQueenSideCastlingAvailable());
    }
}
