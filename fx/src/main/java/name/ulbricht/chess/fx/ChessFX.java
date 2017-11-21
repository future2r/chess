package name.ulbricht.chess.fx;

import javafx.application.Application;
import javafx.stage.Stage;

public class ChessFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController.loadAndShow(primaryStage);
    }
}
