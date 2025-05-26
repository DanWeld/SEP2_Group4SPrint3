package persistence.daos.properties;

import dtos.Facilities;
import dtos.Property;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for Property Data Access Object (DAO)
 * This interface defines methods for creating, reading, updating,
 * deleting, and retrieving properties from a database.
 * @ author Group 4
 * * @ version 1.0
 */
public interface PropertyDAO
{
  Property create(int id, String location, double pricePerNight,
      Facilities facilities) throws SQLException;
  Property readByID(int id) throws SQLException;
  List<Property> readAll() throws SQLException;
  void update(Property property) throws SQLException;
  void delete(int id) throws SQLException;
  List<Property> getAvailableProperties(Date startDate, Date endDate) throws SQLException;
}
