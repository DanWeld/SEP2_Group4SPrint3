package dtos;

/**
 * Represents a login request containing user credentials.
 * Can be used for both email and username-based authentication.
 *
 * @author Group 4
 * @version 1.0
 */
public class LoginRequest {
    private final String email; // Can be either email or username
    private final String password;
    /**
     * Constructor for email-based login (default)
     * @param credential The email
     * @param password The password
     */
    public LoginRequest(String credential, String password) {
        this.email = credential;
        this.password = password;
    }
    
    /**
     * Get the credential (email or username)
     * @return The credential string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password for this login request.
     *
     * @return the password string
     */
    public String getPassword() {
        return password;
    }
}
