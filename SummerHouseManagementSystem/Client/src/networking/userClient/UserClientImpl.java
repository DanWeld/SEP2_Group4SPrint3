package networking.userClient;

import dtos.Request;
import dtos.User;
import networking.ClientSocket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Implementation of UserClient for handling user operations
 * and communication with the server.
 * This class listens for property changes from the client
 * and forwards them to registered listeners.
 *
 * @author Group 4
 * @version 1.0
 */
public class UserClientImpl implements UserClient, PropertyChangeListener
{
  private final ClientSocket client;
  private PropertyChangeSupport support;

  /**
   * Constructor for UserClientImpl
   * Initializes the client and adds a property change listener.
   *
   * @param client The client used for communication with the server
   */
  public UserClientImpl(ClientSocket client)
  {
    this.client = client;
    support = new PropertyChangeSupport(this);
    client.addPropertyChangeListener(this);
  }

  /**
   * get a list of all users
   * sends a request to the server to retrieve all users.
   */
  @Override public void getAllUsers()
  {
    Request request = new Request("user", "getAllUsers", null);
    client.sendRequest(request);
  }

  /**
   * promote a user to admin
   *
   * @param username the username of the user to promote
   *                 This method sends a request to the server
   */
  @Override public void promoteToAdmin(String username)
  {
    Request request = new Request("user", "promote", username);
    client.sendRequest(request);
  }

  /**
   * update a user
   * This method sends a request to the server
   *
   * @param user the user object containing updated information
   */
  @Override public void updateUser(User user)
  {
    Request request = new Request("user", "update", user);
    client.sendRequest(request);
  }

  /**
   * delete a user
   * This method sends a request to the server
   *
   * @param username the username of the user to delete
   */
  @Override public void deleteUser(String username)
  {
    Request request = new Request("user", "delete", username);
    client.sendRequest(request);
  }

  /**
   * Handles property change events and forwards them to registered listeners.
   *
   * @param evt the property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }

  /**
   * Adds a property change listener to this client.
   * This method allows other components to listen for property changes.
   *
   * @param listener the listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes a property change listener from this client.
   * This method allows other components to stop listening for property changes.
   *
   * @param listener the listener to remove
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
