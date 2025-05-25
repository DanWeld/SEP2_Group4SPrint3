package networking;

import services.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class that listens for incoming client connections and handles them
 * using the MainSocketHandler.
 * This server runs on port 8080 and creates a new thread for each client
 * connection.
 * @author Group 4
 * @version 1.0
 */
public class Server
{
  private final ServiceProvider serviceProvider;

  /**
   * Constructor for the Server class.
   * @param serviceProvider The service provider that provides services to the server.
   */
  public Server(ServiceProvider serviceProvider)
  {
    this.serviceProvider = serviceProvider;
  }

  /**
   * Starts the server and listens for incoming client connections.
   * For each connection, it creates a new MainSocketHandler and starts a new
   * thread to handle the client.
   * @throws IOException If an I/O error occurs when opening the socket.
   */
  public void start() throws IOException
  {
    ServerSocket serverSocket = new ServerSocket(8080);
    System.out.println("Server started, listening for connections...");
    while (true)
    {
      Socket socket = serverSocket.accept();
      MainSocketHandler mainSocketHandler = new MainSocketHandler(socket, serviceProvider);
      Thread socketThread = new Thread(mainSocketHandler);
      socketThread.start();
      System.out.println("Client connected.");
    }
  }
}
