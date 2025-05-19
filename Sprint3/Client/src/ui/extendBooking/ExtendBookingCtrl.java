package ui.extendBooking;

import dtos.BookingHistory;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
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
  }      public void initialize(ExtendBookingVM vm, ViewHandler vh, BookingHistory booking) {
    this.viewModel = vm;
    this.viewHandler = vh;

    if (booking == null) {
      // Handle case when no booking is selected
      messageLabel.setText("No booking selected. Please select a booking to extend.");
      // Disable controls
      newEndDatePicker.setDisable(true);
      return;
    }
    try {
      System.out.println("DEBUG: Initializing ExtendBookingCtrl with booking: " + booking);
      System.out.println("DEBUG: Booking details - Location: " + booking.getLocation() +
          ", Start: " + booking.getStartDate() +
          ", End: " + booking.getEndDate() +
          ", Username: " + booking.getUsername() +
          ", PropertyId: " + booking.getPropertyId());

      // Use the method that accepts BookingHistory
      viewModel.setSelectedBookingFromHistory(booking);

      // Set properties directly instead of binding
      propertyNameLabel.setText(booking.getLocation());
      currentStartDateLabel.setText(booking.getStartDate().toString());
      currentEndDateLabel.setText(booking.getEndDate().toString());
      messageLabel.setText(""); // Clear any error messages
      // Set up date picker - enable it and set min/max dates
      LocalDate currentEndDate = booking.getEndDate().toLocalDate();
      LocalDate maxDate = currentEndDate.plusMonths(3); // Allow extension up to 3 months

      newEndDatePicker.setDisable(false);
      newEndDatePicker.setValue(currentEndDate.plusDays(1)); // Default to one day extension
      newEndDatePicker.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
          super.updateItem(date, empty);
          setDisable(empty || date.compareTo(currentEndDate) <= 0 || date.compareTo(maxDate) > 0);
        }
      });

      // Listen for date changes
      newEndDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue != null) {
          viewModel.newEndDateProperty().set(Date.valueOf(newValue));
        }
      });
    } catch (Exception e) {            messageLabel.setText("Error initializing view: " + e.getMessage());
    }
  }
  @FXML
  public void onExtend() {
    if (viewModel == null) {
      // Show error message if viewModel is not initialized
      messageLabel.setText("Cannot extend booking: System not properly initialized");
      return;
    }

    try {
      System.out.println("DEBUG: Attempting to extend booking with new date: " +
          (viewModel.newEndDateProperty().get() != null ?
              viewModel.newEndDateProperty().get() : "No date selected"));

      // Check if a new date is selected
      if (viewModel.newEndDateProperty().get() == null) {
        messageLabel.setText("Please select a new end date");
        return;
      }

      // Check if a booking is selected
      if (viewModel.selectedBookingProperty().get() == null) {
        messageLabel.setText("No booking selected. Please select a booking to extend.");
        return;
      }

      if (viewModel.extendBooking()) {
        // Show success message
        messageLabel.setText("Booking extended successfully!");

        // Wait a moment before returning to future bookings view
        Thread.sleep(1000);

        // Return to future bookings view after successful extension
        viewHandler.showView(ViewHandler.ViewType.FUTURE_BOOKINGS);
      } else {
        // The error message will be set by the viewModel
        System.out.println("DEBUG: Failed to extend booking: " + messageLabel.getText());
      }
    } catch (Exception e) {
      System.out.println("DEBUG: Exception when extending booking: " + e.getMessage());
      e.printStackTrace();
      messageLabel.setText("Error extending booking: " + e.getMessage());
    }    }

  @FXML
  public void onCancel() {
    if (viewHandler == null) {
      // Show error message if viewHandler is not initialized
      messageLabel.setText("Cannot navigate: System not properly initialized");
      return;
    }

    try {
      System.out.println("DEBUG: Canceling extend booking operation");
      // Return to the user dashboard, which is more appropriate than future bookings
      viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
    } catch (Exception e) {
      System.out.println("DEBUG: Error canceling extend booking: " + e.getMessage());
      messageLabel.setText("Error navigating: " + e.getMessage());
    }
  }
}