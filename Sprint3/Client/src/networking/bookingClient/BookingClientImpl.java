package networking.bookingClient;

import networking.Client;

import java.sql.Date;

public class BookingClientImpl implements BookingClient
{
  private Client client;

  public BookingClientImpl(Client client)
  {
    this.client = client;
  }

  @Override
  public void createBooking(int propertyID, Date startDate,
      Date endDate, String username)
  {
    client.createBooking(propertyID, startDate, endDate, username);
  }
}
