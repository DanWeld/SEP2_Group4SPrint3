package networking;

import dtos.LoginRequest;
import dtos.User;
import model.booking.BookingModel;
import networking.bookingHandler.BookingHandler;
import networking.bookingHandler.BookingHandlerImpl;
import networking.propertyListHandler.PropertyListHandler;
import services.auth.AuthenticationService;
import utils.JsonParser;
import model.propertyList.PropertyListModel;
import networking.propertyListHandler.PropertyListHandlerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;

public class MainSocketHandler implements Runnable
{
  private final Socket socket;
  private final PropertyListModel propertyListModel;
  private final BookingModel bookingModel;
  private final BufferedReader in;
  private final PrintWriter out;
  private PropertyListHandler propertyListHandler;
  private BookingHandler bookingHandler;
  private final AuthenticationService authService;

  public MainSocketHandler(Socket socket, PropertyListModel propertyListModel,
      BookingModel bookingModel, AuthenticationService authService) throws IOException, SQLException
  {
    // Initialize the socket
    this.socket = socket;

    // Initialize the input and output streams
    in = new BufferedReader(
        new java.io.InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);

    // Initialize the property list model and booking model
    this.propertyListModel = propertyListModel;
    this.bookingModel = bookingModel;
    this.authService = authService;

    // Initialize the handlers
    propertyListHandler = new PropertyListHandlerImpl(socket,
        propertyListModel);
    bookingHandler = new BookingHandlerImpl(socket, bookingModel);
  }

  public MainSocketHandler(Socket socket, PropertyListModel propertyListModel, BookingModel bookingModel)
  {
  }

  @Override public void run()
  {
    try
    {
      String clientRequest;
      try
      {
        while ((clientRequest = in.readLine()) != null)
        {
          switch (clientRequest)
          {
            case "getAvailableProperties" ->
            {
              // Read the dates from the client
              String datesJson = in.readLine();
              // Parse the dates from JSON
              Date[] dates = JsonParser.jsonToDates(datesJson);

              propertyListHandler.setDates(dates);
              propertyListHandler.getAvailableProperties();
            }
            case "isAvailable" ->
            {
              int propertyID = Integer.parseInt(in.readLine());

              // Read the start and end dates from the client
              String datesJson = in.readLine();

              // Parse the dates from JSON
              Date[] dates = JsonParser.jsonToDates(datesJson);

              // Check if the property is available
              bookingHandler.isAvailable(dates[0], dates[1], propertyID);
            }
            case "getPropertyByID" ->
            {
              //read the property ID from the client
              String propertyID = in.readLine();

              //TODO get the property by ID
            }
            case "createBooking" ->
            {
              int propertyId = Integer.parseInt(in.readLine());
              String username = in.readLine();
              String startDateJson = in.readLine();

              //Convert the dates from Json
              Date[] dates = JsonParser.jsonToDates(startDateJson);
              Date startDate = dates[0];
              Date endDate = dates[1];

              // Create the booking
              bookingHandler.createBooking(propertyId, startDate, endDate,
                  username);
            }
            case "loginRequest" ->
            {
              String request = in.readLine();

              // Parse the JSON request
              LoginRequest loginRequest = JsonParser.jsonToLoginRequest(request);
            }
            case "login" ->
            {
              // Read the login request from the client
              String loginRequestJson = in.readLine();
              
              // Parse the login request from JSON
              LoginRequest loginRequest = JsonParser.jsonToLoginRequest(loginRequestJson);
              
              // Authenticate the user
              String result = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
              
              // Send the result back to the client
              out.println(result);
            }
            case "checkAdmin" ->
            {
              // Read the email from the client
              String email = in.readLine();
              
              // Check if the user is an admin
              boolean isAdmin = authService.isAdmin(email);
              
              // Send the result back to the client
              out.println(isAdmin);
            }
            case "register" ->
            {
              // Read the user from the client
              String userJson = in.readLine();
              
              // Parse the user from JSON
              User user = JsonParser.jsonToUser(userJson);
              
              // Register the user
              String result = authService.registerUser(user);
              
              // Send the result back to the client
              out.println(result);
            }
            case "checkUsername" ->
            {
              // Read the username from the client
              String username = in.readLine();
              
              // Check if the username is unique
              boolean isUnique = authService.isUsernameUnique(username);
              
              // Send the result back to the client
              out.println(isUnique);
            }
            case "checkEmail" ->
            {
              // Read the email from the client
              String email = in.readLine();
              
              // Check if the email is unique
              boolean isUnique = authService.isEmailUnique(email);
              
              // Send the result back to the client
              out.println(isUnique);
            }
          }
        }
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
    finally
    {
      // clean up socket, streams, etc.
      try
      {
        socket.close();
        in.close();
        out.close();
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}