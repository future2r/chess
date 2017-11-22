package name.ulbricht.chess.fx.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Piece;
import name.ulbricht.chess.game.PieceType;
import name.ulbricht.chess.game.Player;

import java.util.HashMap;
import java.util.Map;

final class DefaultBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "default";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);
    private static final Color COLOR_TO = Color.rgb(0, 255, 0, 1.0);
    private static final Color COLOR_CAPTURED = Color.rgb(255, 0, 0, 1.0);

    private final Image lightSquareImage;
    private final Image darkSquareImage;
    private final Map<Player, Map<PieceType, Image>> pieceImages = new HashMap<>();

    public DefaultBoardRenderer() {
        this.lightSquareImage = loadImage("default-square-light.png");
        this.darkSquareImage = loadImage("default-square-dark.png");

        for (Player player : Player.values()) {
            Map<PieceType, Image> images = new HashMap<>();
            this.pieceImages.put(player, images);
            for (PieceType type : PieceType.values()) {
                images.put(type, loadImage("default-" + player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png"));
            }
        }
    }


    @Override
    public void drawSquare(GraphicsContext gc, double size, Coordinate coordinate, Piece piece,
                           boolean focused, boolean squareFocused, boolean squareSelected, SquareIndicator indicator) {
        Image squareImage = ((coordinate.getColumnIndex() + coordinate.getRowIndex()) % 2) == 0 ?
                this.lightSquareImage : this.darkSquareImage;
        gc.drawImage(squareImage, 0, 0, size, size);

        // focused & selected
        if (squareSelected) {
            drawOuterHighlight(gc, size, COLOR_SELECTED);
        } else if (focused && squareFocused) {
            drawOuterHighlight(gc, size, COLOR_FOCUSED);
        }

        if (piece != null) {
            Image image = this.pieceImages.get(piece.getPlayer()).get(piece.getType());

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }

        // captured or move target
        if (indicator == SquareIndicator.CAPTURED) drawInnerHighlight(gc, size, COLOR_CAPTURED);
        else if (indicator == SquareIndicator.TARGET) drawInnerHighlight(gc, size, COLOR_TO);
    }

    private void drawOuterHighlight(GraphicsContext gc, double squareSize, Color color) {
        Stop[] stops = new Stop[]{
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.6, Color.TRANSPARENT),
                new Stop(0.8, color),
                new Stop(1.0, Color.TRANSPARENT)
        };
        RadialGradient radialGradient =
                new RadialGradient(0, 0, squareSize / 2, squareSize / 2,
                        squareSize / 2, false, CycleMethod.NO_CYCLE, stops);
        gc.setFill(radialGradient);
        gc.fillOval(0, 0, squareSize, squareSize);
    }

    private void drawInnerHighlight(GraphicsContext gc, double squareSize, Color color) {
        Stop[] stops = new Stop[]{
                new Stop(0.0, color),
                new Stop(1.0, Color.TRANSPARENT)
        };
        RadialGradient radialGradient =
                new RadialGradient(0, 0, squareSize / 2, squareSize / 2,
                        squareSize / 4, false, CycleMethod.NO_CYCLE, stops);
        gc.setFill(radialGradient);
        gc.fillOval(0, 0, squareSize, squareSize);
    }

    private Image loadImage(String resourceName) {
        return new Image(this.getClass().getResource(resourceName).toString());
    }
}
