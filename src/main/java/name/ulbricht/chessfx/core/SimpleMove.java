package name.ulbricht.chessfx.core;

import java.util.Objects;

final class SimpleMove extends AbstractMove {

    static Move create(Board board, Coordinate from, Coordinate to) {
        Objects.requireNonNull(board, "board cannot be null");
        Objects.requireNonNull(from, "from cannot be null");
        Objects.requireNonNull(to, "to cannot be null");

        if (board.getPiece(to).isPresent()) return new SimpleMove(board, from, to, to);
        else return new SimpleMove(board, from, to, null);
    }

    private SimpleMove(Board board, Coordinate from, Coordinate to, Coordinate captures) {
        super(board, from, to, captures);
    }

    @Override
    public void perform(Board board) {
        // remove the piece
        Piece piece = board.removePiece(getFrom()).orElseThrow(() -> new IllegalStateException("piece expected"));

        // set the piece to the to square
        board.setPiece(getTo(), piece);

        // increment the move count of the moved piece
        piece.incrementMoveCount();
    }
}
