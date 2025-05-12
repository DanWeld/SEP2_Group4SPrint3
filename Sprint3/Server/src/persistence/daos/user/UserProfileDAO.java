package persistence.daos.user;

import dtos.User;
import dtos.UserProfile;

import java.sql.SQLException;

/**
 * Implementation of UserProfileDAO for handling profile-related operations
 */
public class UserProfileDAO {
    private UserDAO userDAO;
    
    public UserProfileDAO() throws SQLException {
        userDAO = UserDAOImpl.getInstance();
    }
    
    /**
     * Get a user profile
     * 
     * @param username The username
     * @return The user profile
     * @throws SQLException If an error occurs
     */
    public UserProfile getProfile(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            return null;
        }
        return new UserProfile(user);
    }
    
    /**
     * Update a user profile
     * 
     * @param profile The profile to update
     * @return True if the update was successful
     * @throws SQLException If an error occurs
     */
    public boolean updateProfile(UserProfile profile) throws SQLException {
        return userDAO.updateUserProfile(profile);
    }
    
    /**
     * Delete a user profile
     * 
     * @param username The username of the user to delete
     * @return True if the deletion was successful
     * @throws SQLException If an error occurs
     */
    public boolean deleteProfile(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            return false;
        }
        userDAO.delete(user);
        return true;
    }
}
