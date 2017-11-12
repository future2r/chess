package name.ulbricht.chessfx.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
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
import name.ulbricht.chessfx.core.*;
import name.ulbricht.chessfx.gui.design.BoardDesign;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
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
    @FXML
    private Button performMoveButton;

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

        this.legalMovesTreeTableView.getSelectionModel().selectedItemProperty().addListener(a -> moveSelected());

        ReadOnlyObjectProperty<TreeItem<MoveItem>> selectedMoveProperty = this.legalMovesTreeTableView.getSelectionModel().selectedItemProperty();
        performMoveButton.disableProperty().bind(
                Bindings.isNull(selectedMoveProperty)
                        .or(Bindings.createBooleanBinding(() -> (selectedMoveProperty.get() != null)
                                && (selectedMoveProperty.get().getValue().getType() != MoveItem.Type.MOVE), selectedMoveProperty)));

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
        if (!this.canvas.isFocused()) this.canvas.requestFocus();

        Square square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) {
            this.canvas.focusSquareAt(square.getCoordinate());
            this.canvas.selectSquareAt(square.getCoordinate());
        }
    }

    private void mouseMovedOnBoard(MouseEvent e) {
        Square square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) {
            this.canvasTooltip.setText(createSquareText(square));
            Tooltip.install(this.canvas, this.canvasTooltip);
        } else {
            this.canvasTooltip.setText("");
            Tooltip.uninstall(this.canvas, this.canvasTooltip);
        }
    }

    private void keyPressedOnBoard(KeyEvent e) {
        Square focused = this.canvas.getFocusedSquare();
        Square selected = this.canvas.getSelectedSquare();

        switch (e.getCode()) {
            case LEFT:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    coordinate.moveLeft().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(Coordinate.COLUMNS - 1, coordinate.getRowIndex())));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case RIGHT:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    coordinate.moveRight().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(0, coordinate.getRowIndex())));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case UP:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    coordinate.moveUp().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(coordinate.getColumnIndex(), 0)));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case DOWN:
                if (focused != null) {
                    Coordinate coordinate = focused.getCoordinate();
                    coordinate.moveDown().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(coordinate.getColumnIndex(), Coordinate.ROWS - 1)));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
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

    private String createSquareText(Square square) {
        if (square.isEmpty())
            return String.format(Messages.getString("squareText.emptyPattern"), square.getCoordinate());
        else
            return String.format(Messages.getString("squareText.pattern"), square.getCoordinate(), square.getPiece());
    }

    private void updateSelectedSquareLabel() {
        String text = null;
        Square square = this.canvas.getSelectedSquare();
        if (square != null) {
            text = createSquareText(square);
        }
        this.selectedSquareLabel.setText(text);
    }

    private void updateLegalMoves() {
        TreeItem<MoveItem> rootItem = new TreeItem<>(MoveItem.root());

        for (Map.Entry<Square, List<Move>> entry : this.game.getLegalMoves().entrySet()) {

            Square from = entry.getKey();
            TreeItem<MoveItem> sourceItem = new TreeItem<>(MoveItem.source(from));
            rootItem.getChildren().add(sourceItem);

            List<Move> moves = entry.getValue();
            for (Move move : moves) {
                TreeItem<MoveItem> moveItem = new TreeItem<>(MoveItem.move(move));
                sourceItem.getChildren().add(moveItem);
            }
        }

        this.legalMovesTreeTableView.setRoot(rootItem);
    }

    private void moveSelected() {

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

    @FXML
    private void performMove() {

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
