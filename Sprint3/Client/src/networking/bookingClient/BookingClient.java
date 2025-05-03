package networking.bookingClient;

import java.sql.Date;

public interface BookingClient
{
  void createBooking(int propertyID, Date startDate, Date endDate, String username) throws Exception;
}
