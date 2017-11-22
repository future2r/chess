package name.ulbricht.chess.fx.design;

import javafx.scene.canvas.GraphicsContext;
import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Piece;

public interface BoardRenderer {

    enum SquareIndicator {
        TARGET, CAPTURED
    }

    enum Border {
        LEFT, RIGHT, TOP, BOTTOM
    }

    enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    void drawBackground(GraphicsContext gc, double width, double height);

    void drawSquare(GraphicsContext gc, double size, Coordinate coordinate, Piece piece,
                    boolean focused, boolean squareFocused, boolean squareSelected, SquareIndicator indicator);

    void drawBorder(GraphicsContext gc, double width, double height, Border border, int index, boolean focused);

    void drawCorner(GraphicsContext gc, double size, Corner corner, boolean focused);
}
