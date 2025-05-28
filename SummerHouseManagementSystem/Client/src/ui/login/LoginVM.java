package ui.login;

import dtos.ErrorResponse;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import dtos.LoginRequest;
import networking.ClientSocket;
import networking.authClient.Authentication;
import networking.authClient.AuthenticationImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ViewModel for the Login view
 * Handles user login logic and state management
 * This class listens for property changes in the authentication service
 * and updates the UI accordingly.
 *
 * @author Group 4
 * @version 1.0
 */
public class LoginVM implements PropertyChangeListener
{
  private final StringProperty emailProp = new SimpleStringProperty(); // Can be email or username
  private final StringProperty pwProp = new SimpleStringProperty();
  private final StringProperty msgProp = new SimpleStringProperty();
  private final BooleanProperty loginBtnEnabledProp = new SimpleBooleanProperty();
  private final BooleanProperty loginSuccessfulProp = new SimpleBooleanProperty(
      false);
  private final Authentication authService;

  /**
   * Constructor for LoginVM
   * Initializes the authentication service and sets up property listeners
   */
  public LoginVM()
  {
    try
    {
      ClientSocket client = new ClientSocket();
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

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   * @return the property values as JavaFX properties
   */
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

  /**
   * Updates the state of the login button based on the email and password fields.
   * If either field is empty, the button is disabled.
   * This method is called whenever the email or password properties change.
   *
   * @param observable the observable object that changed
   */
  public void updateLoginButtonState(Observable observable)
  {
    boolean shouldDisable = emailProp.get() == null || emailProp.get().isEmpty()
        || pwProp.get() == null || pwProp.get().isEmpty();
    loginBtnEnabledProp.set(!shouldDisable);
  }

  /**
   * Attempts to log in the user with the provided email and password.
   * This method sends a login request to the authentication service.
   * If the login is successful, it updates the UserSession with the logged-in user.
   */
  public void login()
  {
    String email = emailProp.get();
    String password = pwProp.get();
    authService.loginUser(new LoginRequest(email, password));
  }

  /**
   * Handles property change events from the authentication service.
   * This method updates the message and login success state based on the event type.
   *
   * @param evt the property change event
   */
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
