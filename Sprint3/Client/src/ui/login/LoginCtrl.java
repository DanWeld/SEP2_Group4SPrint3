package ui.login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.UserSession;
import startup.viewHandler.ViewHandler;

public class LoginCtrl
{
  @FXML private TextField emailField; // This will accept either email or username
  @FXML private TextField passwordField;
  @FXML private Label messageLabel;

  private LoginVM viewModel;
  private ViewHandler viewHandler;

  public LoginCtrl()
  {
  }

  public void initialize(LoginVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;
    emailField.textProperty().bindBidirectional(viewModel.credentialProperty()); // Updated to use credential property
    passwordField.textProperty()
        .bindBidirectional(viewModel.passwordProperty());
    messageLabel.textProperty().bind(viewModel.messageProperty());
    
    // Listen for login success and navigate to appropriate view
    viewModel.loginSuccessfulProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue) {
        try {
          System.out.println("DEBUG: Login successful property changed, navigating to dashboard");
          // Check if user is admin to determine where to navigate
          if (UserSession.getInstance().isAdmin()) {
            viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
          } else {
            viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
          }
        } catch (Exception e) {
          System.out.println("DEBUG: Error in login listener: " + e.getMessage());
          e.printStackTrace();
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
    
    // Force immediate navigation if login was successful
    // This helps in cases where the listener might not be triggered properly
    if (viewModel.loginSuccessfulProperty().get()) {
      try {
        System.out.println("DEBUG: Login successful, navigating to dashboard");
        if (UserSession.getInstance().isAdmin()) {
          viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
        } else {
          viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
        }
      } catch (Exception e) {
        System.out.println("DEBUG: Error navigating after login: " + e.getMessage());
        e.printStackTrace();
      }
    }
  }
}
