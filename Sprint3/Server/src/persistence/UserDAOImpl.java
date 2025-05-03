package persistence;

import dtos.User;

import java.sql.*;

public class UserDAOImpl implements UserDAO {
    private static UserDAOImpl instance;

    private UserDAOImpl() throws SQLException {
        DriverManager.registerDriver(new org.postgresql.Driver());
    }
    
    public static synchronized UserDAOImpl getInstance() throws SQLException {
        if(instance == null) {
            instance = new UserDAOImpl();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres?currentSchema=summerhouse_rental_system", "postgres", "viaviavia");
        return connection;
    }
    
    private void ensureTableExists() throws SQLException {
        try (Connection connection = getConnection()) {
            try {
                // First, try to create the schema if it doesn't exist
                Statement schemaStatement = connection.createStatement();
                schemaStatement.execute("CREATE SCHEMA IF NOT EXISTS summerhouse_rental_system");
                
                // Then, try to create the table if it doesn't exist
                Statement tableStatement = connection.createStatement();
                tableStatement.execute("CREATE TABLE IF NOT EXISTS summerhouse_rental_system.u (" +
                                     "username VARCHAR(255) NOT NULL, " +
                                     "email VARCHAR(255) PRIMARY KEY, " +
                                     "password VARCHAR(255) NOT NULL, " +
                                     "isAdmin BOOLEAN NOT NULL DEFAULT false)");
            } catch (SQLException e) {
                System.out.println("Error creating schema or table: " + e.getMessage());
                // Continue anyway, the table might already exist
            }
        }
    }
      @Override
    public User create(String username, String email, String password, boolean isAdmin) throws SQLException {
        ensureTableExists();
        try(Connection connection = getConnection()){
            // Check the availability of the email
            PreparedStatement checkEmailStatement = connection.prepareStatement("SELECT * FROM u WHERE email = ?");
            checkEmailStatement.setString(1, email);
            ResultSet checkingResultSetEmail = checkEmailStatement.executeQuery();
            if (checkingResultSetEmail.next())
            {
                throw new SQLException("Email not available");
            }

            // Check the availability of the username
            PreparedStatement checkUsernameStatement = connection.prepareStatement("SELECT * FROM u WHERE username = ?");
            checkUsernameStatement.setString(1, username);
            ResultSet checkingResultSetUsername = checkUsernameStatement.executeQuery();
            if (checkingResultSetUsername.next())
            {
                throw new SQLException("Username not available");
            }

            // Create the user
            PreparedStatement statement =
                    connection.prepareStatement("INSERT INTO u(username, email, password, isAdmin) Values(?,?,?,?);");
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setBoolean(4, isAdmin);
            statement.executeUpdate();
            return new User(username, email, password, isAdmin);
        }
    }
    
    @Override
    public boolean saveUser(User user) {
        try {
            create(user.getUsername(), user.getEmail(), user.getPassword(), user.isAdmin());
            return true;
        } catch (SQLException e) {
            System.out.println("Error saving user: " + e.getMessage());
            return false;
        }
    }    @Override
    public User read(String email, String password) throws SQLException {
        try(Connection connection = getConnection()) {
            // Check if the email exists
            PreparedStatement checkingStatement = connection.prepareStatement("SELECT * FROM u WHERE email = ?");
            checkingStatement.setString(1, email);
            ResultSet checkingResultSet = checkingStatement.executeQuery();
            if (!checkingResultSet.next())
            {
               throw new SQLException("Email does not exist");
            }

            // Check email and password together
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM u WHERE email = ? AND password = ?");
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
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
    
    @Override
    public User readByUsername(String username, String password) throws SQLException {
        try(Connection connection = getConnection()) {
            // Check if the username exists
            PreparedStatement checkingStatement = connection.prepareStatement("SELECT * FROM u WHERE username = ?");
            checkingStatement.setString(1, username);
            ResultSet checkingResultSet = checkingStatement.executeQuery();
            if (!checkingResultSet.next())
            {
               throw new SQLException("Username does not exist");
            }

            // Check username and password together
            PreparedStatement statement =
                    connection.prepareStatement("SELECT * FROM u WHERE username = ? AND password = ?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String email = resultSet.getString("email");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                return new User(username, email, password, isAdmin);
            }
            else
            {
                throw new SQLException("Password and Username do not match");
            }
        }
    }
    
    @Override
    public User getUserByEmail(String email) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM u WHERE email = ?");
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                return new User(username, email, password, isAdmin);
            } else {
                return null;
            }
        }
    }
    
    @Override
    public User getUserByUsername(String username) throws SQLException {
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM u WHERE username = ?");
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                boolean isAdmin = resultSet.getBoolean("isAdmin");
                return new User(username, email, password, isAdmin);
            } else {
                return null;
            }
        }
    }@Override
    public void update(User user) throws SQLException {
        try(Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE u SET username = ?, password = ? WHERE email = ?");
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.executeUpdate();
        }
    }
}
