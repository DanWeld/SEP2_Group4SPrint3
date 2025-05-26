package startup;

import networking.ClientSocket;
import networking.authClient.Authentication;
import networking.authClient.AuthenticationImpl;

import java.io.IOException;

/**
 * Factory class for creating client related objects
 */
public class ClientFactory
{
  private static ClientFactory instance;
  private ClientSocket client;
  private Authentication authentication;
  private boolean serverConnected;

  /**
   * Private constructor to initialize the client and authentication service
   */
  private ClientFactory()
  {
    try
    {
      client = new ClientSocket();
      authentication = new AuthenticationImpl(client);
      serverConnected = true;
    }
    catch (IOException e)
    {
      throw new RuntimeException("Server not connected: " + e.getMessage());
    }
  }

  /**
   * Returns the singleton instance of ClientFactory
   *
   * @return The ClientFactory instance
   */
  public static synchronized ClientFactory getInstance()
  {
    if (instance == null)
    {
      instance = new ClientFactory();
    }
    return instance;
  }

  /**
   * gets the client
   * @return The Client instance
   */
  public ClientSocket getClient()
  {
    return client;
  }
}
