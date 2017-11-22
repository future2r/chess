package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Represents a piece on a board. A piece is defined by a piece type and the player that owns this piece.
 */
public final class Piece implements Cloneable {

    private final PieceType type;
    private final Player player;
    private boolean moved;

    /**
     * Creates a new piece.
     *
     * @param type   the type of the piece
     * @param player the player that owns this piece
     */
    Piece(PieceType type, Player player) {
        this.type = Objects.requireNonNull(type, "type cannot be null");
        this.player = Objects.requireNonNull(player, "player cannot be null");
    }

    /**
     * Returns the type of this piece.
     *
     * @return the type of this piece
     */
    public PieceType getType() {
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
     * Returns if this piece has been moved in the current game.
     *
     * @return {@code true} if the piece was moved, otherwise {@code false}
     */
    boolean isMoved() {
        return this.moved;
    }

    /**
     * Marks this piece as moved.
     */
    void markMoved() {
        this.moved = true;
    }

    /**
     * Resets the moved flag. This is used for board setup.
     */
    void resetMoved() {
        this.moved = false;
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
                && this.moved == other.moved;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", this.type.getDisplayName(), this.player.getDisplayName());
    }
}
