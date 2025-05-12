package ui.user;

import dtos.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

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

  public void initialize(ViewHandler viewHandler, UserGuiVM userGuiVM){
    this.userGuiVM=userGuiVM;
    this.viewHandler=viewHandler;
    // Bind the TableView to the ViewModel
    try
    {
      table.setItems(UserGuiVM.getUserList());
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
    UserGuiVM.bindSelectedProperty(
        table.getSelectionModel().selectedItemProperty());

    // Bind isAdmin message to the ViewModel
    isAdmin.textProperty().bind(UserGuiVM.getIsAdmin());

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
