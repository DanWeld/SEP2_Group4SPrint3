package ui.userToAdminUi;

import dtos.Property;
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

public class UserGuiVM implements PropertyChangeListener
{

  private  ObservableList<User> users;
  private StringProperty username;
  private StringProperty email;
  private SimpleObjectProperty<User> selectedUser;
  private  StringProperty isAdmin;
  private CustomerListClient userClient;
  private StringProperty errorMsg;
  private Client client;

  public UserGuiVM()
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
      if (client.isAdmin(email.toString())){
        isAdmin.set("Yes, user is admin");
      }
      else{
        isAdmin.set("No user is not admin");
      }
    return null;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {

  }

  public void searchUsers(String usernameInput, String emailInput)
  {
  }

  public void upgradeUserToAdmin(User selectedItem)
  {
  }
}
