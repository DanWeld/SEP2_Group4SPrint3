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
import networking.Client;
import networking.authClient.Authentication;
import networking.authClient.AuthenticationImpl;
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

  public LoginVM()
  {
    try
    {
      Client client = new Client();
      authService = new AuthenticationImpl(client);
      authService.addPropertyChangeListener(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

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
    authService.loginUser(new LoginRequest(email, password));
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String eventName = evt.getPropertyName();
    switch (eventName)
    {
      case "login" ->
      {
        loginSuccessfulProp.set(true);
        msgProp.set("Login successful");
      }
      case "error" ->
      {
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        msgProp.set(errorResponse.errorMessage());
        loginSuccessfulProp.set(false);
      }
    }
  }
}
