package services.auth;

import dtos.User;
import persistence.daos.user.UserDAO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the AuthenticationService interface
 */
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDAO userDAO;
    private final Map<String, User> loggedInUsers; // Email -> User

    public AuthenticationServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.loggedInUsers = new HashMap<>();
    }    @Override
    public String authenticate(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return "Email cannot be empty";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        
        try {
            User user = userDAO.getUserByEmail(email);
            
            if (user == null) {
                return "Invalid login credentials. Please try again.";
            }
            
            if (!user.getPassword().equals(password)) {
                return "Invalid login credentials. Please try again.";
            }
            
            // User authenticated successfully, add to logged in users
            loggedInUsers.put(email, user);
            
            return "Ok";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    @Override
    public String authenticateByUsername(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Username cannot be empty";
        }
        
        if (password == null || password.trim().isEmpty()) {
            return "Password cannot be empty";
        }
        
        try {
            User user = userDAO.getUserByUsername(username);
            
            if (user == null) {
                return "Invalid login credentials. Please try again.";
            }
            
            if (!user.getPassword().equals(password)) {
                return "Invalid login credentials. Please try again.";
            }
            
            // User authenticated successfully, add to logged in users
            loggedInUsers.put(user.getEmail(), user);
            
            return "Ok";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    @Override
    public String registerUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "Username cannot be empty";
        }
        
        // Validate email format
        String emailValidation = validateEmail(user.getEmail());
        if (!emailValidation.equals("OK")) {
            return emailValidation;
        }
        
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "Password cannot be empty";
        }
        
        // Check password strength with detailed feedback
        if (!isPasswordStrong(user.getPassword())) {
            return "Password must be at least 8 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character";
        }
        
        // Check if username is unique
        if (!isUsernameUnique(user.getUsername())) {
            return "Username is already taken";
        }
        
        // Check if email is unique
        if (!isEmailUnique(user.getEmail())) {
            return "Email is already registered";
        }
        
        // Save the user
        boolean success = userDAO.saveUser(user);
        
        if (success) {
            return "Ok";
        } else {
            return "Failed to register user. Please try again.";
        }
    }    @Override
    public boolean isUsernameUnique(String username) {
        try {
            return userDAO.getUserByUsername(username) == null;
        } catch (SQLException e) {
            System.out.println("Error checking username uniqueness: " + e.getMessage());
            return false; // Default to not unique if there's an error
        }
    }

    @Override
    public boolean isEmailUnique(String email) {
        try {
            return userDAO.getUserByEmail(email) == null;
        } catch (SQLException e) {
            System.out.println("Error checking email uniqueness: " + e.getMessage());
            return false; // Default to not unique if there's an error
        }
    }
      @Override
    public boolean isAdmin(String email) {
        try {
            User user = userDAO.getUserByEmail(email);
            return user != null && user.isAdmin();
        } catch (SQLException e) {
            System.out.println("Error checking admin status: " + e.getMessage());
            return false; // Default to non-admin if there's an error
        }
    }
    
    /**
     * Validates the email format
     * @param email Email to validate
     * @return "OK" if valid, error message otherwise
     */
    private String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (!email.contains("@") || !email.contains(".")) {
            return "Email must be in a correct format.";
        }
        return "OK";
    }
    
    /**
     * Checks if password contains both uppercase and lowercase letters
     * @param password Password to check
     * @return True if contains both, false otherwise
     */
    private boolean containsUpperCaseAndLowerCase(String password) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            }
            if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            }
            if (hasUpperCase && hasLowerCase) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if password contains a number, a letter, and a symbol
     * @param password Password to check
     * @return True if meets requirements, false otherwise
     */
    private boolean containsNumberLetterAndSymbol(String password) {
        boolean hasNumber = false;
        boolean hasSymbol = false;
        boolean hasLetter = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (!Character.isLetterOrDigit(c)) {
                hasSymbol = true;
            }
            if (hasNumber && hasLetter && hasSymbol) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Check if a password meets the strength requirements
     * @param password The password to check
     * @return True if password is strong, false otherwise
     */
    private boolean isPasswordStrong(String password) {
        // Password must be at least 8 characters
        if (password == null || password.length() < 8) {
            System.out.println("Password validation failed: Less than 8 characters");
            return false;
        }
        
        // Check for uppercase and lowercase letters
        if (!containsUpperCaseAndLowerCase(password)) {
            System.out.println("Password validation failed: Missing uppercase or lowercase letter");
            return false;
        }
        
        // Check for number, letter, and symbol
        if (!containsNumberLetterAndSymbol(password)) {
            System.out.println("Password validation failed: Missing number, letter, or symbol");
            return false;
        }
        
        System.out.println("Password validated successfully");
        return true;
    }
}
