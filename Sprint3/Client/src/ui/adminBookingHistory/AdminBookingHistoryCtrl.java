package ui.adminBookingHistory;

import dtos.BookingHistory;
import dtos.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * Controller for the Admin Booking History view.
 * This class handles the user interactions and updates the UI based on the ViewModel.
 */
public class AdminBookingHistoryCtrl
{
  @FXML private TableView<BookingHistory> tableView;
  @FXML private TableColumn<BookingHistory, String> usernameColumn;
  @FXML private TableColumn<BookingHistory, String> emailColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, String> bookingDateColumn;
  @FXML private Button refreshButton;
  @FXML private Button backButton;
  @FXML private Label propertyIDLabel;
  @FXML private Label locationLabel;
  @FXML private Label pricePerNightLabel;
  @FXML private Label errorMsg;

  private AdminBookingHistoryVM adminBookingHistoryVM;
  private ViewHandler viewHandler;

  /**
   * Default constructor for AdminBookingHistoryCtrl.
   * Initializes the controller without any parameters.
   */
  public AdminBookingHistoryCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel, ViewHandler, and Property.
   * Binds the UI components to the ViewModel properties and sets up the table view.
   *
   * @param viewModel the ViewModel for this view
   * @param viewHandler the ViewHandler for navigating between views
   * @param property the Property for which booking history is displayed
   */
  public void initialize(AdminBookingHistoryVM viewModel,
      ViewHandler viewHandler, Property property)
  {
    this.adminBookingHistoryVM = viewModel;
    this.viewHandler = viewHandler;

    viewModel.setProperty(property);

    //binding table view
    tableView.setItems(
        viewModel.getAllPropertyBookingHistory());

    //binding tables.
    usernameColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getUsername()));
    emailColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getEmail()));
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        sdf.format(data.getValue().getStartDate())));
    endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        sdf.format(data.getValue().getEndDate())));
    bookingDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        sdf.format(data.getValue().getBookingDate())));

    //binding labels
    propertyIDLabel.textProperty().bind(viewModel.getPropertyIDProperty());
    locationLabel.textProperty().bind(viewModel.getLocationProperty());
    pricePerNightLabel.textProperty()
        .bind(viewModel.getPricePerNightProperty());
    errorMsg.textProperty().bind(viewModel.getErrorMessage());
  }

  /**
   * Called when the view is shown.
   * Refreshes the booking history to ensure the latest data is displayed.
   */
  @FXML private void onBackButtonPressed()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
  }

  /**
   * Refreshes the booking history when the refresh button is pressed.
   * This method calls the ViewModel to update the booking history data.
   */
  @FXML private void onRefreshButtonPressed()
  {
    adminBookingHistoryVM.refresh();
  }
}
