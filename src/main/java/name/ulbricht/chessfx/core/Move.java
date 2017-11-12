package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Move {

    private final Square fromSquare;
    private final Square toSquare;
    private final Square capturedSquare;

    public Move(Square fromSquare, Square toSquare) {
        this(fromSquare, toSquare, null);
    }

    public Move(Square fromSquare, Square toSquare, Square capturedSquare) {
        this.fromSquare = Objects.requireNonNull(fromSquare, "fromSquare cannot be null");
        this.toSquare = Objects.requireNonNull(toSquare, "toSquare cannot be null");
        if (capturedSquare == null && !this.toSquare.isEmpty()) this.capturedSquare = this.toSquare;
        else this.capturedSquare = null;
    }

    public Square getFromSquare() {
        return this.fromSquare;
    }

    public Square getToSquare() {
        return this.toSquare;
    }

    public Square getCapturedSquare() {
        return this.capturedSquare;
    }

    void perform() {
        if (this.capturedSquare != null) {
            this.capturedSquare.setPiece(null);
        }

        Piece piece = this.fromSquare.getPiece();
        this.fromSquare.setPiece(null);

        this.toSquare.setPiece(piece);
        piece.incrementMoveCount();
    }
}
