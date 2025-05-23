package services.user.security;

import dtos.User;
import services.user.UserAdminPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdminUserProxy implements UserAdminPrivileges, PropertyChangeListener
{
  private final UserAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support;

  public AdminUserProxy(UserAdminPrivileges realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
    this.support = new PropertyChangeSupport(this);
    this.realWriter.addPropertyChangeListener(this);
  }

  public void checkAdmin()
  {
    if (!user.isAdmin())
    {
      throw new SecurityException("Admin privileges required");
    }
  }

  public void promoteToAdmin(String username)
  {
    checkAdmin();
    realWriter.promoteToAdmin(username);
  }

  public void getAllUsers()
  {
    checkAdmin();
    realWriter.getAllUsers();
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
