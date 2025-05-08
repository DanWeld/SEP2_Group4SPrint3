package ui.bookingHistory;

import dtos.BookingHistory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import startup.viewHandler.ViewHandler;

public class BookingHistoryCtrl
{
  @FXML private TableView<BookingHistory> table;
  @FXML private TableColumn<BookingHistory, String> locationColumn;
  @FXML private TableColumn<BookingHistory, String> startDateColumn;
  @FXML private TableColumn<BookingHistory, String> endDateColumn;
  @FXML private TableColumn<BookingHistory, Double> pricePerNightColumn;
  @FXML private Button backButton;

  private BookingHistoryVM bookingHistoryVM;
  private ViewHandler viewHandler;

  public BookingHistoryCtrl()
  {
  }

  public void initialize(BookingHistoryVM bookingHistoryVM, ViewHandler viewHandler)
  {
    this.viewHandler = viewHandler;
    this.bookingHistoryVM = bookingHistoryVM;

    table.setItems(bookingHistoryVM.getBookingHistory());

    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getLocation()));
    startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getStartDate().toString()));
    endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().getEndDate().toString()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().getPricePerNight()).asObject());
  }

  public void onBackButton()
  {
    viewHandler.showView(ViewHandler.ViewType.USER_DASHBOARD);
  }
}
