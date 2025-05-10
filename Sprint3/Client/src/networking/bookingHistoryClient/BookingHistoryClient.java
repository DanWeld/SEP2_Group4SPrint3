package networking.bookingHistoryClient;

import dtos.BookingHistory;

import java.io.IOException;
import java.util.List;

public interface BookingHistoryClient
{
  List<BookingHistory> getPastBookings(String username) throws IOException;
  List<BookingHistory> getCurrentBookings(String username) throws IOException;
  List<BookingHistory> getFutureBookings(String username) throws IOException;
  void cancelBooking(BookingHistory booking) throws IOException;
}
