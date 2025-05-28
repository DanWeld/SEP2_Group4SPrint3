package model.user;

import dtos.User;
import observer.PropertyChangeSubject;

/**
 * UserModel interface defines the operations related to user management.
 * It extends PropertyChangeSubject to allow listeners to be notified of changes.
 *
 * @author Group 4
 * @version 1.0
 */
public interface UserModel extends PropertyChangeSubject
{
  void promoteToAdmin(String username);
  void updateUser(User user);
  void deleteUser(String username);
  void getAllUsers();
}
