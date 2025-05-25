package ui.userProfile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the User Profile view
 */
public class UserProfileCtrl
{
  @FXML private Label usernameLabel;
  @FXML private Label emailLabel;
  @FXML private Label userTypeLabel;
  @FXML private Label messageLabel;
  @FXML private TextField newPasswordField;
  @FXML private TextField repeatPasswordField;
  @FXML private Button buttonSave;

  private UserProfileVM viewModel;
  private ViewHandler viewHandler;

  public UserProfileCtrl()
  {
    // Empty constructor
  }

  public void initialize(UserProfileVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;

    // Bind properties
    usernameLabel.textProperty().bind(viewModel.usernameProperty());
    emailLabel.textProperty().bind(viewModel.emailProperty());
    userTypeLabel.textProperty().bind(viewModel.userTypeProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());
    newPasswordField.textProperty()
        .bindBidirectional(viewModel.passwordProperty());
    repeatPasswordField.textProperty()
        .bindBidirectional(viewModel.repeatProperty());

    buttonSave.disableProperty().bind(viewModel.enableRegisterButtonProperty());
  }

  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  public void onSave()
  {
    new Alert(Alert.AlertType.CONFIRMATION,
        "Profile updated successfully!").showAndWait().ifPresent(response -> {
      if (response == ButtonType.OK)
      {
        viewModel.updateUser();
      }
    });
  }

  public void onDeleteAccount()
  {
    new Alert(Alert.AlertType.CONFIRMATION,
        "Are you sure you want to delete your account? This action cannot be undone.").showAndWait()
        .ifPresent(response -> {
          if (response == ButtonType.OK)
          {
            viewModel.deleteUser();
            viewHandler.showView(ViewHandler.ViewType.WELCOME);
          }
        });
  }
}
