package services.bookingHistory;

import observer.PropertyChangeSubject;

public interface BookingHistoryService extends PropertyChangeSubject
{
  void getAllBookings(int propertyId);
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
}
