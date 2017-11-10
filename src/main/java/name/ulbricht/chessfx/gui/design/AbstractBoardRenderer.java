package name.ulbricht.chessfx.gui.design;

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import name.ulbricht.chessfx.core.Coordinate;

abstract class AbstractBoardRenderer implements BoardRenderer {

    private BoardRendererContext context;

    @Override
    public void setContext(BoardRendererContext context) {
        this.context = context;
    }

    protected BoardRendererContext getContext() {
        return this.context;
    }

    @Override
    public double getPrefSquareSize() {
        return 100;
    }

    @Override
    public double getPrefBorderSize() {
        return 30;
    }

    @Override
    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawBorder(GraphicsContext gc, double width, double height, Border border, int index) {
        gc.setFill(getContext().isBoardFocused() ? Color.GRAY : Color.SILVER);
        gc.fillRect(0, 0, width + 1, height + 1);

        String text = null;
        switch (border) {
            case LEFT:
            case RIGHT:
                text = Coordinate.toRowName(index);
                break;
            case TOP:
            case BOTTOM:
                text = Coordinate.toColumnName(index);
                break;
        }

        gc.setFill(getContext().isBoardFocused() ? Color.WHITE : Color.GRAY);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFont(Font.font("SansSerif", FontWeight.BOLD, Math.min(width, height) * 0.6));
        gc.fillText(text, width / 2, height / 2);
    }

    @Override
    public void drawCorner(GraphicsContext gc, double size, Corner corner) {
        gc.setFill(getContext().isBoardFocused() ? Color.GRAY : Color.SILVER);
        gc.fillRect(0, 0, size + 1, size + 1);
    }
}
