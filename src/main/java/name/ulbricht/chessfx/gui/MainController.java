package name.ulbricht.chessfx.gui;

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
import name.ulbricht.chessfx.core.Coordinate;
import name.ulbricht.chessfx.core.Game;
import name.ulbricht.chessfx.core.Move;
import name.ulbricht.chessfx.core.Piece;
import name.ulbricht.chessfx.gui.design.BoardDesign;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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

    private Game game;
    private BoardCanvas canvas;
    private Tooltip canvasTooltip;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.game = new Game();
        this.game.start();

        this.canvas = new BoardCanvas(game);
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
        updateCurrentPlayer();
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
        this.canvas.setRenderer(design.createRenderer(this.canvas.getRendererContext()));
    }

    private void boardTooltipShowing() {
        Point2D pos = this.canvas.localToScreen(0, 0);
        this.canvasTooltip.setX(pos.getX());
        this.canvasTooltip.setY(pos.getY());
    }

    private void mousePressedOnBoard(MouseEvent e) {
        if (!this.canvas.isFocused()) this.canvas.requestFocus();

        Coordinate square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) selectSquare(square);
    }

    private void mouseMovedOnBoard(MouseEvent e) {
        Coordinate square = this.canvas.getSquareAt(e.getX(), e.getY());
        if (square != null) {
            this.canvasTooltip.setText(createSquareText(square));
            Tooltip.install(this.canvas, this.canvasTooltip);
            this.canvas.focusSquareAt(square);
        } else {
            this.canvasTooltip.setText("");
            Tooltip.uninstall(this.canvas, this.canvasTooltip);
            this.canvas.clearSquareFocus();
        }
    }

    private void keyPressedOnBoard(KeyEvent e) {
        Coordinate focused = this.canvas.getFocusedSquare();
        Coordinate selected = this.canvas.getSelectedSquare();

        switch (e.getCode()) {
            case LEFT:
                if (focused != null) {
                    focused.moveLeft().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(Coordinate.COLUMNS - 1, focused.getRowIndex())));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case RIGHT:
                if (focused != null) {
                    focused.moveRight().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(0, focused.getRowIndex())));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case UP:
                if (focused != null) {
                    focused.moveUp().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(focused.getColumnIndex(), 0)));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case DOWN:
                if (focused != null) {
                    focused.moveDown().ifPresentOrElse(
                            this.canvas::focusSquareAt,
                            () -> this.canvas.focusSquareAt(Coordinate.valueOf(focused.getColumnIndex(), Coordinate.ROWS - 1)));
                } else this.canvas.focusSquareAt(Coordinate.valueOf("a1"));
                e.consume();
                break;
            case ENTER:
                if (focused != null) {
                    selectSquare(focused);
                }
                break;
            case ESCAPE:
                if (selected != null) this.canvas.clearSquareSelection();
                else if (focused != null) this.canvas.clearSquareFocus();
                e.consume();
                break;
        }
    }

    private String createSquareText(Coordinate coordinate) {
        Optional<Piece> piece = this.game.getBoard().getPiece(coordinate);
        if (!piece.isPresent())
            return String.format(Messages.getString("squareText.emptyPattern"), coordinate);
        else
            return String.format(Messages.getString("squareText.pattern"), coordinate, piece.get());
    }

    private void updateSelectedSquareLabel() {
        String text = null;
        Coordinate coordinate = this.canvas.getSelectedSquare();
        if (coordinate != null) {
            text = createSquareText(coordinate);
        }
        this.selectedSquareValueLabel.setText(text);
    }

    private void updateCurrentPlayer() {
        this.currentPlayerValueLabel.setText(this.game.getCurrentPlayer().getDisplayName());
    }

    private void selectSquare(Coordinate coordinate) {
        // check if we can execute a move
        Optional<Move> move = this.canvas.getDisplayedMoves().stream()
                .filter(m -> coordinate.equals(m.getTo()) || (m.getCaptures().isPresent() && coordinate.equals(m.getCaptures().get())))
                .findFirst();

        if (move.isPresent()) {
            performMove(move.get());
        } else {
            this.canvas.selectSquareAt(coordinate);
        }
    }

    @FXML
    private void newGame() {
        if (GUIUtils.showQuestionAlert(this.root, "alert.newGame.title", "alert.newGame.contentText")
                .orElse(ButtonType.CANCEL) == ButtonType.YES) {
            this.game.start();

            updateCurrentPlayer();
            this.canvas.clearSquareFocus();
            this.canvas.clearSquareSelection();
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

    @FXML
    private void performMove(Move move) {
        this.game.performMove(move);

        updateCurrentPlayer();
        this.canvas.clearSquareSelection();
        this.canvas.focusSquareAt(move.getTo());
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
