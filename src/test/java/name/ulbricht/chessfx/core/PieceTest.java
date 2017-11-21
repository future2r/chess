package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.*;

public final class PieceTest {

    @Test
    public void testCreate() {
        Piece piece = new Piece(PieceType.KING, Player.WHITE);

        assertEquals(PieceType.KING, piece.getType());
        assertEquals(Player.WHITE, piece.getPlayer());
    }

    @Test
    public void testCreate_TypeNull() {
        try {
            new Piece(null, Player.WHITE);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("type cannot be null", ex.getMessage());
        }
    }

    @Test
    public void testCreate_PlayerNull() {
        try {
            new Piece(PieceType.KING, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("player cannot be null", ex.getMessage());
        }
    }

    @Test
    public void testToString() {
        Piece piece = new Piece(PieceType.KING, Player.WHITE);

        assertEquals(String.format("%s (%s)", PieceType.KING.getDisplayName(), Player.WHITE.getDisplayName()), piece.toString());
    }

    @Test
    public void testClone() {
        Piece piece = new Piece(PieceType.KING, Player.WHITE);
        piece.incrementMoveCount();

        Piece clone = piece.clone();

        assertFalse(piece == clone);
        assertEquals(piece, clone);

        assertEquals(piece.getType(), clone.getType());
        assertEquals(piece.getPlayer(), clone.getPlayer());
        assertEquals(piece.getMoveCount(), clone.getMoveCount());
    }
}
