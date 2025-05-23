package networking.bookingHistoryClient;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.Request;
import networking.Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class BookingHistoryClientImpl implements BookingHistoryClient,
    PropertyChangeListener
{
  private Client client;
  private PropertyChangeSupport support;

  public BookingHistoryClientImpl(Client client)
  {
    this.client = client;
    this.client.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  @Override public void getPastBookings(String username)
  {
    client.sendRequest(new Request("bookingHistory", "getPastBookings", username));
  }

  @Override public void getCurrentBookings(String username)
  {
    client.sendRequest(new Request("bookingHistory", "getCurrentBookings", username));
  }

  @Override public void getFutureBookings(String username)

  {
    client.sendRequest(new Request("bookingHistory", "getFutureBookings", username));
  }

  @Override public void cancelBooking(BookingHistory booking)
  {
    client.sendRequest(new Request("Booking", "delete", booking));
  }

  @Override public void extendBooking(BookingHistory bookingHistory)
  {
    client.sendRequest(new Request("Booking", "extend", bookingHistory));
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
