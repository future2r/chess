package name.ulbricht.chessfx.gui.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Piece;
import name.ulbricht.chessfx.core.Player;

import java.util.HashMap;
import java.util.Map;

final class ClassicBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "classic";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);
    private static final Color COLOR_MOVE_TARGET = Color.rgb(0, 255, 0, 1.0);
    private static final Color COLOR_CAPTURE = Color.rgb(255, 0, 0, 1.0);

    private final Map<Coordinate.Color, Image> squareImages = new HashMap<>();
    private final Map<Player, Map<Piece.Type, Image>> pieceImages = new HashMap<>();

    public ClassicBoardRenderer() {
        for (Player player : Player.values())
            this.pieceImages.put(player, new HashMap<>());
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

        // focused & selected
        if (square.equals(getContext().getSelectedSquare())) {
            drawHighlight(gc,size,COLOR_SELECTED);
        } else if (square.equals(getContext().getFocusedSquare())) {
            drawHighlight(gc,size,COLOR_FOCUSED);
        }

        if (!square.isEmpty()) {
            Piece piece = square.getPiece();
            Image image = this.pieceImages.get(piece.getPlayer()).computeIfAbsent(piece.getType(), t -> loadPieceImage(t, piece.getPlayer()));

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

    private Image loadPieceImage(Piece.Type type, Player player) {
        String resourceName = "classic-" + player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png";
        return new Image(this.getClass().getResource(resourceName).toString());
    }
}
