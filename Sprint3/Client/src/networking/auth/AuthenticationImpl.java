package networking.auth;

import dtos.User;
import networking.Client;

import java.util.regex.Pattern;

public class AuthenticationImpl implements Authentication {
    private final Client client;
    private boolean offlineMode;

    public AuthenticationImpl(Client client) {
        this.client = client;
        this.offlineMode = (client == null);
    }
    
    @Override
    public String loginUser(String email, String password) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        
        if (offlineMode) {
            // In offline mode, return a message but allow login for testing purposes
            if ("test@test.com".equals(email) && "Test1234".equals(password)) {
                return "Ok";
            } else {
                return "Server connection unavailable. Using demo mode with credentials: test@test.com / Test1234";
            }
        }
        
        try {
            // Send login request to server through the client
            String response = client.sendLoginRequest(email, password);
            return response;
        } catch (Exception e) {
            return "Error connecting to server: " + e.getMessage();
        }
    }

    @Override
    public String registerUser(User user) {
        // Validate user data
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return "Username cannot be empty";
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            return "Email cannot be empty";
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return "Password cannot be empty";
        }
        
        // Check password strength with detailed feedback
        if (!isPasswordStrong(user.getPassword())) {
            return "Password must be at least 8 characters and contain at least 1 uppercase letter, 1 lowercase letter, 1 number, and 1 special character";
        }
        
        try {
            // Check if username is unique
            if (!isUsernameUnique(user.getUsername())) {
                return "Username is already taken";
            }
            
            // Check if email is unique
            if (!isEmailUnique(user.getEmail())) {
                return "Email is already registered";
            }
            
            // Send register request to server through the client
            String response = client.sendRegisterRequest(user);
            return response;
        } catch (Exception e) {
            return "Error connecting to server: " + e.getMessage();
        }
    }
    
    @Override
    public boolean isPasswordStrong(String password) {
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
        
        System.out.println("Password validation passed");
        return true;
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

    @Override
    public boolean isUsernameUnique(String username) {
        // In a real implementation, this would check against the server
        // For now, we'll assume it's a server call through the client
        try {
            return client.isUsernameUnique(username);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isEmailUnique(String email) {
        // In a real implementation, this would check against the server
        // For now, we'll assume it's a server call through the client
        try {
            return client.isEmailUnique(email);
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public boolean isAdmin(String email) {
        // Check if a user is an admin by querying the server
        try {
            return client.isAdmin(email);
        } catch (Exception e) {
            System.out.println("Error checking admin status: " + e.getMessage());
            return false;
        }
    }
}
