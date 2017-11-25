package name.ulbricht.chess.game;

import java.util.Objects;

/**
 * Defines the two players of the game.
 */
public enum Player {

    /**
     * The white player.
     */
    WHITE('w'),

    /**
     * The black player.
     */
    BLACK('b');

    public final char san;

    Player(char san) {
        this.san = san;
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

    public Player opponent() {
        return this == WHITE ? BLACK : WHITE;
    }

    public static Player ofSan(char c) {
        for (Player player : values()) {
            if (player.san == c) return player;
        }
        throw new IllegalArgumentException("Not a SAN player character: " + c);
    }
}