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
      System.out.println("DEBUG: Retrieved " + list.size() + " past bookings for user " + username);
    }
    catch (Exception e)
    {
      System.err.println("ERROR: Failed to read past bookings: " + e.getMessage());
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }

  public void getCurrentBookings(String username)
  {
    try
    {
      System.out.println("DEBUG: Getting current bookings for user: " + username);
      list = bookingDAO.readCurrentBookings(username);
      System.out.println("DEBUG: Retrieved " + list.size() + " current bookings for user " + username);
      
      // Log the bookings for debugging
      if (list.isEmpty()) {
        System.out.println("DEBUG: No current bookings found for " + username);
      } else {
        for (BookingHistory booking : list) {
          System.out.println("DEBUG: Current booking - Location: " + booking.getLocation() + 
                           ", Start: " + booking.getStartDate() + 
                           ", End: " + booking.getEndDate());
        }
      }
    }
    catch (Exception e)
    {
      System.err.println("ERROR: Failed to read current bookings: " + e.getMessage());
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }

  public void getFutureBookings(String username)
  {
    try
    {
      list = bookingDAO.readFutureBookings(username);
      System.out.println("DEBUG: Retrieved " + list.size() + " future bookings for user " + username);
    }
    catch (Exception e)
    {
      System.err.println("ERROR: Failed to read future bookings: " + e.getMessage());
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
