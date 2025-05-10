package networking;

import dtos.*;
import utils.JsonParser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Client
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private PropertyChangeSupport propertyChangeSupport;
  private boolean connected;

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

  public boolean isConnected()
  {
    return connected;
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
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
    PropertyList properties = JsonParser.jsonToProperties(jsonResponse);

    // Notify the listeners about the new properties
    propertyChangeSupport.firePropertyChange("getAllProperties", null,
        properties);
  }

  public void getPropertyByID(int id)
  {
    //Send the request to the server
    out.println("getPropertyByID");
    out.println(id);
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
    Property property = JsonParser.parseProperty(jsonResponse);

    // Notify the listeners about the new property
    propertyChangeSupport.firePropertyChange("getPropertyByID", null, property);
  }

  public void getIsAvailable(Date startDate, Date endDate, int propertyId)
  {
    //Send the request to the server
    out.println("isAvailable");
    out.println(propertyId);
    String datesJson = JsonParser.datesToJson(startDate, endDate);
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
    String datesJson = JsonParser.datesToJson(startDate, endDate);
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

  public String sendRegisterRequest(User user) throws IOException
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
    LoginRequest loginRequest = new LoginRequest(username, password,
        true); // true indicates username login
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
    System.out.println("Cancel booking request sent");
    System.out.println("Booking ID: " + booking);
    out.println(JsonParser.toJson(booking));
    out.flush();
  }
}