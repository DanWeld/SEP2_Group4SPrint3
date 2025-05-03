package persistence.daos.bookings;

import dtos.Booking;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface BookingDAO
{
  Booking create(Date startDate, Date endDate, int propertyId, String username) throws
      SQLException;
  Booking read(Date startDate, int propertyId, String username) throws SQLException;
  List<Booking> readByUsername(String username) throws SQLException;
  List<Booking> readByPropertyId(int propertyId) throws SQLException;
  Booking update(Date startDate,Date endDate, int propertyId, String username) throws SQLException;
  void delete(Date startDate, int propertyId, String username) throws SQLException;
  List<Booking> getAllBookings() throws SQLException;
  boolean isAvailable(Date startDate, Date endDate, int id) throws SQLException;
}
