package name.ulbricht.chess.game;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class CoordinateIndexTest {

    private static Stream<Arguments> createArguments() throws IOException {
        try (InputStream is = CoordinateIndexTest.class.getResourceAsStream("coordinates.txt");
             Reader r = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(r)) {

            Collection<Arguments> parameters = new ArrayList<>();

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {

                // skip the first line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] fields = line.split("\t");
                parameters.add(Arguments.of(
                        Integer.parseInt(fields[0]), // field index
                        Integer.parseInt(fields[1]), // column index
                        fields[2], // column name
                        Integer.parseInt(fields[3]), // row index
                        fields[4], // row name
                        fields[5] // field name
                ));
            }

            return parameters.stream();
        }
    }

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @MethodSource("createArguments")
    public void testCoordinate(int fieldIndex, int columnIndex, String columnName, int rowIndex, String rowName, String fieldName) {
        // parse from name
        Coordinate coordinate = Coordinate.valueOf(fieldName);

        // check field index
        assertEquals(fieldIndex, coordinate.getIndex(), "fieldIndex");

        // check cache
        assertTrue(coordinate == Coordinate.valueOf(coordinate.getIndex()), "cache");

        // check column
        assertEquals(columnIndex, coordinate.getColumnIndex(), "columnIndex");
        assertEquals(columnName, coordinate.getColumnName(), "columnName");

        // check row
        assertEquals(rowIndex, coordinate.getRowIndex(), "rowIndex");
        assertEquals(rowName, coordinate.getRowName(), "rowName");

        // check fieldName
        assertEquals(fieldName, coordinate.toString(), "fieldName");
    }
}
