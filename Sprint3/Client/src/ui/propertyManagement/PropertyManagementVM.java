package ui.propertyManagement;

import dtos.ErrorResponse;
import dtos.Facilities;
import dtos.Property;
import javafx.beans.property.*;
import networking.Client;
import networking.propertyClient.PropertyClient;
import networking.propertyClient.PropertyClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

/**
 * ViewModel for managing property details in the Property Management view.
 * This class handles the
 * logic for updating property information, including location,
 * price per night, and facilities.
 * It listens for property change events and updates the UI accordingly.
 *
 * @author Group 4
 * @version 1.0
 */
public class PropertyManagementVM implements PropertyChangeListener
{
  private final IntegerProperty propertyID = new SimpleIntegerProperty(-1);
  private final StringProperty location = new SimpleStringProperty("");
  private final StringProperty pricePerNight = new SimpleStringProperty("");
  private final BooleanProperty kitchen = new SimpleBooleanProperty(false);
  private final BooleanProperty dishwasher = new SimpleBooleanProperty(false);
  private final BooleanProperty laundryMachine = new SimpleBooleanProperty(
      false);
  private final BooleanProperty swimmingPool = new SimpleBooleanProperty(false);
  private final BooleanProperty internet = new SimpleBooleanProperty(false);
  private final StringProperty message = new SimpleStringProperty("");
  private final BooleanProperty saveDisabled = new SimpleBooleanProperty(true);
  private PropertyClient propertyClient;

  /**
   * Constructor for PropertyManagementVM.
   * Initializes the properties and the property client.
   * It also sets up a listener for changes in the property client.
   */
  public PropertyManagementVM()
  {
    saveDisabled.bind(location.isEmpty().or(pricePerNight.isEmpty()));
    try
    {
      propertyClient = new PropertyClientImpl(new Client());
      propertyClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      message.set("Failed to connect to property client: " + e.getMessage());
    }
  }

  /**
   * Sets the property details in the ViewModel.
   * This method is called when a property is selected for editing.
   * It updates the properties with the details of the selected property.
   *
   * @param property the Property object containing the details to set
   */
  public void setProperty(Property property)
  {
    if (property == null)
      return;
    propertyID.set(property.id());
    location.set(property.location());
    pricePerNight.set(String.valueOf(property.pricePerNight()));
    Facilities facilities = property.facilities();
    if (facilities != null)
    {
      kitchen.set(facilities.kitchen());
      dishwasher.set(facilities.dishwasher());
      laundryMachine.set(facilities.laundryMachine());
      swimmingPool.set(facilities.swimmingPool());
      internet.set(facilities.internet());
    }
    else
    {
      kitchen.set(false);
      dishwasher.set(false);
      laundryMachine.set(false);
      swimmingPool.set(false);
      internet.set(false);
    }
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   *
   * @return the property values as JavaFX properties
   */
  public StringProperty locationProperty()
  {
    return location;
  }

  public StringProperty pricePerNightProperty()
  {
    return pricePerNight;
  }

  public BooleanProperty kitchenProperty()
  {
    return kitchen;
  }

  public BooleanProperty dishwasherProperty()
  {
    return dishwasher;
  }

  public BooleanProperty laundryMachineProperty()
  {
    return laundryMachine;
  }

  public BooleanProperty swimmingPoolProperty()
  {
    return swimmingPool;
  }

  public BooleanProperty internetProperty()
  {
    return internet;
  }

  public StringProperty messageProperty()
  {
    return message;
  }

  public BooleanProperty saveDisabledProperty()
  {
    return saveDisabled;
  }

  /**
   * Gets the property change event.
   * This method is called when a property change event occurs,
   * such as when a property is updated or an error occurs.
   *
   * @param evt the PropertyChangeEvent containing the details of the change
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String propertyName = evt.getPropertyName();

    switch (propertyName)
    {
      case "update":
        Property property = (Property) evt.getNewValue();
        message.set("Property updated: " + property.location() + " at "
            + property.pricePerNight() + " per night" + " with facilities: "
            + property.facilities());
      case "error":
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        message.set("Error: " + errorResponse.errorMessage());
        break;
    }
  }

  /**
   * Saves the updated property details.
   * This method is called when the user clicks the save button.
   * It creates a new Property object with the updated details and sends it
   * to the property client for updating.
   */
  public void saveUpdatedProperty()
  {
    Property property = new Property(propertyID.get(), location.get(),
        Double.parseDouble(pricePerNight.get()),
        new Facilities(kitchen.get(), internet.get(), dishwasher.get(),
            laundryMachine.get(), swimmingPool.get()));

    propertyClient.updateProperty(property);
  }
}
