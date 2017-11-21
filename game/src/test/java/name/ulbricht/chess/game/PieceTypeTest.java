package name.ulbricht.chess.game;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;

public final class PieceTypeTest {

    @ParameterizedTest(name = "{index}: {0}")
    @EnumSource(value = PieceType.class)
    public void testNames(PieceType type) {
        // the display name should be there
        assertFalse( type.getDisplayName().startsWith("!"),"display name not found");
    }
}
