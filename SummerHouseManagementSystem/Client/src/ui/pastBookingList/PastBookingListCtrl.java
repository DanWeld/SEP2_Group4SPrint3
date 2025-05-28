package ui.pastBookingList;

import dtos.BookingHistory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Past Booking List view
 * This class handles the initialization of the view and binds the data
 * to the UI components.
 * It also provides functionality to navigate back to the user dashboard.
 * @author Group 4
 * @version 1.0

 */
public class PastBookingListCtrl
{
  @FXML private TableView<BookingHistory> table;
  @FXML private TableColumn<BookingHistory, String> locationColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, Double> pricePerNightColumn;
  @FXML private Button backButton;

  private PastBookingListVM pastBookingListVM;
  private ViewHandler viewHandler;

  /**
   * Default constructor for PastBookingListCtrl.
   * Initializes the controller without any parameters.
   */
  public PastBookingListCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler.
   * Binds the TableView to the booking history data and sets up the columns.
   * @param pastBookingListVM The ViewModel containing booking history data.
   * @param viewHandler The ViewHandler for navigating between views.
   */
  public void initialize(PastBookingListVM pastBookingListVM, ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.pastBookingListVM = pastBookingListVM;

    table.setItems(pastBookingListVM.getBookingHistory());

    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getLocation()));
    startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getStartDate().toString()));
    endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEndDate().toString()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().getPricePerNight()).asObject());
  }

  /**
   * Handles the back button click event.
   * Navigates back to the user dashboard view.
   */
  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }
}
