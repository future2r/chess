package name.ulbricht.chess.game;

import java.util.Objects;

abstract class AbstractMove implements Move {

    private final Coordinate from;
    private final Coordinate to;
    private final Coordinate captures;

    AbstractMove(Coordinate from, Coordinate to, Coordinate captures) {
        this.from = Objects.requireNonNull(from, "from cannot be null");
        this.to = Objects.requireNonNull(to, "to cannot be null");
        this.captures = captures;
    }

    @Override
    public Coordinate getFrom() {
        return this.from;
    }

    @Override
    public Coordinate getTo() {
        return this.to;
    }

    @Override
    public Coordinate getCaptures() {
        return captures;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("from ");
        sb.append(this.from);
        sb.append(" to ");
        sb.append(this.to);
        if (this.captures != null) {
            sb.append(" captures ");
            sb.append(this.captures);
        }

        return sb.toString();
    }
}
