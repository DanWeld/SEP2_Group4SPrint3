package ui.adminPropertyList;

import dtos.Property;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Admin Property List view.
 * This class handles the user interactions and updates the UI based on the ViewModel.
 */
public class AdminPropertyListCtrl
{
  @FXML private TableView<Property> propertyTableView;
  @FXML private TableColumn<Property, String> locationColumn;
  @FXML private TableColumn<Property, Double> pricePerNightColumn;
  @FXML private TableColumn<Property, String> facilityColumn;
  @FXML private Button backButton;
  @FXML private Button refreshButton;
  @FXML private Button deletePropertyButton;
  @FXML private Button addPropertyButton;
  @FXML private Button updatePropertyButton;
  @FXML private Button viewBookingHistoryButton;
  @FXML private Label errorMsg;

  private AdminPropertyListVM adminPropertyListVM;
  private ViewHandler viewHandler;

  /**
   * Default constructor for AdminPropertyListCtrl.
   * Initializes the controller without any parameters.
   */
  public AdminPropertyListCtrl()
  {
  }

  /**
   * Initializes the controller with the provided ViewModel and ViewHandler.
   * Binds the UI components to the ViewModel properties and sets up the table view.
   *
   * @param adminPropertyListVM the ViewModel for this view
   * @param viewHandler the ViewHandler for navigating between views
   */
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

    //disable the viewBookingHistoryButton if no property is selected
    viewBookingHistoryButton.disableProperty()
        .bind(adminPropertyListVM.getSelectedProperty().isNull());
    //disable the deletePropertyButton if no property is selected
    deletePropertyButton.disableProperty()
        .bind(adminPropertyListVM.getSelectedProperty().isNull());
    //disable the updatePropertyButton if no property is selected
    updatePropertyButton.disableProperty()
        .bind(adminPropertyListVM.getSelectedProperty().isNull());
  }

  /**
   * Called when a property is selected from the table.
   * This method navigates to the booking history view for the selected property.
   */
  public void onSelectProperty()
  {
    ObjectProperty<Property> selected = adminPropertyListVM.getSelectedProperty();
    if (selected != null)
    {
      viewHandler.setPropertyFromAdminPropertyList(selected.getValue());
      viewHandler.showView(ViewHandler.ViewType.ADMIN_BOOKING_HISTORY);
    }
    else
    {
      errorMsg.textProperty().get();
    }
  }

  /**
   * Called when the back button is pressed.
   * This method navigates back to the admin dashboard view.
   */
  public void onRefreshButtonPressed()
  {
    adminPropertyListVM.Refresh();
  }

  /**
   * Called when the delete property button is pressed.
   * This method prompts the user for confirmation before deleting the selected property.
   */
  public void onDeleteProperty()
  {
    new Alert(Alert.AlertType.CONFIRMATION,
        "Are you sure you want to delete the selected property?").showAndWait()
        .ifPresent(response -> {
          if (response == ButtonType.OK)
          {
            adminPropertyListVM.deleteSelectedProperty();
          }
        });
  }

  /**
   * Called when the view booking history button is pressed.
   * This method navigates to the booking history view for the selected property.
   */
  public void onAddProperty()
  {
    viewHandler.showView(ViewHandler.ViewType.ADD_PROPERTY);
  }

  /**
   * Called when the update property button is pressed.
   * This method navigates to the property management view for the selected property.
   */
  public void onUpdateProperty()
  {
    ObjectProperty<Property> selected = adminPropertyListVM.getSelectedProperty();

    viewHandler.setPropertyFromAdminPropertyList(selected.get());
    viewHandler.showView(ViewHandler.ViewType.PROPERTY_MANAGEMENT);
  }

  /**
   * Called when the back button is pressed.
   * This method navigates back to the admin dashboard view.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_DASHBOARD);
  }
}