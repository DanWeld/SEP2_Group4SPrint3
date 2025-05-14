package ui.booking;

import dtos.Booking;
import dtos.Property;
import dtos.User;
import javafx.beans.property.*;
import networking.Client;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import networking.propertyListClient.PropertyListClient;
import networking.propertyListClient.PropertyListClientImpl;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * ViewModel for the Booking view, adhering to MVVM principles.
 */
public class BookingVM implements PropertyChangeListener
{
  private Property property;

  private final IntegerProperty propertyID = new SimpleIntegerProperty();
  private final StringProperty location = new SimpleStringProperty();
  private final StringProperty propertyFacilities = new SimpleStringProperty();
  private final DoubleProperty pricePerNight = new SimpleDoubleProperty();

  private final ObjectProperty<Date> startDate = new SimpleObjectProperty<>();
  private final ObjectProperty<Date> endDate = new SimpleObjectProperty<>();
  private final ObjectProperty<Date> changeEndDate = new SimpleObjectProperty<>();

  private final StringProperty availability = new SimpleStringProperty();
  private final StringProperty errorMsg = new SimpleStringProperty();
  private final BooleanProperty submitButtonDisabled = new SimpleBooleanProperty(
      false);

  private final BookingClient bookingClient;
  private User user;

  public BookingVM()
  {
    try
    {
      Client client = new Client();
      bookingClient = new BookingClientImpl(client);
      client.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(
          "Error initializing BookingVM: " + e.getMessage());
    }

    // Set default dates
    LocalDate today = LocalDate.now();
    Date todayDate = Date.valueOf(today);
    startDate.set(todayDate);
    endDate.set(todayDate);
    changeEndDate.set(todayDate);

    // Initially disable submit button
    submitButtonDisabled.set(false);

    // set user from session
    user = UserSession.getInstance().getCurrentUser();
  }

  public void updateProperty(Property property)
  {
    this.property = property;
    propertyID.set(property.id());
    location.set(property.location());
    propertyFacilities.set(property.facilities().toString());
    pricePerNight.set(property.pricePerNight());
  }

  public void setDates(Date start, Date end)
  {
    startDate.set(start);
    endDate.set(end);
    changeEndDate.set(end);
  }

  public void onChangeEndDate(LocalDate newEndDate)
  {
    if (newEndDate == null)
    {
      errorMsg.set("Please select a date.");
      submitButtonDisabled.set(true);
      return;
    }

    if (newEndDate.isBefore(startDate.get().toLocalDate()))
    {
      errorMsg.set("Please select a date after the start date.");
      submitButtonDisabled.set(true);
      return;
    }

    // Clear error, update temporary end date
    errorMsg.set("");
    changeEndDate.set(Date.valueOf(newEndDate));
    availability.set("Checking...");

    // Call server to check availability
    try
    {
      bookingClient.isAvailable(startDate.get(), changeEndDate.get(),
          propertyID.get());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  public void createBooking()
  {
    try
    {
      bookingClient.createBooking(propertyID.get(), startDate.get(),
          endDate.get(), user.getUsername());
      errorMsg.set("Booking successful from " + startDate.get() + " to "
          + endDate.get());
    }
    catch (Exception e)
    {
      errorMsg.set("Booking failed: " + e.getMessage());
    }
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "isAvailable" ->
      {
        String result = (String) evt.getNewValue();
        boolean isAvailable = Boolean.parseBoolean(result);
        availability.set(isAvailable ? "Available" : "Not Available");
        submitButtonDisabled.set(!isAvailable);

        if (isAvailable)
        {
          endDate.set(changeEndDate.get());
        }
      }

      case "bookingCreated" ->
      {
        Booking booking = (Booking) evt.getNewValue();
        errorMsg.set("Booking created from " + booking.getStartDate() + " to "
            + booking.getEndDate());
      }

      case "error" ->
      {
        errorMsg.set((String) evt.getNewValue());
      }
    }
  }

  // Getters for View Binding
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

  public ObjectProperty<Date> getEndDateProperty()
  {
    return endDate;
  }

  public StringProperty getAvailabilityProperty()
  {
    return availability;
  }

  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }

  public BooleanProperty getSubmitButtonDisabledProperty()
  {
    return submitButtonDisabled;
  }
}
