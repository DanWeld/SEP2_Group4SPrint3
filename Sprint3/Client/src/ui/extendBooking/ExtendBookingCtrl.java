package ui.extendBooking;

import dtos.Booking;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

import java.time.LocalDate;
import java.sql.Date;

/**
 * Controller for the Extend Booking view
 */
public class ExtendBookingCtrl {
    @FXML private Label propertyNameLabel;
    @FXML private Label currentStartDateLabel;
    @FXML private Label currentEndDateLabel;
    @FXML private DatePicker newEndDatePicker;
    @FXML private Label messageLabel;
    
    private ExtendBookingVM viewModel;
    private ViewHandler viewHandler;
    
    public ExtendBookingCtrl() {
        // Empty constructor
    }
    
    public void initialize(ExtendBookingVM vm, ViewHandler vh, Booking booking) {
        this.viewModel = vm;
        this.viewHandler = vh;
        
        // Set the selected booking
        viewModel.setSelectedBooking(booking);
        
        // Bind properties
        propertyNameLabel.textProperty().bind(viewModel.selectedBookingProperty().asString(booking.getProperty().getName()));
        currentStartDateLabel.textProperty().bind(viewModel.selectedBookingProperty().asString(booking.getStartDate().toString()));
        currentEndDateLabel.textProperty().bind(viewModel.selectedBookingProperty().asString(booking.getEndDate().toString()));
        messageLabel.textProperty().bind(viewModel.messageProperty());
        
        // Set up date picker
        LocalDate currentEndDate = booking.getEndDate().toLocalDate();
        newEndDatePicker.setValue(currentEndDate);
        
        // Listen for date changes
        newEndDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                viewModel.newEndDateProperty().set(Date.valueOf(newValue));
            }
        });
    }
    
    @FXML
    public void onExtend() {
        if (viewModel.extendBooking()) {
            // Return to future bookings view after successful extension
            viewHandler.showView(ViewHandler.ViewType.FUTURE_BOOKINGS);
        }
    }
    
    @FXML
    public void onCancel() {
        // Return to future bookings view
        viewHandler.showView(ViewHandler.ViewType.FUTURE_BOOKINGS);
    }
}
