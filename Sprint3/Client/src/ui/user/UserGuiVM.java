package ui.user;

import dtos.User;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.User.CustomerListClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UserGuiVM implements PropertyChangeListener
{

  private  ObservableList<User> users;
  private StringProperty username;
  private StringProperty email;
  private SimpleObjectProperty<User> selectedUser;
  private  StringProperty isAdmin;
  private CustomerListClientImpl userClient;

  public UserGuiVM(CustomerListClientImpl userClient)
  {
    this.userClient = userClient;
    this.users = FXCollections.observableArrayList();
    this.username = new SimpleStringProperty();
    this.email = new SimpleStringProperty();
    this.selectedUser = new SimpleObjectProperty<>();
    this.isAdmin = new SimpleStringProperty();
  }
  public ObservableList<User> getUserList()
  {
    return users ;
  }

  public void bindSelectedProperty(ReadOnlyObjectProperty<User> userReadOnlyObjectProperty)
  {
    selectedUser.bind(userReadOnlyObjectProperty);
    selectedUser.addListener((obs, oldUser, newUser) -> {
      if (newUser != null)
      {
        isAdmin.set(newUser.isAdmin() ? "Yes" : "No");
      }
      else
      {
        isAdmin.set("");
      }
    });
  }

  public  ObservableValue<String> getIsAdmin()
  {


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
