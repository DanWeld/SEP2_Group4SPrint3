package services.property.security;

import dtos.ErrorResponse;
import dtos.Property;
import dtos.User;
import services.property.PropertyWriter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdminPropertyWriterProxy implements PropertyWriter
{
  private final PropertyWriter realWriter;
  private final User user;

  public AdminPropertyWriterProxy(PropertyWriter realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
  }

  private void checkAdmin()
  {
    if (!user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  @Override public void createProperty(Property property)
  {
    checkAdmin();
    realWriter.createProperty(property);
  }

  @Override public void updateProperty(Property property)
  {
    checkAdmin();
    realWriter.updateProperty(property);
  }

  @Override public void deleteProperty(int id)
  {
    checkAdmin();
    realWriter.deleteProperty(id);
  }

  @Override public void getAllProperties()
  {
    checkAdmin();
    realWriter.getAllProperties();
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    realWriter.addPropertyChangeListener(listener);
  }
}
