package name.ulbricht.chess.fx;

import javafx.scene.canvas.Canvas;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import name.ulbricht.chess.fx.design.BoardRenderer;
import name.ulbricht.chess.game.Coordinate;
import name.ulbricht.chess.game.Piece;
import name.ulbricht.chess.game.PieceType;
import name.ulbricht.chess.game.Player;

import java.util.HashMap;
import java.util.Map;

final class PieceTypeCellFactory implements Callback<ListView<PieceType>, ListCell<PieceType>> {

    private final BoardRenderer renderer;
    private final Player player;
    private final Coordinate coordinate;
    private final Map<PieceType, Canvas> images = new HashMap<>();

    PieceTypeCellFactory(BoardRenderer renderer, Player player, Coordinate coordinate) {
        this.renderer = renderer;
        this.player = player;
        this.coordinate = coordinate;
    }

    private Canvas createPieceImage(PieceType pieceType) {
        Piece piece = Piece.valueOf(pieceType, this.player);
        Canvas canvas = new Canvas();
        canvas.setWidth(100);
        canvas.setHeight(100);
        this.renderer.drawSquare(canvas.getGraphicsContext2D(), 100, this.coordinate, piece,
                false, false, false, null);
        return canvas;
    }

    @Override
    public ListCell<PieceType> call(ListView<PieceType> param) {
        return new ListCell<>() {

            public void updateItem(PieceType pieceType, boolean empty) {
                super.updateItem(pieceType, empty);
                setText(null);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(images.computeIfAbsent(pieceType, p -> createPieceImage(pieceType)));
                }

            }
        };
    }
}
