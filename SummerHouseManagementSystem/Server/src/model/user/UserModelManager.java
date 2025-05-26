package model.user;

import dtos.User;
import services.user.UserAdminPrivileges;
import services.user.UserCustomerPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * UserModelManager implements the UserModel interface and manages user operations
 * by delegating to UserCustomerPrivileges and UserAdminPrivileges services.
 * It also supports property change listeners to notify about changes in user data.
 *
 * @author Group 4
 * @version 1.0
 */
public class UserModelManager implements UserModel, PropertyChangeListener
{
  private UserCustomerPrivileges customer;
  private UserAdminPrivileges admin;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  /**
   * Constructs a UserModelManager with the specified customer and admin privileges.
   *
   * @param customer the UserCustomerPrivileges service
   * @param admin the UserAdminPrivileges service
   */
  public UserModelManager(UserCustomerPrivileges customer,
      UserAdminPrivileges admin)
  {
    this.customer = customer;
    this.admin = admin;
    this.admin.addPropertyChangeListener(this);
    this.customer.addPropertyChangeListener(this);
  }

  /**
   * Sets the User to be admin.
   * @param username the username of the user to be promoted to admin
   */
  @Override public void promoteToAdmin(String username)
  {
    admin.promoteToAdmin(username);
  }

  /**
   * Updates the user information.
   * @param user the User object containing updated information
   */
  @Override public void updateUser(User user)
  {
    customer.updateUser(user);
  }

  /**
   * Deletes a user by username.
   * @param username the username of the user to be deleted
   */
  @Override public void deleteUser(String username)
  {
    customer.deleteUser(username);
  }

  /**
   * Retrieves all users.
   */
  @Override public void getAllUsers()
  {
    admin.getAllUsers();
  }

  /**
   * Adds a property change listener to this model.
   *
   * @param listener the PropertyChangeListener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Property change event handler that notifies all registered listeners
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
