package networking.profile;

import dtos.UserProfile;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for user profile operations
 */
public interface UserProfileClient extends Remote {
    
    /**
     * Get the profile for a user
     * 
     * @param username The username of the user
     * @return The user's profile
     * @throws RemoteException If a remote error occurs
     */
    UserProfile getProfile(String username) throws RemoteException;
    
    /**
     * Update a user's profile
     * 
     * @param profile The updated profile
     * @return True if the update was successful
     * @throws RemoteException If a remote error occurs
     */
    boolean updateProfile(UserProfile profile) throws RemoteException;
    
    /**
     * Delete a user account
     * 
     * @param username The username of the user to delete
     * @return True if the deletion was successful
     * @throws RemoteException If a remote error occurs
     */
    boolean deleteProfile(String username) throws RemoteException;
    
    /**
     * Change a user's password
     * 
     * @param username The username of the user
     * @param currentPassword The current password
     * @param newPassword The new password
     * @return True if the password change was successful
     * @throws RemoteException If a remote error occurs
     */
    boolean changePassword(String username, String currentPassword, String newPassword) throws RemoteException;
}
