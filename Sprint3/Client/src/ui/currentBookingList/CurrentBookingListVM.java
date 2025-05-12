package ui.currentBookingList;

import dtos.BookingHistory;
import dtos.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import services.UserSession;

import java.io.IOException;
import java.util.List;

public class CurrentBookingListVM
{
  private ObservableList<BookingHistory> bookings;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  public CurrentBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    try
    {
      bookingHistoryClient = new BookingHistoryClientImpl(new Client());
    }
    catch (IOException e)
    {
      System.err.println("ERROR: Failed to initialize BookingHistoryClient: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    }
    
    refreshUser();
  }
  
  /**
   * Refreshes the user data from the current session
   */
  private void refreshUser() {
    try {
      // Get user from UserSession instead of hardcoding
      UserSession userSession = UserSession.getInstance();
      
      if (userSession.isLoggedIn()) {
        this.user = new User(
            userSession.getName(),
            userSession.getUsername(),
            userSession.getEmail(),
            userSession.getPassword(),
            userSession.isAdmin()
        );
        System.out.println("DEBUG: User loaded - " + user.getUsername());
      } else {
        System.err.println("WARNING: No user logged in when initializing CurrentBookingListVM");
      }
    } catch (Exception e) {
      System.err.println("ERROR: Failed to get user from session: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public ObservableList<BookingHistory> getBookingHistory()
  {
    try
    {
      // Make sure we have the latest user
      refreshUser();
      
      if (user == null || user.getUsername() == null || user.getUsername().isEmpty()) {
        System.err.println("ERROR: Cannot fetch bookings - no valid user found");
        return FXCollections.observableArrayList(); // Return empty list
      }
      
      System.out.println("DEBUG: Fetching current bookings for user: " + user.getUsername());
      
      // Create a test booking for today to verify UI works correctly
      boolean addTestBooking = false;  // Set to true to add a test booking
      
      List<BookingHistory> bookingHistoryList = bookingHistoryClient.getCurrentBookings(user.getUsername());
      System.out.println("DEBUG: Received " + (bookingHistoryList != null ? bookingHistoryList.size() : 0) + " current bookings");
      
      // Clear existing bookings
      bookings.clear();
      
      if (bookingHistoryList != null && !bookingHistoryList.isEmpty()) {
        for (BookingHistory booking : bookingHistoryList) {
          System.out.println("DEBUG: Current booking: " + booking);
          bookings.add(booking);
        }
      } else {
        System.out.println("DEBUG: No current bookings found for user " + user.getUsername());
        
        // For testing, we can add a dummy booking here if needed
        if (addTestBooking) {
          java.util.Date now = new java.util.Date();
          java.sql.Date today = new java.sql.Date(now.getTime());
          java.sql.Date endDate = new java.sql.Date(now.getTime() + 5 * 24 * 60 * 60 * 1000); // 5 days later
          
          BookingHistory testBooking = new BookingHistory(
              user.getUsername(),
              "Test Hotel",
              today,
              endDate,
              150.0,
              1
          );
          
          bookings.add(testBooking);
          System.out.println("DEBUG: Added test booking: " + testBooking);
        }
      }
    }
    catch (Exception e)
    {
      System.err.println("ERROR: Failed to retrieve current bookings: " + e.getMessage());
      e.printStackTrace();
    }
    
    // Log final list of current bookings
    if (bookings.isEmpty()) {
      System.out.println("DEBUG: No current bookings available to display");
    } else {
      System.out.println("DEBUG: Returning " + bookings.size() + " current bookings for display:");
      for (int i = 0; i < bookings.size(); i++) {
        BookingHistory booking = bookings.get(i);
        System.out.println("       " + (i+1) + ". " + booking.getLocation() + 
                         " (ID: " + booking.getPropertyId() + 
                         ", " + booking.getStartDate() + 
                         " to " + booking.getEndDate() + ")");
      }
    }
    
    return bookings;
  }
}
