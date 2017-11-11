package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Move {

    private final Board.Square from;
    private final Board.Square to;
    private final Board.Square captures;

    public Move(Board.Square from, Board.Square to) {
        this(from, to, null);
    }

    public Move(Board.Square from, Board.Square to, Board.Square captures) {
        this.from = Objects.requireNonNull(from, "from cannot be null");
        this.to = Objects.requireNonNull(to, "to cannot be null");
        if (captures == null && !this.to.isEmpty()) this.captures = captures;
        else this.captures = null;
    }

    public Board.Square getFrom() {
        return this.from;
    }

    public Board.Square getTo() {
        return this.to;
    }

    public Board.Square getCaptures() {
        return this.captures;
    }
}
