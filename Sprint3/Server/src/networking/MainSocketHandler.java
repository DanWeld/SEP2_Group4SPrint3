package networking;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.LoginRequest;
import dtos.User;
import dtos.UserProfile;
import model.booking.BookingModel;
import model.bookingHistory.BookingHistoryModel;
import networking.bookingHandler.BookingHandler;
import networking.bookingHandler.BookingHandlerImpl;
import networking.bookingHistoryHandler.BookingHistoryHandler;
import networking.bookingHistoryHandler.BookingHistoryHandlerImpl;
import networking.profileHandler.ProfileHandler;
import networking.profileHandler.ProfileHandlerImpl;
import networking.propertyListHandler.PropertyListHandler;
import model.authentication.AuthenticationService;
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
  private final BookingHistoryModel bookingHistoryModel;
  private final BufferedReader in;
  private final PrintWriter out;
  private PropertyListHandler propertyListHandler;
  private BookingHandler bookingHandler;
  private final AuthenticationService authService;
  private BookingHistoryHandler bookingHistoryHandler;
  private ProfileHandler profileHandler;

  public MainSocketHandler(Socket socket, PropertyListModel propertyListModel,
      BookingModel bookingModel, AuthenticationService authService,
      BookingHistoryModel bookingHistoryModel) throws IOException, SQLException
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
    this.bookingHistoryModel = bookingHistoryModel;

    // Initialize the handlers
    propertyListHandler = new PropertyListHandlerImpl(socket,
        propertyListModel);
    bookingHandler = new BookingHandlerImpl(socket, bookingModel);
    bookingHistoryHandler = new BookingHistoryHandlerImpl(socket,
        bookingHistoryModel);
    try {
      profileHandler = new ProfileHandlerImpl();
    } catch (Exception e) {
      throw new SQLException("Failed to initialize ProfileHandler", e);
    }
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
              LoginRequest loginRequest = JsonParser.jsonToLoginRequest(
                  request);
            }
            case "login" ->
            {
              // Read the login request from the client
              String loginRequestJson = in.readLine();

              // Parse the request
              LoginRequest loginRequest = JsonParser.jsonToLoginRequest(
                  loginRequestJson);

              // Authenticate the user
              String response = authService.authenticate(
                  loginRequest.getCredential(), loginRequest.getPassword());

              // Send the response to the client
              out.println(response);
              out.flush();
            }
            case "loginByUsername" ->
            {
              // Read the login request from the client
              String loginRequestJson = in.readLine();

              // Parse the request
              LoginRequest loginRequest = JsonParser.jsonToLoginRequest(
                  loginRequestJson);

              // Authenticate the user by username
              String response = authService.authenticateByUsername(
                  loginRequest.getCredential(), loginRequest.getPassword());

              // Send the response to the client
              out.println(response);
              out.flush();
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
            case "extendBooking" -> {
              // Read parameters from the client
              String paramsJson = in.readLine();
              System.out.println("DEBUG: Received parameters JSON: " + paramsJson);
              
              try {
                // Parse parameters from JSON array
                Object parsedParams = JsonParser.jsonToObject(paramsJson, Object[].class);
                
                if (!(parsedParams instanceof Object[])) {
                  System.err.println("ERROR: Expected Object[] but got " + 
                      (parsedParams != null ? parsedParams.getClass().getName() : "null"));
                  out.println(false);
                  return;
                }
                
                Object[] params = (Object[]) parsedParams;
                
                if (params.length < 4) {
                  System.err.println("ERROR: Not enough parameters for extendBooking, expected 4, got " + params.length);
                  out.println(false);
                  return;
                }
                
                // Convert parameters to the correct types
                int propertyId;
                Date currentEndDate;
                Date newEndDate;
                String username;
                
                try {
                  // Handle property ID
                  if (params[0] instanceof Number) {
                    propertyId = ((Number) params[0]).intValue();
                  } else if (params[0] instanceof String) {
                    propertyId = Integer.parseInt((String) params[0]);
                  } else {
                    throw new Exception("Invalid property ID type: " + params[0].getClass());
                  }
                  
                  // Handle current end date
                  if (params[1] instanceof Number) {
                    currentEndDate = new Date(((Number) params[1]).longValue());
                  } else if (params[1] instanceof String) {
                    currentEndDate = Date.valueOf((String) params[1]);
                  } else {
                    throw new Exception("Invalid current end date type: " + params[1].getClass());
                  }
                  
                  // Handle new end date
                  if (params[2] instanceof Number) {
                    newEndDate = new Date(((Number) params[2]).longValue());
                  } else if (params[2] instanceof String) {
                    newEndDate = Date.valueOf((String) params[2]);
                  } else {
                    throw new Exception("Invalid new end date type: " + params[2].getClass());
                  }
                  
                  // Handle username
                  if (params[3] instanceof String) {
                    username = (String) params[3];
                  } else {
                    throw new Exception("Invalid username type: " + params[3].getClass());
                  }
                  
                  System.out.println("DEBUG: Server received extend booking request - Property ID: " + propertyId + 
                                   ", Current end date: " + currentEndDate + 
                                   ", New end date: " + newEndDate + 
                                   ", Username: " + username);
                  
                  // Extra validation to ensure dates are valid
                  if (currentEndDate.after(newEndDate)) {
                    out.println(false);
                    System.err.println("ERROR: New end date must be after current end date");
                    return;
                  }
                  
                  // Call the booking handler to extend the booking
                  boolean success = bookingHandler.extendBooking(propertyId, currentEndDate, newEndDate, username);
                  System.out.println("DEBUG: Booking extension result: " + (success ? "Success" : "Failed"));
                  
                  // Send the result back to the client
                  out.println(success);
                } catch (Exception e) {
                  System.err.println("ERROR: Parameter conversion error: " + e.getMessage());
                  e.printStackTrace();
                  out.println(false);
                }
              } catch (Exception e) {
                System.err.println("ERROR: Failed to parse parameters: " + e.getMessage());
                e.printStackTrace();
                out.println(false);
              }
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
            case "getPastBookings" ->
            {
              // Read the username from the client
              String username = in.readLine();

              // Get the booking history
              bookingHistoryHandler.getPastBookings(username);
            }
            case "getCurrentBookings" ->
            {
              // Read the username from the client
              String username = in.readLine();

              // Get the current bookings
              bookingHistoryHandler.getCurrentBookings(username);
            }
            case "getFutureBookings" ->
            {
              // Read the username from the client
              String username = in.readLine();

              // Get the future bookings
              bookingHistoryHandler.getFutureBookings(username);
            }
            case "cancelBooking" ->
            {
              // Read the booking from the client
              String bookingJson = in.readLine();

              // Parse the booking from JSON
              BookingHistory booking = (BookingHistory) JsonParser.jsonToObject(bookingJson,
                  BookingHistory.class);

              // Cancel the booking
              bookingHistoryHandler.cancelBooking(booking);
            }
            case "getProfile" -> {
              // Read the username from the client
              String username = in.readLine();
              
              try {
                // Get the user profile
                UserProfile profile = profileHandler.getProfile(username);
                
                // Convert the profile to JSON
                String profileJson = JsonParser.toJson(profile);
                
                // Send the profile back to the client
                out.println(profileJson);
                out.flush();
              } catch (Exception e) {
                out.println("ERROR: " + e.getMessage());
                out.flush();
              }
            }
            case "updateProfile" -> {
              // Read the profile from the client
              String profileJson = in.readLine();
              
              try {
                // Parse the profile from JSON
                UserProfile profile = (UserProfile) JsonParser.jsonToObject(profileJson, UserProfile.class);
                
                // Update the profile
                boolean success = profileHandler.updateProfile(profile);
                
                // Send the result back to the client
                out.println(success);
                out.flush();
              } catch (Exception e) {
                out.println("ERROR: " + e.getMessage());
                out.flush();
              }
            }
            case "deleteProfile" -> {
              // Read the username from the client
              String username = in.readLine();
              
              try {
                // Delete the profile
                boolean success = profileHandler.deleteProfile(username);
                
                // Send the result back to the client
                out.println(success);
                out.flush();
              } catch (Exception e) {
                out.println("ERROR: " + e.getMessage());
                out.flush();
              }
            }
            case "changePassword" -> {
              // Read the parameters from the client
              String paramsJson = in.readLine();
              
              try {
                // Parse the parameters from JSON
                Object paramsObj = JsonParser.jsonToObject(paramsJson, Object[].class);
                Object[] params;
                
                if (paramsObj instanceof Object[]) {
                  params = (Object[]) paramsObj;
                } else {
                  System.err.println("ERROR: Invalid parameters format for changePassword");
                  out.println(false);
                  return;
                }
                
                String username = (String) params[0];
                String currentPassword = (String) params[1];
                String newPassword = (String) params[2];
                
                // Change the password
                boolean success = profileHandler.changePassword(username, currentPassword, newPassword);
                
                // Send the result back to the client
                out.println(success);
                out.flush();
              } catch (Exception e) {
                out.println("ERROR: " + e.getMessage());
                out.flush();
              }
            }
            // extendBooking case is handled above
          }
        }
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
      catch (IOException e)
      {
        e.printStackTrace();
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