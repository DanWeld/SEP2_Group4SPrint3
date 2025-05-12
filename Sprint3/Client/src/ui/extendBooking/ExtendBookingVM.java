package ui.extendBooking;

import dtos.Booking;
import dtos.BookingHistory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.bookingClient.BookingClient;
import services.UserSession;

import java.sql.Date;

/**
 * ViewModel for extending a booking
 */
public class ExtendBookingVM {
    private final ObjectProperty<Booking> selectedBooking;
    private final ObjectProperty<Date> newEndDate;
    private final StringProperty message;
    private final BookingClient bookingClient;
    
    public ExtendBookingVM(BookingClient bookingClient) {
        this.bookingClient = bookingClient;
        selectedBooking = new SimpleObjectProperty<>();
        newEndDate = new SimpleObjectProperty<>();
        message = new SimpleStringProperty("");
    }
      /**
     * Sets the booking to be extended
     * @param booking The booking to extend
     */
    public void setSelectedBooking(Booking booking) {
        if (booking == null) {
            selectedBooking.set(null);
            newEndDate.set(null);
            message.set("No booking selected. Please select a booking to extend.");
            return;
        }
        
        selectedBooking.set(booking);
        // Initialize newEndDate with the current end date
        newEndDate.set(booking.getEndDate());
    }/**
     * Attempts to extend the booking to the new end date
     * @return true if extension was successful, false otherwise
     */    public boolean extendBooking() {
        // Validate inputs
        if (selectedBooking.get() == null || newEndDate.get() == null) {
            message.set("No booking or end date selected");
            System.out.println("DEBUG: Cannot extend booking - null values detected");
            System.out.println("DEBUG: Selected booking: " + (selectedBooking.get() != null ? "present" : "null"));
            System.out.println("DEBUG: New end date: " + (newEndDate.get() != null ? newEndDate.get() : "null"));
            return false;
        }
        
        // Validate that new end date is after current end date
        if (newEndDate.get().before(selectedBooking.get().getEndDate())) {
            message.set("New end date must be after current end date");
            return false;
        }
        
        try {
            // Call service to extend booking
            int propertyId = selectedBooking.get().getPropertyId();
            Date currentEndDate = selectedBooking.get().getEndDate();
            String username = selectedBooking.get().getUsername();
            
            System.out.println("DEBUG: Extending booking - Property ID: " + propertyId + 
                               ", Current end date: " + currentEndDate + 
                               ", New end date: " + newEndDate.get() + 
                               ", Username: " + username);
              try {
                System.out.println("DEBUG: Calling bookingClient.extendBooking with: " +
                                  "PropertyID=" + propertyId + 
                                  ", CurrentEndDate=" + currentEndDate +
                                  ", NewEndDate=" + newEndDate.get() +
                                  ", Username=" + username);
                                  
                boolean success = bookingClient.extendBooking(
                    propertyId, currentEndDate, newEndDate.get(), username);
                
                if (success) {
                    message.set("Booking extended successfully");
                    return true;
                } else {
                    message.set("Failed to extend booking. The property might not be available for the selected dates.");
                    return false;
                }
            } catch (Exception e) {
                System.err.println("ERROR in extendBooking: " + e.getMessage());
                e.printStackTrace();
                message.set("Error: " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Sets the booking to be extended using a BookingHistory object
     * @param bookingHistory The booking history to convert and use
     */    public void setSelectedBookingFromHistory(BookingHistory bookingHistory) {
        if (bookingHistory == null) {
            selectedBooking.set(null);
            newEndDate.set(null);
            message.set("No booking selected. Please select a booking to extend.");
            return;
        }
        
        System.out.println("DEBUG: Setting booking from history: " + bookingHistory.getLocation() + 
                           ", Start: " + bookingHistory.getStartDate() + 
                           ", End: " + bookingHistory.getEndDate() + 
                           ", PropertyId: " + bookingHistory.getPropertyId() + 
                           ", Username: " + bookingHistory.getUsername());
        
        try {
            // Validate booking data before converting
            if (bookingHistory.getEndDate() == null) {
                throw new Exception("Booking has no end date");
            }
            
            if (bookingHistory.getPropertyId() <= 0) {
                throw new Exception("Invalid property ID: " + bookingHistory.getPropertyId());
            }
            
            // Check if date is in the future
            long today = System.currentTimeMillis();
            if (bookingHistory.getEndDate().getTime() < today) {
                System.out.println("WARNING: Booking end date is in the past: " + bookingHistory.getEndDate());
            }
            
            // Convert BookingHistory to Booking
            Booking booking = new Booking(
                new Date(System.currentTimeMillis()), // Current date as booking date
                bookingHistory.getStartDate(),
                bookingHistory.getEndDate(),
                bookingHistory.getPropertyId(),
                bookingHistory.getUsername()
            );
            
            System.out.println("DEBUG: Created booking object: " + 
                              "PropertyId: " + booking.getPropertyId() + 
                              ", Start: " + booking.getStartDate() + 
                              ", End: " + booking.getEndDate());
            
            // Set the selected booking
            selectedBooking.set(booking);
            
            // Initialize newEndDate with the day after current end date
            java.util.Calendar c = java.util.Calendar.getInstance();
            c.setTime(booking.getEndDate());
            c.add(java.util.Calendar.DATE, 3); // Add 3 days as default extension
            Date newEndDateValue = new Date(c.getTimeInMillis());
            
            System.out.println("DEBUG: Setting default new end date to: " + newEndDateValue);
            newEndDate.set(newEndDateValue);
            
            // Clear any error message
            message.set("");
        } catch (Exception e) {
            System.err.println("ERROR: Failed to create booking from history: " + e.getMessage());
            e.printStackTrace();
            message.set("Error processing booking data: " + e.getMessage());
        }
    }
    
    public ObjectProperty<Booking> selectedBookingProperty() {
        return selectedBooking;
    }
    
    public ObjectProperty<Date> newEndDateProperty() {
        return newEndDate;
    }
    
    public StringProperty messageProperty() {
        return message;
    }
}
