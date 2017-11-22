package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

final class CoordinateTest {

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @CsvFileSource(resources = "coordinates.txt", delimiter = '\t' /* numLinesToSkip=1 (skip header comes in 5.1)*/)
    void testIndices(int fieldIndex, int columnIndex, String columnName, int rowIndex, String rowName, String fieldName) {

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

    @Test
    void testInvalidIndices() {

        int[] indices = new int[]{-10, -1, 64, 100};

        for (int index : indices) {
            try {
                Coordinate.valueOf(index);
                fail("IndexOutOfBoundsException expected");
            } catch (IndexOutOfBoundsException ex) {
                assertEquals(Integer.toString(index), ex.getMessage());
            }
        }
    }

    @Test
    void testNullNames() {
        try {
            Coordinate.valueOf(null);
            fail("NullPointerException expected");
        } catch (NullPointerException ex) {
            assertEquals("Name is null", ex.getMessage());
        }
    }

    @Test
    void testInvalidNames() {

        String[] names = {"", "a", "1", "a 1", "1a", "a9", "x1", "x9"};

        for (String name : names) {
            try {
                Coordinate.valueOf(name);
                fail("IllegalArgumentException expected");
            } catch (IllegalArgumentException ex) {
                assertEquals("No enum constant name.ulbricht.chess.game.Coordinate." + name, ex.getMessage());
            }
        }
    }

    @Test
    void testMoveLeft() {
        assertEquals(Coordinate.g1, Coordinate.h1.moveLeft().get());
        assertEquals(Coordinate.d5, Coordinate.e5.moveLeft().get());
        assertEquals(Coordinate.a8, Coordinate.b8.moveLeft().get());
    }

    @Test
    void testMoveLeft_Fail() {
        assertFalse(Coordinate.a5.moveLeft().isPresent());
    }

    @Test
    void testMoveRight() {
        assertEquals(Coordinate.b1, Coordinate.a1.moveRight().get());
        assertEquals(Coordinate.f5, Coordinate.e5.moveRight().get());
        assertEquals(Coordinate.h8, Coordinate.g8.moveRight().get());
    }

    @Test
    void testMoveRight_Fail() {
        assertFalse(Coordinate.h5.moveRight().isPresent());
    }

    @Test
    void testMoveUp() {
        assertEquals(Coordinate.a2, Coordinate.a1.moveUp().get());
        assertEquals(Coordinate.e6, Coordinate.e5.moveUp().get());
        assertEquals(Coordinate.h8, Coordinate.h7.moveUp().get());
    }

    @Test
    void testMoveUp_Fail() {
        assertFalse(Coordinate.d8.moveUp().isPresent());
    }

    @Test
    void testMoveDown() {
        assertEquals(Coordinate.a7, Coordinate.a8.moveDown().get());
        assertEquals(Coordinate.e4, Coordinate.e5.moveDown().get());
        assertEquals(Coordinate.h1, Coordinate.h2.moveDown().get());
    }

    @Test
    void testMoveDown_Fail() {
        assertFalse(Coordinate.e1.moveDown().isPresent());
    }
}
