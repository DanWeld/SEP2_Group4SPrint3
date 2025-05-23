package services.property.security;

import dtos.Property;
import dtos.User;
import services.property.PropertyAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdminPropertyProxy implements PropertyAdminPrivileges, PropertyChangeListener
{
  private final PropertyAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  public AdminPropertyProxy(PropertyAdminPrivileges realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
