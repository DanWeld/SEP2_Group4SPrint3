package networking.bookingHistoryClient;

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

  @Override public List<BookingHistory> getBookingHistory(String username)
      throws IOException
  {
    return client.getBookingHistory(username);
  }
}
