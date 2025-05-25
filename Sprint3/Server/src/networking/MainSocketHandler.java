package networking;

import dtos.*;
import networking.requestHandlers.RequestHandler;
import services.ServiceProvider;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * MainSocketHandler is responsible for handling incoming socket connections
 * and processing requests from clients. It reads requests, identifies the
 * appropriate request handler, and sends responses back to the client.
 *
 * @author Group 4
 * @version 1.0
 */
public class MainSocketHandler implements Runnable
{
  private final BufferedReader in;
  private final PrintWriter out;
  private final ServiceProvider serviceProvider;
  private Logger logger;

  /**
   * Constructs a MainSocketHandler with the given socket and service provider.
   *
   * @param socket the socket to read from and write to
   * @param serviceProvider the service provider for accessing services and handlers
   */
  public MainSocketHandler(Socket socket, ServiceProvider serviceProvider)
  {
    try
    {
      this.in = new BufferedReader(
          new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    // Initialize the request handlers
    this.serviceProvider = serviceProvider;
    logger = serviceProvider.getLogger();
  }

  /**
   * The run method listens for incoming requests, processes them, and sends
   * responses back to the client. It continuously reads from the input stream
   * until an IOException occurs.
   */
  @Override public void run()
  {
    try
    {
      while (true)
      {
        String handler = in.readLine();
        String action = in.readLine();
        String payloadJson = in.readLine();
        String userJson = in.readLine();

        // Deserialize the user JSON to a User object
        User user = (User) JsonParser.jsonToObject(userJson, User.class);
        System.out.println("User: " + user);
        serviceProvider.setUser(user);

        Request request = new Request(handler, action, payloadJson);
        logger.log("Received request: " + request, LogLevel.INFO);

        // Find the appropriate request handler
        for (RequestHandler requestHandler : serviceProvider.getAllHandlers())
        {
          if (requestHandler.canHandle(handler, action))
          {
            requestHandler.handle(action, payloadJson, out);
            logger.log("Handled request: " + request, LogLevel.INFO);
          }
        }
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
}
