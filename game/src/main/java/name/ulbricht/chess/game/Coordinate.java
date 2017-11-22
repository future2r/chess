package name.ulbricht.chess.game;

/**
 * Represents a coordinate on a board. These coordinates are based on a unique zero-based index (the ordinal value)
 * representing each coordinate. All other properties of a coordinate are derived from that index. The index 0
 * represents the lower left corner of the board. The coordinate objects are cached. Two objects representing the same
 * index will be identical.
 */
public enum Coordinate {

    a1, b1, c1, d1, e1, f1, g1, h1,
    a2, b2, c2, d2, e2, f2, g2, h2,
    a3, b3, c3, d3, e3, f3, g3, h3,
    a4, b4, c4, d4, e4, f4, g4, h4,
    a5, b5, c5, d5, e5, f5, g5, h5,
    a6, b6, c6, d6, e6, f6, g6, h6,
    a7, b7, c7, d7, e7, f7, g7, h7,
    a8, b8, c8, d8, e8, f8, g8, h8;

    /**
     * Number of columns for this kind of coordinates.
     */
    public static final int COLUMNS = 8;

    /**
     * Number of rows for this kind of coordinates.
     */
    public static final int ROWS = 8;

    /**
     * Returns the zero-based column index tof this coordinate.
     *
     * @return the column index of this coordinate
     * @see #getColumnName()
     */
    public int getColumnIndex() {
        return ordinal() % ROWS;
    }

    /**
     * Returns a name for the column index of this coordinate. Usually the name will be single character, alphabetically
     * ordered.
     *
     * @return the name of the column
     * @see #getColumnIndex()
     * @see #toColumnName(int)
     */
    public String getColumnName() {
        return toColumnName(getColumnIndex());
    }

    /**
     * Returns the zero-based row index tof this coordinate.
     *
     * @return the row index of this coordinate
     * @see #getRowName()
     */
    public int getRowIndex() {
        return ordinal() / COLUMNS;
    }

    /**
     * Returns a name for the row index of this coordinate. Usually the name will be single one-based digit.
     *
     * @return the name of the column
     * @see #getColumnIndex()
     * @see #toColumnName(int)
     */
    public String getRowName() {
        return toRowName(getRowIndex());
    }

    /**
     * Returns the coordinate for the square directly left next to the square represented by this coordinate. If
     * there is no such square (because the board ends here) the return value will be {@code null}.
     *
     * @return the coordinate left to this coordinate or {@code null}
     * @see #moveTo(int, int)
     */
    public Coordinate moveLeft() {
        return moveTo(-1, 0);
    }

    /**
     * Returns the coordinate for the square directly right next to the square represented by this coordinate.
     * If there is no such square (because the board ends here) the return value will be {@code null}.
     *
     * @return the coordinate right to this coordinate or {@code null}
     * @see #moveTo(int, int)
     */
    public Coordinate moveRight() {
        return moveTo(1, 0);
    }

    /**
     * Returns the coordinate for the square directly above the square represented by this coordinate. If there
     * is no such square (because the board ends here) the return value will be {@code null}.
     *
     * @return the coordinate above this coordinate or {@code null}
     * @see #moveTo(int, int)
     */
    public Coordinate moveUp() {
        return moveTo(0, 1);
    }

    /**
     * Returns the coordinate for the square directly below the square represented by this coordinate. If there
     * is no such square (because the board ends here) the return value will be {@code null}.
     *
     * @return the coordinate below this coordinate or {@code null}
     * @see #moveTo(int, int)
     */
    public Coordinate moveDown() {
        return moveTo(0, -1);
    }

    /**
     * Returns the coordinate that can be reached by moving the specified offset from this coordinate. If there
     * is no such square (because the board ends here) the return value will be {@code null}..
     *
     * @param columnOffset positive offset will move right, negative offset will move left
     * @param rowOffset    positive offset will move up, negative offset will move down
     * @return the coordinate or {@code null}.
     */
    public Coordinate moveTo(int columnOffset, int rowOffset) {
        int newColumn = getColumnIndex() + columnOffset;
        int newRow = getRowIndex() + rowOffset;

        if (newColumn >= 0 && newColumn < COLUMNS && newRow >= 0 && newRow < ROWS)
            return Coordinate.valueOf(newColumn, newRow);
        else return null;
    }

    /**
     * Returns the column name for the given column index. This method can be used by the application to display column
     * names when drawing a board.
     *
     * @param columnIndex the column index
     * @return a name for the column index
     * @throws IndexOutOfBoundsException if the index exceeds the lower or upper limit
     * @see #getColumnName()
     */
    public static String toColumnName(int columnIndex) throws IndexOutOfBoundsException {
        return Character.valueOf((char) (97 + checkColumnIndex(columnIndex))).toString();
    }

    /**
     * Returns the row name for the given row index. This method can be used by the application to display row names
     * when drawing a board.
     *
     * @param rowIndex the row index
     * @return a name for the row index
     * @throws IndexOutOfBoundsException if the index exceeds the lower or upper limit
     * @see #getRowName()
     */
    public static String toRowName(int rowIndex) {
        return Character.valueOf((char) (49 + checkRowIndex(rowIndex))).toString();
    }

    /**
     * Returns thecoordinate object for the given index.
     *
     * @param index the index
     * @return a coordinate object
     * @throws IndexOutOfBoundsException if the index exceeds the lower or upper limit.
     */
    public static Coordinate valueOf(int index) throws IndexOutOfBoundsException {
        return values()[index];
    }

    /**
     * Returns a cached coordinate object for the given column index and row index.
     *
     * @param columnIndex the column index
     * @param rowIndex    the row index
     * @return a coordinate object
     * @throws IndexOutOfBoundsException if the index exceeds the lower or upper limit.
     */
    public static Coordinate valueOf(int columnIndex, int rowIndex) throws IndexOutOfBoundsException {
        return valueOf(checkRowIndex(rowIndex) * COLUMNS + checkColumnIndex(columnIndex));
    }

    private static int checkColumnIndex(int index) {
        if (index < 0 || index >= COLUMNS)
            throw new IndexOutOfBoundsException("Illegal value for column index " + index);
        return index;
    }

    private static int checkRowIndex(int index) {
        if (index < 0 || index >= ROWS)
            throw new IndexOutOfBoundsException("Illegal value for row index " + index);
        return index;
    }
}
