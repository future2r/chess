package name.ulbricht.chessfx.core;

abstract class AbstractMoveFinder implements MoveFinder {

    protected Coordinate moveTo(Coordinate from, int columnOffset, int rowOffset) {
        int newColumn = from.getColumnIndex() + columnOffset;
        int newRow = from.getRowIndex() + rowOffset;

        if (newColumn >= 0 && newColumn < Coordinate.COLUMNS && newRow >= 0 && newRow < Coordinate.ROWS)
            return Coordinate.valueOf(newColumn, newRow);
        else return null;
    }
}
