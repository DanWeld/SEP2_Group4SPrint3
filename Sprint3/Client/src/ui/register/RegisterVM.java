package ui.register;

import dtos.ErrorResponse;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.*;
import dtos.User;
import networking.Client;
import networking.authClient.Authentication;
import networking.authClient.AuthenticationImpl;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ViewModel for the registration view.
 * Handles user input and communicates with the authentication service.
 */
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

  /**
   * Constructor for RegisterVM.
   * Initializes the authentication service and sets up property listeners.
   */
  public RegisterVM()
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

    usernameProp.addListener(this::updateRegisterButtonState);
    emailProp.addListener(this::updateRegisterButtonState);
    pwProp.addListener(this::updateRegisterButtonState);
    repeatProp.addListener(this::updateRegisterButtonState);
  }

  /**
   * Updates the state of the register button based on the input fields.
   * Disables the button if any field is empty or if passwords do not match.
   * @param observable the observable property that changed
   */
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

  /**
   * Registers a new user with the provided username, email, and password.
   * Calls the authentication service to handle the registration process.
   */
  public void register()
  {
    String username = usernameProp.get();
    String email = emailProp.get();
    String password = pwProp.get();

    authService.registerUser(new User(username, email, password));
  }

  /**
   * Property change handler for the authentication service.
   * Handles events related to user registration.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String evtName = evt.getPropertyName();
    switch (evtName)
    {
      case "register":
      {
        msgProp.set("Registration successful");
        registrationSuccessfulProp.set(true);
        break;
      }
      case "error":
      {
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        msgProp.set(errorResponse.errorMessage());
        registrationSuccessfulProp.set(false);
        break;
      }
    }
  }
}
