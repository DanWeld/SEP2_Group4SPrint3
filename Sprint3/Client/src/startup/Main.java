package startup;

import javafx.application.Application;
import javafx.stage.Stage;
import networking.authClient.Authentication;
import startup.viewHandler.ViewHandler;

public class Main extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    // Create the view model factory
    ViewModelFactory viewModelFactory = new ViewModelFactory();

    // Create and start the view handler
    ViewHandler vh = new ViewHandler(viewModelFactory);
    vh.start();
  }

  public static void main(String[] args)
  {
    launch(args);
  }
}