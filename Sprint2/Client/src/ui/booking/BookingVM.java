package Client.src.ui.booking;

import dtos.Booking;
import dtos.Property;
import javafx.beans.property.*;
import networking.Client;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import networking.propertyListClient.PropertyListClient;
import networking.propertyListClient.PropertyListClientImpl;
import ui.specifyDates.SpecifyDatesVM;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

public class BookingVM implements PropertyChangeListener
{
  private Property property;
  private IntegerProperty propertyID;
  private StringProperty location;
  private StringProperty propertyFacilities;
  private DoubleProperty pricePerNight;
  private ObjectProperty<Date> changeEndDate;
  private StringProperty availability;
  private StringProperty errorMsg;
  private PropertyListClient propertyListClient;
  private BookingClient bookingClient;
  private Date startDate;
  private Date endDate;
  private SpecifyDatesVM specifyDatesVM;

  public BookingVM()
  {
    // Initialize the properties
    propertyID = new SimpleIntegerProperty();
    location = new SimpleStringProperty();
    propertyFacilities = new SimpleStringProperty();
    pricePerNight = new SimpleDoubleProperty();
    changeEndDate = new SimpleObjectProperty<>();
    availability = new SimpleStringProperty();
    errorMsg = new SimpleStringProperty();

    // Initialize the client
    try
    {
      Client client = new Client();
      propertyListClient = new PropertyListClientImpl(client);
      client.addPropertyChangeListener(this);
      bookingClient = new BookingClientImpl(client);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    endDate = Date.valueOf(LocalDate.now());
    startDate = Date.valueOf(LocalDate.now());
    changeEndDate.set(endDate);
  }

  public void updateProperty(Property property) throws Exception
  {
    this.property = property;
    // Update the property details
    propertyID.set(property.id());
    location.set(property.location());
    propertyFacilities.set(property.facilities().toString());
    pricePerNight.set(property.pricePerNight());
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

  public void onChangeEndDate(LocalDate newEndDate)
  {
    if (newEndDate.isBefore(startDate.toLocalDate()))
    {
      errorMsg.setValue("End date cannot be in the past");
      return;
    }

    // Check if the new end date is before the start date
    changeEndDate.set(Date.valueOf(newEndDate));

    // Check availability
    try
    {
      propertyListClient.isAvailable(Date.valueOf(startDate.toLocalDate()),
          Date.valueOf(newEndDate), propertyID.get());
    }
    catch (Exception e)
    {
      errorMsg.setValue("Error checking availability: " + e.getMessage());
      return;
    }
    errorMsg.setValue("");
  }

  public StringProperty getAvailabilityProperty()
  {
    if (availability.get().equals("true"))
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

  public void setDates(Date startDate, Date endDate)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    changeEndDate.set(Date.valueOf(endDate.toLocalDate()));
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void createBooking()
  {
    try
    {
      bookingClient.createBooking(propertyID.get(), startDate, endDate,
          "YoussefTopaji");
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("isAvailable"))
    {
      availability.set((String) evt.getNewValue());
      if (evt.getNewValue().equals("true"))
      {
        endDate = changeEndDate.get();
      }
    }
    else if (evt.getPropertyName().equals("bookingCreated"))
    {
      // Handle booking creation success
      Booking booking = (Booking) evt.getNewValue();
      errorMsg.setValue(
          "Booking created successfully from " + booking.getStartDate() + " to "
              + booking.getEndDate());
    }
    else if (evt.getPropertyName().equals("error"))
    {
      errorMsg.setValue((String) evt.getNewValue());
    }
  }
}