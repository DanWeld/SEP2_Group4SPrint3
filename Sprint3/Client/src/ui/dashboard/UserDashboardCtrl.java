package ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserSession;
import startup.viewHandler.ViewHandler;

public class UserDashboardCtrl
{
  @FXML private Label welcomeLabel;

  private ViewHandler viewHandler;

  public UserDashboardCtrl()
  {
    // Empty constructor
  }

  public void initialize(ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;

    // Set welcome message with user's name
    if (UserSession.getInstance().isLoggedIn())
    {
      welcomeLabel.setText(
          "Welcome, " + UserSession.getInstance().getCurrentUser()
              .getUsername());
    }
  }

  @FXML public void onBrowseProperties()
  {
    viewHandler.showView(ViewHandler.ViewType.SPECIFY_DATES);
  }

  @FXML public void onViewPastBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.PAST_BOOKINGS);
  }

  @FXML public void onViewCurrentBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.CURRENT_BOOKINGS);
  }

  @FXML public void onViewFutureBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.FUTURE_BOOKINGS);
  }

  @FXML public void onViewProfile()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_PROFILE);
  }

  @FXML public void onLogout()
  {
    // Clear the user session
    UserSession.getInstance().logout();

    // Return to welcome screen
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }
}
