package services.profile;

import dtos.User;
import dtos.UserProfile;
import persistence.daos.user.UserDAO;
import persistence.daos.user.UserDAOImpl;

import java.sql.SQLException;

/**
 * Implementation of the UserProfileService interface
 */
public class UserProfileServiceImpl implements UserProfileService {
    private UserDAO userDAO;

    public UserProfileServiceImpl() throws SQLException {
        this.userDAO = UserDAOImpl.getInstance();
    }

    @Override
    public UserProfile getProfile(String username) throws SQLException {
        User user = userDAO.readByUsername(username);
        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        return new UserProfile(user);
    }

    @Override
    public boolean updateProfile(UserProfile profile) throws SQLException {
        // First check if user exists
        User existingUser = userDAO.readByUsername(profile.getUsername());
        if (existingUser == null) {
            throw new SQLException("User not found: " + profile.getUsername());
        }

        // Update the user
        User updatedUser = new User(
            profile.getName(),
            profile.getUsername(), 
            profile.getEmail(), 
            profile.getPassword(), 
            profile.isAdmin()
        );
        
        userDAO.update(updatedUser);
        return true;
    }

    @Override
    public boolean deleteProfile(String username) throws SQLException {
        User user = userDAO.readByUsername(username);
        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        userDAO.delete(user);
        return true;
    }

    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) throws SQLException {
        // Verify the user exists and the current password is correct
        User user = userDAO.readByUsername(username);
        if (user == null) {
            throw new SQLException("User not found: " + username);
        }
        
        if (!user.getPassword().equals(currentPassword)) {
            return false; // Current password is incorrect
        }
        
        // Update the password
        user.setPassword(newPassword);
        userDAO.update(user);
        return true;
    }
}
