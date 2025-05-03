package model.authentication;

import dtos.User;

/**
 * Interface for user authentication service
 */
public interface AuthenticationService {
    /**
     * Authenticate a user with email and password
     * @param email User's email
     * @param password User's password
     * @return Result message indicating success or failure
     */
    String authenticate(String email, String password);
    
    /**
     * Authenticate a user with username and password
     * @param username User's username
     * @param password User's password
     * @return Result message indicating success or failure
     */
    String authenticateByUsername(String username, String password);
    
    /**
     * Register a new user
     * @param user User to register
     * @return Result message indicating success or failure
     */
    String registerUser(User user);
    
    /**
     * Check if a username is unique
     * @param username Username to check
     * @return True if username is unique, false otherwise
     */
    boolean isUsernameUnique(String username);
    
    /**
     * Check if an email is unique
     * @param email Email to check
     * @return True if email is unique, false otherwise
     */
    boolean isEmailUnique(String email);
    
    /**
     * Check if a user is an admin
     * @param email User's email
     * @return True if user is an admin, false otherwise
     */
    boolean isAdmin(String email);
}
