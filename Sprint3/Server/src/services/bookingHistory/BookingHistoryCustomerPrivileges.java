package services.bookingHistory;

import observer.PropertyChangeSubject;

public interface BookingHistoryCustomerPrivileges extends PropertyChangeSubject
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
}
