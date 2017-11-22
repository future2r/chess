package name.ulbricht.chess.game;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;

final class PlayerTest {

    @ParameterizedTest(name = "{index}: {0}")
    @EnumSource(value = Player.class)
    void testNames(Player player) {
        // the display name should be there
        assertFalse(player.getDisplayName().startsWith("!"), "display name not found");
    }
}
