package ui.adminBookingHistory;

import dtos.BookingHistory;
import dtos.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

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

  public AdminBookingHistoryCtrl()
  {
  }

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

  @FXML private void onBackButtonPressed()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
  }

  @FXML private void onRefreshButtonPressed()
  {
    adminBookingHistoryVM.refresh();
  }
}
