package networking.bookingHandler;

import java.sql.Date;
import java.sql.SQLException;

public interface BookingHandler
{
  void createBooking(int propertyID, Date startDate, Date endDate, String username) throws
      SQLException;

  void isAvailable(Date startDate, Date endDate, int propertyId);
}
