package name.ulbricht.chessfx.gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;

import java.util.stream.Collectors;

final class BoardCanvas extends Canvas {

    private static class Dimensions {

        final double xOffset;
        final double yOffset;
        final double keySize;
        final double squareSize;

        Dimensions(double width, double height, BoardDesign design) {
            double prefSquareSize = design.getPrefSquareSize();
            double prefKeySize = design.getPrefKeySize();
            double prefBoardWidth = 2 * prefKeySize + Coordinate.COLUMNS * prefSquareSize;
            double prefBoardHeight = 2 * prefKeySize + Coordinate.ROWS * prefSquareSize;

            double widthScale = width / prefBoardWidth;
            double heightScale = height / prefBoardHeight;
            double scale = Math.min(widthScale, heightScale);

            this.xOffset = (width - (scale * prefBoardWidth)) / 2;
            this.yOffset = (height - (scale * prefBoardHeight)) / 2;

            this.keySize = scale * prefKeySize;
            this.squareSize = scale * prefSquareSize;
        }


    }

    private Board board;
    private BoardDesign design;

    BoardCanvas(Board board, BoardDesign design) {
        this.board = board;
        this.design = design;

        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        Dimensions dim = new Dimensions(width, height, this.design);

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        this.design.drawBackground(gc, width, height);

        for (Coordinate coordinate : Coordinate.values().collect(Collectors.toList())) {

            double squareXOffset = dim.keySize + dim.xOffset + (coordinate.getColumnIndex() * dim.squareSize);
            double squareYOffset = dim.keySize + (dim.yOffset + ((Coordinate.ROWS - 1) * dim.squareSize))
                    - (coordinate.getRowIndex() * dim.squareSize);

            gc.save();
            gc.translate(squareXOffset, squareYOffset);
            this.design.drawSquare(gc, dim.squareSize, board.getSquare(coordinate));
            gc.restore();
        }
    }
}
