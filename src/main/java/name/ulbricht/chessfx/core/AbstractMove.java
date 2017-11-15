package name.ulbricht.chessfx.core;

import java.util.Objects;
import java.util.Optional;

abstract class AbstractMove implements Move {

    private final Board board;
    private final Coordinate from;
    private final Coordinate to;
    private final Coordinate captures;

    AbstractMove(Board board, Coordinate from, Coordinate to, Coordinate captures) {
        this.board = Objects.requireNonNull(board, "board cannot be null");
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
    public Optional<Coordinate> getCaptures() {
        return Optional.ofNullable(captures);
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
