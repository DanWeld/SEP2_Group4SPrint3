package services.user;

import dtos.User;
import observer.PropertyChangeSubject;

public interface UserCustomerPrivileges extends PropertyChangeSubject
{
  void updateUser(User user);
  void deleteUser(String username);
}
