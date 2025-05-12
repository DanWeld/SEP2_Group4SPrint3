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
  
  @Override
  public boolean extendBooking(int propertyId, Date currentEndDate, Date newEndDate, String username) throws Exception {
    try {
      // Check if the property is available for the extended period
      if (!isAvailableForExtension(currentEndDate, newEndDate, propertyId)) {
        return false;
      }
      
      // Update the booking with the new end date
      boolean success = bookingDAO.updateEndDate(propertyId, currentEndDate, newEndDate, username);
      
      if (success) {
        // Fire event that booking was extended
        support.firePropertyChange("bookingExtended", currentEndDate, newEndDate);
      }
      
      return success;
    } catch (SQLException e) {
      throw new Exception("Failed to extend booking: " + e.getMessage(), e);
    }
  }
  
  @Override
  public boolean isAvailableForExtension(Date currentEndDate, Date newEndDate, int propertyId) {
    try {
      // Check if the property is available between the current end date and the new end date
      return bookingDAO.isAvailable(currentEndDate, newEndDate, propertyId);
    } catch (SQLException e) {
      // If there's an error, assume it's not available
      return false;
    }
  }
}
