package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserSession;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Login view
 * Handles user input and interactions with the LoginVM
 *
 * @author Group 4
 * @version 1.0
 */
public class LoginCtrl
{
  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private Label messageLabel;
  @FXML private Button loginButton;

  private LoginVM viewModel;
  private ViewHandler viewHandler;

  /**
   * Default constructor for LoginCtrl
   * Initializes the controller without any parameters
   */
  public LoginCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler
   * Binds UI components to the ViewModel properties
   * Sets up listeners for login success and button enablement
   *
   * @param vm the LoginVM instance to bind to
   * @param vh the ViewHandler instance to navigate views
   */
  public void initialize(LoginVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;
    emailField.textProperty().bindBidirectional(viewModel.emailProperty());
    passwordField.textProperty()
        .bindBidirectional(viewModel.passwordProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());

    // Bind the login button to the view model's property
    loginButton.disableProperty().bind(viewModel.getLoginBtnEnabledProp().not());
    
    // Listen for login success and navigate to the appropriate view
    viewModel.loginSuccessfulProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        // Check if user is admin to determine where to navigate
        if (UserSession.getInstance().isAdmin()) {
          viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
        } else {
          viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
        }
      }
    });
  }

  /**
   * Handles the back button action
   * Navigates the user back to the welcome view
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }

  /**
   * Handles the login button action
   * Invokes the login method on the ViewModel to process user credentials
   */
  public void onLogin()
  {
    viewModel.login();
  }
}
