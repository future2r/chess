package name.ulbricht.chessfx.core;

import java.util.Objects;

/**
 * Represents a piece on a board. A piece is defined by a piece type and the player that owns this piece.
 */
public final class Piece implements Cloneable {

    /**
     * Defines all valid piece types.
     */
    public enum Type {
        PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

        /**
         * Returns a localized short name for this piece type. This name follows the official chess piece notation.
         * Usually this will be a single character. The type {@link #PAWN} may not have a short name.
         *
         * @return the short name for this piece type
         */
        public String getShortName() {
            return Messages.getString("Piece." + this.getClass().getSimpleName() + '.' + name() + ".shortName");
        }

        /**
         * Returns a localized display name for this piece type.
         *
         * @return the display name for this piece type
         */
        public String getDisplayName() {
            return Messages.getString("Piece." + this.getClass().getSimpleName() + '.' + name() + ".displayName");
        }
    }

    private final Type type;
    private final Player player;
    private int moveCount;

    /**
     * Creates a new piece.
     *
     * @param type   the type of the piece
     * @param player the player that owns this piece
     */
    public Piece(Type type, Player player) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.player = Objects.requireNonNull(player, "player cannot be null");
    }

    /**
     * Returns the type of this piece.
     *
     * @return the type of this piece
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Returns the player that owns this piece.
     *
     * @return a player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Returns the number of moves performed with this piece in the current game. This may be used by the the rules.
     *
     * @return the number of moves of this piece
     */
    public int getMoveCount() {
        return this.moveCount;
    }

    /**
     * Increments the number of moves performed with this piece.
     */
    public void incrementMoveCount() {
        this.moveCount++;
    }

    @Override
    public Piece clone() {
        try {
            return (Piece) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
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

        return Objects.equals(this.type, other.type)
                && Objects.equals(this.player, other.player)
                && this.moveCount == other.moveCount;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.type.getDisplayName(), this.player.getDisplayName());
    }
}
