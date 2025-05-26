package ui.userProfile;

import dtos.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import networking.ClientSocket;
import networking.userClient.UserClient;
import networking.userClient.UserClientImpl;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * ViewModel for the User Profile view
 */
public class UserProfileVM implements PropertyChangeListener
{
  private final StringProperty username;
  private final StringProperty email;
  private final StringProperty userType;
  private final StringProperty message;
  private final StringProperty passwordProperty;
  private final StringProperty repeatProperty;
  private UserClient userClient;

  /**
   * Constructor initializes the UserClient and sets up properties
   * to hold user data.
   * Initializes the properties for username, email, userType,
   * password, and repeat.
   */
  public UserProfileVM()
  {
    try
    {
      userClient = new UserClientImpl(new ClientSocket());
      userClient.addPropertyChangeListener(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Failed to initialize UserClient", e);
    }

    username = new SimpleStringProperty("");
    email = new SimpleStringProperty("");
    userType = new SimpleStringProperty("");
    message = new SimpleStringProperty("");
    passwordProperty = new SimpleStringProperty("");
    repeatProperty = new SimpleStringProperty("");

    loadUserData();
  }

  /**
   * Loads the current user's data into the view model
   */
  public void loadUserData()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser != null)
    {
      username.set(currentUser.getUsername());
      email.set(currentUser.getEmail());
      userType.set(currentUser.isAdmin() ? "Admin" : "User");
    }
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   * @return the property values as JavaFX properties
   */
  public StringProperty usernameProperty()
  {
    return username;
  }

  public StringProperty emailProperty()
  {
    return email;
  }

  public StringProperty userTypeProperty()
  {
    return userType;
  }

  public StringProperty messageProperty()
  {
    return message;
  }

  public StringProperty passwordProperty()
  {
    return passwordProperty;
  }

  public StringProperty repeatProperty()
  {
    return repeatProperty;
  }

  /**
   * PropertyChange method to handle events from the UserClient.
   * This method updates the view model properties based on the event type.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "update":
        User updatedUser = (User) evt.getNewValue();
        username.set(updatedUser.getUsername());
        email.set(updatedUser.getEmail());
        userType.set(updatedUser.isAdmin() ? "Admin" : "User");
        message.set("User profile updated successfully");
        break;
      case "delete":
        UserSession.getInstance().logout();
        username.set("");
        email.set("");
        userType.set("");
        break;
      case "error":
        message.set((String) evt.getNewValue());
        break;
      default:
        message.set("Unknown event: " + evt.getPropertyName());
    }
  }

  /**
   * Returns an ObservableValue that indicates whether the register button
   * should be enabled based on the password and repeat fields.
   * The button is enabled when both fields are not empty and match.
   * @return An ObservableValue<Boolean> indicating the button state.
   */
  public ObservableValue<Boolean> enableRegisterButtonProperty()
  {
    return passwordProperty.isNotEmpty()
        .and(repeatProperty.isNotEmpty())
        .and(passwordProperty.isEqualTo(repeatProperty));
  }

  /**
   * Updates the user profile with the current values in the view model.
   * This method retrieves the current user from the UserSession and updates
   * their password if they are logged in.
   */
  public void updateUser()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser != null)
    {
      currentUser.setPassword(passwordProperty.get());
      userClient.updateUser(currentUser);
    }
    else
    {
      message.set("No user is currently logged in");
    }
  }

  /**
   * Deletes the current user profile.
   * This method retrieves the current user from the UserSession and deletes
   * their profile if they are logged in.
   */
  public void deleteUser()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser != null)
    {
      userClient.deleteUser(currentUser.getUsername());
    }
    else
    {
      message.set("No user is currently logged in");
    }
  }
}
