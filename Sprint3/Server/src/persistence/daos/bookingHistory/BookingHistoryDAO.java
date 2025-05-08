package persistence.daos.bookingHistory;

import dtos.BookingHistory;

import java.sql.SQLException;
import java.util.ArrayList;

public interface BookingHistoryDAO
{
  ArrayList<BookingHistory> getBookingHistory(String username) throws
      SQLException;
}
