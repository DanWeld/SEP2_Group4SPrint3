package ui.futureBookingList;

import dtos.BookingHistory;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import startup.viewHandler.ViewHandler;
import javafx.scene.control.Button ;

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
  @FXML private Button extendButton;
  private FutureBookingListVM futureBookingListVM;
  private ViewHandler viewHandler;

  public FutureBookingListCtrl()
  {
  }

  public void initialize(FutureBookingListVM futureBookingListVM,
      ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.futureBookingListVM = futureBookingListVM;

    table.setItems(futureBookingListVM.getFutureBookings());

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
            
    // Disable extend button if no booking is selected
    extendButton.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
  }

  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  public void onCancelButton()
  {
    futureBookingListVM.cancelBooking();
  }
  
  public void onExtendButton()
  {
    try {
      // Get the selected booking
      BookingHistory selectedBooking = futureBookingListVM.selectedBookingProperty().get();
      
      if (selectedBooking != null) {
        System.out.println("DEBUG: Extending booking with ID " + selectedBooking.getPropertyId() + 
                          " for location: " + selectedBooking.getLocation());
        
        // Navigate to the extend booking view with the selected booking
        viewHandler.showExtendBookingView(selectedBooking);
      } else {
        errorLabel.setText("Please select a booking to extend");
      }
    } catch (Exception e) {
      System.err.println("ERROR: Failed to navigate to extend booking view: " + e.getMessage());
      e.printStackTrace();
      errorLabel.setText("Error: " + e.getMessage());
    }
  }
}