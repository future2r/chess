package name.ulbricht.chessfx.gui.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Figure;
import name.ulbricht.chessfx.core.Player;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

final class ClassicBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "classic";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);

    private final Map<Coordinate.Color, Image> squareImages = new HashMap<>();
    private final Map<Player, Map<Figure.Type, Image>> figureImages = new HashMap<>();

    public ClassicBoardRenderer() {
        for (Player player : Player.values())
            this.figureImages.put(player, new HashMap<>());
    }

    @Override
    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Board.Square square, boolean focused, boolean selected) {
        Image squareImage = this.squareImages.computeIfAbsent(square.getCoordinate().getColor(), this::loadSquareImage);
        gc.drawImage(squareImage, 0, 0, size, size);

        // focused & selected
        if (selected) {
            drawHighlight(gc,size,COLOR_SELECTED);
        } else if (focused) {
            drawHighlight(gc,size,COLOR_FOCUSED);
        }

        if (!square.isEmpty()) {
            Figure figure = square.getFigure();
            Image image = this.figureImages.get(figure.getPlayer()).computeIfAbsent(figure.getType(), t -> loadFigureImage(t, figure.getPlayer()));

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }
    }

    private void drawHighlight(GraphicsContext gc, double size, Color color) {
        Stop[] stops = new Stop[]{
                new Stop(0.0, Color.TRANSPARENT),
                new Stop(0.6, Color.TRANSPARENT),
                new Stop(0.8, color),
                new Stop(1.0, Color.TRANSPARENT)
        };
        RadialGradient radialGradient =
                new RadialGradient(0, 0, size / 2, size / 2,
                        size / 2, false, CycleMethod.NO_CYCLE, stops);
        gc.setFill(radialGradient);
        gc.fillOval(0, 0, size, size);
    }

    private Image loadSquareImage(Coordinate.Color color) {
        String resourceName = "classic-square-" + color.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }

    private Image loadFigureImage(Figure.Type type, Player player) {
        String resourceName = "classic-" + player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }
}
