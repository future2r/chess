package name.ulbricht.chessfx.gui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.gui.design.BoardRenderer;

import java.util.stream.Collectors;

final class BoardCanvas extends Canvas {

    private static final class Dimensions {

        final double xOffset;
        final double yOffset;
        final double borderSize;
        final double squareSize;

        Dimensions(double width, double height, BoardRenderer renderer) {
            double prefSquareSize = renderer.getPrefSquareSize();
            double prefBorderSize = renderer.getPrefBorderSize();
            double prefBoardWidth = 2 * prefBorderSize + Coordinate.COLUMNS * prefSquareSize;
            double prefBoardHeight = 2 * prefBorderSize + Coordinate.ROWS * prefSquareSize;

            double widthScale = width / prefBoardWidth;
            double heightScale = height / prefBoardHeight;
            double scale = Math.min(widthScale, heightScale);

            this.xOffset = (width - (scale * prefBoardWidth)) / 2;
            this.yOffset = (height - (scale * prefBoardHeight)) / 2;

            this.borderSize = scale * prefBorderSize;
            this.squareSize = scale * prefSquareSize;
        }
    }

    private final Board board;
    private final ObjectProperty<Coordinate> selectedSquareProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Coordinate> focusedSquareProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<BoardRenderer> rendererProperty = new SimpleObjectProperty<>();

    BoardCanvas(Board board) {
        this.board = board;

        widthProperty().addListener(e -> draw());
        heightProperty().addListener(e -> draw());

        selectedSquareProperty().addListener(e -> draw());
        focusedSquareProperty().addListener(e -> draw());
        rendererProperty().addListener(e -> draw());
    }

    ObjectProperty<Coordinate> selectedSquareProperty() {
        return this.selectedSquareProperty;
    }

    Coordinate getSelectedSquare() {
        return selectedSquareProperty().get();
    }

    void setSelectedSquare(Coordinate selectedSquare) {
        selectedSquareProperty().set(selectedSquare);
    }

    ObjectProperty<Coordinate> focusedSquareProperty() {
        return this.focusedSquareProperty;
    }

    Coordinate getFocusedSquare() {
        return focusedSquareProperty().get();
    }

    void setFocusedSquare(Coordinate focusedSquare) {
        focusedSquareProperty().set(focusedSquare);
    }

    ObjectProperty<BoardRenderer> rendererProperty() {
        return this.rendererProperty;
    }

    BoardRenderer getRenderer() {
        return rendererProperty().get();
    }

    void setRenderer(BoardRenderer renderer) {
        rendererProperty().set(renderer);
    }

    Coordinate getCoordinateAt(double x, double y) {
        if (rendererProperty().get() != null) {
            Dimensions dim = new Dimensions(getWidth(), getHeight(), rendererProperty().get());
            int columnIndex = (int) Math.floor((x - dim.xOffset - dim.borderSize) / dim.squareSize);
            int rowIndex = (int) Math.floor(Coordinate.ROWS - (y - dim.yOffset - dim.borderSize) / dim.squareSize);
            if (columnIndex >= 0 && columnIndex < Coordinate.COLUMNS && rowIndex >= 0 && rowIndex < Coordinate.ROWS)
                return Coordinate.valueOf(columnIndex, rowIndex);
        }
        return null;
    }

    private void draw() {
        double width = getWidth();
        double height = getHeight();

        // clear the drawing
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        if (rendererProperty().get() == null) return;

        // calculate the dimensions
        Dimensions dim = new Dimensions(width, height, rendererProperty().get());

        // draw the canvas background
        rendererProperty().get().drawBackground(gc, width, height);

        // draw vertical borders
        double xLeftBorder = dim.xOffset;
        double xRightBorder = dim.xOffset + dim.borderSize + (Coordinate.COLUMNS * dim.squareSize);
        for (int rowIndex = 0; rowIndex < Coordinate.ROWS; rowIndex++) {
            double yBorder = dim.yOffset + dim.borderSize + ((Coordinate.ROWS - rowIndex - 1) * dim.squareSize);

            // left border
            gc.save();
            gc.translate(xLeftBorder, yBorder);
            rendererProperty().get().drawBorder(gc, dim.borderSize, dim.squareSize, BoardRenderer.Border.LEFT, rowIndex);
            gc.restore();

            // right border
            gc.save();
            gc.translate(xRightBorder, yBorder);
            rendererProperty().get().drawBorder(gc, dim.borderSize, dim.squareSize, BoardRenderer.Border.RIGHT, rowIndex);
            gc.restore();
        }

        // draw the horizontal borders
        double yTopBorder = dim.yOffset;
        double yBottomBorder = dim.yOffset + dim.borderSize + (Coordinate.ROWS * dim.squareSize);
        for (int columnIndex = 0; columnIndex < Coordinate.COLUMNS; columnIndex++) {
            double xBorder = dim.xOffset + dim.borderSize + (columnIndex * dim.squareSize);
            // top border
            gc.save();
            gc.translate(xBorder, yTopBorder);
            rendererProperty().get().drawBorder(gc, dim.squareSize, dim.borderSize, BoardRenderer.Border.TOP, columnIndex);
            gc.restore();

            // bottom border
            gc.save();
            gc.translate(xBorder, yBottomBorder);
            rendererProperty().get().drawBorder(gc, dim.squareSize, dim.borderSize, BoardRenderer.Border.BOTTOM, columnIndex);
            gc.restore();
        }

        // draw top-left corner
        gc.save();
        gc.translate(xLeftBorder, yTopBorder);
        rendererProperty().get().drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_LEFT);
        gc.restore();

        // draw top-right corner
        gc.save();
        gc.translate(xRightBorder, yTopBorder);
        rendererProperty().get().drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_LEFT);
        gc.restore();

        // draw bottom-left corner
        gc.save();
        gc.translate(xLeftBorder, yBottomBorder);
        rendererProperty().get().drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_LEFT);
        gc.restore();

        // draw bottom-right corner
        gc.save();
        gc.translate(xRightBorder, yBottomBorder);
        rendererProperty().get().drawCorner(gc, dim.borderSize, BoardRenderer.Corner.TOP_LEFT);
        gc.restore();

        // draw the squares
        for (Coordinate coordinate : Coordinate.values().collect(Collectors.toList())) {

            double squareXOffset = dim.borderSize + dim.xOffset + (coordinate.getColumnIndex() * dim.squareSize);
            double squareYOffset = dim.borderSize + (dim.yOffset + ((Coordinate.ROWS - 1) * dim.squareSize))
                    - (coordinate.getRowIndex() * dim.squareSize);

            gc.save();
            gc.translate(squareXOffset, squareYOffset);
            rendererProperty().get().drawSquare(gc, dim.squareSize, board.getSquare(coordinate), coordinate.equals(getFocusedSquare()), coordinate.equals(getSelectedSquare()));
            gc.restore();
        }
    }
}
