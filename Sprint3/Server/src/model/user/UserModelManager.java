package model.user;

import dtos.User;
import services.user.UserAdminPrivileges;
import services.user.UserCustomerPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class UserModelManager implements UserModel, PropertyChangeListener
{
  private UserCustomerPrivileges customer;
  private UserAdminPrivileges admin;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public UserModelManager(UserCustomerPrivileges customer,
      UserAdminPrivileges admin)
  {
    this.customer = customer;
    this.admin = admin;
    this.admin.addPropertyChangeListener(this);
  }

  @Override public void promoteToAdmin(String username)
  {
    admin.promoteToAdmin(username);
  }

  @Override public void updateUser(User user)
  {
    customer.updateUser(user);
  }

  @Override public void deleteUser(String username)
  {
    customer.deleteUser(username);
  }

  @Override public void getAllUsers()
  {
    admin.getAllUsers();
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
