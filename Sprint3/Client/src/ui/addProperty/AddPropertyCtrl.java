package ui.addProperty;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import startup.viewHandler.ViewHandler;

public class AddPropertyCtrl
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

  private AddPropertyVM propertyManagementVM;
  private ViewHandler viewHandler;

  /**
   * Constructor for PropertyListController.
   */
  public AddPropertyCtrl()
  {
  }

  /**
   * Initializes the PropertyListController.
   * This method is called by the JavaFX framework to initialize the controller.
   *
   * @param propertyManagementVM The ViewModel for the PropertyList view.
   * @param viewHandler          The ViewHandler for handling view changes.
   */
  public void initialize(AddPropertyVM propertyManagementVM,
      ViewHandler viewHandler)
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
  }

  /**
   * Called when the back button is pressed.
   * This method opens the specify dates view.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.ADMIN_PROPERTY_LIST);
  }

  public void onSave()
  {
    new Alert(Alert.AlertType.CONFIRMATION,
        "Are you sure you want to save the changes?").showAndWait()
        .ifPresent(response -> {
          if (response == ButtonType.OK)
          {
            propertyManagementVM.addProperty();
          }
        });
  }
}
