package networking.bookingHistoryClient;

import dtos.BookingHistory;
import dtos.Request;
import networking.ClientSocket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Implementation of BookingHistoryClient for handling booking history operations
 * and communication with the server.
 * This class listens for property changes from the client
 * and forwards them to registered listeners.
 *
 * @author Group 4
 * @version 1.0
 */
public class BookingHistoryClientImpl implements BookingHistoryClient,
    PropertyChangeListener
{
  private ClientSocket client;
  private PropertyChangeSupport support;

  /**
   * Constructor for BookingHistoryClientImpl
   * Initializes the client and adds a property change listener.
   *
   * @param client The client used for communication with the server
   */
  public BookingHistoryClientImpl(ClientSocket client)
  {
    this.client = client;
    this.client.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  /**
   * get past bookings for a user
   * @param username the username of the user
   */
  @Override public void getPastBookings(String username)
  {
    client.sendRequest(new Request("bookingHistory", "getPastBookings", username));
  }

  /**
   * get current bookings for a user
   * @param username the username of the user
   */
  @Override public void getCurrentBookings(String username)
  {
    client.sendRequest(new Request("bookingHistory", "getCurrentBookings", username));
  }

  /**
   * get future bookings for a user
   * @param username the username of the user
   */
  @Override public void getFutureBookings(String username)
  {
    client.sendRequest(new Request("bookingHistory", "getFutureBookings", username));
  }

  /**
   * Cancel a booking
   * @param booking the booking to cancel
   */
  @Override public void cancelBooking(BookingHistory booking)
  {
    client.sendRequest(new Request("Booking", "delete", booking));
  }

  /**
   * Get booking history for a specific property
   * @param propertyId the ID of the property
   */
  @Override public void getBookingHistory(int propertyId)
  {
    client.sendRequest(new Request("bookingHistory", "getAllBookings", propertyId));
  }

  /**
   * Handles property change events and forwards them to registered listeners.
   * This method is called when the client receives a property change event.
   *
   * @param evt The property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }

  /**
   * Adds a property change listener to this client.
   * The listener will be notified of property changes.
   *
   * @param listener The listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes a property change listener from this client.
   * The listener will no longer be notified of property changes.
   *
   * @param listener The listener to remove
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
