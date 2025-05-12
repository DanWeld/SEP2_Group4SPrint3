package networking.bookingHistoryHandler;

import dtos.BookingHistory;

public interface BookingHistoryHandler
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
  void cancelBooking(BookingHistory booking);
}
