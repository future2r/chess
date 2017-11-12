package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Move {

    private final Square from;
    private final Square to;
    private final Square captures;

    public Move(Square from, Square to) {
        this(from, to, null);
    }

    public Move(Square from, Square to, Square captures) {
        this.from = Objects.requireNonNull(from, "from cannot be null");
        this.to = Objects.requireNonNull(to, "to cannot be null");
        if (captures == null && !this.to.isEmpty()) this.captures = captures;
        else this.captures = null;
    }

    public Square getFrom() {
        return this.from;
    }

    public Square getTo() {
        return this.to;
    }

    public Square getCaptures() {
        return this.captures;
    }
}
