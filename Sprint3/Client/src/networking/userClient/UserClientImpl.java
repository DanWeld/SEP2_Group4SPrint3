package networking.userClient;

import dtos.Request;
import dtos.User;
import networking.Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserClientImpl implements UserClient, PropertyChangeListener
{
  private final Client client;
  private PropertyChangeSupport support;

  public UserClientImpl(Client client)
  {
    this.client = client;
    support = new PropertyChangeSupport(this);
    client.addPropertyChangeListener(this);
  }

  @Override public void getAllUsers()
  {
    Request request = new Request("user", "getAllUsers", null);
    client.sendRequest(request);
  }

  @Override public void promoteToAdmin(String username)
  {
    Request request = new Request("user", "promote", username);
    client.sendRequest(request);
  }

  @Override public void updateUser(User user)
  {
    Request request = new Request("user", "update", user);
    client.sendRequest(request);
  }

  @Override public void deleteUser(String username)
  {
    Request request = new Request("user", "delete", username);
    client.sendRequest(request);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
