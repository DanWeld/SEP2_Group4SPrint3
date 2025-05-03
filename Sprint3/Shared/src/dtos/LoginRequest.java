package dtos;

public class LoginRequest {
    private final String credential; // Can be either email or username
    private final String password;
    private final boolean isUsernameLogin;
    
    /**
     * Constructor for email-based login (default)
     * @param credential The email
     * @param password The password
     */
    public LoginRequest(String credential, String password) {
        this.credential = credential;
        this.password = password;
        this.isUsernameLogin = false;
    }
    
    /**
     * Constructor for specifying login type
     * @param credential The email or username
     * @param password The password
     * @param isUsernameLogin True if logging in with username, false if with email
     */
    public LoginRequest(String credential, String password, boolean isUsernameLogin) {
        this.credential = credential;
        this.password = password;
        this.isUsernameLogin = isUsernameLogin;
    }
    
    /**
     * Get the credential (email or username)
     * @return The credential string
     */
    public String getCredential() {
        return credential;
    }
    
    /**
     * Get the email (for backward compatibility)
     * @return The email
     */
    public String getEmail() {
        if (!isUsernameLogin) {
            return credential;
        }
        return null;
    }
    
    /**
     * Get the username (if this is a username-based login)
     * @return The username
     */
    public String getUsername() {
        if (isUsernameLogin) {
            return credential;
        }
        return null;
    }
    
    public String getPassword() {
        return password;
    }
    
    public boolean isUsernameLogin() {
        return isUsernameLogin;
    }
}
