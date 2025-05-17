package dtos;

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
    
    public String getPassword() {
        return password;
    }
}
