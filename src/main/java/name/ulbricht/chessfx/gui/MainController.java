package name.ulbricht.chessfx.gui;

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
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Player;
import name.ulbricht.chessfx.gui.design.BoardDesign;

import java.io.IOException;
import java.net.URL;
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
            this.canvas.getGame().getBoard().getPiece(selected).ifPresentOrElse(
                    p -> this.selectedSquareValueLabel.setText(selected.toString() + ' ' + p.getType().getDisplayName() + ' ' + p.getPlayer().getDisplayName()),
                    () -> this.selectedSquareValueLabel.setText(selected.toString()));
        } else
            this.selectedSquareValueLabel.setText("");
    }

    private void updateCurrentPlayer(Player player) {
        this.currentPlayerValueLabel.setText(player.getDisplayName());
    }

    @FXML
    private void newGame() {
        if (GUIUtils.showQuestionAlert(this.root, "alert.newGame.title", "alert.newGame.contentText")
                .orElse(ButtonType.CANCEL) == ButtonType.YES) {
            this.canvas.newGame();
        }
    }

    @FXML
    private void exitApplication() {
        getStage().close();
    }

    @FXML
    private void showAbout() {
        GUIUtils.showInfoAlert(this.root, "alert.about.title", "alert.about.contentText");
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
