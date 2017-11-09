package name.ulbricht.chessfx.gui.design;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Piece;
import name.ulbricht.chessfx.core.Player;

final class SimpleBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "simple";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.3);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 0.8);
    private static final Color COLOR_MOVE_TARGET = Color.rgb(0, 255, 0, 0.8);
    private static final Color COLOR_CAPTURE = Color.rgb(255, 0, 0, 8.0);

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
        gc.fillRect(0, 0, size + 1, size + 1);

        // focused & selected
        if (square.equals(getContext().getSelectedSquare())) {
            gc.setFill(COLOR_SELECTED);
            gc.fillRect(0, 0, size + 1, size + 1);
        } else if (square.equals(getContext().getFocusedSquare())) {
            gc.setFill(COLOR_FOCUSED);
            gc.fillRect(0, 0, size + 1, size + 1);
        }

        // piece
        if (!square.isEmpty()) {
            Piece piece = square.getPiece();

            // colors
            Color pieceBackgroundColor;
            Color pieceForegroundColor;
            if (piece.getPlayer() == Player.WHITE) {
                pieceBackgroundColor = Color.WHITE;
                pieceForegroundColor = Color.BLACK;

            } else {
                pieceBackgroundColor = Color.BLACK;
                pieceForegroundColor = Color.WHITE;
            }

            // piece background circle
            double circleInset = 0.15 * size;
            double circleSize = size - (2 * circleInset);
            gc.setFill(pieceBackgroundColor);
            gc.fillOval(circleInset, circleInset, circleSize, circleSize);
            gc.setStroke(pieceForegroundColor);
            gc.strokeOval(circleInset, circleInset, circleSize, circleSize);

            // piece name
            gc.setFill(pieceForegroundColor);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFont(Font.font("SansSerif", FontWeight.BOLD, size * 0.3));
            gc.fillText(piece.getType().getShortName(), size / 2, size / 2);
        }
    }
}
