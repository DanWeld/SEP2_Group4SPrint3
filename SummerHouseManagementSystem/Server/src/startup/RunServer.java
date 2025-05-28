package startup;

import networking.Server;
import services.ServiceProvider;

import java.io.IOException;

/**
 * Main class to start the server.
 * This class initializes
 * the service provider and starts the server.
 */
public class RunServer
{
  /**
   * Main method to run the server.
   *
   * @param args command line arguments (not used)
   * @throws IOException if an I/O error occurs during server startup
   */
  public static void main(String[] args) throws IOException
  {
    ServiceProvider serviceLocator = new ServiceProvider();
    Server server = new Server(serviceLocator);
    server.start();
  }
}
