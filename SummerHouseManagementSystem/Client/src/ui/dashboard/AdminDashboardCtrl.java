package ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserSession;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Admin Dashboard view.
 * This class handles the user interactions and updates the UI based on the ViewModel.
 */
public class AdminDashboardCtrl
{
  @FXML private Label welcomeLabel;

  private ViewHandler viewHandler;

  /**
   * Default constructor for AdminDashboardCtrl.
   * Initializes the controller without any parameters.
   */
  public AdminDashboardCtrl()
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

    // Set welcome message with admin's name
    if (UserSession.getInstance().isLoggedIn())
    {
      welcomeLabel.setText(
          "Welcome, Admin " + UserSession.getInstance().getCurrentUser()
              .getUsername());
    }
  }

  /**
   * Handles the action when the "Manage Profile" button is clicked.
   * Navigates to the User profile.
   */
  @FXML public void onManageProfile()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_PROFILE);
  }

  /**
   * Handles the action when the "Manage Users" button is clicked.
   * Navigates to the Admin User List view.
   */
  @FXML public void onManageUsers()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_LIST);
  }

  /**
   * Views the list of Bookings.
   * This method is called when the "View Bookings" button is clicked.
   * It navigates to the Admin Property List view where all properties and their bookings can be managed.
   */
  @FXML public void onViewBookings()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
  }

  /**
   * Handles logout action.
   * This method clears the user session and navigates back to the welcome screen.
   */
  @FXML public void onLogout()
  {
    // Clear the user session
    UserSession.getInstance().logout();

    // Return to welcome screen
    viewHandler.showView(ViewHandler.ViewType.WELCOME);
  }
}
