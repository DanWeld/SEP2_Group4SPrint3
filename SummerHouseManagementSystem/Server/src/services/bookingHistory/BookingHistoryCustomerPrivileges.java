package services.bookingHistory;

import observer.PropertyChangeSubject;

/**
 * Interface for booking history admin privileges.
 * This interface extends PropertyChangeSubject to allow
 * property change notifications.
 *
 */
public interface BookingHistoryCustomerPrivileges extends PropertyChangeSubject
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
}
