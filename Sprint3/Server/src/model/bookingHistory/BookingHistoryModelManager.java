package model.bookingHistory;

import dtos.Booking;
import dtos.BookingHistory;
import persistence.daos.bookings.BookingDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class BookingHistoryModelManager implements BookingHistoryModel
{
  private PropertyChangeSupport propertyChangeSupport;
  private ArrayList<BookingHistory> list;
  private BookingDAO bookingDAO;


  public BookingHistoryModelManager(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    list = new ArrayList<>();
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void getPastBookings(String username)
  {
    try
    {
      list = bookingDAO.readPastBookings(username);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }

  public void getCurrentBookings(String username)
  {
    try
    {
      list = bookingDAO.readCurrentBookings(username);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }

  public void getFutureBookings(String username)
  {
    try
    {
      list = bookingDAO.readFutureBookings(username);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }

  @Override public void cancelBooking(BookingHistory booking)
  {
    try
    {
      bookingDAO.delete(booking.getStartDate(), booking.getPropertyId(), booking.getUsername());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
