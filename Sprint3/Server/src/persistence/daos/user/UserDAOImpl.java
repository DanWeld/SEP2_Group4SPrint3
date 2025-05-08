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

  private UserDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static synchronized UserDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new UserDAOImpl();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  /**
   * Validates password strength according to requirements:
   * - At least 8 characters
   * - At least 1 numeric character
   * - At least 1 uppercase letter
   * - At least 1 lowercase letter
   *
   * @param password The password to validate
   * @return True if the password meets all requirements
   */
  private boolean isStrongPassword(String password)
  {
    if (password == null || password.length() < 8)
    {
      System.out.println("Password validation failed: Less than 8 characters");
      return false;
    }

    boolean hasUpperCase = false;
    boolean hasLowerCase = false;
    boolean hasDigit = false;

    for (char c : password.toCharArray())
    {
      if (Character.isUpperCase(c))
      {
        hasUpperCase = true;
      }
      else if (Character.isLowerCase(c))
      {
        hasLowerCase = true;
      }
      else if (Character.isDigit(c))
      {
        hasDigit = true;
      }

      // If all requirements are met, return immediately
      if (hasUpperCase && hasLowerCase && hasDigit)
      {
        System.out.println("Password validation passed");
        return true;
      }
    }

    // Log which specific requirement failed
    if (!hasUpperCase)
    {
      System.out.println("Password validation failed: No uppercase letter");
    }
    if (!hasLowerCase)
    {
      System.out.println("Password validation failed: No lowercase letter");
    }
    if (!hasDigit)
    {
      System.out.println("Password validation failed: No numeric digit");
    }

    return false;
  }

  @Override public boolean saveUser(User user)
  {
    // Validate password strength
    if (!isStrongPassword(user.getPassword()))
    {
      System.out.println(
          "Registration failed: Password does not meet strength requirements");
      return false;
    }

    // Validate email and username are not empty
    if (user.getEmail() == null || user.getEmail().trim().isEmpty())
    {
      System.out.println("Registration failed: Email is required");
      return false;
    }

    if (user.getUsername() == null || user.getUsername().trim().isEmpty())
    {
      System.out.println("Registration failed: Username is required");
      return false;
    }

    try (Connection connection = getConnection())
    {
      try
      {
        // First, try to create the schema if it doesn't exist
        Statement schemaStatement = connection.createStatement();
        schemaStatement.execute(
            "CREATE SCHEMA IF NOT EXISTS summerhouse_rental_system");

        // Then, try to create the table if it doesn't exist
        Statement tableStatement = connection.createStatement();
        tableStatement.execute(
            "CREATE TABLE IF NOT EXISTS summerhouse_rental_system.u ("
                + "username VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) PRIMARY KEY, "
                + "password VARCHAR(255) NOT NULL, "
                + "isAdmin BOOLEAN NOT NULL DEFAULT false)");
      }
      catch (SQLException e)
      {
        System.out.println("Error creating schema or table: " + e.getMessage());
        // Continue anyway, the table might already exist
      }

      // Check if email already exists
      PreparedStatement checkEmailStatement = connection.prepareStatement(
          "SELECT * FROM user WHERE email = ?");
      checkEmailStatement.setString(1, user.getEmail());
      ResultSet emailResult = checkEmailStatement.executeQuery();
      if (emailResult.next())
      {
        System.out.println(
            "Registration failed: Email already exists - " + user.getEmail());
        return false; // Email already exists
      }

      // Check if username already exists
      PreparedStatement checkUsernameStatement = connection.prepareStatement(
          "SELECT * FROM user WHERE username = ?");
      checkUsernameStatement.setString(1, user.getUsername());
      ResultSet usernameResult = checkUsernameStatement.executeQuery();
      if (usernameResult.next())
      {
        System.out.println("Registration failed: Username already exists - "
            + user.getUsername());
        return false; // Username already exists
      }

      // Insert the new user
      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO user(username, email, password, isAdmin) VALUES(?, ?, ?, ?)");
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getEmail());
      statement.setString(3, user.getPassword());
      statement.setBoolean(4, user.isAdmin());

      int rowsAffected = statement.executeUpdate();
      if (rowsAffected > 0)
      {
        System.out.println("User registered successfully: " + user.getUsername()
            + (user.isAdmin() ? " (ADMIN)" : ""));
        return true;
      }
      else
      {
        System.out.println("Registration failed: No rows affected");
        return false;
      }
    }
    catch (SQLException e)
    {
      System.out.println("Registration error: " + e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  @Override public User getUserByEmail(String email)
  {
    if (email == null || email.trim().isEmpty())
    {
      System.out.println("Login error: Email is required");
      return null;
    }

    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM \"user\" WHERE email = ?");
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next())
      {
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        System.out.println(
            "User found: " + username + (isAdmin ? " (ADMIN)" : ""));
        return new User(username, email, password, isAdmin);
      }
      System.out.println("Login failed: No user found with email - " + email);
      return null;
    }
    catch (SQLException e)
    {
      System.out.println("Login error: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  @Override public User getUserByUsername(String username)
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM \"user\" WHERE username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next())
      {
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        return new User(username, email, password, isAdmin);
      }
      return null;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  public List<User> getAllUsers()
  {
    List<User> users = new ArrayList<>();
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM u");
      ResultSet resultSet = statement.executeQuery();

      while (resultSet.next())
      {
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        users.add(new User(username, email, password, isAdmin));
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return users;
  }

  public boolean updateUser(User user)
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE u SET username = ?, password = ?, isAdmin = ? WHERE email = ?");
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setBoolean(3, user.isAdmin());
      statement.setString(4, user.getEmail());

      int rowsAffected = statement.executeUpdate();
      return rowsAffected > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteUser(String email)
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM u WHERE email = ?");
      statement.setString(1, email);

      int rowsAffected = statement.executeUpdate();
      return rowsAffected > 0;
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Create a new user
   *
   * @param username The username
   * @param email    The email
   * @param password The password
   * @param isAdmin  Admin status
   * @return The newly created user
   * @throws SQLException If an error occurs
   */
  public User create(String username, String email, String password,
      boolean isAdmin) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      try
      {
        // First, try to create the schema if it doesn't exist
        Statement schemaStatement = connection.createStatement();
        schemaStatement.execute(
            "CREATE SCHEMA IF NOT EXISTS summerhouse_rental_system");

        // Then, try to create the table if it doesn't exist
        Statement tableStatement = connection.createStatement();
        tableStatement.execute(
            "CREATE TABLE IF NOT EXISTS summerhouse_rental_system.u ("
                + "username VARCHAR(255) NOT NULL, "
                + "email VARCHAR(255) PRIMARY KEY, "
                + "password VARCHAR(255) NOT NULL, "
                + "isAdmin BOOLEAN NOT NULL DEFAULT false)");
      }
      catch (SQLException e)
      {
        System.out.println("Error creating schema or table: " + e.getMessage());
        // Continue anyway, the table might already exist
      }

      try
      {
        // Check the availability of the email
        PreparedStatement checkEmailStatement = connection.prepareStatement(
            "SELECT * FROM \"user\" WHERE email = ?");
        checkEmailStatement.setString(1, email);
        ResultSet checkingResultSetEmail = checkEmailStatement.executeQuery();
        if (checkingResultSetEmail.next())
        {
          throw new SQLException("Email not available");
        }

        // Check the availability of the username
        PreparedStatement checkUsernameStatement = connection.prepareStatement(
            "SELECT * FROM \"user\" WHERE username = ?");
        checkUsernameStatement.setString(1, username);
        ResultSet checkingResultSetUsername = checkUsernameStatement.executeQuery();
        if (checkingResultSetUsername.next())
        {
          throw new SQLException("Username not available");
        }

        // Insert the user
        PreparedStatement statement = connection.prepareStatement(
            "INSERT INTO \"user\"(username, email, password, isAdmin) Values(?,?,?,?);");
        statement.setString(1, username);
        statement.setString(2, email);
        statement.setString(3, password);
        statement.setBoolean(4, isAdmin);

        int rowsAffected = statement.executeUpdate();
        if (rowsAffected > 0)
        {
          System.out.println("User created successfully: " + username);
          return new User(username, email, password, isAdmin);
        }
        else
        {
          throw new SQLException("Failed to insert user into database");
        }
      }
      catch (SQLException e)
      {
        System.out.println("Error in create user operation: " + e.getMessage());
        e.printStackTrace();
        throw e; // Re-throw the exception to be handled by the caller
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

  @Override public User readByUsername(String username, String password)
      throws SQLException
  {
    return null;
  }

  /**
   * Update a user by email
   *
   * @param user The user to update
   * @throws SQLException If an error occurs
   */
  public void update(User user) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE \"user\" SET username = ?, password = ?, isAdmin = ? WHERE email = ?");
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setBoolean(3, user.isAdmin());
      statement.setString(4, user.getEmail());
      statement.executeUpdate();
    }
  }

  /**
   * Read a user by username
   *
   * @param username The username
   * @return The user if found, null otherwise
   * @throws SQLException If an error occurs
   */
  public User readByUsername(String username) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM u WHERE username = ?");
      statement.setString(1, username);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        return new User(username, email, password, isAdmin);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Read a user by email
   *
   * @param email The email
   * @return The user if found, null otherwise
   * @throws SQLException If an error occurs
   */
  public User readByEmail(String email) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM u WHERE email = ?");
      statement.setString(1, email);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        return new User(username, email, password, isAdmin);
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Read users by admin status
   *
   * @param isAdmin Admin status to filter by
   * @return List of users with the specified admin status
   * @throws SQLException If an error occurs
   */
  public List<User> readByIsAdmin(boolean isAdmin) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM u WHERE isAdmin = ?");
      statement.setBoolean(1, isAdmin);
      ResultSet resultSet = statement.executeQuery();
      ArrayList<User> result = new ArrayList<>();
      while (resultSet.next())
      {
        String username = resultSet.getString("username");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        User user = new User(username, email, password, isAdmin);
        result.add(user);
      }
      return result;
    }
  }

  /**
   * Delete a user by email
   *
   * @param user The user to delete
   * @throws SQLException If an error occurs
   */
  public void delete(User user) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "DELETE FROM u WHERE email = ?");
      statement.setString(1, user.getEmail());
      statement.executeUpdate();
    }
  }

  /**
   * Authenticate user with email and password
   *
   * @param email    The user's email
   * @param password The user's password
   * @return The User if authentication is successful, null otherwise
   */
  public User authenticateUser(String email, String password)
  {
    // Validate input
    if (email == null || email.trim().isEmpty() || password == null
        || password.trim().isEmpty())
    {
      System.out.println(
          "Authentication failed: Email and password are required");
      return null;
    }

    try (Connection connection = getConnection())
    {
      // Check email and password together
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM u WHERE email = ? AND password = ?");
      statement.setString(1, email);
      statement.setString(2, password);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next())
      {
        String username = resultSet.getString("username");
        boolean isAdmin = resultSet.getBoolean("isAdmin");
        System.out.println("Authentication successful: " + username);
        return new User(username, email, password, isAdmin);
      }
      else
      {
        System.out.println("Authentication failed: Invalid credentials");
        return null;
      }
    }
    catch (SQLException e)
    {
      System.out.println("Authentication error: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
}
