package Client.src.networking;

import dtos.Booking;
import utils.JsonParser;
import dtos.Property;
import dtos.PropertyList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;

public class Client
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private PropertyChangeSupport propertyChangeSupport;

  public Client() throws IOException
  {
    // Initialize the socket connection to the server
    socket = new Socket("localhost", 8080);
    in = new BufferedReader(
        new java.io.InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);
    propertyChangeSupport = new PropertyChangeSupport(this);
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
}