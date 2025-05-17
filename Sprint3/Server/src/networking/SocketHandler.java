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

public class SocketHandler implements Runnable
{
  private final BufferedReader in;
  private final PrintWriter out;
  private final ServiceProvider serviceProvider;
  private Logger logger;

  public SocketHandler(Socket socket, ServiceProvider serviceProvider)
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
        serviceProvider.setUser(user);

        Request request = new Request(handler, action, payloadJson);
        logger.log("Received request: " + request, LogLevel.INFO);
        System.out.println("SocketHandler: Received request: " + payloadJson);

        // Find the appropriate request handler
        for (RequestHandler requestHandler : serviceProvider.getAllHandlers())
        {
          if (requestHandler.canHandle(handler, action))
          {
            requestHandler.handle(action, payloadJson, out);
            System.out.println(
                "SocketHandler: Request handler: " + requestHandler.getClass());
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
