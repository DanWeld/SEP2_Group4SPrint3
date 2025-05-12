package ui.adminUserList;

import dtos.User;
import javafx.beans.value.ObservableBooleanValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Admin User List view
 */
public class AdminUserListCtrl {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, Boolean> isAdminColumn;
    @FXML private Button promoteButton;
    @FXML private Label messageLabel;
    
    private AdminUserListVM viewModel;
    private ViewHandler viewHandler;
    
    public AdminUserListCtrl() {
        // Empty constructor
    }
      public void initialize(AdminUserListVM vm, ViewHandler vh) {
        this.viewModel = vm;
        this.viewHandler = vh;
        
        // Set up table columns
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        // Set static values for firstName and lastName since the User class doesn't have these properties
        firstNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("N/A"));
        lastNameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("N/A"));
        isAdminColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isAdmin()).asObject());
        
        // Bind table items
        userTable.setItems(viewModel.getUsers());
        
        // Bind message
        messageLabel.textProperty().bind(viewModel.messageProperty());
        
        // Load users
        viewModel.loadUsers();
        
        // Disable promote button if no user is selected or if selected user is already admin
        promoteButton.disableProperty().bind(
            userTable.getSelectionModel().selectedItemProperty().isNull()
                .or((ObservableBooleanValue) userTable.getSelectionModel().selectedItemProperty()
                    .map(User::isAdmin).orElse(true))
        );
    }
    
    @FXML
    public void onPromoteToAdmin() {
        User selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            viewModel.promoteToAdmin(selectedUser);
            userTable.refresh();
        }
    }
    
    @FXML
    public void onBack() {
        viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
    }
}
