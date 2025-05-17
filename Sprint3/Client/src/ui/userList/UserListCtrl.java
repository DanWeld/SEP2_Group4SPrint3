package ui.userList;

import dtos.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

import java.io.IOException;

public class UserListCtrl
{
  @FXML private TableView<User> table;
  @FXML private TableColumn<User, String> usernameColumn;
  @FXML private TableColumn<User, String> emailColumn;
  @FXML private TableColumn<User, String> isAdminColumn;
  @FXML private Button search;
  @FXML private Button upgradeToAdmin;
  @FXML private Button back;
  @FXML private TextField userName;
  @FXML private TextField email;

  private ViewHandler viewHandler;
  private UserListVM userListVM;
  private ObjectProperty<User> selectedUser;

  public UserListCtrl(){

  }
@FXML
  public void initialize(ViewHandler viewHandler, UserListVM userListVM)
    throws IOException
{
    this.userListVM = userListVM;
    this.viewHandler=viewHandler;
    // Bind the TableView to the ViewModel
    try
    {
      table.setItems(userListVM.getUserList());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    // Set up the columns
    usernameColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getUsername()));
    emailColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEmail()));
    isAdminColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().isAdmin() ? "Yes" : "No"));

    // Bind the selected property to the ViewModel
    userListVM.bindSelectedUser(
        table.getSelectionModel().selectedItemProperty());


}
@FXML
  public void onSearch(){
    String usernameInput = userName.getText();
    String emailInput = email.getText();
    userListVM.searchUsers(usernameInput);
    // If username is empty, search by email
    if (usernameInput.isEmpty())
    {
      userListVM.searchUsersByEmail(emailInput);
    }
    // If both username and email are empty, show all users
    else if (usernameInput.isEmpty() && emailInput.isEmpty())
    {
      userListVM.getUserList();
    }
  }

  @FXML
  public void onUpgradeToAdmin()
  {
    if (table.getSelectionModel().getSelectedItem() != null)
    {
      userListVM.upgradeUserToAdmin(table.getSelectionModel().getSelectedItem());
    }
  }

  @FXML
  public void onReturn(){
    viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
  }




}
