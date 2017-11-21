package name.ulbricht.chessfx.core;

/**
 * Defines all valid piece types.
 */
public enum PieceType {
    PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING;

    /**
     * Returns a localized display name for this piece type.
     *
     * @return the display name for this piece type
     */
    public String getDisplayName() {
        return Messages.getString(this.getClass().getSimpleName() + '.' + name() + ".displayName");
    }
}
