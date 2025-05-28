package services.user.security;

import dtos.User;
import services.user.UserAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * AdminUserProxy is a proxy class that implements UserAdminPrivileges.
 * It checks if the user has admin privileges before allowing access to admin methods.
 * It also listens for property changes and forwards them to its listeners.
 */
public class AdminUserProxy implements UserAdminPrivileges, PropertyChangeListener
{
  private final UserAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  /**
   * Constructor for AdminUserProxy.
   *
   * @param realWriter The real UserAdminPrivileges implementation to delegate calls to.
   * @param user The user whose privileges are being checked.
   */
  public AdminUserProxy(UserAdminPrivileges realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
  }

  /**
   * Checks if the user has admin privileges.
   * Throws a SecurityException if the user is not an admin.
   */
  public void checkAdmin()
  {
    if (!user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  /**
   * Promotes a user to admin.
   *
   * @param username The username of the user to promote.
   */
  public void promoteToAdmin(String username)
  {
    checkAdmin();
    realWriter.promoteToAdmin(username);
  }

  /**
   * Get all users.
   * This method retrieves all users in the system.
   */
  public void getAllUsers()
  {
    checkAdmin();
    realWriter.getAllUsers();
  }

  /**
   * adds a new Listener to the property change support.
   * @param listener the listener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Property change event handler.
   * This method is called when a property change event occurs.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
