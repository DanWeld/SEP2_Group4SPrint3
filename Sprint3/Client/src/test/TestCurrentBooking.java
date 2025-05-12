package test;

import networking.Client;
import services.UserSession;
import ui.currentBookingList.CurrentBookingListVM;

import java.io.IOException;

/**
 * A simple class to test the current bookings functionality
 */
public class TestCurrentBooking {

    public static void main(String[] args) {
        // Set up test user
        UserSession userSession = UserSession.getInstance();
        
        // For testing, create a specific user
        userSession.setTestUser("test_user", "Test User", "test@test.com", "password", false);
        
        System.out.println("Current user: " + userSession.getUsername());
        
        try {
            // Create the view model
            CurrentBookingListVM viewModel = new CurrentBookingListVM();
            
            // Try to get bookings
            System.out.println("Retrieving current bookings...");
            int bookingsCount = viewModel.getBookingHistory().size();
            System.out.println("Found " + bookingsCount + " current bookings.");
            
        } catch (Exception e) {
            System.err.println("ERROR in test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
