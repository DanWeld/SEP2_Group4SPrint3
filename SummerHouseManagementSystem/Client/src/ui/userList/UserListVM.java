package ui.userList;

import dtos.User;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import networking.ClientSocket;
import networking.userClient.UserClient;
import networking.userClient.UserClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for the UserList view.
 * This class handles the logic for displaying a list of users and managing user-related actions.
 */
public class UserListVM implements PropertyChangeListener
{
  private ObservableList<User> users;
  private StringProperty username;
  private StringProperty email;
  private SimpleObjectProperty<User> selectedUser;
  private StringProperty isAdmin;
  private UserClient userClient;
  private StringProperty errorMsg;
  private ClientSocket client;

  /**
   * Constructor for UserListVM.
   * Initializes the user list and the user client.
   */
  public UserListVM()
  {
    this.users = FXCollections.observableArrayList();
    this.username = new SimpleStringProperty();
    this.email = new SimpleStringProperty();
    this.selectedUser = new SimpleObjectProperty<>();
    this.isAdmin = new SimpleStringProperty();
    this.errorMsg = new SimpleStringProperty();
    try
    {
      ClientSocket client = new ClientSocket();
      this.userClient = new UserClientImpl(client);
      userClient.addPropertyChangeListener(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    userClient.getAllUsers();
  }

  /**
   * Gets the list of users.
   *
   * @return ObservableList of User objects
   */
  public ObservableList<User> getUserList()
  {
    return users;
  }

  /**
   * Searches for users based on the provided username input.
   *
   * @param usernameInput the username input to search for
   */
  public void searchUsers(String usernameInput)
  {
    List<User> searchResults = new ArrayList<>();
    for (User user : users)
    {
      if (user.getUsername().toLowerCase()
          .contains(usernameInput.toLowerCase()))
      {
        searchResults.add(user);
      }
      else
      {
        errorMsg.set("No user found");
      }
    }
    if (searchResults.isEmpty())
    {
      errorMsg.set("No user found");
      return;
    }
    else
    {
      errorMsg.set("");
    }
    users.clear();
    users.addAll(searchResults);
  }

  /**
   * Searches for users based on the provided email input.
   *
   * @param emailInput the email input to search for
   */
  public void searchUsersByEmail(String emailInput)
  {
    List<User> searchResults = new ArrayList<>();
    for (User user : users)
    {
      if (user.getEmail().toLowerCase().contains(emailInput.toLowerCase()))
      {
        searchResults.add(user);
      }
    }
    if (searchResults.isEmpty())
    {
      errorMsg.set("No user found");
      return;
    }
    else
    {
      errorMsg.set("");
    }
    users.clear();
    users.addAll(searchResults);
  }

  /**
   * Promotes a selected user to admin status.
   *
   * @param selectedUser the user to be promoted
   */
  public void promoteUserToAdmin(User selectedUser)
  {
    new Alert(Alert.AlertType.INFORMATION,
        "Are you sure you want to promote " + selectedUser.getUsername()
            + " to admin?").showAndWait().ifPresent(response -> {
      userClient.promoteToAdmin(selectedUser.getUsername());
    });
  }

  /**
   * Message property for displaying error messages.
   * This property is used to show messages in the UI.
   *
   * @return Observable String for error messages
   */
  public ObservableValue<String> messageProperty()
  {
    return errorMsg;
  }

  /**
   * Property for the username input field.
   * This property is used to bind the username input field in the UI.
   *
   * @param evt A PropertyChangeEvent object describing the event source
   *            and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("getAllUsers"))
    {
      users.clear();
      List<User> userList = JsonParser.toList(evt.getNewValue(), User[].class);
      users.addAll(userList);
      System.out.println("Users: " + users);
    }
    else if (evt.getPropertyName().equals("promote"))
    {
      errorMsg.set(evt.getNewValue() + " promoted to admin");
    }
  }
}