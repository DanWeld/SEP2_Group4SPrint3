package ui.welcome;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import startup.viewHandler.ViewHandler;

public class FrontViewCtrl
{
  @FXML private Button buttonRegister;
  @FXML private Button buttonLogin;
  private ViewHandler viewHandler;

  public FrontViewCtrl()
  {
  }

  public void initialize(ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    buttonRegister.setOnAction(e -> openRegister());
    buttonLogin.setOnAction(e -> openLogin());
  }

  public void openLogin()
  {
    viewHandler.showView(ViewHandler.ViewType.LOGIN);
  }

  public void openRegister()
  {
    viewHandler.showView(ViewHandler.ViewType.REGISTER);
  }
}
