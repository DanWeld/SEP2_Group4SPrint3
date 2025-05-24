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

  public void saveUpdatedProperty()
  {
    Property property = new Property(propertyID.get(), location.get(),
        Double.parseDouble(pricePerNight.get()),
        new Facilities(kitchen.get(), internet.get(), dishwasher.get(),
            laundryMachine.get(), swimmingPool.get()));

    propertyClient.updateProperty(property);
  }
}
