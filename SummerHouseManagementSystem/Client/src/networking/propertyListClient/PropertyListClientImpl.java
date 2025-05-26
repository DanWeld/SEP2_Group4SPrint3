package networking.propertyListClient;

import dtos.Request;
import networking.ClientSocket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.util.List;

public class PropertyListClientImpl
    implements PropertyListClient, PropertyChangeListener
{
  private ClientSocket client;
  private PropertyChangeSupport support;

  public PropertyListClientImpl(ClientSocket client)
  {
    this.client = client;
    support = new PropertyChangeSupport(this);
    client.addPropertyChangeListener(this);
  }

  @Override public void getAvailableProperties(Date startDate, Date endDate)
  {
    System.out.println("getAvailableProperties called with startDate: " + startDate +
        ", endDate: " + endDate);
    // Dates to a list
    List<Date> dates = List.of(startDate, endDate);
    System.out.println(dates);
    // Send the request to the server
    System.out.println("Before ClinetImpl");
    client.sendRequest(new Request("property", "readAvailable", dates));
    System.out.println("After ClinetImpl");
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