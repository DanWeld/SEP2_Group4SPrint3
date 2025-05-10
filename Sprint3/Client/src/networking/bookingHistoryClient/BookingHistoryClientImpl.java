package networking.bookingHistoryClient;

import dtos.Booking;
import dtos.BookingHistory;
import networking.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryClientImpl implements BookingHistoryClient
{
  private Client client;

  public BookingHistoryClientImpl(Client client)
  {
    this.client = client;
  }

  @Override public List<BookingHistory> getPastBookings(String username)
      throws IOException
  {
    return client.getBookingHistory(username);
  }

  @Override public List<BookingHistory> getCurrentBookings(String username)
      throws IOException
  {
    return client.getCurrentBookings(username);
  }

  @Override public List<BookingHistory> getFutureBookings(String username)
      throws IOException
  {
    return client.getFutureBookings(username);
  }

  @Override public void cancelBooking(BookingHistory booking) throws IOException
  {
    client.cancelBooking(booking);
  }
}
