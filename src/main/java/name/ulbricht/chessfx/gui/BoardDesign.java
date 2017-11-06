package name.ulbricht.chessfx.gui;

import javafx.scene.canvas.GraphicsContext;
import name.ulbricht.chessfx.core.Board;

public interface BoardDesign {

    double getPrefSquareSize();

    double getPrefKeySize();

    void drawBackground(GraphicsContext gc, double width, double height);

    void drawSquare(GraphicsContext gc, double size, Board.Square square);

}
