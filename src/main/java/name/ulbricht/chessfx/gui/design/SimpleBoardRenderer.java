package name.ulbricht.chessfx.gui.design;

import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Figure;
import name.ulbricht.chessfx.core.Player;

final class SimpleBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "simple";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);

    @Override
    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Board.Square square, boolean focused, boolean selected) {
        // background
        Color backgroundColor;
        if (square.getCoordinate().getColor() == Coordinate.Color.LIGHT) backgroundColor = Color.LIGHTGRAY;
        else backgroundColor = Color.DARKGRAY;
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, size + 1, size + 1);

        // focused & selected
        if (selected) {
            gc.setFill(COLOR_SELECTED);
            gc.fillRect(0, 0, size + 1, size + 1);
        } else if (focused) {
            gc.setFill(COLOR_FOCUSED);
            gc.fillRect(0, 0, size + 1, size + 1);
        }

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
            gc.setFont(Font.font("SansSerif", FontWeight.BOLD, size * 0.3));
            gc.fillText(figure.getType().getShortName(), size / 2, size / 2);
        }
    }
}
