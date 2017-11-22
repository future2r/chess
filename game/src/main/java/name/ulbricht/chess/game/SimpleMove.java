package name.ulbricht.chess.game;

import java.util.Objects;

final class SimpleMove extends AbstractMove {

    static Move create(Board board, Coordinate from, Coordinate to) {
        Objects.requireNonNull(board, "board cannot be null");
        Objects.requireNonNull(from, "from cannot be null");
        Objects.requireNonNull(to, "to cannot be null");

        if (board.getPiece(to) != null) return new SimpleMove(from, to, to);
        else return new SimpleMove(from, to, null);
    }

    private SimpleMove(Coordinate from, Coordinate to, Coordinate captures) {
        super(from, to, captures);
    }

    @Override
    public void perform(Board board) {
        // remove the piece
        Piece piece = board.removePiece(getFrom());

        // set the piece to the to square
        board.setPiece(getTo(), piece);

        // increment the move count of the moved piece
        piece.incrementMoveCount();
    }
}
