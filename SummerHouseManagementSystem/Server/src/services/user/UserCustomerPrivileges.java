package services.user;

import dtos.User;
import observer.PropertyChangeSubject;

/**
 * UserService interface that combines both customer and admin privileges for user management.
 * This interface extends PropertyChangeSubject to allow for property change notifications.
 */
public interface UserCustomerPrivileges extends PropertyChangeSubject
{
  void updateUser(User user);
  void deleteUser(String username);
}
