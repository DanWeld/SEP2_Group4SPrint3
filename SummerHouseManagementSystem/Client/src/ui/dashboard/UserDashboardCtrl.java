package ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserSession;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the user Dashboard view.
 * This class handles the user interactions and updates the UI based on the ViewModel.
 */
public class UserDashboardCtrl
{
  @FXML private Label welcomeLabel;

  private ViewHandler viewHandler;

  /**
   * Default constructor for UserDashboardCtrl.
   * Initializes the controller without any parameters.
   */
  public UserDashboardCtrl()
  {
    // Empty constructor
  }

  /**
   * Initializes the controller with the provided ViewHandler.
   * Sets up the welcome message and binds UI components to the ViewModel properties.
   *
   * @param viewHandler the ViewHandler for navigating between views
   */
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

  /**
   * Handles the action when the "Browse Properties" button is clicked.
   * Navigates to the Specify Dates view.
   */
  @FXML public void onBrowseProperties()
  {
    viewHandler.showView(ViewHandler.ViewType.SPECIFY_DATES);
  }

  /**
   * Handles the action when the "View Past Bookings" button is clicked.
   * Navigates to the Past Bookings view.
   */
  @FXML public void onViewPastBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.PAST_BOOKINGS);
  }

  /**
   * Handles the action when the "View Current Bookings" button is clicked.
   * Navigates to the Current Bookings view.
   */
  @FXML public void onViewCurrentBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.CURRENT_BOOKINGS);
  }

  /**
   * Handles the action when the "View Future Bookings" button is clicked.
   * Navigates to the Future Bookings view.
   */
  @FXML public void onViewFutureBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.FUTURE_BOOKINGS);
  }

  /**
   * Handles the action when the "Manage Profile" button is clicked.
   * Navigates to the User profile.
   */
  @FXML public void onViewProfile()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_PROFILE);
  }

  /**
   * Handles the action when the "Logout" button is clicked.
   * Clears the user session and navigates back to the welcome screen.
   */
  @FXML public void onLogout()
  {
    // Clear the user session
    UserSession.getInstance().logout();

    // Return to welcome screen
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }
}
