package name.ulbricht.chessfx.gui;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
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
    @FXML
    private Label selectedSquareLabel;

    private Board board;
    private BoardCanvas canvas;
    private Tooltip canvasTooltip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.board = new Board();
        this.board.setup();

        this.canvas = new BoardCanvas(board);
        this.boardPane.getChildren().addAll(this.canvas);
        this.canvas.widthProperty().bind(this.boardPane.widthProperty());
        this.canvas.heightProperty().bind(this.boardPane.heightProperty());

        this.canvasTooltip = new Tooltip();
        this.canvasTooltip.setShowDelay(Duration.ZERO);
        this.canvasTooltip.setOnShowing(this::boardTooltipShowing);

        this.canvas.setOnMousePressed(this::mousePressedOnBoard);
        this.canvas.setOnMouseMoved(this::mouseMovedOnBoard);
        this.canvas.getSelectedSquareProperty().addListener(this::selectedSquareChanged);

        Platform.runLater(() -> getScene().setOnKeyPressed(this::keyPressedOnBoard));

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
        this.canvas.setRenderer(design.createRenderer());
    }

    private void boardTooltipShowing(WindowEvent windowEvent) {
        Point2D pos = this.canvas.localToScreen(0, 0);
        this.canvasTooltip.setX(pos.getX());
        this.canvasTooltip.setY(pos.getY());
    }

    private void mousePressedOnBoard(MouseEvent e) {
        this.canvas.setSelectedSquare(this.canvas.getCoordinateAt(e.getX(), e.getY()));
    }

    private void mouseMovedOnBoard(MouseEvent e) {
        Coordinate coordinate = this.canvas.getCoordinateAt(e.getX(), e.getY());
        if (coordinate != null) {
            this.canvasTooltip.setText(createSquareText(coordinate));
            Tooltip.install(this.canvas, this.canvasTooltip);
        } else {
            this.canvasTooltip.setText("");
            Tooltip.uninstall(this.canvas, this.canvasTooltip);
        }
    }

    private void keyPressedOnBoard(KeyEvent e) {
        KeyCode code = e.getCode();
        if (code == KeyCode.LEFT || code == KeyCode.RIGHT || code == KeyCode.UP || code == KeyCode.DOWN) {
            Coordinate coordinate = this.canvas.getSelectedSquare();
            if (coordinate != null) {
                switch (e.getCode()) {
                    case LEFT:
                        if (!coordinate.isLeftColumn()) this.canvas.setSelectedSquare(coordinate.moveLeft());
                        break;
                    case RIGHT:
                        if (!coordinate.isRightColumn()) this.canvas.setSelectedSquare(coordinate.moveRight());
                        break;
                    case UP:
                        if (!coordinate.isTopRow()) this.canvas.setSelectedSquare(coordinate.moveUp());
                        break;
                    case DOWN:
                        if (!coordinate.isBottomRow()) this.canvas.setSelectedSquare(coordinate.moveDown());
                        break;
                }
            } else {
                this.canvas.setSelectedSquare(Coordinate.valueOf("a8"));
            }
        }

    }

    private String createSquareText(Coordinate coordinate) {
        Board.Square square = this.board.getSquare(coordinate);
        if (square.isEmpty())
            return String.format(Messages.getString("squareText.emptyPattern"), coordinate);
        else
            return String.format(Messages.getString("squareText.pattern"), coordinate, square.getFigure());
    }

    private void selectedSquareChanged(Observable observable) {
        String text = null;
        Coordinate coordinate = this.canvas.getSelectedSquare();
        if (coordinate != null) {
            text = createSquareText(coordinate);
        }
        this.selectedSquareLabel.setText(text);
    }

    @FXML
    private void exitApplication() {
        getStage().close();
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
