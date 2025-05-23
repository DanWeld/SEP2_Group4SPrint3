package ui.userList;

import dtos.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import networking.Client;
import networking.userClient.UserClient;
import networking.userClient.UserClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class UserListVM implements PropertyChangeListener
{

  private ObservableList<User> users;
  private StringProperty username;
  private StringProperty email;
  private SimpleObjectProperty<User> selectedUser;
  private StringProperty isAdmin;
  private UserClient userClient;
  private StringProperty errorMsg;
  private Client client;

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
      Client client = new Client();
      this.userClient = new UserClientImpl(client);
      userClient.addPropertyChangeListener(this);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    userClient.getAllUsers();
  }

  public ObservableList<User> getUserList()
  {
    return users;
  }

  public SimpleObjectProperty<User> getSelectedUser()
  {
    if (selectedUser.getValue() == null)
    {
      errorMsg.set("No User selected");
    }
    return selectedUser;
  }

  public void bindSelectedUser(
      ReadOnlyObjectProperty<User> selectedUserFromTable)
  {
    selectedUser.bind(selectedUserFromTable);
  }

  public void searchUsers(String usernameInput)
  {
    List<User> searchResults = new ArrayList<>();
    for (User user : users)
    {
      if (user.getUsername().toLowerCase().contains(usernameInput.toLowerCase()))
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

  public void promoteUserToAdmin(User selectedUser)
  {
    new Alert(Alert.AlertType.INFORMATION,
        "Are you sure you want to promote " + selectedUser.getUsername()
            + " to admin?").showAndWait().ifPresent(response -> {
      userClient.promoteToAdmin(selectedUser.getUsername());
    });
  }

  public ObservableValue<String> messageProperty()
  {
    return errorMsg;
  }

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