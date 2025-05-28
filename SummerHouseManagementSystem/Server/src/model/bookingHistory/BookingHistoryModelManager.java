package model.bookingHistory;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.Response;
import persistence.daos.bookings.BookingDAO;
import services.bookingHistory.BookingHistoryAdminPrivileges;
import services.bookingHistory.BookingHistoryCustomerPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * The BookingHistoryModelManager class implements the BookingHistoryModel interface
 * and manages the booking history operations for both admin and customer privileges.
 * It listens for property changes and notifies listeners of any changes.
 *
 * @author Group 4
 * @version 1.0
 */
public class BookingHistoryModelManager implements BookingHistoryModel,
    PropertyChangeListener
{
  private BookingHistoryAdminPrivileges admin;
  private BookingHistoryCustomerPrivileges customer;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  /**
   * Constructs a BookingHistoryModelManager with the specified admin and customer privileges.
   *
   * @param admin    the BookingHistoryAdminPrivileges instance for admin operations
   * @param customer the BookingHistoryCustomerPrivileges instance for customer operations
   */
  public BookingHistoryModelManager(BookingHistoryAdminPrivileges admin,
      BookingHistoryCustomerPrivileges customer)
  {
    this.admin = admin;
    this.customer = customer;
    this.admin.addPropertyChangeListener(this);
    this.customer.addPropertyChangeListener(this);
  }
  /**
   * Requests the past bookings for a given username via the customer privileges service.
   *
   * @param username the username whose past bookings are requested
   */
  @Override
  public void getPastBookings(String username)
  {
    customer.getPastBookings(username);
  }

  /**
   * Requests the current bookings for a given username via the customer privileges service.
   *
   * @param username the username whose current bookings are requested
   */
  @Override
  public void getCurrentBookings(String username)
  {
    customer.getCurrentBookings(username);
  }

  /**
   * Requests the future bookings for a given username via the customer privileges service.
   *
   * @param username the username whose future bookings are requested
   */
  @Override
  public void getFutureBookings(String username)
  {
    customer.getFutureBookings(username);
  }

  /**
   * Requests all bookings for a given property via the admin privileges service.
   *
   * @param propertyId the ID of the property whose bookings are requested
   */
  @Override
  public void getAllBookings(int propertyId)
  {
    admin.getAllBookings(propertyId);
  }

  /**
   * Adds a property change listener to receive booking history events.
   *
   * @param listener the listener to add
   */
  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Handles property change events from the admin and customer services and
   * notifies registered listeners.
   *
   * @param evt the property change event
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
