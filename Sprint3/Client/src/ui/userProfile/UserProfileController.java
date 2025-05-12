package ui.userProfile;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

/**
 * Controller for the user profile view
 */
public class UserProfileController {    @FXML private Label nameLabel;
    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private TextField phoneTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField nameTextField;
    @FXML private Label errorLabel;
    
    @FXML private Button extendBookingButton;
    
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
        
        // Bind properties        nameLabel.textProperty().bind(viewModel.nameProperty());
        usernameLabel.textProperty().bind(viewModel.usernameProperty());
        emailLabel.textProperty().bind(viewModel.emailProperty());
        // We removed the bioTextArea from the UI
        phoneTextField.textProperty().bindBidirectional(viewModel.phoneNumberProperty());
        addressTextField.textProperty().bindBidirectional(viewModel.addressProperty());
        nameTextField.textProperty().bindBidirectional(viewModel.nameProperty());
        errorLabel.textProperty().bind(viewModel.errorMessageProperty());
          // Make sure error label is initially clear
        viewModel.clearErrorMessage();
          // Bind visibility - using boolean bindings for more complex logic
        editProfileBox.visibleProperty().bind(viewModel.editModeProperty().and(viewModel.passwordChangeModeProperty().not()));
        profileViewBox.visibleProperty().bind(viewModel.editModeProperty().not().and(viewModel.passwordChangeModeProperty().not()));
        changePasswordBox.visibleProperty().bind(viewModel.passwordChangeModeProperty());
        
        // Initialize password fields
        currentPasswordField.textProperty().bindBidirectional(viewModel.currentPasswordProperty());
        newPasswordField.textProperty().bindBidirectional(viewModel.newPasswordProperty());
        confirmPasswordField.textProperty().bindBidirectional(viewModel.confirmPasswordProperty());
        
        // Load initial profile data
        viewModel.loadProfile();
    }
      @FXML
    private void onEditClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.enterEditMode();
    }
      @FXML
    private void onSaveClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.saveProfile();
    }
      @FXML
    private void onCancelClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.cancelEdit();
    }
      @FXML
    private void onChangePasswordClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.enterPasswordChangeMode(); // Use viewModel to handle state
    }
    
    @FXML
    private void onSubmitPasswordClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.changePassword();
        if (errorLabel.getText().isEmpty()) {
            // If no error, exit password change mode
            viewModel.exitPasswordChangeMode();
        }
    }
    
    @FXML
    private void onCancelPasswordClick() {
        viewModel.clearErrorMessage(); // Clear any error messages
        viewModel.exitPasswordChangeMode(); // Use viewModel to handle state
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
    
    @FXML
    private void onExtendBookingClick() {
        viewModel.navigateToExtendBooking();
    }
}
