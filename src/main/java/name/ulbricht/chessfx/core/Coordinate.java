package name.ulbricht.chessfx.core;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class Coordinate implements Comparable<Coordinate> {

    public static final int COLUMNS = 8;
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

    public int getIndex() {
        return this.index;
    }

    public int getColumnIndex() {
        return this.index % ROWS;
    }

    public String getColumnName() {
        return toColumnName(getColumnIndex());
    }

    public int getRowIndex() {
        return this.index / COLUMNS;
    }

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

    @Override
    public String toString() {
        return getColumnName() + getRowName();
    }

    @Override
    public int compareTo(Coordinate other) {
        return Integer.compare(this.index, other.index);
    }

    public boolean isLeftColumn() {
        return getColumnIndex() == 0;
    }

    public Optional<Coordinate> moveLeft() {
        return moveTo(-1, 0);
    }

    public boolean isRightColumn() {
        return getColumnIndex() == (COLUMNS - 1);
    }

    public Optional<Coordinate> moveRight() {
        return moveTo(1, 0);
    }

    public boolean isTopRow() {
        return getRowIndex() == (ROWS - 1);
    }

    public Optional<Coordinate> moveUp() {
        return moveTo(0, 1);
    }

    public boolean isBottomRow() {
        return getRowIndex() == 0;
    }

    public Optional<Coordinate> moveDown() {
        return moveTo(0, -1);
    }

    public Optional<Coordinate> moveTo(int columnOffset, int rowOffset) {
        int newColumn = getColumnIndex() + columnOffset;
        int newRow = getRowIndex() + rowOffset;

        if (newColumn >= 0 && newColumn < COLUMNS && newRow >= 0 && newRow < ROWS)
            return Optional.of(Coordinate.valueOf(newColumn, newRow));
        else return Optional.empty();
    }

    public static String toColumnName(int columnIndex) {
        return Character.valueOf((char) (97 + checkColumnIndex(columnIndex))).toString();
    }

    public static String toRowName(int rowIndex) {
        return Character.valueOf((char) (49 + checkRowIndex(rowIndex))).toString();
    }

    public static Coordinate valueOf(int index) {
        return cachedCoordinates[checkIndex(index)];
    }

    public static Coordinate valueOf(int columnIndex, int rowIndex) {
        return valueOf(checkRowIndex(rowIndex) * COLUMNS + checkColumnIndex(columnIndex));
    }

    public static Coordinate valueOf(String name) {
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

    public static Stream<Coordinate> values() {
        return Stream.of(cachedCoordinates);
    }

    private static int checkIndex(int index) {
        if (index < 0 || index >= (COLUMNS * ROWS))
            throw new IllegalArgumentException("Illegal value for index " + index);
        return index;
    }

    private static int checkColumnIndex(int index) {
        if (index < 0 || index >= COLUMNS)
            throw new IllegalArgumentException("Illegal value for column index " + index);
        return index;
    }

    private static int checkRowIndex(int index) {
        if (index < 0 || index >= ROWS)
            throw new IllegalArgumentException("Illegal value for row index " + index);
        return index;
    }
}
