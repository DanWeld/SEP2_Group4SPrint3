package model.bookingHistory;

import dtos.BookingHistory;
import persistence.daos.bookingHistory.BookingHistoryDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class BookingHistoryModelManager implements BookingHistoryModel
{
  private PropertyChangeSupport propertyChangeSupport;
  private ArrayList<BookingHistory> list;
  private BookingHistoryDAO bookingHistoryDAO;


  public BookingHistoryModelManager(BookingHistoryDAO bookingHistoryDAO)
  {
    this.bookingHistoryDAO = bookingHistoryDAO;
    list = new ArrayList<>();
    propertyChangeSupport = new PropertyChangeSupport(this);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void getBookingHistory(String username)
  {
    try
    {
      list = bookingHistoryDAO.getBookingHistory(username);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    propertyChangeSupport.firePropertyChange("bookingHistory", null, list);
  }
}
