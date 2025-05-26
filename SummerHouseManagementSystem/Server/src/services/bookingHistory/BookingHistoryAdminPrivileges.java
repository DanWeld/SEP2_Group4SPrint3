package services.bookingHistory;

import observer.PropertyChangeSubject;

/**
 * Interface for booking history admin privileges.
 * This interface extends PropertyChangeSubject to allow
 * property change notifications.
 */
public interface BookingHistoryAdminPrivileges extends PropertyChangeSubject
{
  void getAllBookings(int propertyId);
}
