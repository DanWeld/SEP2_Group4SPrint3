package model.booking;

import dtos.Booking;
import persistence.daos.bookings.BookingDAO;
import observer.PropertyChangeSubject;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.sql.SQLException;

public class BookingModelManager implements BookingModel, PropertyChangeSubject
{
  private final PropertyChangeSupport support;
  BookingDAO bookingDAO;
  private int propertyId;

  public BookingModelManager(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    this.support = new PropertyChangeSupport(this);
  }

  @Override public void createBooking(int propertyID, Date startDate,
      Date endDate, String username)
  {
    try
    {
      Booking newBooking = bookingDAO.create(startDate, endDate, propertyID,
          username);
      support.firePropertyChange("bookingCreated", null, newBooking);
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void isAvailable(Date startDate, Date endDate,
      int propertyId)
  {
    try
    {
      boolean isAvailable = bookingDAO.isAvailable(startDate, endDate, propertyId);
      support.firePropertyChange("isAvailable", null, isAvailable);
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
