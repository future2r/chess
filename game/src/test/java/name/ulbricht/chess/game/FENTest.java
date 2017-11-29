package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FENTest {

    @Test
    public void testDefault() {
        Setup setup = FEN.createSetup(FEN.STANDARD);

        assertEquals(Piece.BLACK_ROOK, setup.getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, setup.getPiece(Coordinate.b8));
        assertEquals(Piece.BLACK_BISHOP, setup.getPiece(Coordinate.c8));
        assertEquals(Piece.BLACK_QUEEN, setup.getPiece(Coordinate.d8));
        assertEquals(Piece.BLACK_KING, setup.getPiece(Coordinate.e8));
        assertEquals(Piece.BLACK_BISHOP, setup.getPiece(Coordinate.f8));
        assertEquals(Piece.BLACK_KNIGHT, setup.getPiece(Coordinate.g8));
        assertEquals(Piece.BLACK_ROOK, setup.getPiece(Coordinate.h8));

        assertPiece(setup, Piece.BLACK_PAWN, Coordinate.a7, Coordinate.h7);

        assertEmpty(setup, Coordinate.a6, Coordinate.h3);

        assertPiece(setup, Piece.WHITE_PAWN, Coordinate.a2, Coordinate.h2);

        assertEquals(Piece.WHITE_ROOK, setup.getPiece(Coordinate.a1));
        assertEquals(Piece.WHITE_KNIGHT, setup.getPiece(Coordinate.b1));
        assertEquals(Piece.WHITE_BISHOP, setup.getPiece(Coordinate.c1));
        assertEquals(Piece.WHITE_QUEEN, setup.getPiece(Coordinate.d1));
        assertEquals(Piece.WHITE_KING, setup.getPiece(Coordinate.e1));
        assertEquals(Piece.WHITE_BISHOP, setup.getPiece(Coordinate.f1));
        assertEquals(Piece.WHITE_KNIGHT, setup.getPiece(Coordinate.g1));
        assertEquals(Piece.WHITE_ROOK, setup.getPiece(Coordinate.h1));

        assertEquals(Player.WHITE, setup.getActivePlayer());

        assertTrue(setup.isWhiteKingSideCastlingAvailable());
        assertTrue(setup.isWhiteQueenSideCastlingAvailable());
        assertTrue(setup.isBlackKingSideCastlingAvailable());
        assertTrue(setup.isBlackQueenSideCastlingAvailable());

        assertNull(setup.getEnPassantTarget());

        assertEquals(0, setup.getHalfMoveClock());
        assertEquals(1, setup.getFullMoveNumber());
    }

    @Test
    public void testSpecial() {
        Setup setup = FEN.createSetup("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");

        assertEmpty(setup, Coordinate.a8, Coordinate.d8);
        assertEquals(Piece.BLACK_KING, setup.getPiece(Coordinate.e8));
        assertEmpty(setup, Coordinate.f8, Coordinate.d2);
        assertEquals(Piece.WHITE_PAWN, setup.getPiece(Coordinate.e2));
        assertEmpty(setup, Coordinate.f2, Coordinate.d1);
        assertEquals(Piece.WHITE_KING, setup.getPiece(Coordinate.e1));
        assertEmpty(setup, Coordinate.f1, Coordinate.h1);

        assertEquals(Player.WHITE, setup.getActivePlayer());

        assertFalse(setup.isWhiteKingSideCastlingAvailable());
        assertFalse(setup.isWhiteQueenSideCastlingAvailable());
        assertFalse(setup.isBlackKingSideCastlingAvailable());
        assertFalse(setup.isBlackQueenSideCastlingAvailable());

        assertNull(setup.getEnPassantTarget());

        assertEquals(5, setup.getHalfMoveClock());
        assertEquals(39, setup.getFullMoveNumber());
    }

    @Test
    public void testPieces() {
        assertEquals(Piece.WHITE_PAWN, FEN.createSetup("P7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_ROOK, FEN.createSetup("R7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KNIGHT, FEN.createSetup("N7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_BISHOP, FEN.createSetup("B7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_QUEEN, FEN.createSetup("Q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KING, FEN.createSetup("K7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));

        assertEquals(Piece.BLACK_PAWN, FEN.createSetup("p7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_ROOK, FEN.createSetup("r7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, FEN.createSetup("n7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_BISHOP, FEN.createSetup("b7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_QUEEN, FEN.createSetup("q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KING, FEN.createSetup("k7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
    }

    @Test
    public void testPlayer() {
        assertEquals(Player.WHITE, FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 1").getActivePlayer());
        assertEquals(Player.BLACK, FEN.createSetup("8/8/8/8/8/8/8/8 b - - 0 1").getActivePlayer());
    }

    @Test
    public void testCastling() {
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 1"), false, false, false, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w KQkq - 0 1"), true, true, true, true);

        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w KQ - 0 1"), true, true, false, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w kq - 0 1"), false, false, true, true);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w Kk - 0 1"), true, false, true, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w Qq - 0 1"), false, true, false, true);

        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w K - 0 1"), true, false, false, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w Q - 0 1"), false, true, false, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w k - 0 1"), false, false, true, false);
        assertCastling(FEN.createSetup("8/8/8/8/8/8/8/8 w q - 0 1"), false, false, false, true);
    }

    @Test
    public void testEnPassant() {
        assertNull(FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 1").getEnPassantTarget());

        assertEquals(Coordinate.c3, FEN.createSetup("8/8/8/8/8/8/8/8 b - c3 0 1").getEnPassantTarget());
        assertEquals(Coordinate.d6, FEN.createSetup("8/8/8/8/8/8/8/8 w - d6 0 1").getEnPassantTarget());

        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - c4 0 1"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - c3 0 1"));
    }

    @Test
    public void testHalfMoveClock() {
        assertEquals(0, FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 1").getHalfMoveClock());
        assertEquals(42, FEN.createSetup("8/8/8/8/8/8/8/8 w - - 42 1").getHalfMoveClock());

        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - - -1 1"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - - x 1"));
    }

    @Test
    public void testFullMoveNumber() {
        assertEquals(1, FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 1").getFullMoveNumber());
        assertEquals(42, FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 42").getFullMoveNumber());

        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 0"));
        assertThrows(IllegalArgumentException.class, () -> FEN.createSetup("8/8/8/8/8/8/8/8 w - - 0 x"));
    }

    private static void assertPiece(Setup setup, Piece piece, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertEquals(piece, setup.getPiece(c)));
    }

    private static void assertEmpty(Setup setup, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertNull(setup.getPiece(c)));
    }

    private static void assertCastling(Setup setup, boolean whiteKingSide, boolean whiteQueenSide,
                                       boolean blackKingSide, boolean blackQueenSide) {
        assertEquals(whiteKingSide, setup.isWhiteKingSideCastlingAvailable());
        assertEquals(whiteQueenSide, setup.isWhiteQueenSideCastlingAvailable());
        assertEquals(blackKingSide, setup.isBlackKingSideCastlingAvailable());
        assertEquals(blackQueenSide, setup.isBlackQueenSideCastlingAvailable());
    }

    @Test
    public void assertToString_Standard() {
        Setup setup = Setup.standard();
        assertEquals(FEN.STANDARD, FEN.toString(setup));
    }

    @Test
    public void assertToString_Empty() {
        Setup setup = Setup.empty();
        assertEquals("8/8/8/8/8/8/8/8 w KQkq - 0 1", FEN.toString(setup));
    }
}
