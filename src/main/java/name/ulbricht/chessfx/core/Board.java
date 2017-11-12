package name.ulbricht.chessfx.core;

import java.util.stream.Stream;

public final class Board {

    private final Square[] squares;

    public Board() {
        // create all required squares
        this.squares = Coordinate.values().map(Square::new).toArray(Square[]::new);
    }

    Stream<Square> getSquares() {
        return Stream.of(this.squares);
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
            setPiece(whiteCoordinate.moveUp().get(), new Piece(Piece.Type.PAWN, Player.WHITE));
            if (!whiteCoordinate.isRightColumn()) whiteCoordinate = whiteCoordinate.moveRight().get();

            setPiece(blackCoordinate, new Piece(baseLinePieces[i], Player.BLACK));
            setPiece(blackCoordinate.moveDown().get(), new Piece(Piece.Type.PAWN, Player.BLACK));
            if (!blackCoordinate.isRightColumn()) blackCoordinate = blackCoordinate.moveRight().get();
        }
    }


    public void setPiece(Coordinate coordinate, Piece piece) {
        getSquare(coordinate).setPiece(piece);
    }

    public Piece getPiece(Coordinate coordinate) {
        return getSquare(coordinate).getPiece();
    }

}
