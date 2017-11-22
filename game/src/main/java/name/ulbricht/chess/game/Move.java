package name.ulbricht.chess.game;

/**
 * Represents a move on a board.
 */
public final class Move {

    static Move simple(Game game, Coordinate source, Coordinate target) {
        return new Move(game, MoveType.SIMPLE, source, target, null);
    }

    private final Game game;
    private final MoveType type;
    private final Coordinate source;
    private final Coordinate target;
    private final Coordinate captures;

    private Move(Game game, MoveType type, Coordinate source, Coordinate target, Coordinate captures) {
        this.game = game;
        this.type = type;
        this.source = source;
        this.target = target;
        this.captures = captures;
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
        if (this.captures != null) return this.captures;
        Piece piece = this.game.getPiece(this.target);
        if (piece != null && piece.getPlayer().isOpponent(this.game.getCurrentPlayer())) return this.target;
        return null;
    }
}
