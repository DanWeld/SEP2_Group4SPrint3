package ui.userList;

import dtos.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

import java.io.IOException;

public class UserListCtrl
{
  @FXML private TableView<User> table;
  @FXML private TableColumn<User, String> usernameColumn;
  @FXML private TableColumn<User, String> emailColumn;
  @FXML private TableColumn<User, Boolean> isAdminColumn;
  @FXML private Button search;
  @FXML private Button upgradeToAdmin;
  @FXML private Button back;
  @FXML private TextField userName;
  @FXML private TextField email;
  @FXML private Label messageLabel;

  private ViewHandler viewHandler;
  private UserListVM userListVM;
  private ObjectProperty<User> selectedUser;

  public UserListCtrl()
  {

  }

  @FXML public void initialize(ViewHandler viewHandler, UserListVM userListVM)
      throws IOException
  {
    this.userListVM = userListVM;
    this.viewHandler = viewHandler;
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
    emailColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getEmail()));

    isAdminColumn.setCellValueFactory(
        data -> new SimpleBooleanProperty(data.getValue().isAdmin()));
    // Bind the selected user to the ViewModel
    // Bind table items
    table.setItems(userListVM.getUserList());

    // Bind message
    messageLabel.textProperty().bind(userListVM.messageProperty());

    // Load users
    userListVM.getUserList();

    // Disable promote button if no user is selected or if selected user is already admin


  }

  @FXML public void onSearch()
  {
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
    System.out.println("Search button clicked");
  }

  @FXML public void onUpgradeToAdmin()
  {
    User selectedUser = table.getSelectionModel().getSelectedItem();
    if (selectedUser != null)
    {
      userListVM.upgradeUserToAdmin(selectedUser);
      table.refresh();
    }
    System.out.println("Upgrade to admin button clicked");
  }
  @FXML public void onReturn () {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
    System.out.println("Return button clicked");
  }
  @FXML public void errorMsg()
  {
    messageLabel.setText(userListVM.messageProperty().toString());
  }
  @FXML public void errorMsg(String msg)
  {
    messageLabel.setText(userListVM.messageProperty().toString());
  }

}