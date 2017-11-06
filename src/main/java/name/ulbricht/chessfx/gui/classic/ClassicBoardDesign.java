package name.ulbricht.chessfx.gui.classic;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Figure;
import name.ulbricht.chessfx.core.Player;
import name.ulbricht.chessfx.gui.BoardDesign;

import java.util.HashMap;
import java.util.Map;

public final class ClassicBoardDesign implements BoardDesign {

    private final Map<Board.Square.Color, Image> squareImages = new HashMap<>();
    private final Map<Player, Map<Figure.Type, Image>> figureImages = new HashMap<>();

    public ClassicBoardDesign() {
        for (Board.Square.Color color : Board.Square.Color.values())
            this.squareImages.put(color, loadSquareImage(color));

        for (Player player : Player.values())
            this.figureImages.put(player, loadFigureImages(player));
    }

    @Override
    public double getPrefSquareSize() {
        return 100;
    }

    @Override
    public double getPrefKeySize() {
        return 30;
    }

    @Override
    public void drawBackground(GraphicsContext gc, double width, double height) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, width, height);
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Board.Square square) {
        Image squareImage = this.squareImages.get(square.getColor());
        gc.drawImage(squareImage, 0, 0, size, size);

        if (!square.isEmpty()) {
            Figure figure = square.getFigure();
            Image image = this.figureImages.get(figure.getPlayer()).get(figure.getType());

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }
    }

    private Image loadSquareImage(Board.Square.Color color) {
        String resourceName = "square-" + color.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }

    private Map<Figure.Type, Image> loadFigureImages(Player player) {
        Map<Figure.Type, Image> images = new HashMap<>();
        for (Figure.Type type : Figure.Type.values()) {
            String resourceName = player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png";
            Image image = new Image(this.getClass().getResource(resourceName).toString());
            images.put(type, image);
        }
        return images;
    }
}
