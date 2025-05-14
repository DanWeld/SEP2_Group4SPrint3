package ui.userToAdminUi;

import dtos.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

import java.io.IOException;

public class UserGuiController
{
  @FXML private TableView<User> table;
  @FXML private TableColumn<User, String> userNameCollum;
  @FXML private TableColumn<User, String> emailCollum;
  @FXML private Button search;
  @FXML private Button upgradeToAdmin;
  @FXML private Button back;
  @FXML private TextField userName;
  @FXML private TextField email;
  @FXML private Label isAdmin;

  private ViewHandler viewHandler;
  private UserGuiVM userGuiVM;
  private ObjectProperty<User> selectedUser;

  public UserGuiController(){

  }
@FXML
  public void initialize(ViewHandler viewHandler, UserGuiVM userGuiVM)
    throws IOException
{
    this.userGuiVM=userGuiVM;
    this.viewHandler=viewHandler;
    // Bind the TableView to the ViewModel
    try
    {
      table.setItems(userGuiVM.getUserList());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    // Set up the columns
    userNameCollum.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getUsername()));
    emailCollum.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEmail()));

    // Bind the selected property to the ViewModel
    userGuiVM.bindSelectedUser(
        table.getSelectionModel().selectedItemProperty());

    // Bind isAdmin message to the ViewModel
  try
  {
    isAdmin.textProperty().bind(userGuiVM.getIsAdmin());
  }
  catch (IOException e)
  {
    throw new RuntimeException(e);
  }

}
@FXML
  public void onSearch(){
    String usernameInput = userName.getText();
    String emailInput = email.getText();
    userGuiVM.searchUsers(usernameInput, emailInput);
  }

  @FXML
  public void onUpgradeToAdmin()
  {
    if (table.getSelectionModel().getSelectedItem() != null)
    {
      userGuiVM.upgradeUserToAdmin(table.getSelectionModel().getSelectedItem());
    }
  }

  @FXML
  public void onReturn(){
    viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
  }




}
