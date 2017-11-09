package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Board {

    public static final class Square {

        private final Coordinate coordinate;
        private Piece piece;

        public Square(Coordinate coordinate) {
            this.coordinate = Objects.requireNonNull(coordinate, "coordinate cannot be null");
        }

        public Coordinate getCoordinate() {
            return this.coordinate;
        }

        public void setPiece(Piece piece) {
            this.piece = piece;
        }

        public Piece getPiece() {
            return this.piece;
        }

        public boolean isEmpty() {
            return this.piece == null;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", this.coordinate, this.piece != null ? this.piece : "");
        }
    }

    private final Square[] squares;

    public Board() {
        // create all required squares
        this.squares = Coordinate.values().map(Square::new).toArray(Square[]::new);
    }

    public Square getSquare(Coordinate coordinate) {
        return squares[coordinate.getIndex()];
    }

    public void setup() {

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
            setPiece(whiteCoordinate.moveUp(), new Piece(Piece.Type.PAWN, Player.WHITE));
            if (!whiteCoordinate.isRightColumn()) whiteCoordinate = whiteCoordinate.moveRight();

            setPiece(blackCoordinate, new Piece(baseLinePieces[i], Player.BLACK));
            setPiece(blackCoordinate.moveDown(), new Piece(Piece.Type.PAWN, Player.BLACK));
            if (!blackCoordinate.isRightColumn()) blackCoordinate = blackCoordinate.moveRight();
        }
    }


    public void setPiece(Coordinate coordinate, Piece piece) {
        getSquare(coordinate).setPiece(piece);
    }

    public Piece getPiece(Coordinate coordinate) {
        return getSquare(coordinate).getPiece();
    }

}
