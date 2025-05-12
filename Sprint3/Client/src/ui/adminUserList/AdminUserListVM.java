package ui.adminUserList;

import dtos.User;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Admin User List view
 */
public class AdminUserListVM {
    private final ObservableList<User> users;
    private final StringProperty message;
    
    public AdminUserListVM() {
        users = FXCollections.observableArrayList();
        message = new SimpleStringProperty("");
    }
      /**
     * Loads the list of users
     */
    public void loadUsers() {
        // TODO: Implement fetching users from server
        // For now, use dummy data
        users.clear();
        users.add(new User("user1", "user1@example.com", "password", false));
        users.add(new User("user2", "user2@example.com", "password", false));
        users.add(new User("admin1", "admin1@example.com", "password", true));
    }
    
    /**
     * Promotes a user to admin
     * @param user The user to promote
     * @return true if successful, false otherwise
     */
    public boolean promoteToAdmin(User user) {
        if (user == null) {
            message.set("No user selected");
            return false;
        }
        
        if (user.isAdmin()) {
            message.set("User is already an admin");
            return false;
        }
        
        // TODO: Implement promoting user to admin on server
        // For now, just update locally
        user.setAdmin(true);
        message.set("User promoted to admin successfully");
        return true;
    }
    
    public ObservableList<User> getUsers() {
        return users;
    }
    
    public StringProperty messageProperty() {
        return message;
    }
}
