package ui.profile;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the User Profile view
 */
public class UserProfileCtrl {
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label firstNameLabel;
    @FXML private Label lastNameLabel;
    @FXML private Label userTypeLabel;
    @FXML private Label messageLabel;
    
    private UserProfileVM viewModel;
    private ViewHandler viewHandler;
    
    public UserProfileCtrl() {
        // Empty constructor
    }
    
    public void initialize(UserProfileVM vm, ViewHandler vh) {
        this.viewModel = vm;
        this.viewHandler = vh;
        
        // Bind properties
        usernameLabel.textProperty().bind(viewModel.usernameProperty());
        emailLabel.textProperty().bind(viewModel.emailProperty());
        firstNameLabel.textProperty().bind(viewModel.firstNameProperty());
        lastNameLabel.textProperty().bind(viewModel.lastNameProperty());
        userTypeLabel.textProperty().bind(viewModel.userTypeProperty());
        messageLabel.textProperty().bind(viewModel.messageProperty());
    }
    
    public void onBack() {
        viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
    }
}
