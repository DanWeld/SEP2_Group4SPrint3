package networking.propertyListClient;

import dtos.Property;
import utils.JsonParser;
import dtos.Request;
import networking.Client;
import observer.PropertyChangeSubject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class PropertyListClientImpl implements PropertyListClient,
    PropertyChangeListener
{
  private Client client;
  private PropertyChangeSupport support;

  public PropertyListClientImpl(Client client)
  {
    this.client = client;
    support = new PropertyChangeSupport(this);
    client.addPropertyChangeListener(this);
  }

  @Override
  public void getAvailableProperties(Date startDate, Date endDate)
  {
    // Dates to a list
    List<Date> dates = List.of(startDate, endDate);
    // Send the request to the server
    client.sendRequest(new Request("property", "readAvailable", dates));
  }

  @Override public void getAllProperties()
  {
    client.sendRequest(new Request("property", "readAll", null));
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