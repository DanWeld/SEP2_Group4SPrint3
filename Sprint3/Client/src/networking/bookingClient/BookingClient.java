package networking.bookingClient;

import dtos.Booking;
import java.sql.Date;

public interface BookingClient
{
  void createBooking(int propertyID, Date startDate, Date endDate, String username) throws Exception;
  void isAvailable(Date startDate, Date endDate, int id) throws Exception;
  boolean extendBooking(int propertyId, Date currentEndDate, Date newEndDate, String username) throws Exception;
}
