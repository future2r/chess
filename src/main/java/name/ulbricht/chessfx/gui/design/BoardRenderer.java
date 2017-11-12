package name.ulbricht.chessfx.gui.design;

import javafx.scene.canvas.GraphicsContext;
import name.ulbricht.chessfx.core.Square;

public interface BoardRenderer {

    enum Border {
        LEFT, RIGHT, TOP, BOTTOM
    }

    enum Corner {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    void setContext(BoardRendererContext context);

    double getPrefSquareSize();

    double getPrefBorderSize();

    void drawBackground(GraphicsContext gc, double width, double height);

    void drawSquare(GraphicsContext gc, double size, Square square);

    void drawBorder(GraphicsContext gc, double width, double height, Border border, int index);

    void drawCorner(GraphicsContext gc, double size, Corner corner);
}
