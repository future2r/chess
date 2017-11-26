package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FENSetupTest {

    @Test
    public void testDefault() {
        FENSetup fen = FENSetup.standard();

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
        FENSetup fen = FENSetup.of("4k3/8/8/8/8/8/4P3/4K3 w - - 5 39");

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
        assertEquals(Piece.WHITE_PAWN, FENSetup.of("P7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_ROOK, FENSetup.of("R7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KNIGHT, FENSetup.of("N7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_BISHOP, FENSetup.of("B7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_QUEEN, FENSetup.of("Q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.WHITE_KING, FENSetup.of("K7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));

        assertEquals(Piece.BLACK_PAWN, FENSetup.of("p7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_ROOK, FENSetup.of("r7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KNIGHT, FENSetup.of("n7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_BISHOP, FENSetup.of("b7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_QUEEN, FENSetup.of("q7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
        assertEquals(Piece.BLACK_KING, FENSetup.of("k7/8/8/8/8/8/8/8 w - - 0 1").getPiece(Coordinate.a8));
    }

    @Test
    public void testPlayer() {
        assertEquals(Player.WHITE, FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 1").getActivePlayer());
        assertEquals(Player.BLACK, FENSetup.of("8/8/8/8/8/8/8/8 b - - 0 1").getActivePlayer());
    }

    @Test
    public void testCastling() {
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 1"), false, false, false, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w KQkq - 0 1"), true, true, true, true);

        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w KQ - 0 1"), true, true, false, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w kq - 0 1"), false, false, true, true);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w Kk - 0 1"), true, false, true, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w Qq - 0 1"), false, true, false, true);

        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w K - 0 1"), true, false, false, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w Q - 0 1"), false, true, false, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w k - 0 1"), false, false, true, false);
        assertCastling(FENSetup.of("8/8/8/8/8/8/8/8 w q - 0 1"), false, false, false, true);
    }

    @Test
    public void testEnPassant() {
        assertNull(FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 1").getEnPassantTarget());

        assertEquals(Coordinate.c3, FENSetup.of("8/8/8/8/8/8/8/8 b - c3 0 1").getEnPassantTarget());
        assertEquals(Coordinate.d6, FENSetup.of("8/8/8/8/8/8/8/8 w - d6 0 1").getEnPassantTarget());

        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - c4 0 1"));
        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - c3 0 1"));
    }

    @Test
    public void testHalfMoveClock() {
        assertEquals(0, FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 1").getHalfMoveClock());
        assertEquals(42, FENSetup.of("8/8/8/8/8/8/8/8 w - - 42 1").getHalfMoveClock());

        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - - -1 1"));
        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - - x 1"));
    }

    @Test
    public void testFullMoveNumber() {
        assertEquals(1, FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 1").getFullMoveNumber());
        assertEquals(42, FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 42").getFullMoveNumber());

        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 0"));
        assertThrows(IllegalArgumentException.class, () -> FENSetup.of("8/8/8/8/8/8/8/8 w - - 0 x"));
    }

    private static void assertPiece(FENSetup fen, Piece piece, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertEquals(piece, fen.getPiece(c)));
    }

    private static void assertEmpty(FENSetup fen, Coordinate from, Coordinate to) {
        Stream.of(Coordinate.values())
                .filter(c -> c.compareTo(from) >= 0)
                .filter(c -> c.compareTo(to) <= 0)
                .forEach(c -> assertNull(fen.getPiece(c)));
    }

    private static void assertCastling(FENSetup fen, boolean whiteKingSide, boolean whiteQueenSide,
                                       boolean blackKingSide, boolean blackQueenSide) {
        assertEquals(whiteKingSide, fen.isWhiteKingSideCastlingAvailable());
        assertEquals(whiteQueenSide, fen.isWhiteQueenSideCastlingAvailable());
        assertEquals(blackKingSide, fen.isBlackKingSideCastlingAvailable());
        assertEquals(blackQueenSide, fen.isBlackQueenSideCastlingAvailable());
    }

    @Test
    public void assertToString_Standard(){
        FENSetup fen = FENSetup.standard();
        assertEquals(FENSetup.STANDARD, fen.toString());
    }

    @Test
    public void assertToString_Empty(){
        FENSetup fen = FENSetup.empty();
        assertEquals(FENSetup.EMPTY, fen.toString());
    }
}
