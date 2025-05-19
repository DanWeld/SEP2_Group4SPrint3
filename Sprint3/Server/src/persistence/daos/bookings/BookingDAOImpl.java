package persistence.daos.bookings;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAOImpl implements BookingDAO
{
  private static BookingDAOImpl instance;

  private BookingDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static synchronized BookingDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new BookingDAOImpl();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  @Override public Booking create(Date startDate, Date endDate, int propertyId,
      String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      // Check if the property is available
      PreparedStatement checkingStatement = connection.prepareStatement(
          "SELECT * FROM booking WHERE propertyID = ? AND (start_date, end_date) OVERLAPS (?, ?)");
      checkingStatement.setInt(1, propertyId);
      checkingStatement.setDate(2, startDate);
      checkingStatement.setDate(3, endDate);

      ResultSet checkingResultSet = checkingStatement.executeQuery();
      if (checkingResultSet.next())
      {
        throw new SQLException(
            "Property is already booked for the selected dates.");
      }

      // Prepare the SQL statement
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO booking (username, propertyID, booking_date, start_date, end_date) VALUES (?, ?, ?, ?, ?)");
      Date createDate = new Date(System.currentTimeMillis());
      statement.setString(1, username);
      statement.setInt(2, propertyId);
      statement.setDate(3, createDate);
      statement.setDate(4, startDate);
      statement.setDate(5, endDate);
      // Execute the statement
      statement.executeUpdate();

      return new Booking(createDate, startDate, endDate, propertyId, username);
    }

    catch (SQLException e)
    {
      throw new SQLException(e.getMessage());
    }
  }

  @Override public Booking read(Date startDate, int propertyId, String username)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }
      // Prepare the SQL statement
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM booking WHERE start_date = ? AND propertyID = ? AND username = ?");

      statement.setDate(1, startDate);
      statement.setInt(2, propertyId);
      statement.setString(3, username);
      // Execute the statement
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        Date createDate = resultSet.getDate("booking_date");
        Date endDate = resultSet.getDate("end_date");
        Booking booking = new Booking(createDate, startDate, endDate,
            propertyId, username);
        return booking;
      }
      else
      {
        throw new SQLException("Booking not found.");
      }
    }
    catch (SQLException e)
    {
      throw new SQLException(e.getMessage());
    }
  }

  @Override public Booking update(Date startDate, Date endDate, int propertyId,
      String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      // Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      // Check if the property is available for the new dates
      PreparedStatement checkingStatement = connection.prepareStatement(
          "SELECT 1 FROM booking WHERE propertyID = ? "
              + "AND username != ? AND (start_date, end_date) OVERLAPS (?, ?);");
      checkingStatement.setInt(1, propertyId);
      checkingStatement.setString(2, username);
      checkingStatement.setDate(3, startDate);
      checkingStatement.setDate(4, endDate);

      try (ResultSet checkingResultSet = checkingStatement.executeQuery())
      {
        if (checkingResultSet.next())
        {
          throw new SQLException(
              "Property is already booked for the selected dates.");
        }
      }

      // Prepare the SQL statement for updating
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE booking SET end_date = ? WHERE propertyID = ? AND username = ? AND start_date = ?");

      // Set the parameters
      statement.setDate(1, endDate);
      statement.setInt(2, propertyId);
      statement.setString(3, username);
      statement.setDate(4, startDate);

      // Execute the update
      statement.executeUpdate();

      // Retrieve the updated booking
      Booking booking = read(startDate, propertyId, username);

      // Return the updated booking object
      return new Booking(booking.getBookingDate(), startDate, endDate,
          propertyId, username);
    }
    catch (SQLException e)
    {
      throw new SQLException(e.getMessage());
    }
  }

  @Override public void delete(Date startDate, int propertyId, String username)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      // Check if the booking exists
      Booking booking = read(startDate, propertyId, username);
      if (booking == null)
      {
        throw new SQLException("Booking not found.");
      }

      // Prepare the SQL statement
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM booking WHERE start_date = ? AND propertyID = ? AND username = ?");
      statement.setDate(1, startDate);
      statement.setInt(2, propertyId);
      statement.setString(3, username);

      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  @Override public List<BookingHistory> getAllBookingsByProperty(int propertyId)
      throws SQLException
  {
    ArrayList<BookingHistory> bookingHistoryArrayList = new ArrayList<>();
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }
      PreparedStatement statement = connection.prepareStatement(
          "SELECT b.username, u.email, b.start_date, b.end_date, b.booking_date FROM booking b, \"user\" u, property p WHERE b.propertyID = p.propertyID AND u.username = b.username AND b.propertyID=?");
      statement.setInt(1, propertyId);
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next())
      {
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        Date startdate = resultSet.getDate("start_date");
        Date enddate = resultSet.getDate("end_date");
        Date bookingdate = resultSet.getDate("booking_date");
        BookingHistory bookingHistory = new BookingHistory(username, email,
            startdate, enddate, bookingdate);
        bookingHistoryArrayList.add(bookingHistory);
      }
      //  support.firePropertyChange("getAllBookingHistory", null, bookingHistoryArrayList);
      System.out.println(
          "Database query returned " + bookingHistoryArrayList.size()
              + " booking histories for propertyId: " + propertyId);
    }
    return bookingHistoryArrayList;
  }

  @Override public boolean isAvailable(Date startDate, Date endDate, int id)
      throws SQLException
  {
    //Check the connection
    try (Connection connection = getConnection())
    {
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }
    }

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM booking WHERE propertyID = ? AND (start_date, end_date) OVERLAPS (?, ?)");
      statement.setInt(1, id);
      statement.setDate(2, startDate);
      statement.setDate(3, endDate);
      ResultSet resultSet = statement.executeQuery();
      // If the result set is empty, the property is available
      return !resultSet.next();
    }
  }

  public ArrayList<BookingHistory> readPastBookings(String username)
      throws SQLException
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
          "select * from booking b, property p\n"
              + "where p.propertyid = b.propertyid and end_date < current_date and b.username = ?;");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      while (resultSet.next())
      {
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        int propertyId = resultSet.getInt("propertyid");

        double pricePerNight = resultSet.getDouble("pricepernight");

        BookingHistory bookingHistory = new BookingHistory(username, location,
            startDate, endDate, pricePerNight, propertyId);
        bookingHistoryList.add(bookingHistory);
      }
      return bookingHistoryList;
    }
  }

  public ArrayList<BookingHistory> readCurrentBookings(String username)
      throws SQLException
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
          "select * from booking b, property p "
              + "where p.propertyid = b.propertyid and start_date <= current_date\n"
              + "and end_date >= current_date and b.username = ?;");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      while (resultSet.next())
      {
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        int propertyId = resultSet.getInt("propertyid");
        double pricePerNight = resultSet.getDouble("pricepernight");

        BookingHistory bookingHistory = new BookingHistory(username, location,
            startDate, endDate, pricePerNight, propertyId);
        bookingHistoryList.add(bookingHistory);
      }
      return bookingHistoryList;
    }
  }

  public ArrayList<BookingHistory> readFutureBookings(String username)
      throws SQLException
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
          "select * from booking b, property p\n"
              + "where p.propertyid = b.propertyid and start_date > current_date and b.username = ?;");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      while (resultSet.next())
      {
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        double pricePerNight = resultSet.getDouble("pricepernight");
        int propertyId = resultSet.getInt("propertyid");

        BookingHistory bookingHistory = new BookingHistory(username, location,
            startDate, endDate, pricePerNight, propertyId);
        bookingHistoryList.add(bookingHistory);
      }
      return bookingHistoryList;
    }
  }
}
