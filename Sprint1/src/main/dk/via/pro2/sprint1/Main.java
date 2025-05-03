package main.dk.via.pro2.sprint1;

import javafx.application.Application;
import javafx.stage.Stage;
import main.dk.via.pro2.sprint1.model.PropertyListModel;
import main.dk.via.pro2.sprint1.model.PropertyListModelManager;
import main.dk.via.pro2.sprint1.viewmodel.ViewHandler;

public class Main extends Application {
    @Override public void start(Stage primaryStage) throws Exception
    {
        PropertyListModel propertyListModel = new PropertyListModelManager();
        ViewHandler vh = new ViewHandler(propertyListModel);
        vh.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}