package persistence.daos.bookingHistory;

import dtos.BookingHistory;

import java.sql.*;
import java.util.ArrayList;

public class BookingHistoryDAOImpl implements BookingHistoryDAO
{
  private static BookingHistoryDAOImpl instance;

  private BookingHistoryDAOImpl()
  {
    // Constructor is private to prevent instantiation
  }

  public static synchronized BookingHistoryDAOImpl getInstance()
  {
    if (instance == null)
    {
      instance = new BookingHistoryDAOImpl();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  public ArrayList<BookingHistory> getBookingHistory(String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      // Implement the logic to retrieve booking history from the database
      PreparedStatement statement = connection.prepareStatement(
          "select p.location,b.start_date, b.end_date, p.pricepernight\n"
              + "from booking b, property p\n"
              + "where p.propertyid = b.propertyid and b.username = ?;");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      while (resultSet.next())
      {
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        double pricePerNight = resultSet.getDouble("pricepernight");

        BookingHistory bookingHistory = new BookingHistory(location, startDate,
            endDate, pricePerNight);
        bookingHistoryList.add(bookingHistory);
      }
      return bookingHistoryList;
    }
  }
}
