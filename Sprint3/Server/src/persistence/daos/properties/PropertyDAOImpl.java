package persistence.daos.properties;

import dtos.Facilities;
import dtos.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of PropertyDAO that uses a PostgreSQL database for storage
 */
public class PropertyDAOImpl implements PropertyDAO
{
  private static PropertyDAOImpl instance;

  private PropertyDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  /**
   * Get the singleton instance of PropertyDAOImpl
   *
   * @return The singleton instance
   * @throws SQLException If an error occurs while registering the driver
   */
  public static synchronized PropertyDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new PropertyDAOImpl();
    }
    return instance;
  }

  /**
   * Get a connection to the PostgreSQL database
   *
   * @return A Connection object
   * @throws SQLException If an error occurs while establishing the connection
   */
  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

  /**
   * Create a new property in the database
   *
   * @param id            The ID of the property (not used, as ID is auto-generated)
   * @param location      The location of the property
   * @param pricePerNight The price per night for the property
   * @param facilities    The facilities available at the property
   * @return The newly created Property object
   * @throws SQLException If an error occurs while creating the property
   */
  @Override public Property create(int id, String location,
      double pricePerNight, Facilities facilities) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      //Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      PreparedStatement statement = connection.prepareStatement(
          "INSERT INTO property (location, pricePerNight, kitchen, internet, swimmingPool, dishwasher, laundryMachine) VALUES (?, ?, ?, ?, ?, ?, ?)",
          PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, location);
      statement.setDouble(2, pricePerNight);
      statement.setBoolean(3, facilities.kitchen());
      statement.setBoolean(4, facilities.internet());
      statement.setBoolean(5, facilities.swimmingPool());
      statement.setBoolean(6, facilities.dishwasher());
      statement.setBoolean(7, facilities.laundryMachine());
      statement.executeUpdate();
      ResultSet keys = statement.getGeneratedKeys();
      if (keys.next())
      {
        return new Property(keys.getInt("propertyid"), location, pricePerNight,
            facilities);
      }
      else
      {
        throw new SQLException("Failed to create property, no ID generated.");
      }
    }
    catch (SQLException e)
    {
      e.printStackTrace();
      throw e;
    }
  }

  /**
   * Read a property by its ID from the database
   *
   * @param id The ID of the property
   * @return The Property object if found, null otherwise
   * @throws SQLException If an error occurs while reading the property
   */
  @Override public Property readByID(int id) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "Select * FROM property Where propertyid = ?");
      statement.setInt(1, id);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next())
      {
        String location = resultSet.getString("location");
        double pricePerNight = resultSet.getDouble("pricepernight");
        boolean kitchen = resultSet.getBoolean("kitchen");
        boolean internet = resultSet.getBoolean("internet");
        boolean swimmingPool = resultSet.getBoolean("swimmingPool");
        boolean dishWasher = resultSet.getBoolean("dishWasher");
        boolean laundryMachine = resultSet.getBoolean("laundryMachine");
        Property property = new Property(id, location, pricePerNight,
            new Facilities(kitchen, internet, dishWasher, laundryMachine,
                swimmingPool));
        return property;
      }
      else
      {
        return null;
      }
    }
  }

  /**
   * Read all properties from the database
   *
   * @return A list of all Property objects
   * @throws SQLException If an error occurs while reading the properties
   */
  @Override public List<Property> readAll()
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM property");

      ResultSet resultSet = statement.executeQuery();
      List<Property> properties = new ArrayList<>();
      while (resultSet.next())
      {
        int id = resultSet.getInt("propertyid");
        String loc = resultSet.getString("location");
        double pricePerNight = resultSet.getDouble("pricepernight");
        boolean kitchen = resultSet.getBoolean("kitchen");
        boolean internet = resultSet.getBoolean("internet");
        boolean swimmingPool = resultSet.getBoolean("swimmingPool");
        boolean dishWasher = resultSet.getBoolean("dishWasher");
        boolean laundryMachine = resultSet.getBoolean("laundryMachine");
        Property property = new Property(id, loc, pricePerNight,
            new Facilities(kitchen, internet, dishWasher, laundryMachine,
                swimmingPool));
        properties.add(property);
      }
      if (properties.size() > 0)
      {
        return properties;
      }
      else
      {
        throw new SQLException("No properties found in the database.");
      }
    }
  }

  /**
   * Update an existing property in the database
   *
   * @param property The Property object containing updated information
   * @throws SQLException If an error occurs while updating the property
   */
  @Override public void update(Property property) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      // Check if the connection is established
      if (connection == null || connection.isClosed())
      {
        throw new SQLException(
            "Failed to establish a connection to the database.");
      }

      // Check if the property exists
      Property existingProperty = readByID(property.id());
      if (existingProperty == null)
      {
        throw new SQLException("Property with ID " + property.id() + " does not exist.");
      }

      // Update the property in the database
      PreparedStatement statement = connection.prepareStatement(
          "UPDATE property SET location = ?, pricePerNight = ?, kitchen = ?, internet = ?, swimmingPool = ?, dishwasher = ?, laundryMachine = ? WHERE propertyid = ?");
      statement.setString(1, property.location());
      statement.setDouble(2, property.pricePerNight());
      statement.setBoolean(3, property.facilities().kitchen());
      statement.setBoolean(4, property.facilities().internet());
      statement.setBoolean(5, property.facilities().swimmingPool());
      statement.setBoolean(6, property.facilities().dishwasher());
      statement.setBoolean(7, property.facilities().laundryMachine());
      statement.setInt(8, property.id());
      int rowsUpdated = statement.executeUpdate();

      if (rowsUpdated == 0)
      {
        throw new SQLException("Failed to update property with ID " + property.id());
      }
    }
  }

  /**
   * Delete a property by its ID from the database
   *
   * @param id The ID of the property to delete
   * @throws SQLException If an error occurs while deleting the property
   */
  @Override public void delete(int id) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      if (readByID(id) != null)
      {
        PreparedStatement statement = connection.prepareStatement(
            "DELETE FROM property WHERE propertyid = ?");
        statement.setInt(1, id);
        statement.executeUpdate();
      }
      else
      {
        throw new SQLException("Property with ID: " + id + " does not exist.");
      }
    }
  }

  /**
   * Get all properties that are available for booking within a specified date range
   *
   * @param startDate The start date of the booking
   * @param endDate   The end date of the booking
   * @return A list of available Property objects
   * @throws SQLException If an error occurs while fetching available properties
   */
  @Override public List<Property> getAvailableProperties(Date startDate,
      Date endDate) throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM property p WHERE NOT EXISTS (SELECT * FROM booking b WHERE b.propertyID = p.propertyID AND (b.start_date, b.end_date) OVERLAPS (?, ?))");
      statement.setDate(1, startDate);
      statement.setDate(2, endDate);
      ResultSet resultSet = statement.executeQuery();

      ArrayList<Property> properties = new ArrayList<>();
      while (resultSet.next())
      {
        int id = resultSet.getInt("propertyid");
        String location = resultSet.getString("location");
        double pricePerNight = resultSet.getDouble("pricepernight");
        boolean kitchen = resultSet.getBoolean("kitchen");
        boolean internet = resultSet.getBoolean("internet");
        boolean swimmingPool = resultSet.getBoolean("swimmingPool");
        boolean dishWasher = resultSet.getBoolean("dishWasher");
        boolean laundryMachine = resultSet.getBoolean("laundryMachine");
        Property property = new Property(id, location, pricePerNight,
            new Facilities(kitchen, internet, dishWasher, laundryMachine,
                swimmingPool));
        properties.add(property);
      }
      return properties;
    }
  }
}
