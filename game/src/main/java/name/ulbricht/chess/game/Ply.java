package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a go on a board.
 */
public final class Ply {

    static Ply simple(Player player, Coordinate source, Coordinate target) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Ply(player, PlyType.SIMPLE, source, target, null);
    }

    static Ply simpleCaptures(Player player, Coordinate source, Coordinate target) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new Ply(player, PlyType.SIMPLE, source, target, target);
    }

    static Ply pawnDoubleAdvance(Player player, Coordinate source) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(source);

        Coordinate target;
        if ((player == Player.WHITE && source.rowIndex != 1) || (player == Player.BLACK && source.rowIndex != 6))
            throw new IllegalArgumentException("Illegal source for player.");

        target = source.go(Direction.forward(player), 2);
        return new Ply(player, PlyType.PAWN_DOUBLE_ADVANCE, source, target, null);
    }

    private final Player player;
    private final PlyType type;
    private final Coordinate source;
    private final Coordinate target;
    private final Coordinate captures;

    private Ply(Player player, PlyType type, Coordinate source, Coordinate target, Coordinate captures) {
        this.player = player;
        this.type = type;
        this.source = source;
        this.target = target;
        this.captures = captures;
    }

    public Player getPlayer() {
        return this.player;
    }

    public PlyType getType() {
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
        Ply other = (Ply) obj;

        return Objects.equals(this.type, other.type)
                && Objects.equals(this.source, other.source)
                && Objects.equals(this.target, other.target)
                && Objects.equals(this.captures, other.captures);
    }
}
