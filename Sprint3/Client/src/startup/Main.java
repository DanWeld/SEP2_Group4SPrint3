package startup;

import javafx.application.Application;
import javafx.stage.Stage;
import networking.authClient.Authentication;
import startup.viewHandler.ViewHandler;

/**
 * Main class for the client application.
 * Initializes the client and starts the view handler.
 */
public class Main extends Application
{
  /**
   * Starts the JavaFX application.
   * Initializes the client and view handler.
   *
   * @param primaryStage The primary stage for this application
   * @throws Exception If an error occurs during startup
   */
  @Override public void start(Stage primaryStage) throws Exception
  {
    ClientFactory.getInstance();
    // Create the view model factory
    ViewModelFactory viewModelFactory = new ViewModelFactory();

    // Create and start the view handler
    ViewHandler vh = new ViewHandler(viewModelFactory);
    vh.start();
  }

  /**
   * Main method to launch the JavaFX application.
   *
   * @param args Command line arguments
   */
  public static void main(String[] args)
  {
    launch(args);
  }
}