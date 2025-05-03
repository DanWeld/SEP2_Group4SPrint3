package networking.auth;

import dtos.User;

public interface Authentication
{
  /**
   * Logs in a user with the provided email and password
   * @param email User's email
   * @param password User's password
   * @return Message indicating success or failure
   */
  String loginUser(String email, String password);
  
  /**
   * Registers a new user with the system
   * @param user The user to register
   * @return Message indicating success or failure
   */
  String registerUser(User user);
  
  /**
   * Validates if a password meets the strength requirements
   * @param password The password to validate
   * @return True if password meets requirements, false otherwise
   */
  boolean isPasswordStrong(String password);
  
  /**
   * Checks if a username is already taken
   * @param username The username to check
   * @return True if username is unique, false otherwise
   */
  boolean isUsernameUnique(String username);
  
  /**
   * Checks if an email is already registered
   * @param email The email to check
   * @return True if email is unique, false otherwise
   */
  boolean isEmailUnique(String email);
  
  /**
   * Checks if a user has admin privileges
   * @param email The email of the user to check
   * @return True if user is an admin, false otherwise
   */
  boolean isAdmin(String email);
}
