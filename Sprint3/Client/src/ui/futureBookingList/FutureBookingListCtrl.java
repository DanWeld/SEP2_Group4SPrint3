package ui.futureBookingList;

import dtos.BookingHistory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

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

  public FutureBookingListCtrl()
  {
  }

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

  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }

  public void onCancelButton()
  {
    futureBookingListVM.cancelBooking();
  }
}