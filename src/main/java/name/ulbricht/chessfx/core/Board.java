package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Board {

    public static final class Square {

        public enum Color {
            LIGHT, DARK;
        }

        private final Coordinate coordinate;
        private Figure figure;

        public Square(Coordinate coordinate) {
            this.coordinate = Objects.requireNonNull(coordinate, "coordinate cannot be null");
        }

        public Coordinate getCoordinate() {
            return this.coordinate;
        }

        public Color getColor() {
            if (this.coordinate.getRowIndex() % 2 == 0){
                if (this.coordinate.getColumnIndex() % 2 == 0) return Color.LIGHT;
                else return Color.DARK;
            }
            if (this.coordinate.getColumnIndex() % 2 == 0) return Color.DARK;
            else return Color.LIGHT;
        }

        public void setFigure(Figure figure) {
            this.figure = figure;
        }

        public Figure getFigure() {
            return this.figure;
        }

        public boolean isEmpty() {
            return this.figure == null;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", this.coordinate, this.figure != null ? this.figure : "");
        }
    }

    private final Square[] squares;

    public Board() {
        // create all required squares
        this.squares = Coordinate.values().map(Square::new).toArray(l -> new Square[l]);
    }

    public Square getSquare(Coordinate coordinate) {
        return squares[coordinate.getIndex()];
    }

    public void setup() {

        Figure.Type[] baseLineFigures = new Figure.Type[]{
                Figure.Type.ROOK,
                Figure.Type.KNIGHT,
                Figure.Type.BISHOP,
                Figure.Type.QUEEN,
                Figure.Type.KING,
                Figure.Type.BISHOP,
                Figure.Type.KNIGHT,
                Figure.Type.ROOK};

        Coordinate whiteCoordinate = Coordinate.valueOf("a1");
        Coordinate blackCoordinate = Coordinate.valueOf("a8");

        for (int i = 0; i < Coordinate.COLUMNS; i++) {

            setFigure(whiteCoordinate, new Figure(baseLineFigures[i], Player.WHITE));
            setFigure(whiteCoordinate.moveUp(), new Figure(Figure.Type.PAWN, Player.WHITE));
            if (!whiteCoordinate.isRightColumn()) whiteCoordinate = whiteCoordinate.moveRight();

            setFigure(blackCoordinate, new Figure(baseLineFigures[i], Player.BLACK));
            setFigure(blackCoordinate.moveDown(), new Figure(Figure.Type.PAWN, Player.BLACK));
            if (!blackCoordinate.isRightColumn()) blackCoordinate = blackCoordinate.moveRight();
        }
    }


    public void setFigure(Coordinate coordinate, Figure figure) {
        getSquare(coordinate).setFigure(figure);
    }

    public Figure getFigure(Coordinate coordinate) {
        return getSquare(coordinate).getFigure();
    }

}
