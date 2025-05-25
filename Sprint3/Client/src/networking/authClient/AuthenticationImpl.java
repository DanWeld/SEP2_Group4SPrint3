package networking.authClient;

import dtos.LoginRequest;
import dtos.Request;
import dtos.User;
import networking.Client;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Implementation of the Authentication interface for handling user authentication
 * and registration through a client-server architecture.
 * @author Group 4
 * @version 1.0
 */
public class AuthenticationImpl
    implements Authentication, PropertyChangeListener
{
  private final Client client;
  private final PropertyChangeSupport support;

  /**
   * Constructor for AuthenticationImpl.
   * Initializes the client and adds a property change listener to it.
   *
   * @param client The client used to communicate with the server.
   */
  public AuthenticationImpl(Client client)
  {
    this.client = client;

    client.addPropertyChangeListener(this);

    support = new PropertyChangeSupport(this);
  }

  /**
   * Logs in a user by sending a login request to the server.
   *
   * @param loginRequest The login request containing user credentials.
   */
  @Override public void loginUser(LoginRequest loginRequest)
  {
    // Send login request to server through the client
    client.sendRequest(new Request("auth", "login", loginRequest));
  }

  /**
   * Registers a new user by sending a registration request to the server.
   *
   * @param user The user object containing registration details.
   */
  @Override public void registerUser(User user)
  {
    client.sendRequest(new Request("auth", "register", user));
  }

  /**
   * Catches property change events from the client and notifies listeners
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("login"))
    {
      //fire a property change event to notify listeners
      User user = JsonParser.convertPayload(evt.getNewValue(), User.class);
      support.firePropertyChange("login", null, user);
    }
    else if (evt.getPropertyName().equals("register"))
    {
      //fire a property change event to notify listeners
      User user = JsonParser.convertPayload(evt.getNewValue(), User.class);
      support.firePropertyChange("register", null, user);
    }
    else if (evt.getPropertyName().equals("error"))
    {
      support.firePropertyChange("error", null, evt.getNewValue());
    }
  }

  /**
   * adds a property change listener to this AuthenticationImpl instance.
   *
   * @param listener the PropertyChangeListener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * removes a property change listener from this AuthenticationImpl instance.
   *
   * @param listener the PropertyChangeListener to be removed
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
