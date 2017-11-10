package name.ulbricht.chessfx.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Game;
import name.ulbricht.chessfx.core.Move;
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
    private Label selectedSquareLabel;
    @FXML
    private Label currentPlayerValueLabel;
    @FXML
    private TreeTableView<MoveItem> legalMovesTreeTableView;

    private Game game;
    private BoardCanvas canvas;
    private Tooltip canvasTooltip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.game = new Game();
        this.game.start();

        this.canvas = new BoardCanvas(game.getBoard());
        this.boardPane.getChildren().addAll(this.canvas);
        this.canvas.widthProperty().bind(this.boardPane.widthProperty());
        this.canvas.heightProperty().bind(this.boardPane.heightProperty());

        this.canvasTooltip = new Tooltip();
        this.canvasTooltip.setShowDelay(Duration.ZERO);
        this.canvasTooltip.setOnShowing(e -> boardTooltipShowing());

        this.canvas.setFocusTraversable(true);
        this.canvas.setOnMousePressed(this::mousePressedOnBoard);
        this.canvas.setOnMouseMoved(this::mouseMovedOnBoard);
        this.canvas.selectedSquareProperty().addListener(e -> updateSelectedSquareLabel());
        this.canvas.setOnKeyPressed(this::keyPressedOnBoard);

        createDesignMenuItems();
        this.currentPlayerValueLabel.setText(this.game.getCurrentPlayer().getDisplayName());
        updateLegalMoves();
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
        this.canvas.setRenderer(design.createRenderer(this.canvas));
    }

    private void boardTooltipShowing() {
        Point2D pos = this.canvas.localToScreen(0, 0);
        this.canvasTooltip.setX(pos.getX());
        this.canvasTooltip.setY(pos.getY());
    }

    private void mousePressedOnBoard(MouseEvent e) {
        Board.Square square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) {
            this.canvas.focusSquareAt(square.getCoordinate());
            this.canvas.selectSquareAt(square.getCoordinate());
        }
    }

    private void mouseMovedOnBoard(MouseEvent e) {
        Board.Square square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) {
            this.canvasTooltip.setText(createSquareText(square));
            Tooltip.install(this.canvas, this.canvasTooltip);
        } else {
            this.canvasTooltip.setText("");
            Tooltip.uninstall(this.canvas, this.canvasTooltip);
        }
    }

    private void keyPressedOnBoard(KeyEvent e) {
        Board.Square focused = this.canvas.getFocusedSquare();
        Board.Square selected = this.canvas.getSelectedSquare();

        switch (e.getCode()) {
            case LEFT:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    if (!coordinate.isLeftColumn()) this.canvas.focusSquareAt(coordinate.moveLeft());
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a8"));
                e.consume();
                break;
            case RIGHT:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    if (!coordinate.isRightColumn()) this.canvas.focusSquareAt(coordinate.moveRight());
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a8"));
                e.consume();
                break;
            case UP:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    if (!coordinate.isTopRow()) this.canvas.focusSquareAt(coordinate.moveUp());
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a8"));
                e.consume();
                break;
            case DOWN:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    if (!coordinate.isBottomRow()) this.canvas.focusSquareAt(coordinate.moveDown());
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a8"));
                e.consume();
                break;
            case ENTER:
                if (focused != null) this.canvas.selectSquareAt(focused.getCoordinate());
                break;
            case ESCAPE:
                if (selected != null) this.canvas.clearSquareFocus();
                else if (focused != null) this.canvas.clearSquareSelection();
                e.consume();
                break;
        }
    }

    private String createSquareText(Board.Square square) {
        if (square.isEmpty())
            return String.format(Messages.getString("squareText.emptyPattern"), square.getCoordinate());
        else
            return String.format(Messages.getString("squareText.pattern"), square.getCoordinate(), square.getPiece());
    }

    private void updateSelectedSquareLabel() {
        String text = null;
        Board.Square square = this.canvas.getSelectedSquare();
        if (square != null) {
            text = createSquareText(square);
        }
        this.selectedSquareLabel.setText(text);
    }

    private void updateLegalMoves() {
        TreeItem<MoveItem> rootItem = new TreeItem<>(MoveItem.root());

        TreeItem<MoveItem> squareItem = new TreeItem<>(MoveItem.source(this.game.getBoard().getSquare(Coordinate.valueOf("a2"))));
        squareItem.setExpanded(true);
        rootItem.getChildren().add(squareItem);
        squareItem.getChildren().add(new TreeItem<>(MoveItem.move(null)));
        squareItem.getChildren().add(new TreeItem<>(MoveItem.move(null)));

        squareItem = new TreeItem<>(MoveItem.source(this.game.getBoard().getSquare(Coordinate.valueOf("b1"))));
        squareItem.setExpanded(true);
        rootItem.getChildren().add(squareItem);
        squareItem.getChildren().add(new TreeItem<>(MoveItem.move(null)));
        squareItem.getChildren().add(new TreeItem<>(MoveItem.move(null)));

        this.legalMovesTreeTableView.setRoot(rootItem);
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
        alert.setTitle(Messages.getString("about.title"));
        alert.setContentText(Messages.getString("about.contentText"));
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
