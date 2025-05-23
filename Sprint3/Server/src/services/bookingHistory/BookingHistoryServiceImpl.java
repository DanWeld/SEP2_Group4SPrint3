package services.bookingHistory;

import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.Response;
import persistence.daos.bookings.BookingDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class BookingHistoryServiceImpl implements BookingHistoryCustomerPrivileges, BookingHistoryAdminPrivileges, BookingHistoryService
{
  private PropertyChangeSupport propertyChangeSupport;
  private BookingDAO bookingDAO;

  public BookingHistoryServiceImpl(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

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

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }
}
