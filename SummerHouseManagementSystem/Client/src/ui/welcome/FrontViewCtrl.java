package ui.welcome;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Front View, which serves as the entry point for the application.
 * It provides options to navigate to the login and registration views.
 */
public class FrontViewCtrl
{
  @FXML private Button buttonRegister;
  @FXML private Button buttonLogin;
  private ViewHandler viewHandler;

  /**
   * Default constructor for FrontViewCtrl.
   * Initializes the controller without any specific ViewModel.
   */
  public FrontViewCtrl()
  {
  }

  /**
   * Initializes the FrontViewCtrl with the provided ViewHandler.
   * This method sets up the event handlers for the buttons.
   *
   * @param viewHandler The ViewHandler to manage view transitions.
   */
  public void initialize(ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    buttonRegister.setOnAction(e -> openRegister());
    buttonLogin.setOnAction(e -> openLogin());
  }

  /**
   * Opens the login view.
   * This method is called when the login button is clicked.
   */
  public void openLogin()
  {
    viewHandler.showView(ViewHandler.ViewType.LOGIN);
  }

  /**
   * Opens the registration view.
   * This method is called when the register button is clicked.
   */
  public void openRegister()
  {
    viewHandler.showView(ViewHandler.ViewType.REGISTER);
  }
}
