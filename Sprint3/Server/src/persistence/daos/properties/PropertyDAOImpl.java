package persistence.daos.properties;

import dtos.Facilities;
import dtos.Property;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PropertyDAOImpl implements PropertyDAO
{
  private static PropertyDAOImpl instance;

  private PropertyDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  public static synchronized PropertyDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new PropertyDAOImpl();
    }
    return instance;
  }

  private Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5433/postgres?currentSchema=summerhouse_rental_system",
        "postgres", "viaviavia");
  }

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
        System.out.println("id: " + keys.getInt("propertyid"));
        return new Property(keys.getInt("propertyid"), location, pricePerNight,
            true, facilities);
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
        Property property = new Property(id, location, pricePerNight, true,
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

  @Override public List<Property> readByLocation(String location)
      throws SQLException
  {
    try (Connection connection = getConnection())
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT * FROM property WHERE location ILIKE ?");
      statement.setString(1, "%" + location + "%");
      ResultSet resultSet = statement.executeQuery();
      ArrayList<Property> properties = new ArrayList<>();
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
        Property property = new Property(id, loc, pricePerNight, true,
            new Facilities(kitchen, internet, dishWasher, laundryMachine,
                swimmingPool));
        properties.add(property);
      }
      return properties;
    }
  }

  @Override public void update(Property property) throws SQLException
  {
    try (Connection connection = getConnection())
    {
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
      statement.executeUpdate();
    }
  }

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
        System.out.println("Property with id " + id + " does not exist.");
      }
    }
  }

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
        Property property = new Property(id, location, pricePerNight, true,
            new Facilities(kitchen, internet, dishWasher, laundryMachine,
                swimmingPool));
        properties.add(property);
      }
      return properties;
    }
  }
}
