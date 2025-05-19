package ui.login;

import dtos.ErrorResponse;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import dtos.LoginRequest;
import dtos.User;
import networking.authClient.Authentication;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginVM implements PropertyChangeListener
{
  private final StringProperty emailProp = new SimpleStringProperty(); // Can be email or username
  private final StringProperty pwProp = new SimpleStringProperty();
  private final StringProperty msgProp = new SimpleStringProperty();
  private final BooleanProperty loginBtnEnabledProp = new SimpleBooleanProperty();
  private final BooleanProperty loginSuccessfulProp = new SimpleBooleanProperty(
      false);
  private final Authentication authService;

  public LoginVM(Authentication authService)
  {
    this.authService = authService;

    authService.addPropertyChangeListener(this);

    emailProp.addListener(this::updateLoginButtonState);
    pwProp.addListener(this::updateLoginButtonState);
  }

  public StringProperty emailProperty()
  {
    return emailProp;
  }

  public StringProperty passwordProperty()
  {
    return pwProp;
  }

  public StringProperty messageProperty()
  {
    return msgProp;
  }

  public BooleanProperty getLoginBtnEnabledProp()
  {
    return loginBtnEnabledProp;
  }

  public BooleanProperty loginSuccessfulProperty()
  {
    return loginSuccessfulProp;
  }

  public void updateLoginButtonState(Observable observable)
  {
    boolean shouldDisable = emailProp.get() == null || emailProp.get().isEmpty()
        || pwProp.get() == null || pwProp.get().isEmpty();
    loginBtnEnabledProp.set(!shouldDisable);
  }

  public void login()
  {
    String email = emailProp.get();
    String password = pwProp.get();

    new Thread(() -> {
      // Send login request to server through the authService
      authService.loginUser(new LoginRequest(email, password));
    }).start();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> {
      if (evt.getPropertyName().equals("login")
          && evt.getNewValue() instanceof User)
      {
        handleLoginResponse((User) evt.getNewValue());
      }
      else if (evt.getPropertyName().equals("error")
          && evt.getNewValue() instanceof ErrorResponse)
      {
        handleErrorResponse((ErrorResponse) evt.getNewValue());
      }
    });
  }

  private void handleLoginResponse(User newValue)
  {
    // Set the user in the session
    UserSession.getInstance().setCurrentUser(newValue);
    System.out.println("Login successful: " + newValue.getUsername());
    loginSuccessfulProp.set(true);
    msgProp.set("Login successful");
  }

  private void handleErrorResponse(ErrorResponse errorResponse)
  {
    // Handle the error response
    msgProp.set(errorResponse.errorMessage());
    loginSuccessfulProp.set(false);
  }
}
