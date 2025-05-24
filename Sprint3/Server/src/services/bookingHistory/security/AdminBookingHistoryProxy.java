package services.bookingHistory.security;

import dtos.User;
import services.bookingHistory.BookingHistoryAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdminBookingHistoryProxy implements BookingHistoryAdminPrivileges, PropertyChangeListener
{
  private final BookingHistoryAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  public AdminBookingHistoryProxy(BookingHistoryAdminPrivileges realWriter,
      User user)
  {
    this.user = user;
    this.realWriter = realWriter;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
  }

  private void checkAdmin()
  {
    if (user == null || !user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  @Override public void getAllBookings(int propertyId)
  {
    checkAdmin();
    realWriter.getAllBookings(propertyId);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    realWriter.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
