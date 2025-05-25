package ui.propertyManagement;

import dtos.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the Property Management view.
 * This class handles the logic for managing properties, including editing and saving property details.
 *
 * @author Group 4
 * @version 1.0
 */
public class PropertyManagementCtrl
{
  @FXML public TextField locationTextField;
  @FXML public TextField pricePerNightTextField;
  @FXML public CheckBox kitchenCheckBox;
  @FXML public CheckBox dishwasherCheckBox;
  @FXML public CheckBox laundryMachineCheckBox;
  @FXML public CheckBox swimmingPoolCheckBox;
  @FXML public CheckBox internetCheckBox;
  @FXML public Label messageLabel;
  @FXML public Button backButton;
  @FXML public Button saveButton;

  private PropertyManagementVM propertyManagementVM;
  private ViewHandler viewHandler;

  /**
   * Constructor for PropertyListController.
   */
  public PropertyManagementCtrl()
  {
  }

  /**
   * Initializes the PropertyListController.
   * This method is called by the JavaFX framework to initialize the controller.
   *
   * @param propertyManagementVM The ViewModel for the PropertyList view.
   * @param viewHandler          The ViewHandler for handling view changes.
   */
  public void initialize(PropertyManagementVM propertyManagementVM,
      ViewHandler viewHandler, Property property)
  {
    this.propertyManagementVM = propertyManagementVM;
    this.viewHandler = viewHandler;

    // Bind the UI components to the ViewModel properties
    locationTextField.textProperty()
        .bindBidirectional(propertyManagementVM.locationProperty());
    pricePerNightTextField.textProperty()
        .bindBidirectional(propertyManagementVM.pricePerNightProperty());
    kitchenCheckBox.selectedProperty()
        .bindBidirectional(propertyManagementVM.kitchenProperty());
    dishwasherCheckBox.selectedProperty()
        .bindBidirectional(propertyManagementVM.dishwasherProperty());
    laundryMachineCheckBox.selectedProperty()
        .bindBidirectional(propertyManagementVM.laundryMachineProperty());
    swimmingPoolCheckBox.selectedProperty()
        .bindBidirectional(propertyManagementVM.swimmingPoolProperty());
    internetCheckBox.selectedProperty()
        .bindBidirectional(propertyManagementVM.internetProperty());
    messageLabel.textProperty().bind(propertyManagementVM.messageProperty());
    saveButton.disableProperty()
        .bind(propertyManagementVM.saveDisabledProperty());

    // Set the property if provided
    if (property != null)
    {
      propertyManagementVM.setProperty(property);
    }
  }

  /**
   * Called when the back button is pressed.
   * This method opens the specify dates view.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
  }

  /**
   * Called when the save button is pressed.
   * This method prompts the user for confirmation before saving the changes to the property.
   */
  public void onSave()
  {
    new Alert(Alert.AlertType.CONFIRMATION,
        "Are you sure you want to save the changes?").showAndWait()
        .ifPresent(response -> {
          if (response == ButtonType.OK)
          {
            propertyManagementVM.saveUpdatedProperty();
          }
        });
  }
}
