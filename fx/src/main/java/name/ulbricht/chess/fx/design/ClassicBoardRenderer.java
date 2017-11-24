package name.ulbricht.chess.fx.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Piece;

import java.util.HashMap;
import java.util.Map;

final class ClassicBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "classic";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);
    private static final Color COLOR_TO = Color.rgb(0, 255, 0, 0.7);
    private static final Color COLOR_CAPTURED = Color.rgb(255, 0, 0, 0.7);

    private final Map<Piece, Image> pieceImages = new HashMap<>();

    public ClassicBoardRenderer() {
        for (Piece piece : Piece.values()) {
            pieceImages.put(piece, loadImage("classic-" + piece.name().toLowerCase() + ".png"));
        }
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Coordinate coordinate, Piece piece,
                           boolean focused, boolean squareFocused, boolean squareSelected, SquareIndicator indicator) {
        // background
        Color backgroundColor;
        if (((coordinate.columnIndex + coordinate.rowIndex) % 2) == 0)
            backgroundColor = Color.rgb(0xFF, 0xCC, 0x99);
        else backgroundColor = Color.rgb(0xCC, 0x99, 0x66);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, size + 1, size + 1);

        // focused & selected
        if (squareSelected) {
            gc.setFill(COLOR_SELECTED);
            gc.fillRect(0, 0, size + 1, size + 1);
        } else if (focused && squareFocused) {
            gc.setFill(COLOR_FOCUSED);
            gc.fillRect(0, 0, size + 1, size + 1);
        }

        if (piece != null) {
            Image image = this.pieceImages.get(piece);

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }

        // captured or move target
        if (indicator == SquareIndicator.CAPTURED) {
            gc.setFill(COLOR_CAPTURED);
            double circleInset = 0.25 * size;
            double circleSize = size - (2 * circleInset);
            gc.fillOval(circleInset, circleInset, circleSize, circleSize);
        } else if (indicator == SquareIndicator.TARGET) {
            gc.setFill(COLOR_TO);
            double circleInset = 0.25 * size;
            double circleSize = size - (2 * circleInset);
            gc.fillOval(circleInset, circleInset, circleSize, circleSize);
        }
    }

    private Image loadImage(String resourceName) {
        return new Image(this.getClass().getResource(resourceName).toString());
    }
}
