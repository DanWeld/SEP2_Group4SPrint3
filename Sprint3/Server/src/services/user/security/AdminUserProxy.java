package services.user.security;

import dtos.User;
import services.user.UserAdminPrivileges;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class AdminUserProxy implements UserAdminPrivileges
{
  private final UserAdminPrivileges realWriter;
  private final User user;
  private PropertyChangeSupport support = new PropertyChangeSupport(this);

  public AdminUserProxy(UserAdminPrivileges realWriter, User user)
  {
    this.realWriter = realWriter;
    this.user = user;
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
}
