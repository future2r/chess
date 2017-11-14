package name.ulbricht.chessfx.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public final class PlayerTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Stream.of(Player.values()).map(v -> new Object[]{v}).collect(Collectors.toList());
    }

    private final Player player;

    public PlayerTest(Player player) {
        this.player = player;
    }

    @Test
    public void testNames() {
        // the short name should be just one characters
        assertFalse("short name not found", this.player.getShortName().startsWith("!"));
        assertEquals("short name should be one character only", 1, this.player.getShortName().length());

        // the display name should be longer
        assertFalse("display name not found", this.player.getDisplayName().startsWith("!"));
        assertTrue("display name should be longer than one character", this.player.getDisplayName().length() > 1);
    }
}
