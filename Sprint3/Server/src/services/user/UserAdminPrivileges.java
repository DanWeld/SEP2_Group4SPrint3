package services.user;

import observer.PropertyChangeSubject;

public interface UserAdminPrivileges extends PropertyChangeSubject
{
  void promoteToAdmin(String username);
  void getAllUsers();
}
