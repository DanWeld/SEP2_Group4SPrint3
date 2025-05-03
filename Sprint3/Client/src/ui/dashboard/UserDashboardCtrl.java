package ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserSession;
import startup.viewHandler.ViewHandler;

public class UserDashboardCtrl {
    @FXML private Label welcomeLabel;
    
    private ViewHandler viewHandler;
    
    public UserDashboardCtrl() {
        // Empty constructor
    }
      public void initialize(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        
        // Set welcome message with user's name
        if (UserSession.getInstance().isLoggedIn()) {
            welcomeLabel.setText("Welcome, " + UserSession.getInstance().getCurrentUser().getUsername());
        }
    }
    
    @FXML
    public void onBrowseProperties() {
        viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
    }
    
    @FXML
    public void onViewBookings() {
        // TODO: Implement bookings view
        // For now, just show property list
        viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
    }
    
    @FXML
    public void onViewProfile() {
        // TODO: Implement profile view
        // For now, just show property list
        viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
    }
    
    @FXML
    public void onLogout() {
        // Clear the user session
        UserSession.getInstance().logout();
        
        // Return to welcome screen
        viewHandler.showView(ViewHandler.ViewType.WELCOME);
    }
}
