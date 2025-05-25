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

  public void addProperty()
  {
    Property property = new Property(propertyID.get(), location.get(),
        Double.parseDouble(pricePerNight.get()),
        new Facilities(kitchen.get(), internet.get(), dishwasher.get(),
            laundryMachine.get(), swimmingPool.get()));

    propertyClient.createProperty(property);
  }
}
