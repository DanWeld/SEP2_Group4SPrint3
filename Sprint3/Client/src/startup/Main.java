package startup;

import javafx.application.Application;
import javafx.stage.Stage;
import networking.auth.Authentication;
import startup.viewHandler.ViewHandler;

public class Main extends Application {
    @Override public void start(Stage primaryStage) throws Exception
    {
        // Initialize the client and authentication service
        ClientFactory clientFactory = ClientFactory.getInstance();
        Authentication authService = clientFactory.getAuthentication();
        
        // Create the view model factory
        ViewModelFactory viewModelFactory = new ViewModelFactory(authService);
        
        // Create and start the view handler
        ViewHandler vh = new ViewHandler(viewModelFactory);
        vh.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}