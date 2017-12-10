package name.ulbricht.chess.fx;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import name.ulbricht.chess.fx.design.BoardRenderer;
import name.ulbricht.chess.game.Board;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class NewGameController implements Initializable {

    public static void showDialog(Node owner, Board board, BoardRenderer renderer) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PromotionController.class.getResource("new-game.fxml"));
        loader.setResources(ResourceBundle.getBundle(Messages.BUNDLE_NAME));
        Parent pane;
        try {
            pane = loader.load();
        } catch (IOException e) {
            throw new InternalError(e);
        }

        NewGameController controller = loader.getController();
        controller.initBoard(board);
        controller.initRenderer(renderer);

        Dialog<ButtonType> dlg = GUIUtils.okCancelDialog()
                .owner(owner)
                .title(Messages.getString("new-game.title"))
                .headerText(Messages.getString("new-game.headerText"))
                .content(pane).build();
        Optional<ButtonType> result = dlg.showAndWait();
    }

    @FXML
    private ComboBox whitePlayerComboBox;
    @FXML
    private ComboBox blackPlayerComboBox;

    @FXML
    private Pane canvasPane;
    @FXML
    private RadioButton whiteStartPlayerRadioButton;
    @FXML
    private RadioButton blackStartPlayerRadioButton;
    @FXML
    private ComboBox enPassantTargetComboBox;
    @FXML
    private CheckBox whiteKingSideCastlingCheckBox;
    @FXML
    private CheckBox whiteQueenSideCastlingCheckBox;
    @FXML
    private CheckBox blackKingSideCastlingCheckBox;
    @FXML
    private CheckBox blackQueenKingSideCastlingCheckBox;

    private BoardCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.whitePlayerComboBox.getSelectionModel().select(0);
        this.blackPlayerComboBox.getSelectionModel().select(0);

        this.canvas = new BoardCanvas();
        this.canvasPane.getChildren().addAll(this.canvas);
        this.canvas.widthProperty().bind(this.canvasPane.widthProperty());
        this.canvas.heightProperty().bind(this.canvasPane.heightProperty());

        Platform.runLater(() -> this.whitePlayerComboBox.requestFocus());
    }

    private void initBoard(Board board) {
        this.canvas.setBoard(board);
    }

    private void initRenderer(BoardRenderer renderer) {
        this.canvas.setRenderer(renderer);
    }
}
