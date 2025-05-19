package networking.authClient;

import dtos.LoginRequest;
import dtos.User;
import observer.PropertyChangeSubject;

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
