package ui.dashboard;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import services.UserSession;
import startup.viewHandler.ViewHandler;

public class AdminDashboardCtrl {
    @FXML private Label welcomeLabel;
    
    private ViewHandler viewHandler;
    
    public AdminDashboardCtrl() {
        // Empty constructor
    }
      public void initialize(ViewHandler viewHandler) {
        this.viewHandler = viewHandler;
        
        // Set welcome message with admin's name
        if (UserSession.getInstance().isLoggedIn()) {
            welcomeLabel.setText("Welcome, Admin " + UserSession.getInstance().getCurrentUser().getUsername());
        }
    }
    
    @FXML
    public void onManageProperties() {
        // TODO: Implement property management view
        // For now, just show property list
        viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
    }
    
    @FXML
    public void onManageUsers() {
        // TODO: Implement user management view
        // For now, just show property list
        viewHandler.showView(ViewHandler.ViewType.USER_LIST);
    }
    
    @FXML
    public void onViewBookings() {
        // TODO: Implement bookings view
        // For now, just show property list
        viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
    }
    
    @FXML
    public void onLogout() {
        // Clear the user session
        UserSession.getInstance().logout();
        
        // Return to welcome screen
        viewHandler.showView(ViewHandler.ViewType.WELCOME);
    }
}
