package ui.adminPropertyHistory;

import dtos.Booking;
import dtos.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import startup.viewHandler.ViewHandler;

import java.text.SimpleDateFormat;

/**
 * Controller for the Admin Property History view
 */
public class AdminPropertyHistoryCtrl {
    @FXML private Label propertyNameLabel;
    @FXML private Label propertyAddressLabel;
    @FXML private Label propertyDetailsLabel;
    
    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML private TableColumn<Booking, String> usernameColumn;
    @FXML private TableColumn<Booking, java.sql.Date> startDateColumn;
    @FXML private TableColumn<Booking, java.sql.Date> endDateColumn;
    
    @FXML private Label messageLabel;
    
    private AdminPropertyHistoryVM viewModel;
    private ViewHandler viewHandler;
    
    public AdminPropertyHistoryCtrl() {
        // Empty constructor
    }
    
    public void initialize(AdminPropertyHistoryVM vm, ViewHandler vh, Property property) {
        this.viewModel = vm;
        this.viewHandler = vh;
        
        // Set up property details
        viewModel.setProperty(property);
        
        propertyNameLabel.setText(property.getName());
        propertyAddressLabel.setText(property.getAddress());
        propertyDetailsLabel.setText(
            String.format("Bedrooms: %d | Bathrooms: %d | Price: $%.2f per night", 
                property.getBedrooms(), property.getBathrooms(), property.getPrice())
        );
        
        // Set up table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        
        // Custom formatting for dates
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startDateColumn.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            @Override
            protected void updateItem(java.sql.Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(format.format(date));
                }
            }
        });
        
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endDateColumn.setCellFactory(column -> new TableCell<>() {
            private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            
            @Override
            protected void updateItem(java.sql.Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(format.format(date));
                }
            }
        });
        
        // Bind table items
        bookingTable.setItems(viewModel.getBookings());
        
        // Bind message
        messageLabel.textProperty().bind(viewModel.messageProperty());
    }
    
    @FXML
    public void onBack() {
        viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
    }
}
