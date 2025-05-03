package model.authentication;

import dtos.User;
import persistence.UserDAO;

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
    }

    @Override
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
        if (user == null) {
            return "User cannot be null";
        }
        
        String email = user.getEmail();
        String password = user.getPassword();
        String username = user.getUsername();
        
        // Validate email and password
        String emailValidation = validateEmail(email);
        String passwordValidation = validatePassword(password);
        
        if (!emailValidation.equals("OK")) {
            return emailValidation;
        }
        
        if (!passwordValidation.equals("OK")) {
            return passwordValidation;
        }
        
        try {
            // Check if email is unique
            if (!isEmailUnique(email)) {
                return "Email already in use";
            }
            
            // Check if username is unique
            if (!isUsernameUnique(username)) {
                return "Username already in use";
            }
            
            // Create user with parameters instead of passing the User object
            userDAO.create(username, email, password, user.isAdmin());
            return "Ok";
        } catch (SQLException e) {
            return "Database error: " + e.getMessage();
        }
    }
    
    @Override
    public boolean isUsernameUnique(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        try {
            User user = userDAO.getUserByUsername(username);
            return user == null;
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public boolean isEmailUnique(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            User user = userDAO.getUserByEmail(email);
            return user == null;
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public boolean isAdmin(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            User user = userDAO.getUserByEmail(email);
            return user != null && user.isAdmin();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Helper methods for validation
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

    private boolean containsNumberLetterAndSymbol(String pw) {
        boolean hasNumber = false;
        boolean hasSymbol = false;
        boolean hasLetter = false;
        
        for (char c : pw.toCharArray()) {
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

    private String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (!email.contains("@") && !email.endsWith(".com")) {
            return "Email must be in a correct format.";
        }
        return "OK";
    }

    private String validatePassword(String newPassword) {
        if (newPassword == null || newPassword.isEmpty()) {
            return "Password cannot be empty";
        }
        if (newPassword.length() < 8) {
            return "Passwords must be 8 or greater character length.";
        }
        if (!containsUpperCaseAndLowerCase(newPassword)) {
            return "Passwords must have atleast one upper and atleast one lower character.";
        }
        if (!containsNumberLetterAndSymbol(newPassword)) {
            return "password must have at least one number, one letter and one symbol";
        }
        return "OK";
    }
}
