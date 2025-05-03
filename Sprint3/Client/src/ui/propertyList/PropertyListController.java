package ui.propertyList;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import dtos.Property;
import startup.viewHandler.ViewHandler;

/**
 * Controller for the PropertyList view.
 * This class handles the logic for displaying a list of properties.
 *
 * @author Group 4
 * @version 1.0
 */
public class PropertyListController
{
  @FXML private TableView<Property> table;
  @FXML private TableColumn<Property, String> locationColumn;
  @FXML private TableColumn<Property, Double> pricePerNightColumn;
  @FXML private TableColumn<Property, String> facilitiesColumn;
  @FXML private Button selectButton;
  @FXML private Button backButton;
  @FXML private Label errorMsg;

  private PropertyListVM propertyListVM;
  private ObjectProperty<Property> selectedProperty;
  private ViewHandler viewHandler;

  /**
   * Constructor for PropertyListController.
   */
  public PropertyListController()
  {
  }

  /**
   * Initializes the PropertyListController.
   * This method is called by the JavaFX framework to initialize the controller.
   *
   * @param propertyListVM The ViewModel for the PropertyList view.
   * @param viewHandler The ViewHandler for handling view changes.
   */
  public void initialize(PropertyListVM propertyListVM, ViewHandler viewHandler)
  {
    this.propertyListVM = propertyListVM;
    this.viewHandler = viewHandler;

    // Bind the TableView to the ViewModel
    try
    {
      table.setItems(propertyListVM.getPropertyList());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }

    // Set up the columns
    locationColumn.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().location()));
    pricePerNightColumn.setCellValueFactory(data -> new SimpleDoubleProperty(
        data.getValue().pricePerNight()).asObject());
    facilitiesColumn.setCellValueFactory(data -> new SimpleStringProperty(
        data.getValue().facilities().toString()));

    // Bind the selected property to the ViewModel
    propertyListVM.bindSelectedProperty(
        table.getSelectionModel().selectedItemProperty());

    // Bind the error message to the ViewModel
    errorMsg.textProperty().bind(propertyListVM.getErrorMsgProperty());
  }

  /**
   * Called when the select button is pressed.
   * This method opens the booking view for the selected property.
   *
   * @throws Exception If an error occurs while opening the booking view.
   */
  public void onSelectProperty() throws Exception
  {
    if (propertyListVM.getSelectedProperty().get() != null)
    {
      viewHandler.showView(ViewHandler.ViewType.BOOKING);
    }
  }

  /**
   * Called when the back button is pressed.
   * This method opens the specify dates view.
   */
  public void onBack()
  {
    viewHandler.showView(ViewHandler.ViewType.SPECIFY_DATES);
  }
}
