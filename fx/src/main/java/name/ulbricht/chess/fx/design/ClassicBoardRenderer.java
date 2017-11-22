package name.ulbricht.chess.fx.design;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Piece;
import name.ulbricht.chess.game.PieceType;
import name.ulbricht.chess.game.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

final class ClassicBoardRenderer extends AbstractBoardRenderer {

    static final String ID = "classic";

    private static final Color COLOR_FOCUSED = Color.rgb(255, 255, 0, 0.5);
    private static final Color COLOR_SELECTED = Color.rgb(255, 255, 0, 1.0);
    private static final Color COLOR_TO = Color.rgb(0, 255, 0, 0.7);
    private static final Color COLOR_CAPTURED = Color.rgb(255, 0, 0, 0.7);

    private final Map<Player, Map<PieceType, Image>> pieceImages = new HashMap<>();

    public ClassicBoardRenderer() {
        for (Player player : Player.values()) {
            Map<PieceType, Image> images = new HashMap<>();
            this.pieceImages.put(player, images);
            for (PieceType type : PieceType.values()) {
                images.put(type, loadImage("classic-" + player.name().toLowerCase() + "-" + type.name().toLowerCase() + ".png"));
            }
        }
    }

    @Override
    public void drawSquare(GraphicsContext gc, double size, Coordinate coordinate) {
        // background
        Color backgroundColor;
        if (((coordinate.getColumnIndex() + coordinate.getRowIndex()) % 2) == 0)
            backgroundColor = Color.rgb(0xFF, 0xCC, 0x99);
        else backgroundColor = Color.rgb(0xCC, 0x99, 0x66);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, size + 1, size + 1);

        // focused & selected
        if (coordinate.equals(getContext().getSelectedSquare())) {
            gc.setFill(COLOR_SELECTED);
            gc.fillRect(0, 0, size + 1, size + 1);
        } else if (getContext().isBoardFocused() && coordinate.equals(getContext().getFocusedSquare())) {
            gc.setFill(COLOR_FOCUSED);
            gc.fillRect(0, 0, size + 1, size + 1);
        }

        Piece piece = getContext().getBoard().getPiece(coordinate);
        if (piece!=null) {
            Image image = this.pieceImages.get(piece.getPlayer()).get(piece.getType());

            double imageWidth = image.getWidth();
            double imageHeight = image.getHeight();
            double scaledImageHeight = 0.85 * size; // 85% of the square size
            double imageScale = scaledImageHeight / imageHeight;
            double scaledImageWidth = imageScale * imageWidth;

            gc.drawImage(image, (size - scaledImageWidth) / 2, (size - scaledImageHeight) / 2, scaledImageWidth, scaledImageHeight);
        }

        // captured or move target
        if (getContext().isDisplayedCapturedSquare(coordinate)) {
            gc.setFill(COLOR_CAPTURED);
            double circleInset = 0.25 * size;
            double circleSize = size - (2 * circleInset);
            gc.fillOval(circleInset, circleInset, circleSize, circleSize);
        } else if (getContext().isDisplayedToSquare(coordinate)) {
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
