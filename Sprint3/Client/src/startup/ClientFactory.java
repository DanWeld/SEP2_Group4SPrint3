package startup;

import networking.Client;
import networking.auth.Authentication;
import networking.auth.AuthenticationImpl;

import java.io.IOException;

/**
 * Factory class for creating client related objects
 */
public class ClientFactory {    
    private static ClientFactory instance;
    private Client client;
    private Authentication authentication;
    private boolean serverConnected;
    
    private ClientFactory() {
        try {
            client = new Client();
            authentication = new AuthenticationImpl(client);
            serverConnected = true;
        } catch (IOException e) {
            System.out.println("Warning: Could not connect to server. Some functionality may be limited.");
            client = null;
            authentication = new AuthenticationImpl(null); // Offline mode
            serverConnected = false;
        }
    }
    
    public static synchronized ClientFactory getInstance() {
        if (instance == null) {
            instance = new ClientFactory();
        }
        return instance;
    }
    
    public Client getClient() {
        return client;
    }
    
    public Authentication getAuthentication() {
        return authentication;
    }
}
