package Client.src.startup;

import javafx.application.Application;
import javafx.stage.Stage;
import ui.viewHandler.ViewHandler;

public class Main extends Application {
    @Override public void start(Stage primaryStage) throws Exception
    {
        ViewHandler vh = new ViewHandler();
        vh.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}