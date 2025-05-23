package networking.userClient;

import dtos.Request;
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
