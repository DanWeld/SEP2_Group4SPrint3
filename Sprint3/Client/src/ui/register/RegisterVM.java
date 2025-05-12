package ui.register;

import javafx.beans.Observable;
import javafx.beans.property.*;
import dtos.User;
import networking.auth.Authentication;
import services.UserSession;

public class RegisterVM
{
  private final StringProperty usernameProp = new SimpleStringProperty();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty pwProp = new SimpleStringProperty();
  private final StringProperty repeatProp = new SimpleStringProperty();
  private final StringProperty msgProp = new SimpleStringProperty();
  private final BooleanProperty enableRegisterButtonProp = new SimpleBooleanProperty(
      true);
  private final BooleanProperty registrationSuccessfulProp = new SimpleBooleanProperty(false);
  private final Authentication authService;

  public RegisterVM(Authentication authService)
  {
    this.authService = authService;
    usernameProp.addListener(this::updateRegisterButtonState);
    emailProp.addListener(this::updateRegisterButtonState);
    pwProp.addListener(this::updateRegisterButtonState);
    repeatProp.addListener(this::updateRegisterButtonState);
  }

  private void updateRegisterButtonState(Observable observable)
  {
    boolean shouldDisable = emailProp.get() == null || emailProp.get().isEmpty()
        || pwProp.get() == null || pwProp.get().isEmpty()
        || repeatProp.get() == null || repeatProp.get().isEmpty();
    enableRegisterButtonProp.set(shouldDisable);
  }

  public void register()
  {
    String username = usernameProp.get();
    String email = emailProp.get();
    String password = pwProp.get();
    String repeat = repeatProp.get();

    // Input validation
    if (username == null || username.isEmpty()) {
      msgProp.set("Username cannot be empty");
      return;
    }
    
    if (email == null || email.isEmpty()) {
      msgProp.set("Email cannot be empty");
      return;
    }
    
    if (password == null || password.isEmpty()) {
      msgProp.set("Password cannot be empty");
      return;
    }
    
    if (repeat == null || repeat.isEmpty()) {
      msgProp.set("Please confirm your password");
      return;
    }
    
    if (!password.equals(repeat)) {
      msgProp.set("Passwords do not match");
      return;
    }
    
    // Check password strength
    if (!authService.isPasswordStrong(password)) {
      msgProp.set("Password must be at least 8 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character");
      return;
    }
    
    // Create user object
    User newUser = new User(username, email, password);
    
    // Register the user using authentication service
    String resultMsg = authService.registerUser(newUser);
    
    if (resultMsg.equals("Ok"))
    {
      // Store user in session for auto-login after registration
      UserSession.getInstance().setCurrentUser(newUser);
      
      System.out.println("User registered");
      msgProp.set("Registration successful");
      
      // Set registration as successful
      registrationSuccessfulProp.set(true);
      
      //clear fields
      usernameProp.set("");
      emailProp.set("");
      pwProp.set("");
      repeatProp.set("");
    }
    else
    {
      registrationSuccessfulProp.set(false);
      msgProp.set(resultMsg);
    }
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty passwordProperty()
  {
    return pwProp;
  }

  public StringProperty repeatProperty()
  {
    return repeatProp;
  }

  public StringProperty messageProperty()
  {
    return msgProp;
  }

  public StringProperty usernameProperty()
  {
    return usernameProp;
  }

  public BooleanProperty enableRegisterButtonProperty()
  {
    return enableRegisterButtonProp;
  }
  
  public BooleanProperty registrationSuccessfulProperty()
  {
    return registrationSuccessfulProp;
  }
}
