package ui.booking;

import dtos.Booking;
import dtos.Property;
import dtos.User;
import javafx.beans.property.*;
import networking.ClientSocket;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import services.UserSession;
import utils.JsonParser;

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

  /**
   * Default constructor for BookingVM.
   * Initializes the BookingClient and sets up default values for properties.
   */
  public BookingVM()
  {
    try
    {
      ClientSocket client = new ClientSocket();
      bookingClient = new BookingClientImpl(client);
      bookingClient.addPropertyChangeListener(this);
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
  }

  /**
   * Sets the property details for the booking.
   * This method updates the ViewModel with the selected property information.
   *
   * @param property the Property object containing details of the property
   */
  public void setProperty(Property property)
  {
    this.property = property;
    location.set(property.location());
    pricePerNight.set(property.pricePerNight());
    propertyFacilities.set(property.facilities().toString());
    propertyID.set(property.id());
  }

  /**
   * Sets the start and end dates for the booking.
   * This method is called when the user selects dates in the UI.
   *
   * @param start the start date of the booking
   * @param end the end date of the booking
   */
  public void setDates(Date start, Date end)
  {
    startDate.set(start);
    endDate.set(end);
    changeEndDate.set(end);
  }

  /**
   * Handles changes to the end date selected by the user.
   * This method validates the new end date and checks availability.
   *
   * @param newEndDate the new end date selected by the user
   */
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
      Booking booking = new Booking(startDate.get(), changeEndDate.get(),
          propertyID.get());
      bookingClient.isAvailable(booking);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Creates a booking for the selected property with the specified dates.
   * This method is called when the user submits the booking form.
   * It checks if the user is logged in and then sends the booking request to the server.
   */
  public void createBooking()
  {
    // Get the current user
    user = UserSession.getInstance().getCurrentUser();
    if (user == null) {
      errorMsg.set("Please log in to book a property.");
      submitButtonDisabled.set(true);
      return;
    }

    Booking booking = new Booking(startDate.get(), endDate.get(),
        propertyID.get(), user.getUsername());
    bookingClient.createBooking(booking);
  }

  /**
   * Property change listener method.
   * This method is called when a property change event occurs in the BookingClient.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    switch (evt.getPropertyName())
    {
      case "isAvailable" ->
      {
        boolean isAvailable = (boolean) evt.getNewValue();
        availability.set(isAvailable ? "Available" : "Not Available");
        submitButtonDisabled.set(!isAvailable);

        if (isAvailable)
        {
          endDate.set(changeEndDate.get());
        }
      }
      case "create" ->
      {
        Booking booking = JsonParser.convertPayload(evt.getNewValue(),
            Booking.class);
        errorMsg.set("Booking created from " + booking.getStartDate() + " to "
            + booking.getEndDate());
      }
      case "error" ->
      {
        errorMsg.set((String) evt.getNewValue());
      }
    }
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   * @return the property values as JavaFX properties
   */
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
