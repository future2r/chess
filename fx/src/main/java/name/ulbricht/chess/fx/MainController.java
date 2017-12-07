package name.ulbricht.chess.fx;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import name.ulbricht.chess.fx.design.BoardDesign;
import name.ulbricht.chess.game.*;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

public final class MainController implements Initializable {

    public static void loadAndShow(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("main.fxml"), ResourceBundle.getBundle(Messages.BUNDLE_NAME));

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle(Messages.getString("main.title"));
        stage.getIcons().addAll(GUIUtils.loadImages("main.icons"));
        stage.show();
    }

    @FXML
    private BorderPane root;
    @FXML
    private MenuItem undoMenuItem;
    @FXML
    private MenuItem redoMenuItem;
    @FXML
    private Menu designMenu;
    @FXML
    private Pane boardPane;
    @FXML
    private Label selectedSquareValueLabel;
    @FXML
    private Label currentPlayerValueLabel;
    @FXML
    private Label checkLabel;

    private BoardCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.canvas = new BoardCanvas();
        this.boardPane.getChildren().addAll(this.canvas);
        this.canvas.widthProperty().bind(this.boardPane.widthProperty());
        this.canvas.heightProperty().bind(this.boardPane.heightProperty());

        this.canvas.selectedSquareProperty().addListener((observable, oldValue, newValue) -> updateSelectedSquareLabel(newValue));
        this.canvas.activePlayerProperty().addListener((observable, oldValue, newValue) -> updateCurrentPlayer(newValue));
        this.canvas.checkStateProperty().addListener((observable, oldValue, newValue) -> updateCheckState(newValue));

        this.undoMenuItem.disableProperty().bind(Bindings.not(this.canvas.undoAvailableProperty()));
        this.redoMenuItem.disableProperty().bind(Bindings.not(this.canvas.redoAvailableProperty()));

        createDesignMenuItems();
        updateCurrentPlayer(this.canvas.activePlayerProperty().get());

        Platform.runLater(() -> this.canvas.requestFocus());
    }

    private void createDesignMenuItems() {
        RadioMenuItem firstMenuItem = null;
        ToggleGroup toggleGroup = new ToggleGroup();
        for (BoardDesign design : BoardDesign.getDesigns()) {
            RadioMenuItem menuItem = new RadioMenuItem(design.getDisplayName());
            menuItem.setUserData(design);
            menuItem.setToggleGroup(toggleGroup);
            menuItem.setOnAction(this::changeDesign);
            this.designMenu.getItems().add(menuItem);

            if (firstMenuItem == null) firstMenuItem = menuItem;
        }
        if (firstMenuItem != null) {
            firstMenuItem.setSelected(true);
            firstMenuItem.fire();
        }
    }

    private void changeDesign(ActionEvent e) {
        RadioMenuItem menuItem = (RadioMenuItem) e.getSource();
        BoardDesign design = (BoardDesign) menuItem.getUserData();
        this.canvas.setDesign(design);
    }

    private void updateSelectedSquareLabel(Coordinate selected) {
        if (selected != null) {
            Piece piece = this.canvas.getGame().getPiece(selected);
            if (piece != null)
                this.selectedSquareValueLabel.setText(selected.toString() + ' ' + piece.getDisplayName());
            else this.selectedSquareValueLabel.setText(selected.toString());
        } else
            this.selectedSquareValueLabel.setText(Messages.getString("main.selectedSquareValueLabel.text.none"));
    }

    private void updateCurrentPlayer(Player player) {
        this.currentPlayerValueLabel.setText(player.getDisplayName());
    }

    private void updateCheckState(CheckState checkState) {
        if (checkState != CheckState.NONE) {
            this.checkLabel.setText(checkState.getDisplayName());
        } else {
            this.checkLabel.setText(null);
        }
    }

    @FXML
    private void newGame() {
        if (GUIUtils.showQuestion(this.root, Messages.getString("alert.newGame.title"),
                Messages.getString("alert.newGame.contentText"))
                .orElse(ButtonType.CANCEL) == ButtonType.YES) {
            this.canvas.setGame(new Game());
        }
    }

    @FXML
    private void openBoard() {
        if (GUIUtils.showQuestion(this.root, Messages.getString("alert.openBoard.title"),
                Messages.getString("alert.openBoard.question.contentText"))
                .orElse(ButtonType.CANCEL) == ButtonType.YES) {
            Path file = FileChoosers.openFile(this.root, FileChoosers.Category.BOARDS, FileChoosers.Format.FEN);
            if (file != null) {
                try {
                    Board setup = FEN.fromFile(file);
                    Game game = new Game(setup);
                    this.canvas.setGame(game);
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                    GUIUtils.showError(this.root, Messages.getString("alert.openBoard.error.contentText") + "\n" + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void saveBoard() {
        Board board = this.canvas.getGame().getBoard();

        Path file = FileChoosers.saveFile(this.root, FileChoosers.Category.BOARDS, FileChoosers.Format.FEN);
        if (file != null) {
            try {
                FEN.toFile(file, board);
            } catch (IOException e) {
                GUIUtils.showError(this.root, Messages.getString("alert.saveBoard.error.contentText") + "\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void exitApplication() {
        getStage().close();
    }

    @FXML
    private void undo() {
        this.canvas.undo();
    }

    @FXML
    private void redo() {
        this.canvas.redo();
    }

    @FXML
    private void showAbout() {
        GUIUtils.showInfo(this.root, Messages.getString("alert.about.contentText"));
    }

    private Scene getScene() {
        return this.root.getScene();
    }

    private Window getWindow() {
        return getScene().getWindow();
    }

    private Stage getStage() {
        return (Stage) getWindow();
    }
}
