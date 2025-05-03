package persistence.daos;

import dtos.Property;
import persistence.daos.bookings.BookingDAO;
import persistence.daos.bookings.BookingDAOImpl;
import persistence.daos.properties.PropertyDAO;
import persistence.daos.properties.PropertyDAOImpl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Test
{
  public static void main(String[] args) throws SQLException
  {
    BookingDAO bookingDAO = BookingDAOImpl.getInstance();
    PropertyDAO propertyDAO = PropertyDAOImpl.getInstance();
    List<Property> properties = propertyDAO.getAvailableProperties(Date.valueOf("2025-10-01"), Date.valueOf("2025-10-05"));
    System.out.println("Available properties:" + properties);
  }
}
