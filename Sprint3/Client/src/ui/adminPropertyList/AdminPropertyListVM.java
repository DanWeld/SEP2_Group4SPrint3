package ui.adminPropertyList;

import dtos.ErrorResponse;
import dtos.Property;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.propertyClient.PropertyClient;
import networking.propertyClient.PropertyClientImpl;
import networking.propertyListClient.PropertyListClient;
import networking.propertyListClient.PropertyListClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
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

  public AdminPropertyListVM()
  {
    try
    {
      this.propertyListClient = new PropertyListClientImpl(new Client());
      this.propertyListClient.addPropertyChangeListener(this);
      this.propertyClient = new PropertyClientImpl(new Client());
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

  public ObservableList<Property> getAllPropertiesList()
  {
    return properties;
  }

  public ObjectProperty<Property> getSelectedProperty()
  {
    return selectedProperty;
  }

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

  public StringProperty messageProperty()
  {
    return errorMessage;
  }

  public void Refresh()
  {
    this.properties.clear();
    propertyListClient.getAllProperties();
  }

  public void deleteSelectedProperty()
  {
    propertyClient.deleteProperty(selectedPropertyId.get());
  }

  public void addNewProperty(Property property)
  {
    //TODO implement add new property functionality;
  }

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
        errorMessage.set("Property with ID " + deletedPropertyId + " has been deleted successfully.");
        break;

      case "error":
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        errorMessage.set(errorResponse.errorMessage());
        break;
    }
  }
}