package networking.bookingClient;

import dtos.Booking;
import dtos.Request;
import networking.Client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

/**
 * Implementation of BookingClient for handling booking-related operations
 * * This class communicates with the server to create, check availability, and extend bookings.
 * It also implements PropertyChangeListener to handle property changes
 * and notify listeners of changes in booking status.
 * * @author Group 4
 * @version 1.0
 */
public class BookingClientImpl implements BookingClient, PropertyChangeListener
{
  private Client client;
  private final PropertyChangeSupport support;

  /**
   * Constructor for BookingClientImpl
   * @param client
   */
  public BookingClientImpl(Client client)
  {
    this.client = client;
    client.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  /**
   * Creates a new booking by sending a request to the server.
   * @param booking The booking to create
   */
  @Override public void createBooking(Booking booking)
  {
    client.sendRequest(new Request("Booking", "create", booking));
  }

  /**
   * Checks if a booking is available by sending a request to the server.
   * @param booking The booking to check
   */
  @Override public void isAvailable(Booking booking)
  {
    client.sendRequest(
        new Request("Booking", "isAvailable", booking));
  }

  /**
   * Extends an existing booking by sending a request to the server.
   * @param booking The booking to extend
   */
  @Override public void extendBooking(Booking booking)
  {
     client.sendRequest(
        new Request("booking", "extend", booking));
  }

  /**
   * Handles property change events by firing a property change event
   * to notify listeners of changes in booking status.
   * @param evt The property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }

  /**
   * Adds a property change listener to this client.
   * @param listener The listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes a property change listener from this client.
   * @param listener The listener to remove
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
