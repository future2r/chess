package name.ulbricht.chess.fx;

import javafx.application.Platform;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
    private Menu designMenu;
    @FXML
    private Pane boardPane;
    @FXML
    private Label selectedSquareValueLabel;
    @FXML
    private Label currentPlayerValueLabel;

    private BoardCanvas canvas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.canvas = new BoardCanvas();
        this.boardPane.getChildren().addAll(this.canvas);
        this.canvas.widthProperty().bind(this.boardPane.widthProperty());
        this.canvas.heightProperty().bind(this.boardPane.heightProperty());

        this.canvas.selectedSquareProperty().addListener((observable, oldValue, newValue) -> updateSelectedSquareLabel(newValue));
        this.canvas.currentPlayerProperty().addListener((observable, oldValue, newValue) -> updateCurrentPlayer(newValue));

        createDesignMenuItems();
        updateCurrentPlayer(this.canvas.currentPlayerProperty().get());

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
                this.selectedSquareValueLabel.setText(selected.toString() + ' ' + piece.type.getDisplayName() + ' ' + piece.type.getDisplayName());
            else this.selectedSquareValueLabel.setText(selected.toString());
        } else
            this.selectedSquareValueLabel.setText("");
    }

    private void updateCurrentPlayer(Player player) {
        this.currentPlayerValueLabel.setText(player.getDisplayName());
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
            Path file = FileChoosers.openFile(this.root, FileChoosers.Category.BOARDS);
            if (file != null) {
                try {
                    List<String> lines = Files.readAllLines(file);
                    if (lines.isEmpty()) throw new IOException();

                    FENPositions fen = FENPositions.of(lines.get(0));
                    Game game = new Game();
                    game.setup(fen);
                    this.canvas.setGame(game);
                } catch (IOException | IllegalArgumentException ex) {
                    GUIUtils.showError(this.root, Messages.getString("alert.openBoard.error.contentText") + "\n" + ex.getMessage());
                }
            }
        }
    }

    @FXML
    private void saveBoard() {

    }

    @FXML
    private void exitApplication() {
        getStage().close();
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
