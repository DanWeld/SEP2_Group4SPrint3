package networking.bookingClient;

import dtos.Request;
import networking.Client;

import java.sql.Date;

public class BookingClientImpl implements BookingClient
{
  private Client client;

  public BookingClientImpl(Client client)
  {
    this.client = client;
  }

  @Override public void createBooking(int propertyID, Date startDate,
      Date endDate, String username)
  {
    client.createBooking(propertyID, startDate, endDate, username);
  }

  @Override public void isAvailable(Date startDate, Date endDate, int id)
  {
    client.getIsAvailable(startDate, endDate, id);
  }

  @Override public boolean extendBooking(int propertyId, Date currentEndDate,
      Date date, String username)
  {
     client.sendRequest(
        new Request("booking", "extendBooking", date));
    return false;
  }
}
