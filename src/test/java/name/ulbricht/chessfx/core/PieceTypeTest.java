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
        return Stream.of(Piece.Type.values()).map(v -> new Object[]{v}).collect(Collectors.toList());
    }

    private final Piece.Type type;

    public PieceTypeTest(Piece.Type type) {
        this.type = type;
    }

    @Test
    public void testNames() {
        // the short name should be just one characters
        assertFalse("short name not found", this.type.getShortName().startsWith("!"));
        if (this.type != Piece.Type.PAWN) {
            assertEquals("short name should be one character only", 1, this.type.getShortName().length());
        } else {
            assertEquals("short name should be empty", "", this.type.getShortName());
        }

        // the display name should be longer
        assertFalse("display name not found", this.type.getDisplayName().startsWith("!"));
        assertTrue("display name should be longer than one character", this.type.getDisplayName().length() > 1);
    }
}
