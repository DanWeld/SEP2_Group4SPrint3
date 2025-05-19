package persistence.daos.bookings;

import dtos.Booking;
import dtos.BookingHistory;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface BookingDAO
{
  Booking create(Date startDate, Date endDate, int propertyId, String username) throws
      SQLException;
  Booking read(Date startDate, int propertyId, String username) throws SQLException;
  Booking update(Date startDate,Date endDate, int propertyId, String username) throws SQLException;
  void delete(Date startDate, int propertyId, String username) throws SQLException;
  List<BookingHistory> getAllBookingsByProperty(int propertyId) throws SQLException;
  boolean isAvailable(Date startDate, Date endDate, int id) throws SQLException;
  ArrayList<BookingHistory> readPastBookings(String username) throws
      SQLException;
  ArrayList<BookingHistory> readCurrentBookings(String username) throws
      SQLException;
  ArrayList<BookingHistory> readFutureBookings(String username) throws
      SQLException;
}
