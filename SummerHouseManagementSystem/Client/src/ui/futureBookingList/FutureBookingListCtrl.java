package ui.futureBookingList;

import dtos.BookingHistory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Future Booking List view
 * This class handles the interaction between the view and the ViewModel,
 * displaying future bookings and allowing the user to cancel them.
 *
 * @author Group 4
 * @version 1.0
 */
public class FutureBookingListCtrl
{
  @FXML private TableView<BookingHistory> table;
  @FXML private TableColumn<BookingHistory, String> locationColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, Double> pricePerNightColumn;
  @FXML private Label errorLabel;
  @FXML private Button backButton;
  @FXML private Button cancelButton;
  private FutureBookingListVM futureBookingListVM;
  private ViewHandler viewHandler;

  /**
   * Default constructor for FutureBookingListCtrl.
   * Initializes the controller without any parameters.
   */
  public FutureBookingListCtrl()
  {
  }

  /**
   * Initializes the controller with the ViewModel and ViewHandler.
   * Binds the table view to the ViewModel's future bookings and sets up
   * the columns to display booking details.
   *
   * @param futureBookingListVM The ViewModel containing future bookings data.
   * @param viewHandler The ViewHandler to manage view transitions.
   */
  public void initialize(FutureBookingListVM futureBookingListVM,
      ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.futureBookingListVM = futureBookingListVM;

    table.setItems(futureBookingListVM.getFutureBookings());
    System.out.println("Future bookings: " + futureBookingListVM.getFutureBookings());

    futureBookingListVM.selectedBookingProperty().bind(table.getSelectionModel().selectedItemProperty());
    futureBookingListVM.errMsgProperty().bindBidirectional(errorLabel.textProperty());


    locationColumn.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getLocation()));
    startDateColumn.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStartDate().toString()));
    endDateColumn.setCellValueFactory(
        data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEndDate().toString()));
    pricePerNightColumn.setCellValueFactory(
        data -> new javafx.beans.property.SimpleDoubleProperty(
            data.getValue().getPricePerNight()).asObject());
  }

  /**
   * Handles the action when the user clicks the "Back" button.
   * Calls the ViewModel to cancel the selected booking.
   */
  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  /**
   * Handles the action when the user clicks the "Cancel Booking" button.
   * Calls the ViewModel to cancel the selected booking.
   */
  public void onCancelButton()
  {
    futureBookingListVM.cancelBooking();
  }
}