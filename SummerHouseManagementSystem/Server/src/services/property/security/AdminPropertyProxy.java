package services.property.security;

import dtos.Property;
import dtos.User;
import services.property.PropertyAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * AdminPropertyProxy is a security proxy that checks if the user has admin privileges
 * before allowing access to property management methods.
 */
public class AdminPropertyProxy
    implements PropertyAdminPrivileges, PropertyChangeListener
{
  private final PropertyAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  /**
   * Constructs an AdminPropertyProxy with the given real writer and user.
   *
   * @param realWriter the real property admin privileges implementation
   * @param user       the user whose privileges are being checked
   */
  public AdminPropertyProxy(PropertyAdminPrivileges realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
  }

  /**
   * Checks if the user has admin privileges.
   *
   * @throws SecurityException if the user does not have admin privileges
   */
  private void checkAdmin()
  {
    if (!user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  /**
   * Checks if the user has admin privileges before allowing access to property management methods.
   *
   * @param property the property to be created
   */
  @Override public void createProperty(Property property)
  {
    checkAdmin();
    realWriter.createProperty(property);
  }

  /**
   * Checks if the user has admin privileges before allowing access to property management methods.
   *
   * @param property the property to be updated
   */
  @Override public void updateProperty(Property property)
  {
    checkAdmin();
    realWriter.updateProperty(property);
  }

  /**
   * Checks if the user has admin privileges before allowing access to property management methods.
   *
   * @param id the ID of the property to be deleted
   */
  @Override public void deleteProperty(int id)
  {
    checkAdmin();
    realWriter.deleteProperty(id);
  }

  /**
   * gets all properties, only accessible by admin users.
   */
  @Override public void getAllProperties()
  {
    checkAdmin();
    realWriter.getAllProperties();
  }

  /**
   * Adds a property change listener to the real writer.
   *
   * @param listener the listener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    realWriter.addPropertyChangeListener(listener);
  }

  /**
   * Property change event handler that forwards the event to the support object.
   *
   * @param evt the property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
