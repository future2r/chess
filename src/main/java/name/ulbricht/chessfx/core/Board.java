package name.ulbricht.chessfx.core;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents the board where the pieces are placed and moved.
 */
public final class Board implements Cloneable {

    private Piece[] pieces;

    /**
     * Creates a new empty board.
     */
    public Board() {
        // create all required squares
        this.pieces = new Piece[Coordinate.COLUMNS * Coordinate.ROWS];
    }

    /**
     * Returns a map with all pieces on the board. The keys are the coordinates and the values are the pieces. Empty
     * square will not be returned.
     *
     * @return a map with coordinates and pieces
     */
    Map<Coordinate, Piece> pieces() {
        return Coordinate.values()
                .filter(c -> this.pieces[c.getIndex()] != null)
                .collect(Collectors.toMap(c -> c, c -> this.pieces[c.getIndex()]));
    }

    /**
     * Sets a piece to the square defined by the given coordinate. If the piece is {@code null} then the square will be
     * cleared. Any current piece will be replaced and returned.
     *
     * @param coordinate the coordinate
     * @param piece      the field or {@code null}
     * @return an optional replaced piece
     * @see #getPiece(Coordinate)
     * @see #removePiece(Coordinate)
     */
    public Optional<Piece> setPiece(Coordinate coordinate, Piece piece) {
        Optional<Piece> replacedPiece = getPiece(Objects.requireNonNull(coordinate, "coordinate cannot be null"));
        this.pieces[coordinate.getIndex()] = piece;
        return replacedPiece;
    }

    /**
     * Returns the piece at the given coordinate. If the square is empty the returned value will be empty.
     *
     * @param coordinate the coordinate
     * @return an optional piece
     * @see #setPiece(Coordinate, Piece)
     */
    public Optional<Piece> getPiece(Coordinate coordinate) {
        return Optional.ofNullable(this.pieces[Objects.requireNonNull(coordinate, "coordinate cannot be null").getIndex()]);
    }

    /**
     * Removes the current piece from the square and returns a potentially replaced piece
     *
     * @param coordinate the coordinate
     * @return an optional replaced piece
     */
    public Optional<Piece> removePiece(Coordinate coordinate) {
        return setPiece(coordinate, null);
    }

    /**
     * Initializes the board with the initial positions of the pieces.
     */
    public void setup() {

        // clear all squares
        Arrays.fill(this.pieces, null);

        Piece.Type[] baseLinePieces = new Piece.Type[]{
                Piece.Type.ROOK, Piece.Type.KNIGHT, Piece.Type.BISHOP, Piece.Type.QUEEN,
                Piece.Type.KING, Piece.Type.BISHOP, Piece.Type.KNIGHT, Piece.Type.ROOK};

        Coordinate whiteCoordinate = Coordinate.valueOf("a1");
        Coordinate blackCoordinate = Coordinate.valueOf("a8");

        for (int i = 0; i < Coordinate.COLUMNS; i++) {

            setPiece(whiteCoordinate, new Piece(baseLinePieces[i], Player.WHITE));
            whiteCoordinate.moveUp().ifPresent(c -> setPiece(c, new Piece(Piece.Type.PAWN, Player.WHITE)));
            whiteCoordinate = whiteCoordinate.moveRight().orElse(null);

            setPiece(blackCoordinate, new Piece(baseLinePieces[i], Player.BLACK));
            blackCoordinate.moveDown().ifPresent(c -> setPiece(c, new Piece(Piece.Type.PAWN, Player.BLACK)));
            blackCoordinate = blackCoordinate.moveRight().orElse(null);
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
