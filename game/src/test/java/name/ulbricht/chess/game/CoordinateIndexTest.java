package name.ulbricht.chess.game;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CoordinateIndexTest {

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @CsvFileSource(resources = "coordinates.txt", delimiter = '\t' /* numLinesToSkip=1 (skip header comes in 5.1)*/)
    public void testCoordinate(int fieldIndex, int columnIndex, String columnName, int rowIndex, String rowName, String fieldName) {

        // parse from name
        Coordinate coordinate = Coordinate.valueOf(fieldName);

        // check field index
        assertEquals(fieldIndex, coordinate.ordinal(), "fieldIndex");

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
