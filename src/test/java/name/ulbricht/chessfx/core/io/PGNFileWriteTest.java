package name.ulbricht.chessfx.core.io;

import name.ulbricht.chessfx.core.Game;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PGNFileWriteTest {

    @Test
    public void writeDefaultGame() throws IOException {
        Game game = new Game();
        game.start();

        PGNFile file = new PGNFile();
        writeAndAssert(file, game, "default.pgn");
    }

    private void writeAndAssert(PGNFile file, Game game, String expectedFileName) throws IOException {
        byte[] data;

        // write
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            file.write(out, game);
            data = out.toByteArray();
        }

        // read expected lines
        List<String> expectedLines = new ArrayList<>();
        try (InputStream is = getClass().getResourceAsStream(expectedFileName);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                expectedLines.add(line);
            }
        }

        // read actual written lines
        List<String> actualLines = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(data);
             InputStreamReader isr = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                actualLines.add(line);
            }
        }

        for (int i = 0; i < expectedLines.size(); i++) {
            if (actualLines.size() <= i) fail("Line expected at index " + i);

            String expectedLine = expectedLines.get(i);
            String actualLine = actualLines.get(i);
            assertEquals("Line mismatch at index " + 1, expectedLine, actualLine);
        }
        if (actualLines.size() > expectedLines.size()) fail("End of file expected");
    }
}
