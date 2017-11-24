package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a move on a board.
 */
public final class Move {

    static Move simple(Coordinate source, Coordinate target, Coordinate captures) {
        return new Move(MoveType.SIMPLE, source, target, captures);
    }

    private final MoveType type;
    private final Coordinate source;
    private final Coordinate target;
    private final Coordinate captures;

    private Move(MoveType type, Coordinate source, Coordinate target, Coordinate captures) {
        this.type = type;
        this.source = source;
        this.target = target;
        this.captures = captures;
    }

    public MoveType getType() {
        return this.type;
    }

    public Coordinate getSource() {
        return this.source;
    }

    public Coordinate getTarget() {
        return this.target;
    }

    public Coordinate getCaptures() {
        return this.captures;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.source, this.target, this.captures);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Move other = (Move) obj;

        return Objects.equals(this.type, other.type)
                && Objects.equals(this.source, other.source)
                && Objects.equals(this.target, other.target)
                && Objects.equals(this.captures, other.captures);
    }
}
