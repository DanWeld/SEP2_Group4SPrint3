package services.bookingHistory;

import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.Response;
import persistence.daos.bookings.BookingDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/**
 * Implementation of the BookingHistoryService interface that provides methods
 * to retrieve booking history for customers and admins.
 * This class uses a BookingDAO to access the database
 * and notifies listeners of changes
 * to booking history data.
 */
public class BookingHistoryServiceImpl implements BookingHistoryCustomerPrivileges, BookingHistoryAdminPrivileges, BookingHistoryService
{
  private PropertyChangeSupport propertyChangeSupport;
  private BookingDAO bookingDAO;

  /**
   * Constructor for BookingHistoryServiceImpl.
   * Initializes the BookingDAO and PropertyChangeSupport.
   *
   * @param bookingDAO the BookingDAO to use for database operations
   */
  public BookingHistoryServiceImpl(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  /**
   * Retrieves past bookings for a given username.
   * Notifies listeners of the result via property change events.
   *
   * @param username the username of the customer
   */
  public void getPastBookings(String username)
  {
    try
    {
      List<BookingHistory> list = bookingDAO.readPastBookings(username.replace("\"", ""));
      Response response = new Response("SUCCESS", list);
      propertyChangeSupport.firePropertyChange("pastBookingsSuccess", null,
          response);
    }
    catch (Exception e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      propertyChangeSupport.firePropertyChange("pastBookingsFailure", null,
          response);
    }
  }

  /**
   * Retrieves current bookings for a given username.
   * Notifies listeners of the result via property change events.
   *
   * @param username the username of the customer
   */
  public void getCurrentBookings(String username)
  {
    try
    {
      List<BookingHistory> list = bookingDAO.readCurrentBookings(username.replace("\"", ""));
      Response response = new Response("SUCCESS", list);
      propertyChangeSupport.firePropertyChange("currentBookingsSuccess", null,
          response);
    }
    catch (Exception e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      propertyChangeSupport.firePropertyChange("currentBookingsFailure", null,
          response);
    }
  }

  /**
   * Retrieves future bookings for a given username.
   * Notifies listeners of the result via property change events.
   *
   * @param username the username of the customer
   */
  public void getFutureBookings(String username)
  {
    try
    {
      List<BookingHistory> list = bookingDAO.readFutureBookings(username.replace("\"", ""));
      Response response = new Response("SUCCESS", list);
      propertyChangeSupport.firePropertyChange("futureBookingsSuccess", null,
          response);
    }
    catch (Exception e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      propertyChangeSupport.firePropertyChange("futureBookingsFailure", null,
          response);
    }
  }

  /**
   * Retrieves all bookings for a given property ID.
   * Notifies listeners of the result via property change events.
   *
   * @param propertyId the ID of the property
   */
  @Override public void getAllBookings(int propertyId)
  {
    try
    {
      List<BookingHistory> list = bookingDAO.getAllBookingsByProperty(propertyId);
      Response response = new Response("SUCCESS", list);
      propertyChangeSupport.firePropertyChange("allBookingsSuccess", null,
          response);
    }
    catch (Exception e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      propertyChangeSupport.firePropertyChange("allBookingsFailure", null,
          response);
    }
  }

  /**
   * Adds a PropertyChangeListener to this service.
   * Listeners will be notified of changes to booking history data.
   *
   * @param listener the PropertyChangeListener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }
}
