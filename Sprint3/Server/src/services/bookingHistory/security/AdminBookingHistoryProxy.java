package services.bookingHistory.security;

import dtos.User;
import services.bookingHistory.BookingHistoryAdminPrivileges;

import java.beans.PropertyChangeListener;

public class AdminBookingHistoryProxy
    implements BookingHistoryAdminPrivileges
{
  private final BookingHistoryAdminPrivileges realWriter;
  private final User user;

  public AdminBookingHistoryProxy(BookingHistoryAdminPrivileges realWriter, User user)
  {
    this.user = user;
    this.realWriter = realWriter;
  }

  private void checkAdmin()
  {
    if (!user.isAdmin())
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
}
