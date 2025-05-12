package main.dk.via.pro2.sprint1.viewmodel;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import main.dk.via.pro2.sprint1.model.*;
import main.dk.via.pro2.sprint1.model.Property;

import java.time.LocalDate;
import java.sql.Date;

public class BookingVM
{
  private IntegerProperty propertyID;
  private StringProperty location;
  private StringProperty propertyFacilities;
  private DoubleProperty pricePerNight;
  private ObjectProperty<Date> changeEndDate;
  private BooleanProperty availability;
  private StringProperty errorMsg;
  private PropertyListModel bookingModel;

  public BookingVM(PropertyListModel bookingModel)
  {
    this.bookingModel = bookingModel;
    propertyID = new SimpleIntegerProperty();
    location = new SimpleStringProperty();
    propertyFacilities = new SimpleStringProperty();
    pricePerNight = new SimpleDoubleProperty();
    changeEndDate = new SimpleObjectProperty<>();
    availability = new SimpleBooleanProperty();
    errorMsg = new SimpleStringProperty();
  }

  public void updateProperty(int id)
  {
    Property property = bookingModel.getByID(id);
    if (property != null)
    {
      location.set(property.getLocation());
      propertyFacilities.set(property.getFacilities().toString());
      pricePerNight.set(property.getPricePerNight());
    }
    else
    {
      location.set("");
      propertyFacilities.set("");
      pricePerNight.set(0);
    }
  }

  public IntegerProperty getPropertyID()
  {
    return propertyID;
  }

  public StringProperty getLocationProperty()
  {
    return location;
  }

  public StringProperty getFacilitiesProperty()
  {
    return propertyFacilities;
  }

  public DoubleProperty getPricePerNightProperty()
  {
    return pricePerNight;
  }

  public void changeEndDate(LocalDate date)
  {
    if (date.isBefore(LocalDate.now()))
    {
      errorMsg.setValue("End date cannot be in the past");
      return;
    }
    changeEndDate.set(Date.valueOf(date));
    // Check availability
    //TODO: To be changed according to the new model
    availability.set(bookingModel.isAvailable(Date.valueOf(date), Date.valueOf(date), propertyID.get()));
    errorMsg.setValue("");
  }

  public StringProperty getAvailabilityProperty()
  {
    if (availability.get())
    {
      return new SimpleStringProperty("Available");
    }
    else
    {
      return new SimpleStringProperty("Not Available");
    }
  }


  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }
}