package name.ulbricht.chess.fx.design;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import name.ulbricht.chess.game.Coordinate;

abstract class AbstractBoardRenderer implements BoardRenderer {

    @Override
    public void drawBorder(GraphicsContext gc, double width, double height, Border border, int index, boolean focused) {
        gc.setFill(focused ? Color.GRAY : Color.SILVER);
        gc.fillRect(0, 0, width + 1, height + 1);

        String text = null;
        switch (border) {
            case LEFT:
            case RIGHT:
                text = Character.toString(Coordinate.toRowName(index));
                break;
            case TOP:
            case BOTTOM:
                text = Character.toString(Coordinate.toColumnName(index));
                break;
        }

        gc.setFill(focused ? Color.WHITE : Color.GRAY);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, Math.min(width, height) * 0.6));
        gc.fillText(text, width / 2, height / 2);
    }

    @Override
    public void drawCorner(GraphicsContext gc, double size, Corner corner, boolean focused) {
        gc.setFill(focused ? Color.GRAY : Color.SILVER);
        gc.fillRect(0, 0, size + 1, size + 1);
    }
}
