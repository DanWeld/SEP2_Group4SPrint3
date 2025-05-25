package services.property;

import observer.PropertyChangeSubject;

/**
 * PropertyAdminPrivileges interface defines the methods for managing properties
 * with administrative privileges. It extends PropertyChangeSubject to allow
 * listeners to be notified of property changes.
 */
public interface PropertyAdminPrivileges extends PropertyChangeSubject
{
  void createProperty(dtos.Property property);

  void updateProperty(dtos.Property property);

  void deleteProperty(int id);

  void getAllProperties();
}
