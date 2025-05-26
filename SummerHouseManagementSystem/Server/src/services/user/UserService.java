package services.user;

import dtos.User;
import observer.PropertyChangeSubject;

/**
 * UserService interface that combines both customer and admin privileges for user management.
 * This interface extends PropertyChangeSubject to allow for property change notifications.
 * It includes methods for promoting a user to admin, updating user details,
 * deleting a user, and retrieving all users.
 */
public interface UserService extends PropertyChangeSubject
{
  void promoteToAdmin(String username);
  void updateUser(User user);
  void deleteUser(String username);
  void getAllUsers();
}
