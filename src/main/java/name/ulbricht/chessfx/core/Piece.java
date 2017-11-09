package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Piece {

    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

        public String getShortName() {
            return Messages.getString(String.format("Piece.%s.%s.shortName", this.getClass().getSimpleName(), name()));
        }

        public String getDisplayName() {
            return Messages.getString(String.format("Piece.%s.%s.displayName", this.getClass().getSimpleName(), name()));
        }

        @Override
        public String toString() {
            return getDisplayName();
        }
    }

    private final Type type;
    private final Player player;

    public Piece(Type type, Player player) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.player = Objects.requireNonNull(player, "player cannot be null");
    }

    public Type getType() {
        return this.type;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.type, this.player);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Piece other = (Piece) obj;

        return Objects.equals(this.type, other.type) && Objects.equals(this.player, other.player);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.type.getDisplayName(), this.player.getDisplayName());
    }
}
