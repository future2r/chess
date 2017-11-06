package name.ulbricht.chessfx;

import javafx.application.Application;
import javafx.stage.Stage;
import name.ulbricht.chessfx.gui.MainController;

public class ChessFX extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainController.loadAndShow(primaryStage);
    }
}
