package ui.extendBooking;

import dtos.BookingHistory;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Extend Booking view
 */
public class ExtendBookingCtrl
{
  @FXML private Label propertyLocationLabel;
  @FXML private Label currentStartDateLabel;
  @FXML private Label currentEndDateLabel;
  @FXML private DatePicker newEndDatePicker;
  @FXML private Label messageLabel;
  private ObjectProperty<BookingHistory> selectedBooking;

  private ExtendBookingVM viewModel;
  private ViewHandler viewHandler;

  public ExtendBookingCtrl()
  {
  }

  public void initialize(ExtendBookingVM vm, ViewHandler vh)
  {
    this.viewModel = vm;
    this.viewHandler = vh;

    //initialize labels with the selected booking information from VM
    propertyLocationLabel.setText(viewModel.selectedBookingProperty().get().getLocation());
    currentStartDateLabel.setText(viewModel.selectedBookingProperty().get().getStartDate().toString());
    currentEndDateLabel.setText(viewModel.selectedBookingProperty().get().getEndDate().toString());
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
  }

  @FXML public void onExtend()
  {
    viewModel.extendBooking();
  }

  @FXML public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.CURRENT_BOOKINGS);
  }
}