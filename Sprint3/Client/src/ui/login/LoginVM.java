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
            // Create a mock user for testing (in a real app, we'd get this from the server)
            User user;
            if (credential.contains("@")) {
                user = new User("User", credential, "", false);
            } else {
                user = new User(credential, "user@example.com", "", false);
            }
            
            // Handle demo mode
            if (resultMsg.contains("demo mode")) {
                user = new User("TestUser", "test@test.com", "", false);
            }
            
            // Set the user in the session
            UserSession.getInstance().setCurrentUser(user);
            
            // Set login as successful
            loginSuccessfulProp.set(true);
            
            msgProp.set("Login successful");
            // Clear fields
            credentialProp.set("");
            pwProp.set("");
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
