package name.ulbricht.chessfx.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public final class CoordinateIndexTest {

    @Parameterized.Parameters(name = "{index}: index={0} name={5}")
    public static Collection<Object[]> createParameters() throws IOException {
        try (InputStream is = CoordinateIndexTest.class.getResourceAsStream("coordinates.txt");
             Reader r = new InputStreamReader(is);
             BufferedReader br = new BufferedReader(r)) {

            Collection<Object[]> parameters = new ArrayList<>();

            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {

                // skip the first line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] fields = line.split("\t");
                parameters.add(new Object[]{
                        Integer.parseInt(fields[0]), // field index
                        Integer.parseInt(fields[1]), // column index
                        fields[2], // column name
                        Integer.parseInt(fields[3]), // row index
                        fields[4], // row name
                        fields[5] // field name
                });
            }

            return parameters;
        }
    }

    private final int fieldIndex;
    private final int columnIndex;
    private final String columnName;
    private final int rowIndex;
    private final String rowName;
    private final String fieldName;

    public CoordinateIndexTest(int fieldIndex, int columnIndex, String columnName, int rowIndex, String rowName, String fieldName) {
        this.fieldIndex = fieldIndex;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
        this.rowIndex = rowIndex;
        this.rowName = rowName;
        this.fieldName = fieldName;
    }

    @Test
    public void testCoordinate() {
        // parse from name
        Coordinate coordinate = Coordinate.valueOf(fieldName);

        // check field index
        assertEquals("fieldIndex", this.fieldIndex, coordinate.getIndex());

        // check cache
        assertTrue("cache", coordinate == Coordinate.valueOf(coordinate.getIndex()));

        // check column
        assertEquals("columnIndex", this.columnIndex, coordinate.getColumnIndex());
        assertEquals("columnName", this.columnName, coordinate.getColumnName());

        // check row
        assertEquals("rowIndex", this.rowIndex, coordinate.getRowIndex());
        assertEquals("rowName", this.rowName, coordinate.getRowName());

        // check fieldName
        assertEquals("fieldName", this.fieldName, coordinate.toString());
    }
}
