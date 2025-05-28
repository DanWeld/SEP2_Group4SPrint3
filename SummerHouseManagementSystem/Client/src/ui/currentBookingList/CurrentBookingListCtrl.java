package ui.currentBookingList;

import dtos.BookingHistory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import startup.viewHandler.ViewHandler;
import ui.pastBookingList.PastBookingListVM;

/**
 * Controller for the Current Booking List view.
 * This class handles the user interactions and updates the UI based on the ViewModel.
 */
public class CurrentBookingListCtrl
{
  @FXML private TableView<BookingHistory> table;
  @FXML private TableColumn<BookingHistory, String> locationColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, Double> pricePerNightColumn;
  @FXML private Label messageLabel;
  private ObjectProperty<BookingHistory> selectedBooking;
  private CurrentBookingListVM currentBookingListVM;
  private ViewHandler viewHandler;

  /**
   * Default constructor for CurrentBookingListCtrl.
   * Initializes the controller without any parameters.
   */
  public CurrentBookingListCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler.
   * Binds the UI components to the ViewModel properties and sets up the table view.
   *
   * @param currentBookingListVM the ViewModel for this view
   * @param viewHandler the ViewHandler for navigating between views
   */
  public void initialize(CurrentBookingListVM currentBookingListVM,
      ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.currentBookingListVM = currentBookingListVM;

    table.setItems(currentBookingListVM.getBookingHistory());

    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getLocation()));
    startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getStartDate().toString()));
    endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEndDate().toString()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().getPricePerNight()).asObject());

    this.selectedBooking = currentBookingListVM.selectedBookingProperty();
    table.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          selectedBooking.set(newSelection);
        });

    // Clear selection and selectedBooking when view is shown
    table.getSelectionModel().clearSelection();
    selectedBooking.set(null);

    currentBookingListVM.msgProperty().addListener((obs, oldMsg, newMsg) -> {
      if (newMsg != null && !newMsg.isEmpty())
      {
        messageLabel.setText(newMsg);
      }
      else
      {
        messageLabel.setText("");
      }
    });

    messageLabel.textProperty().bindBidirectional(currentBookingListVM.msgProperty());
  }

  /**
   * Called when the view is shown.
   * This method is used to refresh the booking list when the view is displayed.
   */
  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  /**
   * Called when the "View Past Bookings" button is clicked.
   * This method navigates to the Past Booking List view.
   */
  public void onExtendBooking()
  {
    if (selectedBooking.get() == null)
    {
      messageLabel.setText("Please select a booking to extend.");
      return;
    }
    currentBookingListVM.msgProperty().set("");
    viewHandler.setBooking(selectedBooking.get());
    viewHandler.showView(ViewHandler.ViewType.EXTEND_BOOKING);
  }
}
