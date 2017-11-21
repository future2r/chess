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
public final class PieceTypeTest {

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Collection<Object[]> createParameters() {
        return Stream.of(PieceType.values()).map(v -> new Object[]{v}).collect(Collectors.toList());
    }

    private final PieceType type;

    public PieceTypeTest(PieceType type) {
        this.type = type;
    }

    @Test
    public void testNames() {
        // the display name should be there
        assertFalse("display name not found", this.type.getDisplayName().startsWith("!"));
    }
}
