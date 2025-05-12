package services.profile;

import dtos.User;
import dtos.UserProfile;

import java.sql.SQLException;

/**
 * Interface for managing user profile operations
 */
public interface UserProfileService 
{
  /**
   * Get the profile for a user
   * 
   * @param username The username of the user
   * @return The user's profile
   * @throws SQLException If an error occurs
   */
  UserProfile getProfile(String username) throws SQLException;
  
  /**
   * Update a user's profile
   * 
   * @param profile The updated profile
   * @return True if the update was successful
   * @throws SQLException If an error occurs
   */
  boolean updateProfile(UserProfile profile) throws SQLException;
  
  /**
   * Delete a user account
   * 
   * @param username The username of the user to delete
   * @return True if the deletion was successful
   * @throws SQLException If an error occurs
   */
  boolean deleteProfile(String username) throws SQLException;
  
  /**
   * Change a user's password
   * 
   * @param username The username of the user
   * @param currentPassword The current password
   * @param newPassword The new password
   * @return True if the password change was successful
   * @throws SQLException If an error occurs
   */
  boolean changePassword(String username, String currentPassword, String newPassword) throws SQLException;
}
