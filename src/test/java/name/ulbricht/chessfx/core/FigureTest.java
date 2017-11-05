package name.ulbricht.chessfx.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public final class FigureTest {

    @Test
    public void testCreate() {
        Figure figure = new Figure(Figure.Type.KING, Player.WHITE);

        assertEquals(Figure.Type.KING, figure.getType());
        assertEquals(Player.WHITE, figure.getPlayer());
    }

    @Test
    public void testCreate_TypeNull() {
        try {
            new Figure(null, Player.WHITE);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("type cannot be null", ex.getMessage());
        }
    }

    @Test
    public void testCreate_PlayerNull() {
        try {
            new Figure(Figure.Type.KING, null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("player cannot be null", ex.getMessage());
        }
    }

    @Test
    public void testToString() {
        Figure figure = new Figure(Figure.Type.KING, Player.WHITE);

        assertEquals(String.format("%s (%s)", Figure.Type.KING.getDisplayName(), Player.WHITE.getDisplayName()), figure.toString());
    }
}
