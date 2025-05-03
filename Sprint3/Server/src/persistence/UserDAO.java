package persistence;

import dtos.User;

import java.util.List;

/**
 * Interface for User Data Access Object
 */
public interface UserDAO {
    /**
     * Save a user to the database
     * @param user User to save
     * @return True if successful, false otherwise
     */
    boolean saveUser(User user);
    
    /**
     * Get a user by email
     * @param email User's email
     * @return User if found, null otherwise
     */
    User getUserByEmail(String email);
    
    /**
     * Get a user by username
     * @param username User's username
     * @return User if found, null otherwise
     */
    User getUserByUsername(String username);
    
    /**
     * Get all users
     * @return List of all users
     */
    List<User> getAllUsers();
    
    /**
     * Update a user
     * @param user User to update
     * @return True if successful, false otherwise
     */
    boolean updateUser(User user);
    
    /**
     * Delete a user
     * @param email User's email
     * @return True if successful, false otherwise
     */
    boolean deleteUser(String email);
}
