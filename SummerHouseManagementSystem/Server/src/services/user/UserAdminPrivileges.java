package services.user;

import observer.PropertyChangeSubject;

/**
 * UserAdminPrivileges interface defines the methods that allow an admin user
 * to manage other users in the system, such as promoting users to admin status
 * and retrieving a list of all users.
 */
public interface UserAdminPrivileges extends PropertyChangeSubject
{
  void promoteToAdmin(String username);
  void getAllUsers();
}
