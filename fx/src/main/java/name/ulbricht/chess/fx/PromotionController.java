package name.ulbricht.chess.fx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import name.ulbricht.chess.fx.design.BoardRenderer;
import name.ulbricht.chess.game.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public final class PromotionController implements Initializable {

    public static PieceType showDialog(Node owner, BoardRenderer renderer, Ply ply) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PromotionController.class.getResource("promotion.fxml"));
        loader.setResources(ResourceBundle.getBundle(Messages.BUNDLE_NAME));
        Parent pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new InternalError(e);
        }
        PromotionController controller = loader.getController();

        controller.initRenderer(renderer, ply.piece.player, ply.target);

        Dialog<ButtonType> dlg = GUIUtils.okCancelDialog()
                .owner(owner)
                .title(Messages.getString("promotion.title"))
                .headerText(Messages.getString("promotion.headerText"))
                .content(pane).build();
        Optional<ButtonType> result = dlg.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) return controller.getSelectedPieceType();
        return null;
    }

    @FXML
    private ListView<PieceType> pieceListView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pieceListView.getItems().addAll(Rules.promotionPieceTypes);
        this.pieceListView.getSelectionModel().select(0);
        Platform.runLater(() -> this.pieceListView.requestFocus());
    }

    private void initRenderer(BoardRenderer renderer, Player player, Coordinate coordinate) {
        this.pieceListView.setCellFactory(new PieceTypeCellFactory(renderer, player, coordinate));
    }

    private PieceType getSelectedPieceType() {
        return this.pieceListView.getSelectionModel().getSelectedItem();
    }
}
