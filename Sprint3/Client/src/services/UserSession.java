package services;

import dtos.User;

/**
 * Singleton class that maintains the current user's session information.
 * This class stores the authenticated user's information during the application's runtime.
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private boolean isLoggedIn;

    private UserSession() {
        // Private constructor to enforce singleton pattern
        isLoggedIn = false;
        currentUser = null;
    }

    /**
     * Returns the singleton instance of UserSession.
     * @return The UserSession instance
     */
    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    /**
     * Sets the current logged in user and marks the session as logged in.
     * @param user The authenticated user
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.isLoggedIn = (user != null);
    }

    /**
     * Returns the current logged in user.
     * @return The current user, or null if no user is logged in
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Checks if a user is currently logged in.
     * @return true if a user is logged in, false otherwise
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }    /**
     * Logs out the current user by clearing the user data and setting isLoggedIn to false.
     */
    public void logout() {
        this.currentUser = null;
        this.isLoggedIn = false;
        System.out.println("User logged out");
    }

    /**
     * Checks if the current logged in user is an admin.
     * @return true if the current user is an admin, false otherwise
     */
    public boolean isAdmin() {
        return isLoggedIn && currentUser != null && currentUser.isAdmin();
    }
    
    /**
     * Gets the username of the current user.
     * @return The username of the current user, or null if no user is logged in
     */
    public String getUsername() {
        return isLoggedIn && currentUser != null ? currentUser.getUsername() : null;
    }
    
    /**
     * Gets the email of the current user.
     * @return The email of the current user, or null if no user is logged in
     */
    public String getEmail() {
        return isLoggedIn && currentUser != null ? currentUser.getEmail() : null;
    }
    
    /**
     * Gets the password of the current user.
     * @return The password of the current user, or null if no user is logged in
     */
    public String getPassword() {
        return isLoggedIn && currentUser != null ? currentUser.getPassword() : null;
    }
    
    /**
     * Gets the name of the current user.
     * @return The name of the current user, or null if no user is logged in
     */
    public String getName() {
        return isLoggedIn && currentUser != null ? currentUser.getName() : null;
    }
    
    /**
     * Sets the name of the current user.
     * @param name The new name
     */
    public void setName(String name) {
        if (isLoggedIn && currentUser != null) {
            currentUser.setName(name);
        }
    }
    
    /**
     * Sets the password of the current user.
     * @param password The new password
     */
    public void setPassword(String password) {
        if (isLoggedIn && currentUser != null) {
            currentUser.setPassword(password);
        }
    }
    
    /**
     * Sets a test user for debugging purposes
     * @param username The username
     * @param name The name
     * @param email The email
     * @param password The password
     * @param isAdmin Whether the user is an admin
     */
    public void setTestUser(String username, String name, String email, String password, boolean isAdmin) {
        User user = new User(name, username, email, password, isAdmin);
        setCurrentUser(user);
        System.out.println("Test user set: " + username);
    }
}
