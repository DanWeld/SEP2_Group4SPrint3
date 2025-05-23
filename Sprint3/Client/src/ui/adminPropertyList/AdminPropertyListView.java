package ui.adminPropertyList;

import dtos.Property;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.bookingHistory.PropertyBookingHistoryModel;
import startup.viewHandler.ViewHandler;

import java.sql.SQLException;

/**
 * Controller for the Admin Property List view
 */
public class AdminPropertyListView {
    @FXML private TableView<Property> propertyTableView;
    @FXML private TableColumn<Property, String> locationColumn;
    @FXML private TableColumn<Property, Double> pricePerNightColumn;
    @FXML private TableColumn<Property, String> facilityColumn;
    @FXML private Button backButton;
    @FXML private Button refreshButton;
    @FXML private Button viewBookingHistoryButton;
    @FXML private Label errorMsg;

    private AdminPropertyListVM adminPropertyListVM;
 //   private PropertyBookingHistoryModel propertyBookingHistoryModel;
    private ObjectProperty<Property> selectedProperty;
    private ViewHandler viewHandler;

    public AdminPropertyListView(){

    }
    public void initialize(AdminPropertyListVM adminPropertyListVM, ViewHandler viewHandler) throws SQLException {
        //passing all data
        this.viewHandler = viewHandler;
//        this.propertyBookingHistoryModel = propertyBookingHistoryModel;
        this.adminPropertyListVM = adminPropertyListVM;

        //binding table view to observableList.
        propertyTableView.setItems(adminPropertyListVM.getAllPropertiesList());

        //Table column bindings
        locationColumn.setCellValueFactory(data ->new SimpleStringProperty(data.getValue().location()));
        pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPricePerNight()).asObject());
        facilityColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().facilities().toString()));

        //bind selected property
        adminPropertyListVM.bindSelectedProperty(propertyTableView.getSelectionModel().selectedItemProperty());
        //bind error message label.
        errorMsg.textProperty().bind(adminPropertyListVM.messageProperty());
        //Add selection listener
      /*  propertyTableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue!=null){
                try {
                    onSelectProperty();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
       */
    }

    /*public void onTableViewClicked(MouseEvent event) throws Exception {
        if (event.getClickCount() == 3 && propertyTableView.getSelectionModel().getSelectedItem() != null) {
            onSelectProperty();
        }else {
            errorMsg.textProperty().set("Error on selecting property!");
        }
    }*/

    public void onSelectProperty() throws Exception
    {
        ObjectProperty<Property> selected = adminPropertyListVM.getSelectedProperty();
       // int propertyId = adminPropertyListVM.getSelectedProperty().get().id();
        if(selected!=null){
            viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_BOOKING_HISTORY);
        }
        else {
            errorMsg.textProperty().get();
        }
    }
    public void onRefreshButtonPressed(){
        try {
            adminPropertyListVM.Refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void onBack(){
        viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
    }

}