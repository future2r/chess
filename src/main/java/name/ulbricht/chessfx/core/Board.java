package name.ulbricht.chessfx.core;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class Board implements Cloneable {

    private Piece[] pieces;

    public Board() {
        // create all required squares
        this.pieces = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
    }

    Map<Coordinate, Piece> pieces() {
        return Coordinate.values()
                .filter(c -> this.pieces[c.getIndex()] != null)
                .collect(Collectors.toMap(c -> c, c -> this.pieces[c.getIndex()]));
    }

    public void setPiece(Coordinate coordinate, Piece piece) {
        this.pieces[coordinate.getIndex()] = piece;
    }

    public Optional<Piece> getPiece(Coordinate coordinate) {
        return Optional.ofNullable(this.pieces[coordinate.getIndex()]);
    }

    public Optional<Piece> removePiece(Coordinate coordinate) {
        Optional<Piece> oldPiece = getPiece(coordinate);
        setPiece(coordinate, null);
        return oldPiece;
    }

    public void setup() {

        // clear all squares
        Arrays.fill(this.pieces, null);

        Piece.Type[] baseLinePieces = new Piece.Type[]{
                Piece.Type.ROOK,
                Piece.Type.KNIGHT,
                Piece.Type.BISHOP,
                Piece.Type.QUEEN,
                Piece.Type.KING,
                Piece.Type.BISHOP,
                Piece.Type.KNIGHT,
                Piece.Type.ROOK};

        Coordinate whiteCoordinate = Coordinate.valueOf("a1");
        Coordinate blackCoordinate = Coordinate.valueOf("a8");

        for (int i = 0; i < Coordinate.COLUMNS; i++) {

            setPiece(whiteCoordinate, new Piece(baseLinePieces[i], Player.WHITE));
            setPiece(whiteCoordinate.moveUp().get(), new Piece(Piece.Type.PAWN, Player.WHITE));
            if (!whiteCoordinate.isRightColumn()) whiteCoordinate = whiteCoordinate.moveRight().get();

            setPiece(blackCoordinate, new Piece(baseLinePieces[i], Player.BLACK));
            setPiece(blackCoordinate.moveDown().get(), new Piece(Piece.Type.PAWN, Player.BLACK));
            if (!blackCoordinate.isRightColumn()) blackCoordinate = blackCoordinate.moveRight().get();
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(this.pieces));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;

        Board other = (Board) obj;
        return Arrays.deepEquals(this.pieces, other.pieces);
    }

    @Override
    public Board clone() {
        try {
            Board clone = (Board) super.clone();
            clone.pieces = new Piece[this.pieces.length];
            for (int i = 0; i < clone.pieces.length; i++) {
                Piece piece = this.pieces[i];
                if (piece != null) clone.pieces[i] = piece.clone();
            }
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError(ex);
        }
    }
}
