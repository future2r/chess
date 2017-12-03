package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

final class PieceTest {

    @Test
    void testToString() {
        Piece piece = Piece.WHITE_KING;

        assertEquals(String.format("%s (%s)", PieceType.KING.getDisplayName(), Player.WHITE.getDisplayName()), piece.getDisplayName());
    }
}
