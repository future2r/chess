package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a move on a board.
 */
public final class Move {

    static Move simple(Player player, Coordinate source, Coordinate target) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Move(player, MoveType.SIMPLE, source, target, null);
    }

    static Move simpleCaptures(Player player, Coordinate source, Coordinate target) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Move(player, MoveType.SIMPLE, source, target, target);
    }

    static Move pawnDoubleAdvance(Player player, Coordinate source) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);

        Coordinate target;
        if (player == Player.WHITE) {
            if (source.rowIndex != 1) throw new IllegalArgumentException("Illegal source for player.");
            target = source.moveTo(0, 2);
        } else {
            if (source.rowIndex != 6) throw new IllegalArgumentException("Illegal source for player.");
            target = source.moveTo(0, -2);
        }
        return new Move(player, MoveType.PAWN_DOUBLE_ADVANCE, source, target, null);
    }

    private final Player player;
    private final MoveType type;
    private final Coordinate source;
    private final Coordinate target;
    private final Coordinate captures;

    private Move(Player player, MoveType type, Coordinate source, Coordinate target, Coordinate captures) {
        this.player = player;
        this.type = type;
        this.source = source;
        this.target = target;
        this.captures = captures;
    }

    public Player getPlayer() {
        return this.player;
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
