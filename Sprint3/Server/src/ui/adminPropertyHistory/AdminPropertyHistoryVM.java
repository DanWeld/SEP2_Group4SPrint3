package ui.adminPropertyHistory;

import dtos.Booking;
import dtos.Property;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Date;

/**
 * ViewModel for the Admin Property History view
 */
public class AdminPropertyHistoryVM {
    private final ObjectProperty<Property> property;
    private final ObservableList<Booking> bookings;
    private final StringProperty message;
    
    public AdminPropertyHistoryVM() {
        property = new SimpleObjectProperty<>();
        bookings = FXCollections.observableArrayList();
        message = new SimpleStringProperty("");
    }
    
    /**
     * Sets the property to view history for
     * @param property The property
     */
    public void setProperty(Property property) {
        this.property.set(property);
        loadBookingHistory();
    }
    
    /**
     * Loads the booking history for the current property
     */
    private void loadBookingHistory() {
        Property currentProperty = property.get();
        if (currentProperty == null) {
            message.set("No property selected");
            return;
        }
        
        // TODO: Implement fetching booking history from server
        // For now, use dummy data
        bookings.clear();
          // Create some sample bookings with different dates
        long now = System.currentTimeMillis();
        long dayMillis = 24 * 60 * 60 * 1000;
        
        // Past bookings
        bookings.add(new Booking(new Date(now), new Date(now - 30 * dayMillis), new Date(now - 23 * dayMillis),
                currentProperty.getId(), "user1"));
        bookings.add(new Booking(new Date(now), new Date(now - 20 * dayMillis), new Date(now - 15 * dayMillis),
                currentProperty.getId(), "user2"));
                  // Current booking
        bookings.add(new Booking(new Date(now), new Date(now - 3 * dayMillis), new Date(now + 4 * dayMillis),
                currentProperty.getId(), "user3"));
                
        // Future bookings
        bookings.add(new Booking(new Date(now), new Date(now + 10 * dayMillis), new Date(now + 17 * dayMillis),
                currentProperty.getId(), "user1"));
        bookings.add(new Booking(new Date(now), new Date(now + 20 * dayMillis), new Date(now + 27 * dayMillis),
                currentProperty.getId(), "user4"));
                
        message.set("Loaded booking history for " + currentProperty.getName());
    }
    
    public ObjectProperty<Property> propertyProperty() {
        return property;
    }
    
    public ObservableList<Booking> getBookings() {
        return bookings;
    }
    
    public StringProperty messageProperty() {
        return message;
    }
}
