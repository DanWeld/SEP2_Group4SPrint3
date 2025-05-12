package ui.extendBooking;

import dtos.Booking;
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
        selectedBooking.set(booking);
        // Initialize newEndDate with the current end date
        newEndDate.set(booking.getEndDate());
    }    /**
     * Attempts to extend the booking to the new end date
     * @return true if extension was successful, false otherwise
     */
    public boolean extendBooking() {
        // Validate inputs
        if (selectedBooking.get() == null || newEndDate.get() == null) {
            message.set("No booking or end date selected");
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
            message.set("Error: " + e.getMessage());
            return false;
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
