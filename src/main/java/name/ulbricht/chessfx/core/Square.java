package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Square implements Cloneable, Comparable<Square> {

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
    public Square clone() {
        try {
            Square clone = (Square) super.clone();
            if (this.piece != null) this.piece.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.coordinate, this.piece);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Square other = (Square) obj;
        return Objects.equals(this.coordinate, other.coordinate)
                && Objects.equals(this.piece, other.piece);
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
