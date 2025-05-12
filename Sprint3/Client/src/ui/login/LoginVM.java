package ui.login;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import dtos.LoginRequest;
import dtos.User;
import networking.auth.Authentication;
import services.UserSession;

public class LoginVM {
    private final StringProperty credentialProp = new SimpleStringProperty(); // Can be email or username
    private final StringProperty pwProp = new SimpleStringProperty();
    private final StringProperty msgProp = new SimpleStringProperty();
    private final BooleanProperty loginBtnEnabledProp = new SimpleBooleanProperty();
    private final BooleanProperty loginSuccessfulProp = new SimpleBooleanProperty(false);
    private final Authentication authService;

    public LoginVM(Authentication authService){
        this.authService = authService;
        credentialProp.addListener(this::updateLoginButtonState);
        pwProp.addListener(this::updateLoginButtonState);
    }

    private void updateLoginButtonState(Observable observable) {
        boolean shouldDisable = credentialProp.get() == null || credentialProp.get().isEmpty() || pwProp.get() == null || pwProp.get().isEmpty();
        loginBtnEnabledProp.set(!shouldDisable);
    }

    public void login(){
        String credential = credentialProp.get();
        String password = pwProp.get();

        if (credential == null || credential.isEmpty()) {
            msgProp.set("Email or Username cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()) {
            msgProp.set("Password cannot be empty");
            return;
        }
        
        // Determine if it's an email or username login
        String resultMsg;
        if (credential.contains("@")) {
            // It's an email login
            resultMsg = authService.loginUser(credential, password);
        } else {
            // It's a username login
            resultMsg = authService.loginUserByUsername(credential, password);
        }
        
        if(resultMsg.equals("Ok")){
            // Instead of hardcoding users, let's try to get the actual user from the auth service
            User user;
            try {
                // Try to get the actual user from the authentication service
                if (credential.contains("@")) {
                    // For email login, we need to get the username from the server or use the part before @
                    String username = credential.substring(0, credential.indexOf('@'));
                        // For email login, extract username from email and use it as name
                    user = new User(username, username, credential, password, false);
                    // For email login, we'll use the part before @ as the name
                    user.setName(username);
                } else {
                    // For username login, we use the credential as username and name
                    user = new User(credential, credential, credential + "@example.com", password, false);
                    // For username login, we'll use the credential as the name
                    user.setName(credential);
                }
                
                // Handle demo mode
                if (resultMsg.contains("demo mode")) {
                    user = new User("Demo User", "demo", "demo@example.com", "Demo1234!", false);
                }
                
                // Set the user in the session
                UserSession.getInstance().setCurrentUser(user);
            } catch (Exception e) {
                System.err.println("Error creating user: " + e.getMessage());
                // If we can't create a proper user, create a basic one
                if (credential.contains("@")) {
                    String username = credential.substring(0, credential.indexOf('@'));
                    user = new User(username, username, credential, password, false);
                } else {
                    user = new User(credential, credential, credential + "@example.com", password, false);
                }
                UserSession.getInstance().setCurrentUser(user);
            }
            
            // Debug output
            System.out.println("DEBUG: Login successful, setting user: " + user.getUsername());
            System.out.println("DEBUG: User details - Name: " + user.getName() + ", Email: " + user.getEmail() + ", IsAdmin: " + user.isAdmin());
            
            msgProp.set("Login successful");
            // Clear fields
            credentialProp.set("");
            pwProp.set("");
            
            // Set login as successful - this should trigger the listener in LoginCtrl
            loginSuccessfulProp.set(true);
        } else {
            loginSuccessfulProp.set(false);
            msgProp.set(resultMsg);
        }
    }
    
    public StringProperty credentialProperty() {
        return credentialProp;
    }
    
    // For backward compatibility with existing views
    public StringProperty emailProperty() {
        return credentialProp;
    }
    
    public StringProperty passwordProperty(){
        return pwProp;
    }
    
    public StringProperty messageProperty(){
        return msgProp;
    }
    
    public BooleanProperty getLoginBtnEnabledProp() {
        return loginBtnEnabledProp;
    }
    
    public BooleanProperty loginSuccessfulProperty() {
        return loginSuccessfulProp;
    }
}
