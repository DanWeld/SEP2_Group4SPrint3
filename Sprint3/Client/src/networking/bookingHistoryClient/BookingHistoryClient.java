package networking.bookingHistoryClient;

import dtos.BookingHistory;
import observer.PropertyChangeSubject;

import java.io.IOException;
import java.util.List;

public interface BookingHistoryClient extends PropertyChangeSubject
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
  void cancelBooking(BookingHistory booking);
  void extendBooking(BookingHistory bookingHistory);
}
