package services.bookingHistory;

import observer.PropertyChangeSubject;

/**
 * Interface for booking history admin privileges.
 * This interface extends PropertyChangeSubject to allow
 * property change notifications.
 * It defines methods to retrieve past, current, and future bookings
 * for a user or all bookings for a property.
 */
public interface BookingHistoryService extends PropertyChangeSubject
{
  void getAllBookings(int propertyId);
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
}
