package name.ulbricht.chess.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

final class CoordinateTest {

    @ParameterizedTest(name = "{index}: [{arguments}]")
    @CsvFileSource(resources = "coordinates.txt", delimiter = '\t' /* numLinesToSkip=1 (skip header comes in 5.1)*/)
    void testIndices(int fieldIndex, int columnIndex, String columnName, int rowIndex, String rowName, String fieldName) {

        // parse from name
        Coordinate coordinate = Coordinate.valueOf(fieldName);

        // check field index
        assertEquals(fieldIndex, coordinate.ordinal(), "fieldIndex");

        // check column
        assertEquals(columnIndex, coordinate.columnIndex, "columnIndex");
        assertEquals(columnName, coordinate.columnName, "columnName");

        // check row
        assertEquals(rowIndex, coordinate.rowIndex, "rowIndex");
        assertEquals(rowName, coordinate.rowName, "rowName");

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
        assertEquals(Coordinate.g1, Coordinate.h1.moveLeft());
        assertEquals(Coordinate.d5, Coordinate.e5.moveLeft());
        assertEquals(Coordinate.a8, Coordinate.b8.moveLeft());
    }

    @Test
    void testMoveLeft_Fail() {
        assertNull(Coordinate.a5.moveLeft());
    }

    @Test
    void testMoveRight() {
        assertEquals(Coordinate.b1, Coordinate.a1.moveRight());
        assertEquals(Coordinate.f5, Coordinate.e5.moveRight());
        assertEquals(Coordinate.h8, Coordinate.g8.moveRight());
    }

    @Test
    void testMoveRight_Fail() {
        assertNull(Coordinate.h5.moveRight());
    }

    @Test
    void testMoveUp() {
        assertEquals(Coordinate.a2, Coordinate.a1.moveUp());
        assertEquals(Coordinate.e6, Coordinate.e5.moveUp());
        assertEquals(Coordinate.h8, Coordinate.h7.moveUp());
    }

    @Test
    void testMoveUp_Fail() {
        assertNull(Coordinate.d8.moveUp());
    }

    @Test
    void testMoveDown() {
        assertEquals(Coordinate.a7, Coordinate.a8.moveDown());
        assertEquals(Coordinate.e4, Coordinate.e5.moveDown());
        assertEquals(Coordinate.h1, Coordinate.h2.moveDown());
    }

    @Test
    void testMoveDown_Fail() {
        assertNull(Coordinate.e1.moveDown());
    }
}
