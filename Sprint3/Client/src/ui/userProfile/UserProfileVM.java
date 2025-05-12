package ui.userProfile;

import dtos.User;
import dtos.UserProfile;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.profile.UserProfileClient;
import services.UserSession;

import java.rmi.RemoteException;

/**
 * View model for user profile operations
 */
public class UserProfileVM {
    private UserProfileClient profileClient;
    private UserSession userSession;
    
    // Properties for binding to the view
    private StringProperty name;
    private StringProperty username;
    private StringProperty email;
    private StringProperty bio;
    private StringProperty phoneNumber;
    private StringProperty address;
    private StringProperty currentPassword;
    private StringProperty newPassword;
    private StringProperty confirmPassword;
    private StringProperty errorMessage;
    private BooleanProperty editMode;
    
    public UserProfileVM(UserProfileClient profileClient, UserSession userSession) {
        this.profileClient = profileClient;
        this.userSession = userSession;
        
        // Initialize properties
        name = new SimpleStringProperty("");
        username = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        bio = new SimpleStringProperty("");
        phoneNumber = new SimpleStringProperty("");
        address = new SimpleStringProperty("");
        currentPassword = new SimpleStringProperty("");
        newPassword = new SimpleStringProperty("");
        confirmPassword = new SimpleStringProperty("");
        errorMessage = new SimpleStringProperty("");
        editMode = new SimpleBooleanProperty(false);
    }
    
    /**
     * Load the user profile from the server
     */
    public void loadProfile() {
        try {
            // Get the current user's username from the session
            String loggedInUsername = userSession.getUsername();
            if (loggedInUsername == null || loggedInUsername.isEmpty()) {
                setErrorMessage("No user is currently logged in");
                return;
            }
            
            // Get the profile from the server
            UserProfile profile = profileClient.getProfile(loggedInUsername);
            
            // Update the properties
            name.set(profile.getName());
            username.set(profile.getUsername());
            email.set(profile.getEmail());
            bio.set(profile.getBio());
            phoneNumber.set(profile.getPhoneNumber());
            address.set(profile.getAddress());
            
            // Clear error message
            errorMessage.set("");
        } catch (RemoteException e) {
            setErrorMessage("Failed to load profile: " + e.getMessage());
        }
    }
    
    /**
     * Save changes to the user profile
     */
    public void saveProfile() {
        try {            // Create a User object first
            User user = new User(
                name.get(),  // Use the current name value
                userSession.getUsername(), 
                userSession.getEmail(), 
                userSession.getPassword(), 
                userSession.isAdmin()
            );
            
            // Create a profile with the user
            UserProfile profile = new UserProfile(user);
            
            // Update the profile with the edited values
            profile.setBio(bio.get());
            profile.setPhoneNumber(phoneNumber.get());
            profile.setAddress(address.get());
            
            // Send the updated profile to the server
            boolean success = profileClient.updateProfile(profile);
            
            if (success) {
                // Exit edit mode
                editMode.set(false);
                // Clear error message
                errorMessage.set("");
                // Update session with new name
                userSession.setName(name.get());
            } else {
                setErrorMessage("Failed to update profile");
            }
        } catch (RemoteException e) {
            setErrorMessage("Failed to save profile: " + e.getMessage());
        }
    }
    
    /**
     * Change the user's password
     */
    public void changePassword() {
        try {
            // Validate passwords
            if (currentPassword.get().isEmpty()) {
                setErrorMessage("Current password is required");
                return;
            }
            
            if (newPassword.get().isEmpty()) {
                setErrorMessage("New password is required");
                return;
            }
            
            if (!newPassword.get().equals(confirmPassword.get())) {
                setErrorMessage("New passwords do not match");
                return;
            }
            
            // Change the password on the server
            boolean success = profileClient.changePassword(
                userSession.getUsername(),
                currentPassword.get(),
                newPassword.get()
            );
            
            if (success) {
                // Clear password fields
                currentPassword.set("");
                newPassword.set("");
                confirmPassword.set("");
                // Clear error message
                errorMessage.set("");
                // Update session with new password
                userSession.setPassword(newPassword.get());
            } else {
                setErrorMessage("Failed to change password: current password is incorrect");
            }
        } catch (RemoteException e) {
            setErrorMessage("Failed to change password: " + e.getMessage());
        }
    }
    
    /**
     * Delete the user's account
     */
    public boolean deleteAccount() {
        try {
            // Delete the account on the server
            boolean success = profileClient.deleteProfile(userSession.getUsername());
            
            if (success) {
                // Clear the user session
                userSession.logout();
                // Clear error message
                errorMessage.set("");
                return true;
            } else {
                setErrorMessage("Failed to delete account");
                return false;
            }
        } catch (RemoteException e) {
            setErrorMessage("Failed to delete account: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Set the view to edit mode
     */
    public void enterEditMode() {
        editMode.set(true);
    }
    
    /**
     * Cancel editing and revert changes
     */
    public void cancelEdit() {
        // Reload the profile to discard changes
        loadProfile();
        // Exit edit mode
        editMode.set(false);
    }
    
    private void setErrorMessage(String message) {
        errorMessage.set(message);
    }
    
    // Getters for properties
    public StringProperty nameProperty() {
        return name;
    }
    
    public StringProperty usernameProperty() {
        return username;
    }
    
    public StringProperty emailProperty() {
        return email;
    }
    
    public StringProperty bioProperty() {
        return bio;
    }
    
    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
    
    public StringProperty addressProperty() {
        return address;
    }
    
    public StringProperty currentPasswordProperty() {
        return currentPassword;
    }
    
    public StringProperty newPasswordProperty() {
        return newPassword;
    }
    
    public StringProperty confirmPasswordProperty() {
        return confirmPassword;
    }
    
    public StringProperty errorMessageProperty() {
        return errorMessage;
    }
    
    public BooleanProperty editModeProperty() {
        return editMode;
    }
}
