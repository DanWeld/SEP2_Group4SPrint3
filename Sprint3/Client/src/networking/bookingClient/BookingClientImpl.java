package networking.bookingClient;

import dtos.Booking;
import dtos.Request;
import networking.Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class BookingClientImpl implements BookingClient, PropertyChangeListener
{
  private Client client;
  private final PropertyChangeSupport support;

  public BookingClientImpl(Client client)
  {
    this.client = client;
    client.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  @Override public void createBooking(Booking booking)
  {
    client.sendRequest(new Request("Booking", "create", booking));
  }

  @Override public void isAvailable(Booking booking)
  {
    client.sendRequest(
        new Request("Booking", "isAvailable", booking));
  }

  @Override public void extendBooking(Booking booking)
  {
     client.sendRequest(
        new Request("booking", "extend", booking));
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
