package name.ulbricht.chess.game;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Represents a coordinate on a board. These coordinates are based on a unique zero-based index representing each
 * coordinate. All other properties of a coordinate are derived from that index. The index 0 represents the lower left
 * corner of the board. The coordinate objects are cached. Two objects representing the same index will be identical.
 */
public final class Coordinate implements Comparable<Coordinate> {

    /**
     * Number of columns for this kind of coordinates.
     */
    public static final int COLUMNS = 8;

    /**
     * Number of rows for this kind of coordinates.
     */
    public static final int ROWS = 8;

    private static final Coordinate[] cachedCoordinates;

    static {
        cachedCoordinates = IntStream.range(0, COLUMNS * ROWS)
                .mapToObj(Coordinate::new)
                .toArray(Coordinate[]::new);
    }

    private final int index;

    private Coordinate(int index) {
        this.index = checkIndex(index);
    }

    /**
     * Returns the zero-based index that is represented by this coordinate.
     *
     * @return the index of this coordinate
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the zero-based column index tof this coordinate.
     *
     * @return the column index of this coordinate
     * @see #getColumnName()
     */
    public int getColumnIndex() {
        return this.index % ROWS;
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
        return this.index / COLUMNS;
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

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Coordinate other = (Coordinate) obj;

        return this.index == other.index;
    }

    /**
     * Returns the name for this coordinate. Usually this will be the name of the column followed by the name of the
     * row. This name uses the same syntax that can be used to parse coordinate names into coordinate objects.
     *
     * @return the name of the coordinate
     * @see #getColumnName()
     * @see #getRowName()
     * @see #valueOf(String)
     */
    @Override
    public String toString() {
        return getColumnName() + getRowName();
    }

    @Override
    public int compareTo(Coordinate other) {
        return Integer.compare(this.index, other.index);
    }

    /**
     * Returns an optional coordinate for the square directly left next to the square represented by this coordinate. If
     * there is no such square (because the board ends here) the return value will be empty.
     *
     * @return an optional coordinate left to this coordinate
     * @see #moveTo(int, int)
     */
    public Optional<Coordinate> moveLeft() {
        return moveTo(-1, 0);
    }

    /**
     * Returns an optional coordinate for the square directly right next to the square represented by this coordinate.
     * If there is no such square (because the board ends here) the return value will be empty.
     *
     * @return an optional coordinate right to this coordinate
     * @see #moveTo(int, int)
     */
    public Optional<Coordinate> moveRight() {
        return moveTo(1, 0);
    }

    /**
     * Returns an optional coordinate for the square directly above the square represented by this coordinate. If there
     * is no such square (because the board ends here) the return value will be empty.
     *
     * @return an optional coordinate above this coordinate
     * @see #moveTo(int, int)
     */
    public Optional<Coordinate> moveUp() {
        return moveTo(0, 1);
    }

    /**
     * Returns an optional coordinate for the square directly below the square represented by this coordinate. If there
     * is no such square (because the board ends here) the return value will be empty.
     *
     * @return an optional coordinate below this coordinate
     * @see #moveTo(int, int)
     */
    public Optional<Coordinate> moveDown() {
        return moveTo(0, -1);
    }

    /**
     * Returns an optional coordinate that can be reached by moving the specified offset from this coordinate. If there
     * is no such square (because the board ends here) the return value will be empty.
     *
     * @param columnOffset positive offset will move right, negative offset will move left
     * @param rowOffset    positive offset will move up, negative offset will move down
     * @return an optional coordinate
     */
    public Optional<Coordinate> moveTo(int columnOffset, int rowOffset) {
        int newColumn = getColumnIndex() + columnOffset;
        int newRow = getRowIndex() + rowOffset;

        if (newColumn >= 0 && newColumn < COLUMNS && newRow >= 0 && newRow < ROWS)
            return Optional.of(Coordinate.valueOf(newColumn, newRow));
        else return Optional.empty();
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
     * Returns a cached coordinate object for the given index.
     *
     * @param index the index
     * @return a coordinate object
     * @throws IndexOutOfBoundsException if the index exceeds the lower or upper limit.
     */
    public static Coordinate valueOf(int index) throws IndexOutOfBoundsException {
        return cachedCoordinates[checkIndex(index)];
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

    /**
     * Returns a cached coordinate object for the given coordinate name. The name must be a valid column name followed
     * by a valid row name.
     *
     * @param name a coordinate name
     * @return a coordinate object
     * @throws IllegalArgumentException  if the given name cannot be parsed
     * @throws IndexOutOfBoundsException if the given name results in invalid value column index or row index
     * @see #toColumnName(int)
     * @see #toRowName(int)
     */
    public static Coordinate valueOf(String name) throws IllegalArgumentException, IndexOutOfBoundsException {
        Objects.requireNonNull(name, "name cannot be null");

        Pattern pattern = Pattern.compile("([a-h])([1-8])");
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            char columnName = matcher.group(1).charAt(0);
            char rowName = matcher.group(2).charAt(0);

            int columnIndex = columnName - 97;
            int rowIndex = rowName - 49;

            return valueOf(rowIndex * COLUMNS + columnIndex);
        }
        throw new IllegalArgumentException("Illegal coordinate name " + name);
    }

    /**
     * Returns a stream of all cached coordinate objects. This stream can be used to easily iterate over all the
     * coordinates.
     *
     * @return a stream of coordinate objects
     */
    public static Stream<Coordinate> values() {
        return Stream.of(cachedCoordinates);
    }

    private static int checkIndex(int index) {
        if (index < 0 || index >= (COLUMNS * ROWS))
            throw new IndexOutOfBoundsException("Illegal value for index " + index);
        return index;
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
