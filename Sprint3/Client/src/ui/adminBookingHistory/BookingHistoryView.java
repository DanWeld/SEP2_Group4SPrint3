package ui.adminBookingHistory;

import dtos.BookingHistory;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.bookingHistory.PropertyBookingHistoryModel;
import persistence.daos.AdminPanel.PropertyBookingHistoryDAO;
import startup.viewHandler.ViewHandler;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class BookingHistoryView {
    @FXML private TableView<BookingHistory> tableView;
    @FXML private TableColumn<BookingHistory, String> usernameColumn;
    @FXML private TableColumn<BookingHistory, String> emailColumn;
    @FXML private TableColumn<BookingHistory, String> startDateColumn;
    @FXML private TableColumn<BookingHistory, String> endDateColumn;
    @FXML private TableColumn<BookingHistory, String> bookingDateColumn;
//    @FXML private TableColumn<BookingHistory2, String> locationColumn;
    @FXML private Button refreshButton;
    @FXML private Button backButton;
    @FXML private Label propertyIDLabel;
    @FXML private Label locationLabel;
    @FXML private Label pricePerNightLabel;
    @FXML private Label errorMsg;
  //  @FXML private CheckBox pastCheckBox, presentCheckBox, futureCheckBox;

    private BookingHistoryVM bookingHistoryVM;
//    private PropertyBookingHistoryModel propertyBookingHistoryModel;
    private ViewHandler viewHandler;

    public BookingHistoryView(){
                 //
    }

    public void initialize(BookingHistoryVM viewModel, ViewHandler viewHandler, int propertyId) throws SQLException {
  //      this.propertyBookingHistoryModel = propertyBookingHistoryModel;   //passing values from model to viewModel. so not creating a newone.
        this.bookingHistoryVM = viewModel;
        this.viewHandler = viewHandler;

        System.out.println("Initializing BookingHistoryView of propertyId: " + propertyId);
        viewModel.setPropertyID(propertyId);   //booking history of selected property
        //binding table view
            tableView.setItems(viewModel.getAllPropertyBookingHistory());      //for robustness instead tableView.setItems() but it showed exception and we use setItems()
        //binding tables.
        usernameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        startDateColumn.setCellValueFactory(data->new SimpleStringProperty(sdf.format(data.getValue().getStartDate())));
         endDateColumn.setCellValueFactory(data->new SimpleStringProperty(sdf.format(data.getValue().getEndDate())));
         bookingDateColumn.setCellValueFactory(data->new SimpleStringProperty(sdf.format(data.getValue().getBookingDate())));


     //binding labels
        propertyIDLabel.textProperty().bind(viewModel.getPropertyIDProperty());
        locationLabel.textProperty().bind(viewModel.getLocationProperty());
        pricePerNightLabel.textProperty().bind(viewModel.getPricePerNightProperty());
            errorMsg.textProperty().bind(viewModel.getErrorMessage());
       /* pastCheckBox.selectedProperty().bindBidirectional(bookingHistoryViewModel.showPastProperty());
        presentCheckBox.selectedProperty().bindBidirectional(bookingHistoryViewModel.showPresentProperty());
        futureCheckBox.selectedProperty().bindBidirectional(bookingHistoryViewModel.showFutureProperty());
    */
   /*  usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
     emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    */
    }

  @FXML  private void onBackButtonPressed() {
     viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
    }
   @FXML private void onRefreshButtonPressed() throws SQLException {
       bookingHistoryVM.Refresh();
    }
}
