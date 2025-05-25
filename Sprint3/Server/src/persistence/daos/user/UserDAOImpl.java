package persistence.daos.user;

import dtos.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of UserDAO that uses a PostgreSQL database for storage
 */
public class UserDAOImpl implements UserDAO
{
  private static UserDAOImpl instance;

  /**
   * Private constructor to prevent instantiation
   *
   * @throws SQLException If an error occurs while registering the driver
   */
  private UserDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * Get the singleton instance of UserDAOImpl
   *
   * @return The singleton instance
   * @throws SQLException If an error occurs while registering the driver
   */
  public static synchronized UserDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new UserDAOImpl();
    }
    return instance;
  }

  /**
   * Get a connection to the PostgreSQL database
   *
   * @return A Connection object
   * @throws SQLException If an error occurs while connecting to the database
   */
  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  /**
   * Create a new user
   *
   * @param username The username
   * @param email    The email
   * @param password The password
   * @return The newly created user
   * @throws SQLException If an error occurs
   */
  public User create(String username, String email, String password)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      try
      {
        // Check the availability of the email
        PreparedStatement checkEmailStatement = connection.prepareStatement(
            "SELECT * FROM \"user\" WHERE email = ?");
        checkEmailStatement.setString(1, email);
        ResultSet checkingResultSetEmail = checkEmailStatement.executeQuery();
        if (checkingResultSetEmail.next())
        {
          throw new SQLException(
              "Email has been registered, try to log in instead");
        }

        // Check the availability of the username
        PreparedStatement checkUsernameStatement = connection.prepareStatement(
            "SELECT * FROM \"user\" WHERE username = ?");
        checkUsernameStatement.setString(1, username);
        ResultSet checkingResultSetUsername = checkUsernameStatement.executeQuery();
        if (checkingResultSetUsername.next())
        {
          throw new SQLException("Username has been taken, try another one");
        }

        // Insert the user
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO \"user\"(username, email, password, isAdmin) Values(?,?,?,?);");
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, password);
        statement.setBoolean(4, false);

        int affectedRows = statement.executeUpdate();
        if (affectedRows == 0)
        {
          throw new SQLException("Failed to create user");
        }

        return new User(username, email, password, false);
      }
      catch (SQLException e)
      {
        throw new SQLException(e.getMessage());
      }
    }
  }

  /**
   * Read a user by email and password (for authentication)
   *
   * @param email    The email
   * @param password The password
   * @return The user if found
   * @throws SQLException If an error occurs
   */
  public User read(String email, String password) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      // Check if the email exists
      PreparedStatement checkingStatement = connection.prepareStatement(
          "SELECT * FROM \"user\" WHERE email = ?");
      checkingStatement.setString(1, email);
      ResultSet checkingResultSet = checkingStatement.executeQuery();
      if (!checkingResultSet.next())
      {
        throw new SQLException("Email does not exist");
      }

      // Check email and password together
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM \"user\" WHERE email = ? AND password = ?");
      statement.setString(1, email);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String username = resultSet.getString("username");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        return new User(username, email, password, isAdmin);
      }
      else
      {
        throw new SQLException("Password and Email do not match");
      }
    }
  }

  /**
   * Update a user (only password can be updated)
   * @param user The user to update
   * @throws SQLException If an error occurs
   */
  public void update(User user) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE \"user\" SET password = ? WHERE username = ?;");
      statement.setString(1, user.getPassword());
      statement.setString(2, user.getUsername());

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0)
      {
        throw new SQLException("Failed to update user");
      }
    }
  }

  /**
   * Delete a user by username
   *
   * @param username The username of the user to delete
   * @throws SQLException If an error occurs
   */
  public void delete(String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM \"user\" WHERE username = ?;");
      statement.setString(1, username);

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0)
      {
        throw new SQLException("Failed to delete user");
      }
    }
  }

  /**
   * Promote a user to admin by username
   *
   * @param username The username of the user to promote
   * @throws SQLException If an error occurs
   */
  public void promoteToAdmin(String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE \"user\" SET isAdmin = ? WHERE username = ?;");
      statement.setBoolean(1, true);
      statement.setString(2, username);

      int affectedRows = statement.executeUpdate();
      if (affectedRows == 0)
      {
        throw new SQLException("Failed to promote user to admin");
      }
    }
  }

  /**
   * Get all users from the database
   *
   * @return A list of all users
   * @throws SQLException If an error occurs
   */
  public List<User> getAllUsers() throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM \"user\";");
      ResultSet resultSet = statement.executeQuery();

      List<User> users = new ArrayList<>();
      while (resultSet.next())
      {
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        users.add(new User(username, email, password, isAdmin));
      }
      return users;
    }
  }
}
