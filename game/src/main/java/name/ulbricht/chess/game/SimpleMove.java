package name.ulbricht.chess.game;

import java.util.Objects;

final class SimpleMove extends AbstractMove {

    static Move create(Game game, Coordinate from, Coordinate to) {
        Objects.requireNonNull(game, "board cannot be null");
        Objects.requireNonNull(from, "from cannot be null");
        Objects.requireNonNull(to, "to cannot be null");

        if (game.getPiece(to) != null) return new SimpleMove(from, to, to);
        else return new SimpleMove(from, to, null);
    }

    private SimpleMove(Coordinate from, Coordinate to, Coordinate captures) {
        super(from, to, captures);
    }

    @Override
    public void perform(Game game) {
        // remove the piece
        Piece piece = game.removePiece(getFrom());

        // set the piece to the to square
        game.setPiece(getTo(), piece);

        // increment the move count of the moved piece
        piece.incrementMoveCount();
    }
}
