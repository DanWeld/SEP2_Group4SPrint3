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
    private final StringProperty emailProp = new SimpleStringProperty();
    private final StringProperty pwProp = new SimpleStringProperty();
    private final StringProperty msgProp = new SimpleStringProperty();
    private final BooleanProperty loginBtnEnabledProp = new SimpleBooleanProperty();
    private final BooleanProperty loginSuccessfulProp = new SimpleBooleanProperty(false);
    private final Authentication authService;

    public LoginVM(Authentication authService){
        this.authService = authService;
        emailProp.addListener(this::updateLoginButtonState);
        pwProp.addListener(this::updateLoginButtonState);
    }

    private void updateLoginButtonState(Observable observable) {
        boolean shouldDisable = emailProp.get() == null || emailProp.get().isEmpty() || pwProp.get() == null || pwProp.get().isEmpty();
        loginBtnEnabledProp.set(!shouldDisable);
    }

    public void login(){
        String email = emailProp.get();
        String password = pwProp.get();

        if (email == null || email.isEmpty()) {
            msgProp.set("Email cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()) {
            msgProp.set("Password cannot be empty");
            return;
        }
        
        // Call the authentication service
        String resultMsg = authService.loginUser(email, password);
        
        if(resultMsg.equals("Ok")){
            // Create a mock user for testing (in a real app, we'd get this from the server)
            User user = new User("User", email, "", false);
            
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
            emailProp.set("");
            pwProp.set("");
        } else {
            loginSuccessfulProp.set(false);
            msgProp.set(resultMsg);
        }
    }
    
    public StringProperty emailProperty() {
        return emailProp;
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
