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

  /**
   * Constructor for UserProfileCtrl
   */
  public UserProfileCtrl()
  {
    // Empty constructor
  }

  /**
   * Initializes the UserProfileCtrl.
   * @param vm the ViewModel for the UserProfile view
   * @param vh the ViewHandler for handling view changes
   */
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

  /**
   * Initializes the UserProfileCtrl with the current user data.
   * This method is called by the JavaFX framework to initialize the controller.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  /**
   * Saves the updated user profile.
   * This method is called when the user clicks the save button.
   */
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

  /**
   * Deletes the user account.
   * This method is called when the user clicks the delete account button.
   */
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
