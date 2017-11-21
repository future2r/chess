package name.ulbricht.chessfx.gui.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Piece;
import name.ulbricht.chessfx.core.PieceType;
import name.ulbricht.chessfx.core.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
    public void drawSquare(GraphicsContext gc, double size, Coordinate coordinate) {
        Image squareImage = ((coordinate.getColumnIndex() + coordinate.getRowIndex()) % 2) == 0 ?
                this.lightSquareImage : this.darkSquareImage;
        gc.drawImage(squareImage, 0, 0, size, size);

        // focused & selected
        if (coordinate.equals(getContext().getSelectedSquare())) {
            drawOuterHighlight(gc, size, COLOR_SELECTED);
        } else if (getContext().isBoardFocused() && coordinate.equals(getContext().getFocusedSquare())) {
            drawOuterHighlight(gc, size, COLOR_FOCUSED);
        }

        Optional<Piece> piece = getContext().getBoard().getPiece(coordinate);
        if (piece.isPresent()) {
            Image image = this.pieceImages.get(piece.get().getPlayer()).get(piece.get().getType());

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }

        // captured or move target
        if (getContext().isDisplayedCapturedSquare(coordinate)) drawInnerHighlight(gc, size, COLOR_CAPTURED);
        else if (getContext().isDisplayedToSquare(coordinate)) drawInnerHighlight(gc, size, COLOR_TO);
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
