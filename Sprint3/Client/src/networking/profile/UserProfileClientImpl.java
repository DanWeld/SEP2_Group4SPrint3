package networking.profile;

import dtos.UserProfile;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Implementation of the UserProfileClient interface
 */
public class UserProfileClientImpl extends UnicastRemoteObject implements UserProfileClient {
    private networking.Client client;
    
    public UserProfileClientImpl(networking.Client client) throws RemoteException {
        super();
        this.client = client;
    }
    
    @Override
    public UserProfile getProfile(String username) throws RemoteException {
        try {
            return client.call("getProfile", username, UserProfile.class);
        } catch (Exception e) {
            throw new RemoteException("Failed to get profile", e);
        }
    }
    
    @Override
    public boolean updateProfile(UserProfile profile) throws RemoteException {
        try {
            return client.call("updateProfile", profile, Boolean.class);
        } catch (Exception e) {
            throw new RemoteException("Failed to update profile", e);
        }
    }
    
    @Override
    public boolean deleteProfile(String username) throws RemoteException {
        try {
            return client.call("deleteProfile", username, Boolean.class);
        } catch (Exception e) {
            throw new RemoteException("Failed to delete profile", e);
        }
    }
    
    @Override
    public boolean changePassword(String username, String currentPassword, String newPassword) throws RemoteException {
        try {
            // Create an array to hold the parameters
            Object[] params = new Object[] { username, currentPassword, newPassword };
            return client.call("changePassword", params, Boolean.class);
        } catch (Exception e) {
            throw new RemoteException("Failed to change password", e);
        }
    }
}
