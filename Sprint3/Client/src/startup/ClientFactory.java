package startup;

import networking.Client;
import networking.auth.Authentication;
import networking.auth.AuthenticationImpl;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import networking.profile.UserProfileClient;
import networking.profile.UserProfileClientImpl;

import java.io.IOException;

/**
 * Factory class for creating client related objects
 */
public class ClientFactory {    
    private static ClientFactory instance;
    private Client client;
    private Authentication authentication;
    private UserProfileClient userProfileClient;
    private BookingClient bookingClient;
    private BookingHistoryClient bookingHistoryClient;
    private boolean serverConnected;
    
    private ClientFactory() {
        try {
            client = new Client();
            authentication = new AuthenticationImpl(client);
            userProfileClient = new UserProfileClientImpl(client);
            bookingClient = new BookingClientImpl(client);
            bookingHistoryClient = new BookingHistoryClientImpl(client);
            serverConnected = true;
        } catch (IOException e) {
            System.out.println("Warning: Could not connect to server. Some functionality may be limited.");
            client = null;
            authentication = new AuthenticationImpl(null); // Offline mode
            userProfileClient = null; // No profile client in offline mode
            bookingClient = null; // No booking client in offline mode
            bookingHistoryClient = null; // No booking history client in offline mode
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
    
    public UserProfileClient getUserProfileClient() {
        return userProfileClient;
    }
    
    public BookingClient getBookingClient() {
        return bookingClient;
    }
    
    public BookingHistoryClient getBookingHistoryClient() {
        return bookingHistoryClient;
    }
}
