package startup;

import model.booking.BookingModel;
import model.booking.BookingModelManager;
import model.propertyList.PropertyListModel;
import model.propertyList.PropertyListModelManager;
import networking.MainSocketHandler;
import persistence.UserDAO;
import persistence.UserDAOImpl;
import persistence.daos.bookings.BookingDAO;
import persistence.daos.bookings.BookingDAOImpl;
import persistence.daos.properties.PropertyDAO;
import persistence.daos.properties.PropertyDAOImpl;
import services.auth.AuthenticationService;
import services.auth.AuthenticationServiceImpl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server
{
  private final static int PORT = 8080;

  public static void main(String[] args) throws SQLException, IOException
  {
    System.out.println("Server started on port: " + PORT);

    //Create a server socket
    ServerSocket serverSocket = new ServerSocket(PORT);

    while (true)
    {
      System.out.println("Waiting for client connection...");

      //Accept a client connection
      Socket socket = serverSocket.accept();
      String clientAddress = socket.getInetAddress().getHostAddress();
      System.out.println("Client connected from: " + clientAddress);

      //Create a property list model
      PropertyDAO propertyDAO = PropertyDAOImpl.getInstance();
      // Create a booking DAO instance
      BookingDAO bookingDAO = BookingDAOImpl.getInstance();

      // Create a property list model manager
      PropertyListModel propertyListModel = new PropertyListModelManager(
          propertyDAO);
      // Create a booking model
      BookingModel bookingModel = new BookingModelManager(bookingDAO);
      
      // Create a UserDAO and AuthenticationService
      UserDAO userDAO = UserDAOImpl.getInstance();
      AuthenticationService authService = new AuthenticationServiceImpl(userDAO);

      // Create a new thread for the client
      Thread clientThread = new Thread(
          new MainSocketHandler(socket, propertyListModel, bookingModel, authService));
      clientThread.start();
    }
  }
}
