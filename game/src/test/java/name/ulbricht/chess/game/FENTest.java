package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

final class FENTest {

    @Test
    void testDefault() {
        Board state = FEN.createBoard(FEN.INITIAL);

        assertEquals(Piece.BLACK_ROOK, state.getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, state.getPiece(Coordinate.b8));
        assertEquals(Piece.BLACK_BISHOP, state.getPiece(Coordinate.c8));
        assertEquals(Piece.BLACK_QUEEN, state.getPiece(Coordinate.d8));
        assertEquals(Piece.BLACK_KING, state.getPiece(Coordinate.e8));
        assertEquals(Piece.BLACK_BISHOP, state.getPiece(Coordinate.f8));
        assertEquals(Piece.BLACK_KNIGHT, state.getPiece(Coordinate.g8));
        assertEquals(Piece.BLACK_ROOK, state.getPiece(Coordinate.h8));

        assertPiece(state, Piece.BLACK_PAWN, Coordinate.a7, Coordinate.h7);

        assertEmpty(state, Coordinate.a6, Coordinate.h3);

        assertPiece(state, Piece.WHITE_PAWN, Coordinate.a2, Coordinate.h2);

        assertEquals(Piece.WHITE_ROOK, state.getPiece(Coordinate.a1));
        assertEquals(Piece.WHITE_KNIGHT, state.getPiece(Coordinate.b1));
        assertEquals(Piece.WHITE_BISHOP, state.getPiece(Coordinate.c1));
        assertEquals(Piece.WHITE_QUEEN, state.getPiece(Coordinate.d1));
        assertEquals(Piece.WHITE_KING, state.getPiece(Coordinate.e1));
        assertEquals(Piece.WHITE_BISHOP, state.getPiece(Coordinate.f1));
        assertEquals(Piece.WHITE_KNIGHT, state.getPiece(Coordinate.g1));
        assertEquals(Piece.WHITE_ROOK, state.getPiece(Coordinate.h1));

        assertEquals(Player.WHITE, state.getActivePlayer());

        assertTrue(state.isWhiteKingSideCastlingAvailable());
        assertTrue(state.isWhiteQueenSideCastlingAvailable());
        assertTrue(state.isBlackKingSideCastlingAvailable());
        assertTrue(state.isBlackQueenSideCastlingAvailable());

        assertNull(state.getEnPassantTarget());

        assertEquals(0, state.getHalfMoveClock());
        assertEquals(1, state.getFullMoveNumber());
    }

    @Test
    void testSpecial() {
        Board state = FEN.createBoard("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");

        assertEmpty(state, Coordinate.a8, Coordinate.d8);
        assertEquals(Piece.BLACK_KING, state.getPiece(Coordinate.e8));
        assertEmpty(state, Coordinate.f8, Coordinate.d2);
        assertEquals(Piece.WHITE_PAWN, state.getPiece(Coordinate.e2));
        assertEmpty(state, Coordinate.f2, Coordinate.d1);
        assertEquals(Piece.WHITE_KING, state.getPiece(Coordinate.e1));
        assertEmpty(state, Coordinate.f1, Coordinate.h1);

        assertEquals(Player.WHITE, state.getActivePlayer());

        assertFalse(state.isWhiteKingSideCastlingAvailable());
        assertFalse(state.isWhiteQueenSideCastlingAvailable());
        assertFalse(state.isBlackKingSideCastlingAvailable());
        assertFalse(state.isBlackQueenSideCastlingAvailable());

        assertNull(state.getEnPassantTarget());

        assertEquals(5, state.getHalfMoveClock());
        assertEquals(39, state.getFullMoveNumber());
    }

    @Test
    void testPieces() {
        assertEquals(Piece.WHITE_PAWN, FEN.createBoard("P7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_ROOK, FEN.createBoard("R7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KNIGHT, FEN.createBoard("N7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_BISHOP, FEN.createBoard("B7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_QUEEN, FEN.createBoard("Q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KING, FEN.createBoard("K7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));

        assertEquals(Piece.BLACK_PAWN, FEN.createBoard("p7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_ROOK, FEN.createBoard("r7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, FEN.createBoard("n7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_BISHOP, FEN.createBoard("b7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_QUEEN, FEN.createBoard("q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KING, FEN.createBoard("k7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
    }

    @Test
    void testPlayer() {
        assertEquals(Player.WHITE, FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 1").getActivePlayer());
        assertEquals(Player.BLACK, FEN.createBoard("8/8/8/8/8/8/8/8 b - - 0 1").getActivePlayer());
    }

    @Test
    void testCastling() {
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 1"), false, false, false, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w KQkq - 0 1"), true, true, true, true);

        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w KQ - 0 1"), true, true, false, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w kq - 0 1"), false, false, true, true);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w Kk - 0 1"), true, false, true, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w Qq - 0 1"), false, true, false, true);

        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w K - 0 1"), true, false, false, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w Q - 0 1"), false, true, false, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w k - 0 1"), false, false, true, false);
        assertCastling(FEN.createBoard("8/8/8/8/8/8/8/8 w q - 0 1"), false, false, false, true);
    }

    @Test
    void testEnPassant() {
        assertNull(FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 1").getEnPassantTarget());

        assertEquals(Coordinate.c3, FEN.createBoard("8/8/8/8/8/8/8/8 b - c3 0 1").getEnPassantTarget());
        assertEquals(Coordinate.d6, FEN.createBoard("8/8/8/8/8/8/8/8 w - d6 0 1").getEnPassantTarget());

        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - c4 0 1"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - c3 0 1"));
    }

    @Test
    void testHalfMoveClock() {
        assertEquals(0, FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 1").getHalfMoveClock());
        assertEquals(42, FEN.createBoard("8/8/8/8/8/8/8/8 w - - 42 1").getHalfMoveClock());

        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - - -1 1"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - - x 1"));
    }

    @Test
    void testFullMoveNumber() {
        assertEquals(1, FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 1").getFullMoveNumber());
        assertEquals(42, FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 42").getFullMoveNumber());

        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 0"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createBoard("8/8/8/8/8/8/8/8 w - - 0 x"));
    }

    private static void assertPiece(Board setup, Piece piece, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertEquals(piece, setup.getPiece(c)));
    }

    private static void assertEmpty(Board setup, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertNull(setup.getPiece(c)));
    }

    private static void assertCastling(Board setup, boolean whiteKingSide, boolean whiteQueenSide,
                                       boolean blackKingSide, boolean blackQueenSide) {
        assertEquals(whiteKingSide, setup.isWhiteKingSideCastlingAvailable());
        assertEquals(whiteQueenSide, setup.isWhiteQueenSideCastlingAvailable());
        assertEquals(blackKingSide, setup.isBlackKingSideCastlingAvailable());
        assertEquals(blackQueenSide, setup.isBlackQueenSideCastlingAvailable());
    }

    @Test
    void assertToString_Standard() {
        Board state = Board.initial();
        assertEquals(FEN.INITIAL, FEN.toString(state));
    }

    @Test
    void assertToString_Empty() {
        Board state = new Board();
        System.out.println(state.toString());
        assertEquals("8/8/8/8/8/8/8/8 w KQkq - 0 1", FEN.toString(state));
    }
}
