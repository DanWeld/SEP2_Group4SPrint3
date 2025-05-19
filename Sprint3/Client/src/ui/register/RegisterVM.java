package ui.register;

import dtos.ErrorResponse;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import dtos.User;
import networking.authClient.Authentication;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RegisterVM implements PropertyChangeListener
{
  private final StringProperty usernameProp = new SimpleStringProperty();
  private final StringProperty emailProp = new SimpleStringProperty();
  private final StringProperty pwProp = new SimpleStringProperty();
  private final StringProperty repeatProp = new SimpleStringProperty();
  private final StringProperty msgProp = new SimpleStringProperty();
  private final BooleanProperty enableRegisterButtonProp = new SimpleBooleanProperty(
      true);
  private final BooleanProperty registrationSuccessfulProp = new SimpleBooleanProperty(
      false);
  private final Authentication authService;

  public RegisterVM(Authentication authService)
  {
    this.authService = authService;
    authService.addPropertyChangeListener(this);

    usernameProp.addListener(this::updateRegisterButtonState);
    emailProp.addListener(this::updateRegisterButtonState);
    pwProp.addListener(this::updateRegisterButtonState);
    repeatProp.addListener(this::updateRegisterButtonState);
  }

  private void updateRegisterButtonState(Observable observable)
  {
    boolean shouldDisable =
        usernameProp.get() == null || usernameProp.get().isEmpty()
            || emailProp.get() == null || emailProp.get().isEmpty()
            || pwProp.get() == null || pwProp.get().isEmpty()
            || repeatProp.get() == null || repeatProp.get().isEmpty()
            || !pwProp.get().equals(repeatProp.get());
    enableRegisterButtonProp.set(shouldDisable);
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

  public void register()
  {
    String username = usernameProp.get();
    String email = emailProp.get();
    String password = pwProp.get();

    new Thread(() -> authService.registerUser(
        // Create a new User object with the provided username, email, and password
        new User(username, email, password))).start();
    System.out.println(
        "RegisterVM: register called with username: " + username + ", email: "
            + email + ", password: " + password);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> {
      if (evt.getPropertyName().equals("register"))
      {
        handleRegisterResponse((User) evt.getNewValue());
      }
      else if (evt.getPropertyName().equals("error"))
      {
        handleErrorResponse((ErrorResponse) evt.getNewValue());
      }
    });
  }

  private void handleRegisterResponse(User newValue)
  {
    // Handle the successful registration response
    msgProp.set("Registration successful");
    UserSession.getInstance().setCurrentUser(newValue);
    registrationSuccessfulProp.set(true);
  }

  private void handleErrorResponse(ErrorResponse newValue)
  {
    Platform.runLater(() -> {
      // Handle the error response
      msgProp.set(newValue.errorMessage());
      System.out.println(
          "RegisterVM: error event received" + newValue.errorMessage());
      registrationSuccessfulProp.set(false);
    });
  }
}
