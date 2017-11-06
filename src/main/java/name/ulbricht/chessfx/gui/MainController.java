package name.ulbricht.chessfx.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import name.ulbricht.chessfx.core.Board;
import name.ulbricht.chessfx.gui.classic.ClassicBoardDesign;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    public static void loadAndShow(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(MainController.class.getResource("main.fxml"));

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setTitle(Messages.getString("main.title"));
        stage.getIcons().addAll(GUIUtils.loadImages("main.icons"));
        stage.show();
    }

    @FXML
    private BorderPane root;
    @FXML
    private Pane boardPane;

    private Canvas boardCanvas;

    private Board board;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.board = new Board();
        this.board.setup();

        this.boardCanvas = new BoardCanvas(board, new ClassicBoardDesign());
        this.boardPane.getChildren().addAll(this.boardCanvas);
        this.boardCanvas.widthProperty().bind(this.boardPane.widthProperty());
        this.boardCanvas.heightProperty().bind(this.boardPane.heightProperty());

    }
}
