package model.bookingHistory;

import dtos.Booking;
import dtos.BookingHistory;
import observer.PropertyChangeSubject;

public interface BookingHistoryModel extends PropertyChangeSubject
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
  void cancelBooking(BookingHistory booking);
}
