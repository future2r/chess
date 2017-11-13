package name.ulbricht.chessfx.core;

import java.util.Objects;
import java.util.Optional;

public final class Move {

    private final Coordinate from;
    private final Coordinate to;
    private final Coordinate captured;

    public Move(Coordinate from, Coordinate to, Coordinate captured) {
        this.from = Objects.requireNonNull(from, "from cannot be null");
        this.to = Objects.requireNonNull(to, "to cannot be null");
        this.captured = captured;
    }

    public Coordinate getFrom() {
        return this.from;
    }

    public Coordinate getTo() {
        return this.to;
    }

    public Optional<Coordinate> getCaptured() {
        return Optional.ofNullable(this.captured);
    }

    void perform(Board board) {
        // remove the captured piece
        if (this.captured != null) {
            Square capturedSquare = board.getSquare(this.captured);
            capturedSquare.clear();
        }

        // get and remove the piece from the from square
        Square fromSquare = board.getSquare(this.from);
        Piece piece = fromSquare.getPiece();
        fromSquare.clear();

        // set the piece to the to square
        Square toSquare = board.getSquare(this.to);
        toSquare.setPiece(piece);

        // increment the move count of the moved piece
        piece.incrementMoveCount();
    }
}
