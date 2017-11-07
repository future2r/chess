package name.ulbricht.chessfx.gui.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Figure;
import name.ulbricht.chessfx.core.Player;

import java.util.HashMap;
import java.util.Map;

final class ClassicBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "classic";

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
    public void drawSquare(GraphicsContext gc, double size, Board.Square square) {
        Image squareImage = this.squareImages.computeIfAbsent(square.getCoordinate().getColor(), this::loadSquareImage);
        gc.drawImage(squareImage, 0, 0, size, size);

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

    private Image loadSquareImage(Coordinate.Color color) {
        String resourceName = "classic-square-" + color.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }

    private Image loadFigureImage(Figure.Type type, Player player) {
        String resourceName = "classic-" + player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }
}
