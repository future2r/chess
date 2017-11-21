package name.ulbricht.chess.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static junit.framework.TestCase.assertFalse;

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
        // the display name should be there
        assertFalse("display name not found", this.player.getDisplayName().startsWith("!"));
    }
}
