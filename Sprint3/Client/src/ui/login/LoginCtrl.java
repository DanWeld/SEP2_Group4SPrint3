package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserSession;
import startup.viewHandler.ViewHandler;

public class LoginCtrl
{
  @FXML private TextField emailField;
  @FXML private TextField passwordField;
  @FXML private Label messageLabel;
  @FXML private Button loginButton;

  private LoginVM viewModel;
  private ViewHandler viewHandler;

  public LoginCtrl()
  {
  }

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

  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }

  public void onLogin()
  {
    viewModel.login();
  }
}
