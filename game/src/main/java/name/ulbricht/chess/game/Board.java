package name.ulbricht.chess.game;

import java.util.Objects;

final class Board implements Cloneable {

    private Piece[] pieces = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];

    Piece getPiece(Coordinate coordinate) {
        return this.pieces[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()];
    }

    boolean isEmpty(Coordinate coordinate) {
        return getPiece(coordinate) == null;
    }

    void setPiece(Coordinate coordinate, Piece piece) {
        this.pieces[Objects.requireNonNull(coordinate, "coordinate cannot be null").ordinal()] = piece;
    }

    Coordinate king(Player player) {
        return findFirst(Piece.valueOf(PieceType.KING, Objects.requireNonNull(player, "player cannot be null")));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{pieces=");
        for (int row = 0; row < Coordinate.ROWS; row++) {
            sb.append('\n');
            for (int column = Coordinate.COLUMNS - 1; column >= 0; column--) {
                Piece piece = getPiece(Coordinate.valueOf(column, row));
                if (piece != null) sb.append(FEN.symbol(piece));
                else sb.append(' ');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    protected Board clone() {
        try {
            Board copy = (Board) super.clone();
            copy.pieces = this.pieces.clone();
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private Coordinate findFirst(Piece piece) {
        for (Coordinate coordinate : Coordinate.values()) {
            if (getPiece(coordinate) == piece) return coordinate;
        }
        return null;
    }
}
