package networking.authClient;

import dtos.LoginRequest;
import dtos.User;
import observer.PropertyChangeSubject;

/**
 * Interface for Authentication, which handles user authentication operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 * This interface defines methods for logging in and registering users.
 *
 * @author Group 4
 * @version 1.0
 */
public interface Authentication extends PropertyChangeSubject
{
  /**
   * Logs in a user with the provided email and password
   * @param loginRequest The login request containing email and password
   */
  void loginUser(LoginRequest loginRequest);
  
  /**
   * Registers a new user with the system
   * @param user The user to register
   */
  void registerUser(User user);
}
