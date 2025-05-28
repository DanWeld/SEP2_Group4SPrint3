package persistence.daos.bookings;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BookingDAO that uses a PostgreSQL database for storage
 */
public class BookingDAOImpl implements BookingDAO
{
  private static BookingDAOImpl instance;

  /**
   * Private constructor to prevent instantiation
   *
   * @throws SQLException If an error occurs while registering the driver
   */
  private BookingDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * Get the singleton instance of BookingDAOImpl
   *
   * @return The singleton instance of BookingDAOImpl
   * @throws SQLException If an error occurs while getting the connection
   */
  public static synchronized BookingDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new BookingDAOImpl();
    }
    return instance;
  }

  /**
   * Get a connection to the PostgreSQL database
   *
   * @return A Connection object to the database
   * @throws SQLException If an error occurs while getting the connection
   */
  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  /**
   * Create a new booking
   *
   * @param startDate  The start date of the booking
   * @param endDate    The end date of the booking
   * @param propertyId The ID of the property being booked
   * @param username   The username of the user making the booking
   * @return The newly created Booking object
   * @throws SQLException If an error occurs while creating the booking
   */
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

  /**
   * Read a booking by start date, property ID, and username
   *
   * @param startDate  The start date of the booking
   * @param propertyId The ID of the property being booked
   * @param username   The username of the user who made the booking
   * @return The Booking object if found
   * @throws SQLException If an error occurs while reading the booking
   */
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

  /**
   * Update an existing booking
   *
   * @param startDate  The start date of the booking to update
   * @param endDate    The new end date for the booking
   * @param propertyId The ID of the property being booked
   * @param username   The username of the user who made the booking
   * @return The updated Booking object
   * @throws SQLException If an error occurs while updating the booking
   */
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
      int rowsUpdated = statement.executeUpdate();
      if (rowsUpdated == 0)
      {
        throw new SQLException("No booking found to update.");
      }

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

  /**
   * Delete a booking by start date, property ID, and username
   *
   * @param startDate  The start date of the booking to delete
   * @param propertyId The ID of the property being booked
   * @param username   The username of the user who made the booking
   * @throws SQLException If an error occurs while deleting the booking
   */
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

  /**
   * Get all bookings for a specific property
   *
   * @param propertyId The ID of the property
   * @return A list of BookingHistory objects for the specified property
   * @throws SQLException If an error occurs while retrieving the bookings
   */
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
    }
    return bookingHistoryArrayList;
  }

  /**
   * Check if a property is available for booking within a specified date range
   *
   * @param startDate The start date of the booking
   * @param endDate   The end date of the booking
   * @param id        The ID of the property being checked
   * @return true if the property is available, false otherwise
   * @throws SQLException If an error occurs while checking availability
   */
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

  /**
   * Read past bookings for a specific user
   *
   * @param username The username of the user
   * @return A list of BookingHistory objects for past bookings
   * @throws SQLException If an error occurs while retrieving the bookings
   */
  public List<BookingHistory> readPastBookings(String username)
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

  /**
   * Read current bookings for a specific user
   *
   * @param username The username of the user
   * @return A list of BookingHistory objects for current bookings
   * @throws SQLException If an error occurs while retrieving the bookings
   */
  public List<BookingHistory> readCurrentBookings(String username)
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

  /**
   * Read current bookings for a specific user
   *
   * @param username The username of the user
   * @return A list of BookingHistory objects for current bookings
   * @throws SQLException If an error occurs while retrieving the bookings
   */
  public List<BookingHistory> readFutureBookings(String username)
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
          "SELECT * FROM booking b "
              + "JOIN property p ON p.propertyid = b.propertyid "
              + "WHERE b.start_date > CURRENT_DATE AND b.username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      List<BookingHistory> bookingHistoryList = new ArrayList<>();
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
