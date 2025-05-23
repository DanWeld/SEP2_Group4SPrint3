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
import startup.viewHandler.ViewHandler;

import java.sql.SQLException;


public class AdminPropertyListCtrl
{
  @FXML private TableView<Property> propertyTableView;
  @FXML private TableColumn<Property, String> locationColumn;
  @FXML private TableColumn<Property, Double> pricePerNightColumn;
  @FXML private TableColumn<Property, String> facilityColumn;
  @FXML private Button backButton;
  @FXML private Button refreshButton;
  @FXML private Button viewBookingHistoryButton;
  @FXML private Label errorMsg;

  private AdminPropertyListVM adminPropertyListVM;
  private ViewHandler viewHandler;

  public AdminPropertyListCtrl()
  {
  }

  public void initialize(AdminPropertyListVM adminPropertyListVM,
      ViewHandler viewHandler)
  {
    //passing all data
    this.viewHandler = viewHandler;
    //        this.propertyBookingHistoryModel = propertyBookingHistoryModel;
    this.adminPropertyListVM = adminPropertyListVM;

    //binding table view to observableList.
    propertyTableView.setItems(adminPropertyListVM.getAllPropertiesList());

    //Table column bindings
    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().location()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().getPricePerNight()).asObject());
    facilityColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().facilities().toString()));

    //bind selected property
    adminPropertyListVM.bindSelectedProperty(
        propertyTableView.getSelectionModel().selectedItemProperty());
    //bind error message label.
    errorMsg.textProperty().bind(adminPropertyListVM.messageProperty());
  }

  public void onSelectProperty()
  {
    ObjectProperty<Property> selected = adminPropertyListVM.getSelectedProperty();
    if (selected != null)
    {
      viewHandler.setProperty(selected.getValue().id());
      viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
    }
    else
    {
      errorMsg.textProperty().get();
    }
  }

  public void onRefreshButtonPressed()
  {
    adminPropertyListVM.Refresh();
  }

  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
  }
}