package name.ulbricht.chess.game;

/**
 * Represents a move on a board.
 */
public interface Move {

    Coordinate getFrom();

    Coordinate getTo();

    Coordinate getCaptures();

    void perform(Game game);

    static Move simple(Game game, Coordinate from, Coordinate to) {
        return SimpleMove.create(game, from, to);
    }
}
