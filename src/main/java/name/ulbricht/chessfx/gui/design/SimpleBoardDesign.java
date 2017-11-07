package name.ulbricht.chessfx.gui.design;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Figure;
import name.ulbricht.chessfx.core.Player;

public class SimpleBoardDesign extends AbstractBoardDesign {

    @Override
    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Board.Square square) {
        // background
        Color backgroundColor;
        if (square.getCoordinate().getColor() == Coordinate.Color.LIGHT) backgroundColor = Color.LIGHTGRAY;
        else backgroundColor = Color.DARKGRAY;
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, size, size);

        // figure
        if (!square.isEmpty()) {
            Figure figure = square.getFigure();

            // colors
            Color figureBackgroundColor;
            Color figureForegroundColor;
            if (figure.getPlayer() == Player.WHITE) {
                figureBackgroundColor = Color.WHITE;
                figureForegroundColor = Color.BLACK;

            } else {
                figureBackgroundColor = Color.BLACK;
                figureForegroundColor = Color.WHITE;
            }

            // figure background circle
            double circleInset = 0.15 * size;
            double circleSize = size - (2 * circleInset);
            gc.setFill(figureBackgroundColor);
            gc.fillOval(circleInset, circleInset, circleSize, circleSize);
            gc.setStroke(figureForegroundColor);
            gc.strokeOval(circleInset, circleInset, circleSize, circleSize);

            // figure name
            gc.setFill(figureForegroundColor);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font("SansSerif", FontWeight.BOLD, size / 3));
            gc.fillText(figure.getType().getShortName(), size / 2, size / 2, size);
        }
    }

    @Override
    public void drawBorder(GraphicsContext gc, double width, double height, Border border, int index) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawCorner(GraphicsContext gc, double size, Corner corner) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, size, size);
    }
}
