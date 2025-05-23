package networking;

import dtos.*;
import observer.PropertyChangeSubject;
import services.UserSession;
import ui.userList.UserListVM;
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
  private User currentUser;

  public Client() throws IOException
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
      out.println(JsonParser.toJson(currentUser));
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
}