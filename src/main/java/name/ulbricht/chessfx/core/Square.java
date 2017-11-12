package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Square implements Comparable<Square> {

    private final Coordinate coordinate;
    private Piece piece;

    public Square(Coordinate coordinate) {
        this.coordinate = Objects.requireNonNull(coordinate, "coordinate cannot be null");
    }

    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public boolean isEmpty() {
        return this.piece == null;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.coordinate, this.piece != null ? this.piece : "");
    }

    @Override
    public int compareTo(Square other) {
        return this.coordinate.compareTo(other.coordinate);
    }
}
