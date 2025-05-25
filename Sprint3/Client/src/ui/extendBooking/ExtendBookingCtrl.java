package ui.extendBooking;

import dtos.BookingHistory;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Extend Booking view
 * This controller handles the user interface for extending a booking.
 * It allows the user to view the current booking details,
 * and select a new end date for the booking.
 *
 * @author Group 4
 * @version 1.0
 */
public class ExtendBookingCtrl
{
  @FXML private Label propertyLocationLabel;
  @FXML private Label currentStartDateLabel;
  @FXML private Label currentEndDateLabel;
  @FXML private DatePicker newEndDatePicker;
  @FXML private Label messageLabel;

  private ExtendBookingVM viewModel;
  private ViewHandler viewHandler;

  /**
   * Default constructor for ExtendBookingCtrl.
   * This constructor is used by the JavaFX framework to create an instance of this controller.
   */
  public ExtendBookingCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler.
   * This method sets up the initial state of the view, including labels and listeners.
   *
   * @param vm The ViewModel containing booking data and logic.
   * @param vh The ViewHandler to manage view transitions.
   */
  public void initialize(ExtendBookingVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;

    //initialize labels with the selected booking information from VM
    propertyLocationLabel.setText(
        viewModel.selectedBookingProperty().get().getLocation());
    currentStartDateLabel.setText(
        viewModel.selectedBookingProperty().get().getStartDate().toString());
    currentEndDateLabel.setText(
        viewModel.selectedBookingProperty().get().getEndDate().toString());
    viewModel.messageProperty().addListener((obs, oldMsg, newMsg) -> {
      if (newMsg != null && !newMsg.isEmpty())
      {
        messageLabel.setText(newMsg);
      }
      else
      {
        messageLabel.setText("");
      }
    });
    newEndDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
      if (newDate != null)
      {
        viewModel.newEndDateProperty().set(java.sql.Date.valueOf(newDate));
      }
    });
  }

  /**
   * Handles the event when the user clicks the "Extend" button.
   * This method calls the ViewModel to extend the booking
   * and updates the view accordingly.
   *
   */
  @FXML public void onExtend()
  {
    viewModel.extendBooking();
  }

  /**
   * Handles the event when the user clicks the "Back" button.
   * This method navigates back to the current bookings view.
   */
  @FXML public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.CURRENT_BOOKINGS);
  }
}