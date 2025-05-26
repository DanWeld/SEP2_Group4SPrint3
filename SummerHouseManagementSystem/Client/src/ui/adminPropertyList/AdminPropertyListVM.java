package ui.adminPropertyList;

import dtos.ErrorResponse;
import dtos.Property;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.ClientSocket;
import networking.propertyClient.PropertyClient;
import networking.propertyClient.PropertyClientImpl;
import networking.propertyListClient.PropertyListClient;
import networking.propertyListClient.PropertyListClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * ViewModel for the Admin Property List view
 */
public class AdminPropertyListVM implements PropertyChangeListener
{
  private final ObservableList<Property> properties;
  private SimpleObjectProperty<Property> selectedProperty;
  private IntegerProperty selectedPropertyId;          //for any selected property.
  private StringProperty selectedPropertyLocation;
  private DoubleProperty selectedPropertyPricePerNight;
  private StringProperty errorMessage;
  private final PropertyListClient propertyListClient;
  private final PropertyClient propertyClient;

  /**
   * Default constructor for AdminPropertyListVM.
   * Initializes the PropertyListClient and PropertyClient, and sets up the properties.
   */
  public AdminPropertyListVM()
  {
    try
    {
      this.propertyListClient = new PropertyListClientImpl(new ClientSocket());
      this.propertyListClient.addPropertyChangeListener(this);
      this.propertyClient = new PropertyClientImpl(new ClientSocket());
      this.propertyClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    this.properties = FXCollections.observableArrayList();
    this.selectedProperty = new SimpleObjectProperty<>();
    this.selectedPropertyId = new SimpleIntegerProperty(0);
    this.selectedPropertyLocation = new SimpleStringProperty("");
    this.selectedPropertyPricePerNight = new SimpleDoubleProperty(0.00);

    this.errorMessage = new SimpleStringProperty();
    Refresh();
  }

  /**
   * Returns the list of all properties.
   * This list is bound to the UI components in the Admin Property List view.
   *
   * @return the observable list of properties
   */
  public ObservableList<Property> getAllPropertiesList()
  {
    return properties;
  }

  /**
   * Returns the selected property.
   * This property is bound to the UI components in the Admin Property List view.
   *
   * @return the selected property as an ObjectProperty
   */
  public ObjectProperty<Property> getSelectedProperty()
  {
    return selectedProperty;
  }

  /**
   * Binds the selected property from the table to the ViewModel.
   *
   * @param selectedFromTable the ReadOnlyObjectProperty of the selected property from the table
   */
  public void bindSelectedProperty(
      ReadOnlyObjectProperty<Property> selectedFromTable)
  {
    selectedProperty.bind(selectedFromTable);
    // Update selectedPropertyId when selectedProperty changes
    selectedProperty.addListener((obs, oldValue, newValue) -> {
      if (newValue != null)
      {
        selectedPropertyId.set(newValue.id());
        selectedPropertyLocation.set(newValue.location());
        selectedPropertyPricePerNight.set(newValue.pricePerNight());
      }
      else
      {
        selectedPropertyId.set(0);
        selectedPropertyLocation.set("");
        selectedPropertyPricePerNight.set(0.00);
      }
    });
  }

  /**
   * Getter for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   *
   * @return the property values as JavaFX properties
   */
  public StringProperty messageProperty()
  {
    return errorMessage;
  }

  /**
   * Refreshes the list of properties.
   * This method clears the current properties and fetches the latest properties from the server.
   * This is typically called when the view is initialized or when the user requests a refresh.
   */
  public void Refresh()
  {
    this.properties.clear();
    propertyListClient.getAllProperties();
  }

  /**
   * Deletes the selected property.
   * This method sends a request to the server to delete the property with the currently selected ID.
   * It also updates the properties list and error message accordingly.
   */
  public void deleteSelectedProperty()
  {
    propertyClient.deleteProperty(selectedPropertyId.get());
  }

  /**
   * Property change event handler.
   * This method listens for property change events from the PropertyClient and PropertyListClient.
   * It updates the properties list, selected property, or error message based on the event type.
   * @param evt the PropertyChangeEvent containing the new value
   *            and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "readAll":
        properties.clear();
        List<Property> propertyList = JsonParser.toList(evt.getNewValue(),
            Property[].class);
        properties.addAll(propertyList);
        break;

      case "delete":
        Integer deletedPropertyId = (Integer) evt.getNewValue();
        properties.removeIf(property -> property.id() == deletedPropertyId);
        errorMessage.set("Property with ID " + deletedPropertyId
            + " has been deleted successfully.");
        break;

      case "error":
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        errorMessage.set(errorResponse.errorMessage());
        break;
    }
  }
}