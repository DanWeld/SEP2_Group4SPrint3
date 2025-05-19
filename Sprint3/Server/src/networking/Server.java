package networking;

import services.ServiceProvider;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
  private final ServiceProvider serviceProvider;

  public Server(ServiceProvider serviceProvider)
  {
    this.serviceProvider = serviceProvider;
  }

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
