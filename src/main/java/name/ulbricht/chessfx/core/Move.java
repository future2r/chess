package name.ulbricht.chessfx.core;

import java.util.Optional;

/**
 * Represents a move on a board.
 */
public interface Move {

    Coordinate getFrom();

    Coordinate getTo();

    Optional<Coordinate> getCaptures();

    void perform(Board board);

    static Move simple(Board board, Coordinate from, Coordinate to) {
        return SimpleMove.create(board, from, to);
    }
}
