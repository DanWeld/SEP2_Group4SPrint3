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
        // Direct to Admin Property List view
        viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
    }
    
    @FXML
    public void onManageUsers() {
        // Direct to Admin User List view
        viewHandler.showView(ViewHandler.ViewType.ADMIN_USER_LIST);
    }
    
    @FXML
    public void onViewBookings() {
        // Implement bookings view
        viewHandler.showView(ViewHandler.ViewType.PROPERTY_LIST);
    }
    
    @FXML
    public void onViewReports() {
        // Implement reports view
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
