package ui.profile;

import dtos.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import services.UserSession;

/**
 * ViewModel for the User Profile view
 */
public class UserProfileVM {
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty userType;
    private final StringProperty message;
    
    public UserProfileVM() {
        username = new SimpleStringProperty("");
        email = new SimpleStringProperty("");
        firstName = new SimpleStringProperty("");
        lastName = new SimpleStringProperty("");
        userType = new SimpleStringProperty("");
        message = new SimpleStringProperty("");
        
        loadUserData();
    }
      /**
     * Loads the current user's data into the view model
     */
    public void loadUserData() {
        User currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            username.set(currentUser.getUsername());
            email.set(currentUser.getEmail());
            firstName.set("N/A"); // User class doesn't have firstName
            lastName.set("N/A");  // User class doesn't have lastName
            userType.set(currentUser.isAdmin() ? "Admin" : "User");
        }
    }
    
    public StringProperty usernameProperty() {
        return username;
    }
    
    public StringProperty emailProperty() {
        return email;
    }
    
    public StringProperty firstNameProperty() {
        return firstName;
    }
    
    public StringProperty lastNameProperty() {
        return lastName;
    }
    
    public StringProperty userTypeProperty() {
        return userType;
    }
    
    public StringProperty messageProperty() {
        return message;
    }
}
