package ui.addProperty;

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
 * ViewModel for the AddProperty view.
 * Handles the logic for adding a new property, including validation and communication with the PropertyClient.
 */
public class AddPropertyVM implements PropertyChangeListener
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
   * Constructor for AddPropertyVM.
   * Initializes the properties and sets up the PropertyClient.
   */
  public AddPropertyVM()
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
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
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
   * Handles property change events from the PropertyClient.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String propertyName = evt.getPropertyName();

    switch (propertyName)
    {
      case "create":
        Property property = (Property) evt.getNewValue();
        message.set("Property created: " + property.location() + " at "
            + property.pricePerNight() + " per night" + " with facilities: "
            + property.facilities());
      case "error":
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        message.set("Error: " + errorResponse.errorMessage());
        break;
    }
  }

  /**
   * Adds a new property using the PropertyClient.
   * Validates the input and creates a Property object to send to the server.
   */
  public void addProperty()
  {
    Property property = new Property(propertyID.get(), location.get(),
        Double.parseDouble(pricePerNight.get()),
        new Facilities(kitchen.get(), internet.get(), dishwasher.get(),
            laundryMachine.get(), swimmingPool.get()));

    propertyClient.createProperty(property);
  }
}
