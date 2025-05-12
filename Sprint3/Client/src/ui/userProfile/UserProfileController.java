package ui.userProfile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller for the user profile view
 */
public class UserProfileController {
    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private TextArea bioTextArea;
    @FXML private TextField phoneTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField nameTextField;
    @FXML private Label errorLabel;
    
    @FXML private VBox profileViewBox;
    @FXML private VBox editProfileBox;
    @FXML private VBox changePasswordBox;
    
    @FXML private Button editButton;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button changePasswordButton;
    @FXML private Button deleteAccountButton;
    
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Button submitPasswordButton;
    @FXML private Button cancelPasswordButton;
    
    private UserProfileVM viewModel;
    
    public void init(UserProfileVM viewModel) {
        this.viewModel = viewModel;
        
        // Bind properties
        nameLabel.textProperty().bind(viewModel.nameProperty());
        usernameLabel.textProperty().bind(viewModel.usernameProperty());
        emailLabel.textProperty().bind(viewModel.emailProperty());
        bioTextArea.textProperty().bindBidirectional(viewModel.bioProperty());
        phoneTextField.textProperty().bindBidirectional(viewModel.phoneNumberProperty());
        addressTextField.textProperty().bindBidirectional(viewModel.addressProperty());
        nameTextField.textProperty().bindBidirectional(viewModel.nameProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
        
        // Bind visibility
        editProfileBox.visibleProperty().bind(viewModel.editModeProperty());
        profileViewBox.visibleProperty().bind(viewModel.editModeProperty().not());
        changePasswordBox.setVisible(false);
        
        // Initialize password fields
        currentPasswordField.textProperty().bindBidirectional(viewModel.currentPasswordProperty());
        newPasswordField.textProperty().bindBidirectional(viewModel.newPasswordProperty());
        confirmPasswordField.textProperty().bindBidirectional(viewModel.confirmPasswordProperty());
        
        // Load initial profile data
        viewModel.loadProfile();
    }
    
    @FXML
    private void onEditClick() {
        viewModel.enterEditMode();
    }
    
    @FXML
    private void onSaveClick() {
        viewModel.saveProfile();
    }
    
    @FXML
    private void onCancelClick() {
        viewModel.cancelEdit();
    }
    
    @FXML
    private void onChangePasswordClick() {
        changePasswordBox.setVisible(true);
        profileViewBox.setVisible(false);
        editProfileBox.setVisible(false);
    }
    
    @FXML
    private void onSubmitPasswordClick() {
        viewModel.changePassword();
        if (errorLabel.getText().isEmpty()) {
            // If no error, hide the change password box and show the profile view
            changePasswordBox.setVisible(false);
            profileViewBox.setVisible(true);
        }
    }
    
    @FXML
    private void onCancelPasswordClick() {
        // Clear password fields
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
        
        // Hide the change password box and show the profile view
        changePasswordBox.setVisible(false);
        profileViewBox.setVisible(true);
    }
    
    @FXML
    private void onDeleteAccountClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Account");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");
        
        // Add buttons to the alert
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
        
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == buttonTypeYes) {
                boolean success = viewModel.deleteAccount();
                if (success) {
                    // Navigate to the login screen
                    // This will be handled by the view handler
                }
            }
        });
    }
}
