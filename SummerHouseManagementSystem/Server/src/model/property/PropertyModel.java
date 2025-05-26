package model.property;

import dtos.Property;
import observer.PropertyChangeSubject;

import java.sql.Date;

/**
 * PropertyModel interface defines the methods for managing properties in the system.
 * It extends PropertyChangeSubject to allow for property change notifications.
 *
 * @author Group 4
 * @version 1.0
 */
public interface PropertyModel extends PropertyChangeSubject
{
  void createProperty(Property property);

  void updateProperty(Property property);

  void deleteProperty(int id);

  void getAllProperties();

  void getAvailableProperties(Date startDate, Date endDate);
}
