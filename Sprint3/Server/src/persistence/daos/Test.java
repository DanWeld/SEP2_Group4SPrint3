package persistence.daos;

import dtos.Property;
import dtos.User;
import persistence.daos.bookings.BookingDAO;
import persistence.daos.bookings.BookingDAOImpl;
import persistence.daos.properties.PropertyDAO;
import persistence.daos.properties.PropertyDAOImpl;
import persistence.daos.user.UserDAO;
import persistence.daos.user.UserDAOImpl;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class Test
{
  public static void main(String[] args) throws SQLException
  {
    UserDAO userDAO = UserDAOImpl.getInstance();
    User user = userDAO.create("testUser", "test@test.com", "password");
    System.out.println("User created: " + user);
  }
}
