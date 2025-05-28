package networking.userClient;

import dtos.User;
import observer.PropertyChangeSubject;

/**
 * Interface for UserClient, which handles user-related operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 * This interface defines methods for retrieving all users, promoting a user to admin,
 * updating user information, and deleting a user.
 *
 * @author Group 4
 * @version 1.0
 */
public interface UserClient  extends PropertyChangeSubject
{
  /**
   * Get all users.
   */
  void getAllUsers();

  /**
   * Promote a user to admin.
   *
   * @param username the username of the user to promote
   */
  void promoteToAdmin(String username);

  /**
   * Update user information.
   *
   * @param user the user object containing updated information
   */
  void updateUser(User user);

  /**
   * Delete a user by their username.
   *
   * @param username the username of the user to delete
   */
  void deleteUser(String username);
}
