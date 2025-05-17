package services.property;

import observer.PropertyChangeSubject;

public interface PropertyWriter extends PropertyChangeSubject
{
  void createProperty(dtos.Property property);

  void updateProperty(dtos.Property property);

  void deleteProperty(int id);

  void getAllProperties();
}
