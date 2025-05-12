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
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=summerhouse_rental_system",
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
      e.printStackTrace();
      throw e;
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
      e.printStackTrace();
      throw e;
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
      e.printStackTrace();
      throw e;
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

  @Override public List<Booking> getAllBookings() throws SQLException
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
          "SELECT * FROM booking");
      ResultSet resultSet = statement.executeQuery();

      ArrayList<Booking> bookings = new ArrayList<>();

      while (resultSet.next())
      {
        Date createDate = resultSet.getDate("booking_date");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        int propertyId = resultSet.getInt("propertyID");
        String username = resultSet.getString("username");
        Booking booking = new Booking(createDate, startDate, endDate,
            propertyId, username);
        bookings.add(booking);
      }
      return bookings;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw e;
    }
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

      System.out.println("Executing readPastBookings for username: " + username);

      // Implement the logic to retrieve booking history from the database
      PreparedStatement statement = connection.prepareStatement(
          "select * from booking b, property p\n"
              + "where p.propertyid = b.propertyid and end_date < current_date and b.username = ?;");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      int count = 0;      
      while (resultSet.next())
      {
        count++;
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        int propertyId = resultSet.getInt("propertyid");
        double pricePerNight = resultSet.getDouble("pricepernight");

        System.out.println(" Found current booking - Location: " + location +
                         ", Start: " + startDate + 
                         ", End: " + endDate + 
                         ", PropertyId: " + propertyId);

        BookingHistory bookingHistory = new BookingHistory(username, location,
            startDate, endDate, pricePerNight, propertyId);
        bookingHistoryList.add(bookingHistory);
      }
      
      System.out.println(" Total current bookings found: " + count);
      
      // Check if we have an empty result
      if (count == 0) {
        // Let's try to find out if there are any bookings at all for this user
        PreparedStatement checkStatement = connection.prepareStatement(
            "SELECT COUNT(*) FROM booking WHERE username = ?");
        checkStatement.setString(1, username);
        ResultSet checkResult = checkStatement.executeQuery();
        
        if (checkResult.next()) {
          int totalBookings = checkResult.getInt(1);
          System.out.println(" Total bookings for user " + username + ": " + totalBookings);
        }
      }
      
      return bookingHistoryList;
    }
  }

  /**
   * Helper method to check if a date is between start and end dates (inclusive)
   */
  private boolean isBetween(java.util.Date date, Date start, Date end) {
    long checkTime = date.getTime();
    return checkTime >= start.getTime() && checkTime <= end.getTime();
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

      System.out.println(" Executing readCurrentBookings for username: " + username);
      
      // Calculate today's date
      java.util.Date today = new java.util.Date();
      System.out.println(" Current date: " + today);
      
      // Extra debugging - List all bookings for this user regardless of date
      try {
        PreparedStatement allBookingsStatement = connection.prepareStatement(
            "SELECT b.booking_id, b.start_date, b.end_date, p.location, p.propertyid " +
            "FROM booking b JOIN property p ON b.propertyid = p.propertyid " +
            "WHERE b.username = ? ORDER BY b.start_date");
        allBookingsStatement.setString(1, username);
        
        ResultSet allBookings = allBookingsStatement.executeQuery();
        System.out.println(" DEBUG: All bookings for user " + username + ":");
        int allCount = 0;
        while (allBookings.next()) {
          allCount++;
          int bookingId = allBookings.getInt("booking_id");
          Date start = allBookings.getDate("start_date");
          Date end = allBookings.getDate("end_date");
          String loc = allBookings.getString("location");
          int propId = allBookings.getInt("propertyid");
          
          System.out.println("   " + allCount + ". Booking #" + bookingId + 
                           ": " + loc + " (ID: " + propId + "), " + 
                           start + " to " + end + 
                           (isBetween(today, start, end) ? " - CURRENT" : ""));
        }
        
        if (allCount == 0) {
          System.out.println("   No bookings found for this user");
        }
      } catch (Exception e) {
        System.err.println("ERROR in debug query: " + e.getMessage());
      }
      
      // Implement the logic to retrieve booking history from the database
      // The query finds bookings where current_date is between start_date and end_date
      String query = "SELECT b.*, p.* FROM booking b " +
                    "JOIN property p ON b.propertyid = p.propertyid " +
                    "WHERE b.username = ? " +
                    "AND b.start_date <= CURRENT_DATE " +
                    "AND b.end_date >= CURRENT_DATE";
      
      System.out.println(" SQL Query: " + query);
      
      PreparedStatement statement = connection.prepareStatement(query);
      statement.setString(1, username);
      System.out.println(" Executing with parameter: " + username);
      
      ResultSet resultSet = statement.executeQuery();
      System.out.println(" Query executed successfully");

      ArrayList<BookingHistory> bookingHistoryList = new ArrayList<>();
      int count = 0;
      
      while (resultSet.next())
      {
        count++;
        String location = resultSet.getString("location");
        Date startDate = resultSet.getDate("start_date");
        Date endDate = resultSet.getDate("end_date");
        int propertyId = resultSet.getInt("propertyid");
        double pricePerNight = resultSet.getDouble("pricepernight");

        System.out.println(" Found current booking - Location: " + location +
                         ", Start: " + startDate + 
                         ", End: " + endDate + 
                         ", PropertyId: " + propertyId);

        BookingHistory bookingHistory = new BookingHistory(username, location,
            startDate, endDate, pricePerNight, propertyId);
        bookingHistoryList.add(bookingHistory);
      }
      
      System.out.println(" Total current bookings found: " + count);
      
      // Check if we have an empty result
      if (count == 0) {
        // Let's try to find out if there are any bookings at all for this user
        PreparedStatement checkStatement = connection.prepareStatement(
            "SELECT COUNT(*) FROM booking WHERE username = ?");
        checkStatement.setString(1, username);
        ResultSet checkResult = checkStatement.executeQuery();
        
        if (checkResult.next()) {
          int totalBookings = checkResult.getInt(1);
          System.out.println(" Total bookings for user " + username + ": " + totalBookings);
        }
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
  
  @Override
  public boolean updateEndDate(int propertyId, Date currentEndDate, Date newEndDate, String username) throws SQLException {
    try (Connection connection = getConnection()) {
      // Check if the connection is established
      if (connection == null || connection.isClosed()) {
        throw new SQLException("Failed to establish a connection to the database.");
      }
      
      System.out.println("DEBUG: updateEndDate - Starting with propertyId=" + propertyId + 
                       ", currentEndDate=" + currentEndDate + 
                       ", newEndDate=" + newEndDate + 
                       ", username=" + username);
      
      // First, check if the booking with the given parameters exists
      // However, we need to be flexible with the end_date match as it might not be exactly identical
      // Let's find the most appropriate booking based on propertyId and username
      PreparedStatement checkStatement = connection.prepareStatement(
          "SELECT * FROM booking WHERE propertyID = ? AND username = ? " +
          "ORDER BY ABS(EXTRACT(EPOCH FROM (end_date - ?))) ASC LIMIT 1");
      checkStatement.setInt(1, propertyId);
      checkStatement.setString(2, username);
      checkStatement.setDate(3, currentEndDate);
      
      ResultSet checkResult = checkStatement.executeQuery();
      if (!checkResult.next()) {
        System.out.println("DEBUG: No matching booking found for propertyId=" + propertyId + 
                          ", username=" + username);
        // Booking not found
        return false;
      }
      
      // Print the found booking for debugging
      int bookingId = checkResult.getInt("booking_id");
      Date actualEndDate = checkResult.getDate("end_date");
      Date actualStartDate = checkResult.getDate("start_date");
      
      System.out.println("DEBUG: Found booking with id=" + bookingId + 
                       ", start_date=" + actualStartDate + 
                       ", end_date=" + actualEndDate);
      
      // Check if the property is available for the extended period
      // We need to make sure no other booking exists for this property between actualEndDate and newEndDate
      PreparedStatement availabilityStatement = connection.prepareStatement(
          "SELECT * FROM booking WHERE propertyID = ? AND username != ? AND " +
          "(start_date, end_date) OVERLAPS (?, ?) AND booking_id != ?");
      availabilityStatement.setInt(1, propertyId);
      availabilityStatement.setString(2, username);
      availabilityStatement.setDate(3, actualEndDate);
      availabilityStatement.setDate(4, newEndDate);
      availabilityStatement.setInt(5, bookingId);
      
      ResultSet availabilityResult = availabilityStatement.executeQuery();
      if (availabilityResult.next()) {
        System.out.println("DEBUG: Property is not available for extension - found conflicting booking");
        // Property is not available for extension
        return false;
      }
      
      // Update the booking with the new end date - using booking ID for precision
      PreparedStatement updateStatement = connection.prepareStatement(
          "UPDATE booking SET end_date = ? WHERE booking_id = ?");
      updateStatement.setDate(1, newEndDate);
      updateStatement.setInt(2, bookingId);
      
      System.out.println("DEBUG: Executing update for booking_id=" + bookingId + 
                        " to set end_date to " + newEndDate);
      
      int rowsAffected = updateStatement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    }
  }
}
