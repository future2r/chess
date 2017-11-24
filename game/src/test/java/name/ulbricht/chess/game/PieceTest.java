package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public final class PieceTest {

    @Test
    public void testToString() {
        Piece piece = Piece.WHITE_KING;

        assertEquals(String.format("%s (%s)", PieceType.KING.getDisplayName(), Player.WHITE.getDisplayName()), piece.toString());
    }
}
