package ui.register;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserSession;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the registration view.
 * Handles user input for registration and communicates with the ViewModel.
 */
public class RegisterCtrl
{
  @FXML private TextField usernameField;
  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private TextField repeatField;
  @FXML private Label messageLabel;
  @FXML private Button buttonRegister;

  private RegisterVM viewModel;
  private ViewHandler viewHandler;

  /**
   * Constructor for RegisterCtrl.
   * Initializes the controller without any parameters.
   */
  public RegisterCtrl()
  {
  }

  /**
   * Initializes the RegisterCtrl with the provided ViewModel and ViewHandler.
   * Binds UI components to ViewModel properties and sets up listeners for user actions.
   *
   * @param vm The ViewModel for registration logic.
   * @param vh The ViewHandler for navigating between views.
   */
  public void initialize(RegisterVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;

    // Bind UI components to ViewModel properties
    emailField.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordField.textProperty()
        .bindBidirectional(viewModel.passwordProperty());
    repeatField.textProperty().bindBidirectional(viewModel.repeatProperty());
    usernameField.textProperty()
        .bindBidirectional(viewModel.usernameProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());

    // Disable the register button if the ViewModel indicates it's not ready
    buttonRegister.disableProperty()
        .bind(viewModel.enableRegisterButtonProperty());

    // Listen for registration success and navigate to the appropriate view
    viewModel.registrationSuccessfulProperty()
        .addListener((observable, oldValue, newValue) -> {
          if (newValue)
          {
            // Navigate to user dashboard after successful registration
            viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
          }
        });
  }

  /**
   * Handles the back button action.
   * Navigates the user back to the welcome view.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }

  /**
   * Handles the register button action.
   * Calls the ViewModel to perform the registration logic.
   */
  public void onRegister()
  {
    viewModel.register();
  }
}
