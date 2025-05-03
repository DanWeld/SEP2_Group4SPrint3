package ui.booking;

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

/**
 * ViewModel for the Booking view.
 * This class handles the logic for booking a property.
 *
 * @author Group 4
 * @version 1.0
 */
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

  /**
   * Constructor for BookingVM.
   * Initializes the properties and the clients.
   */
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

  /**
   * Updates the property details in the view model.
   *
   * @param property The property to update.
   */
  public void updateProperty(Property property)
  {
    this.property = property;
    // Update the property details
    propertyID.set(property.id());
    location.set(property.location());
    propertyFacilities.set(property.facilities().toString());
    pricePerNight.set(property.pricePerNight());
  }

  /**
   * Gets the property ID.
   * @return The property ID as IntegerProperty.
   */
  public IntegerProperty getPropertyID()
  {
    return propertyID;
  }

  /**
   * Gets the property location.
   * @return The property location as StringProperty.
   */
  public StringProperty getLocationProperty()
  {
    return location;
  }

  /**
   * Gets the property facilities.
   * @return The property facilities as StringProperty.
   */
  public StringProperty getFacilitiesProperty()
  {
    return propertyFacilities;
  }

  /**
   * Gets the property price per night.
   * @return The property price per night as DoubleProperty.
   */
  public DoubleProperty getPricePerNightProperty()
  {
    return pricePerNight;
  }

  /**
   * change end date method.
   * Validates new end date.
   *
   * @param newEndDate The new end date to set.
   */
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

  /**
   * Gets the end date property.
   * @return The end date as StringProperty.
   */
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

  /**
   * Gets the error message property.
   * @return The error message as StringProperty.
   */
  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }

  /**
   * Sets the start and end dates for the booking.
   * @param startDate
   * @param endDate
   */
  public void setDates(Date startDate, Date endDate)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    changeEndDate.set(Date.valueOf(endDate.toLocalDate()));
  }

  /**
   * Gets the start date.
   * @return The start date as Date.
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * Gets the end date.
   * @return The end date as Date.
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * Creates a booking for the property.
   * This method is called when the user clicks the "Submit" button.
   */
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

  /**
   * Handles property change events.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
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