package networking;

import dtos.*;
import networking.userClient.UserClient;
import observer.PropertyChangeSubject;
import services.UserSession;
import utils.JsonParser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.List;

public class Client implements PropertyChangeSubject
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private PropertyChangeSupport propertyChangeSupport;
  private boolean connected;
  private UserClient currentUser;

  public Client() throws IOException
  {
    try
    {
      // Initialize the socket connection to the server
      socket = new Socket("localhost", 8080);
      in = new BufferedReader(
          new java.io.InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      propertyChangeSupport = new PropertyChangeSupport(this);
      connected = true;
    }
    catch (IOException e)
    {
      System.out.println(
          "Warning: Could not connect to server. Running in offline mode.");
      connected = false;
      propertyChangeSupport = new PropertyChangeSupport(this);
      // Let the exception propagate to handle it at a higher level
      throw e;
    }
  }

  public void sendRequest(Request request)
  {
    if (!connected)
    {
      throw new IllegalStateException("Not connected to server");
    }

    try
    {
      // Send the handler name
      out.println(request.handler());

      // Send the action name
      out.println(request.action());

      // Send the parameters as JSON
      String paramsJson = JsonParser.toJson(request.payload());
      out.println(paramsJson);

      // Send the user object as JSON
      UserClient user = (request.handler().equals("auth") ? null : this.currentUser);
      out.println(JsonParser.toJson(user));

      // Flush the output stream
      out.flush();

      // Read the response
      String response = in.readLine();

      // Parse the response
      Response parsedResponse = (Response) JsonParser.jsonToObject(response,
          Response.class);
      if (parsedResponse.status().equals("ERROR"))
      {
        // Handle error response
        ErrorResponse errorResponse = JsonParser.convertPayload(
            parsedResponse.payload(), ErrorResponse.class);
        propertyChangeSupport.firePropertyChange("error", null, errorResponse);
        System.out.println(
            "Client: Error response received: " + errorResponse.errorMessage());
      }
      else if (parsedResponse.status().equals("SUCCESS"))
      {
        // Assign the current user if the request is for authentication
        if (request.handler().equals("auth"))
        {
          UserClient userasd = JsonParser.convertPayload(parsedResponse.payload(), UserClient.class);
          System.out.println(
              "Client: User logged in: " + userasd.getUsername());
          UserSession.getInstance().setCurrentUser(currentUser);
        }

        // Handle success response
        propertyChangeSupport.firePropertyChange(request.action(), null,
            parsedResponse.payload());
        System.out.println(
            "Client: Success response received: " + request.action() + " : "
                + parsedResponse.payload());
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public void requestAvailableProperties(String datesJson)
  {
    // Send the request to the server
    out.println("getAvailableProperties");
    out.println(datesJson);
    out.flush();

    // Read the response from the server
    String jsonResponse = null;
    try
    {
      jsonResponse = in.readLine();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    // Parse the JSON response
    List<Property> properties = JsonParser.toList(jsonResponse,
        Property[].class);

    // Notify the listeners about the new properties
    propertyChangeSupport.firePropertyChange("getAllProperties", null,
        properties);
  }

  public void getIsAvailable(Date startDate, Date endDate, int propertyId)
  {
    //Send the request to the server
    out.println("isAvailable");
    out.println(propertyId);

    // Convert the dates to JSON
    Date[] dates = new Date[2];
    dates[0] = startDate;
    dates[1] = endDate;
    String datesJson = JsonParser.toJson(dates);

    // Send the dates JSON to the server
    out.println(datesJson);
    out.flush();

    // Read the response from the server
    String jsonResponse = null;
    try
    {
      jsonResponse = in.readLine();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    // Parse the JSON response
    propertyChangeSupport.firePropertyChange("isAvailable", null, jsonResponse);
  }

  public void createBooking(int propertyID, Date startDate, Date endDate,
      String username)
  {
    //Send the request to the server
    out.println("createBooking");
    out.println(propertyID);
    out.println(username);

    // Convert the dates to JSON
    Date[] dates = new Date[2];
    dates[0] = startDate;
    dates[1] = endDate;
    String datesJson = JsonParser.toJson(dates);

    // Send the dates JSON to the server
    out.println(datesJson);
    out.flush();

    // Read the response from the server
    String jsonResponse = null;
    try
    {
      jsonResponse = in.readLine();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    Booking newBooking = JsonParser.jsonToBooking(jsonResponse);

    // Parse the JSON response
    propertyChangeSupport.firePropertyChange("bookingCreated", null,
        newBooking);
  }

  public String sendLoginRequest(String email, String password)
      throws IOException
  {
    // Create a login request
    LoginRequest loginRequest = new LoginRequest(email, password);
    String loginRequestJson = JsonParser.toJson(loginRequest);

    // Send the request to the server
    out.println("login");
    out.println(loginRequestJson);
    out.flush();

    // Read the response from the server
    String response = in.readLine();

    // If login is successful, fire an event to notify listeners
    if (response != null && response.equals("Ok"))
    {
      propertyChangeSupport.firePropertyChange("userLoggedIn", null, email);
    }

    return response;
  }

  public String sendRegisterRequest(UserClient user) throws IOException
  {
    // Serialize the user object to JSON
    String userJson = JsonParser.toJson(user);

    // Send the request to the server
    out.println("register");
    out.println(userJson);
    out.flush();

    // Read the response from the server
    return in.readLine();
  }

  public String sendLoginByUsernameRequest(String username, String password)
      throws IOException
  {
    // Create a login request with username
    LoginRequest loginRequest = new LoginRequest(username,
        password); // true indicates username login
    String loginRequestJson = JsonParser.toJson(loginRequest);

    // Send the request to the server
    out.println("loginByUsername");
    out.println(loginRequestJson);
    out.flush();

    // Read the response from the server
    String response = in.readLine();

    // If login is successful, fire an event to notify listeners
    if (response != null && response.equals("Ok"))
    {
      propertyChangeSupport.firePropertyChange("userLoggedIn", null, username);
    }

    return response;
  }

  public boolean isUsernameUnique(String username) throws IOException
  {
    // Send the request to the server
    out.println("checkUsername");
    out.println(username);
    out.flush();

    // Read the response from the server
    String response = in.readLine();
    return Boolean.parseBoolean(response);
  }

  public boolean isEmailUnique(String email) throws IOException
  {
    // Send the request to the server
    out.println("checkEmail");
    out.println(email);
    out.flush();

    // Read the response from the server
    String response = in.readLine();
    return Boolean.parseBoolean(response);
  }

  public boolean isAdmin(String email) throws IOException
  {
    // Send the request to the server
    out.println("checkAdmin");
    out.println(email);
    out.flush();

    // Read the response from the server
    String response = in.readLine();
    return Boolean.parseBoolean(response);
  }

  public List<BookingHistory> getBookingHistory(String username)
      throws IOException
  {
    // Send request to the server
    out.println("getPastBookings");
    out.println(username);
    out.flush();

    // Read the response from the server
    String jsonResponse = in.readLine();
    return JsonParser.jsonToBookingHistory(jsonResponse);
  }

  public List<BookingHistory> getCurrentBookings(String username)
      throws IOException
  {
    // Send request to the server
    out.println("getCurrentBookings");
    out.println(username);
    out.flush();

    // Read the response from the server
    String jsonResponse = in.readLine();
    return JsonParser.jsonToBookingHistory(jsonResponse);
  }

  public List<BookingHistory> getFutureBookings(String username)
      throws IOException
  {
    // Send request to the server
    out.println("getFutureBookings");
    out.println(username);
    out.flush();

    // Read the response from the server
    String jsonResponse = in.readLine();
    return JsonParser.jsonToBookingHistory(jsonResponse);
  }

  public void cancelBooking(BookingHistory booking) throws IOException
  {
    // Send request to the server
    out.println("cancelBooking");
    out.println(JsonParser.toJson(booking));
    out.flush();
  }
}