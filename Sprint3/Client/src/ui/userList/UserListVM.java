package ui.userList;

import dtos.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.userListToAdmin.CustomerListClient;
import networking.userListToAdmin.CustomerListClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class UserListVM implements PropertyChangeListener
{

  private  ObservableList<User> users;
  private StringProperty username;
  private StringProperty email;
  private SimpleObjectProperty<User> selectedUser;
  private  StringProperty isAdmin;
  private CustomerListClient userClient;
  private StringProperty errorMsg;
  private Client client;

  public UserListVM()
  {
    this.users = FXCollections.observableArrayList();
    this.username = new SimpleStringProperty();
    this.email = new SimpleStringProperty();
    this.selectedUser = new SimpleObjectProperty<>();
    this.isAdmin = new SimpleStringProperty();
    try
    {
      Client client = new Client();
      this.userClient = new CustomerListClientImpl(client);
      client.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
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

  public void bindSelectedUser(ReadOnlyObjectProperty<User> selectedUserFromTable)
  {
    selectedUser.bind(selectedUserFromTable);
  }

  public  ObservableValue<String> getIsAdmin() throws IOException
  {
    if (getSelectedUser().getValue().isAdmin())
    {
      isAdmin.set("yes");
    }

     return isAdmin;

  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("userList"))
    {
      users.clear();
      users.addAll((ObservableList<User>) evt.getNewValue());
    }
    else if (evt.getPropertyName().equals("isAdmin"))
    {
      isAdmin.set((String) evt.getNewValue());
    }
  }

  public void searchUsers(String usernameInput)
  {
    for (User user:users)
    {
      if (user.getUsername().equals(usernameInput))
      {
        users.clear();
        users.add(user);
      }
      else
      {
        errorMsg.set("No user found");
      }
    }
  }
  public void searchUsersByEmail(String emailInput)
  {
    for (User user:users)
    {
      if (user.getEmail().equals(emailInput))
      {
        users.clear();
        users.add(user);
      }
      else
      {
        errorMsg.set("No user found");
      }
    }
  }

  public void upgradeUserToAdmin(User selectedItem)
  {

      try
      {
        userClient.upgradeToAdmin(selectedItem.getUsername());
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }

  }

