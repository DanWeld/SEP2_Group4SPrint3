package networking.bookingHistoryClient;

import dtos.BookingHistory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface BookingHistoryClient
{
  List<BookingHistory> getBookingHistory(String username)
      throws IOException;
}
