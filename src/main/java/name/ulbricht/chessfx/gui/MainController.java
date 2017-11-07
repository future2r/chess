package name.ulbricht.chessfx.gui;

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
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.gui.design.BoardDesign;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public final class MainController implements Initializable {

    public static void loadAndShow(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("main.fxml"), ResourceBundle.getBundle(Messages.BUNDLE_NAME));

        Scene scene = new Scene(root, 800, 600);
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

    private BoardCanvas boardCanvas;

    private Board board;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.board = new Board();
        this.board.setup();

        this.boardCanvas = new BoardCanvas(board);
        this.boardPane.getChildren().addAll(this.boardCanvas);
        this.boardCanvas.widthProperty().bind(this.boardPane.widthProperty());
        this.boardCanvas.heightProperty().bind(this.boardPane.heightProperty());

        createDesignMenuItems();
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
        firstMenuItem.setSelected(true);
        firstMenuItem.fire();
    }

    private void changeDesign(ActionEvent e) {
        RadioMenuItem menuItem = (RadioMenuItem) e.getSource();
        BoardDesign design = (BoardDesign) menuItem.getUserData();
        this.boardCanvas.setRenderer(design.createRenderer());
    }

    @FXML
    private void exitApplication() {
        Stage stage = (Stage) getWindow();
        stage.close();
    }

    @FXML
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.getButtonTypes().add(ButtonType.OK);
        alert.initOwner(getWindow());
        alert.setTitle(Messages.getString("aboutAlert.title"));
        alert.setContentText(Messages.getString("aboutAlert.contentText"));
        alert.showAndWait();
    }

    private Window getWindow(){
        return this.root.getScene().getWindow();
    }
}
