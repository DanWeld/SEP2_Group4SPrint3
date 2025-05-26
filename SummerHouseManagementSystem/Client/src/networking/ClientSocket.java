package networking;

import dtos.*;
import observer.PropertyChangeSubject;
import services.UserSession;
import utils.JsonParser;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Client class for handling communication with the server.
 * It manages the socket connection, sends requests, and handles responses.
 * It also implements PropertyChangeSubject to notify listeners of property changes.
 *
 * @author Group 4
 * @version 1.0
 */
public class ClientSocket implements PropertyChangeSubject
{
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private PropertyChangeSupport propertyChangeSupport;
  private boolean connected;
  private User currentUser;

  /**
   * Constructor for Client
   * Initializes the socket connection to the server and sets up the user session.
   *
   * @throws IOException if an I/O error occurs when creating the socket
   */
  public ClientSocket() throws IOException
  {
    try
    {
      this.currentUser = UserSession.getInstance().getCurrentUser();
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

  /**
   * sends a request to the server.
   * This method sends the handler name, action name,
   * parameters as JSON,
   * and the current user object as JSON.
   * It then reads the response from the server
   * and handles it accordingly.
   *
   * @param request the request to send
   */
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
      out.println(JsonParser.toJson(getCurrentUser()));
      // Flush the output stream
      out.flush();

      // Read the response
      String response = in.readLine();

      // Parse the response
      Response parsedResponse = (Response) JsonParser.jsonToObject(response,
          Response.class);
      handleResponse(request, parsedResponse);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Handles the response from the server.
   * This method checks the status of the response
   * and notifies listeners of property changes accordingly.
   *
   * @param request the request that was sent
   * @param parsedResponse the parsed response from the server
   */
  private void handleResponse(Request request, Response parsedResponse)
  {
    if (parsedResponse.status().equals("ERROR"))
    {
      // Handle error response
      ErrorResponse errorResponse = JsonParser.convertPayload(
          parsedResponse.payload(), ErrorResponse.class);
      propertyChangeSupport.firePropertyChange("error", null, errorResponse);
    }

    else if (parsedResponse.status().equals("SUCCESS"))
    {
      // Assign the current user if the request is for authentication
      if (request.handler().equals("auth"))
      {
        this.currentUser = JsonParser.convertPayload(parsedResponse.payload(),
            User.class);
        UserSession.getInstance().setCurrentUser(currentUser);
      }

      // Handle success response
      propertyChangeSupport.firePropertyChange(request.action(), null,
          parsedResponse.payload());
    }
  }

  /**
   * Add a property change listener to this client.
   *
   * @param listener the listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  /**
   * Remove a property change listener from this client.
   *
   * @param listener the listener to remove
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  /**
   * Get the current user from the user session.
   * This method retrieves the current user from the UserSession singleton.
   *
   * @return the current user
   */
  private User getCurrentUser()
  {
    return UserSession.getInstance().getCurrentUser();
  }
}