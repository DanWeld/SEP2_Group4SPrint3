package networking.authClient;

import dtos.LoginRequest;
import dtos.Request;
import dtos.User;
import networking.Client;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AuthenticationImpl
    implements Authentication, PropertyChangeListener
{
  private final Client client;
  private final PropertyChangeSupport support;

  public AuthenticationImpl(Client client)
  {
    this.client = client;

    client.addPropertyChangeListener(this);

    support = new PropertyChangeSupport(this);
  }

  @Override public void loginUser(LoginRequest loginRequest)
  {
    // Send login request to server through the client
    client.sendRequest(new Request("auth", "login", loginRequest));
  }

  @Override public void registerUser(User user)
  {
    System.out.println("AuthenticationImpl: Registering user: " + user);
    client.sendRequest(new Request("auth", "register", user));
  }

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
      System.out.println("AuthenticationImpl: propertyChange: firing event " + user);
      support.firePropertyChange("register", null, user);
    }
    else if (evt.getPropertyName().equals("error"))
    {
      System.out.println("AuthenticationImpl: propertyChange: firing event " +evt.getNewValue());
      support.firePropertyChange("error", null, evt.getNewValue());
    }
    else
    {
      System.out.println("Unknown error occurred");
    }
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
