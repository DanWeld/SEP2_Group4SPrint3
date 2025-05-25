package services.bookingHistory.security;

import dtos.User;
import services.bookingHistory.BookingHistoryAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Proxy class that checks if the user has admin privileges before allowing access
 * to booking history methods that require admin rights.
 */
public class AdminBookingHistoryProxy implements BookingHistoryAdminPrivileges, PropertyChangeListener
{
  private final BookingHistoryAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  /**
   * Constructs a proxy for the BookingHistoryAdminPrivileges that checks if the user
   * has admin privileges before allowing access to methods.
   *
   * @param realWriter The real BookingHistoryAdminPrivileges implementation.
   * @param user The user whose privileges are being checked.
   */
  public AdminBookingHistoryProxy(BookingHistoryAdminPrivileges realWriter,
      User user)
  {
    this.user = user;
    this.realWriter = realWriter;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
  }

  /**
   * Checks if the user has admin privileges.
   * If not, throws a SecurityException.
   */
  private void checkAdmin()
  {
    if (user == null || !user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  /**
   * Gets all bookings for a specific property.
   * Requires admin privileges.
   *
   * @param propertyId The ID of the property to get bookings for.
   */
  @Override public void getAllBookings(int propertyId)
  {
    checkAdmin();
    realWriter.getAllBookings(propertyId);
  }

  /**
   * Adds a property change listener to the real writer.
   * This allows the proxy to forward property change events.
   *
   * @param listener The PropertyChangeListener to add.
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    realWriter.addPropertyChangeListener(listener);
  }

  /**
   * Property change event handler that forwards events from the real writer
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
