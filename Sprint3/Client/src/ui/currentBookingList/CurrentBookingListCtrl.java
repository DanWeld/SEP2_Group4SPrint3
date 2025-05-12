package ui.currentBookingList;

import dtos.BookingHistory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import startup.viewHandler.ViewHandler;

public class CurrentBookingListCtrl
{
  @FXML private TableView<BookingHistory> table;
  @FXML private TableColumn<BookingHistory, String> locationColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, Double> pricePerNightColumn;
  @FXML private Label statusLabel;
  @FXML private Button refreshButton;
  
  private CurrentBookingListVM currentBookingListVM;
  private ViewHandler viewHandler;

  public CurrentBookingListCtrl()
  {
    // Empty constructor
  }

  public void initialize(CurrentBookingListVM currentBookingListVM, ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.currentBookingListVM = currentBookingListVM;

    // Set up table columns
    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getLocation()));
    startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getStartDate().toString()));
    endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEndDate().toString()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().getPricePerNight()).asObject());
    
    // Load data
    loadBookings();
    
    // Add refresh button if available in the FXML
    if (refreshButton != null) {
      refreshButton.setOnAction(e -> loadBookings());
    }
  }
  
  private void loadBookings() {
    try {
      // Get the bookings data from view model
      table.setItems(currentBookingListVM.getBookingHistory());
      
      // Show status message if no bookings
      if (table.getItems().isEmpty() && statusLabel != null) {
        statusLabel.setText("You have no current bookings.");
      } else if (statusLabel != null) {
        statusLabel.setText(""); // Clear any error
      }
    } catch (Exception e) {
      System.err.println("ERROR: Failed to load current bookings: " + e.getMessage());
      e.printStackTrace();
      if (statusLabel != null) {
        statusLabel.setText("Error loading bookings. Please try again.");
      }
    }
  }

  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }
  
  @FXML
  public void onRefresh() {
    loadBookings();
  }
  
  @FXML
  public void onExtendBooking() {
    BookingHistory selectedBooking = table.getSelectionModel().getSelectedItem();
    if (selectedBooking == null) {
      if (statusLabel != null) {
        statusLabel.setText("Please select a booking to extend");
      }
      return;
    }
    
    try {
      System.out.println("DEBUG: Extending booking for property: " + selectedBooking.getLocation());
      viewHandler.showExtendBookingView(selectedBooking);
    } catch (Exception e) {
      System.err.println("ERROR: Failed to open extend booking view: " + e.getMessage());
      e.printStackTrace();
      statusLabel.setText("Error opening extend booking view: " + e.getMessage());
    }
  }
}
