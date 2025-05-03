package persistence.daos.properties;

import dtos.Facilities;
import dtos.Property;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface PropertyDAO
{
  Property create(int id, String location, double pricePerNight,
      Facilities facilities) throws SQLException;
  Property readByID(int id) throws SQLException;
  List<Property> readByLocation(String location) throws SQLException;
  void update(Property property) throws SQLException;
  void delete(int id) throws SQLException;
  List<Property> getAvailableProperties(Date startDate, Date endDate) throws SQLException;
}
