package name.ulbricht.chessfx.core;

import java.util.Objects;

public final class Board {

    public static final class Field {

        private final Coordinate coordinate;
        private Figure figure;

        public Field(Coordinate coordinate) {
            this.coordinate = Objects.requireNonNull(coordinate, "coordinate cannot ne null");
        }

        public Coordinate getCoordinate() {
            return this.coordinate;
        }

        public void setFigure(Figure figure) {
            this.figure = figure;
        }

        public Figure getFigure() {
            return this.figure;
        }

        @Override
        public String toString() {
            return String.format("%s: %s", this.coordinate, this.figure != null ? this.figure : "");
        }
    }

    private final Field[] fields;

    public Board() {
        // create all required fields
        this.fields = Coordinate.values().map(Field::new).toArray(l -> new Field[l]);
    }

    public Field getField(Coordinate coordinate) {
        return fields[coordinate.getIndex()];
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
        this.fields[coordinate.getIndex()].setFigure(figure);
    }

    public Figure getFigure(Coordinate coordinate){
        return this.fields[coordinate.getIndex()].getFigure();
    }

}
