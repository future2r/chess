package name.ulbricht.chessfx.core;

import java.util.Objects;

/**
 * Defines the two players of the game.
 */
public enum Player {

    /**
     * The white player.
     */
    WHITE,

    /**
     * The black player.
     */
    BLACK;

    /**
     * Returns a localized short name for this player. This will be a single character.
     *
     * @return the short name of the player.
     */
    public String getShortName() {
        return Messages.getString(this.getClass().getSimpleName() + '.' + name() + ".shortName");
    }

    /**
     * Returns the localized display name for this player.
     *
     * @return the display name of this player.
     */
    public String getDisplayName() {
        return Messages.getString(this.getClass().getSimpleName() + '.' + name() + ".displayName");
    }

    /**
     * Returns {@code true} if the given player is the opponent of this player.
     *
     * @param other another player
     * @return {@code true} if the given player is the opponent, otherwise {@code false}
     */
    public boolean isOpponent(Player other) {
        return this != Objects.requireNonNull(other, "other player cannot be null");
    }
}