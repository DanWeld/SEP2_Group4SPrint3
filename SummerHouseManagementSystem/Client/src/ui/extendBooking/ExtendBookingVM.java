package ui.extendBooking;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.ErrorResponse;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.ClientSocket;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;

/**
 * ViewModel for extending a booking
 * This class handles the logic for extending a booking,
 * validating the new end date,
 * and communicating with the BookingClient service.
 *
 * @author group 4
 * @version 1.0
 */
public class ExtendBookingVM implements PropertyChangeListener
{
  private final ObjectProperty<BookingHistory> selectedBooking;
  private final ObjectProperty<Date> newEndDate;
  private final StringProperty message;
  private final BookingClient bookingClient;

  /**
   * Constructor initializes the BookingClient and properties
   */
  public ExtendBookingVM()
  {
    try
    {
      ClientSocket client = new ClientSocket();
      bookingClient = new BookingClientImpl(client);
      bookingClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    selectedBooking = new SimpleObjectProperty<>();
    newEndDate = new SimpleObjectProperty<>();
    message = new SimpleStringProperty("");
  }

  /**
   * Sets the selected booking and initializes the new end date
   *
   * @param booking the booking to extend
   */
  public void setBooking(BookingHistory booking)
  {
    if (booking == null)
    {
      selectedBooking.set(null);
      newEndDate.set(null);
      message.set("No booking selected. Please select a booking to extend.");
      return;
    }
    selectedBooking.set(booking);
    newEndDate.set(booking.getEndDate());
  }

  /**
   * Attempts to extend the booking to the new end date
   * Validates that the new end date is after the current end date.
   * If validation fails, sets an error message.
   * If successful, calls the BookingClient to extend the booking.
   */
  public void extendBooking()
  {
    if (newEndDate.get() == null)
    {
      message.set("end date isn't selected");
      return;
    }

    // Validate that new end date is after the current end date
    if (newEndDate.get().before(selectedBooking.get().getEndDate()))
    {
      message.set("New end date must be after current end date");
      return;
    }

    // Call service to extend booking
    int propertyId = selectedBooking.get().getPropertyId();
    String username = selectedBooking.get().getUsername();

    Booking booking = new Booking(selectedBooking.get().getStartDate(),
        newEndDate.get(), propertyId, username);
    bookingClient.extendBooking(booking);
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   * @return the property values as JavaFX properties
   */
  public ObjectProperty<BookingHistory> selectedBookingProperty()
  {
    return selectedBooking;
  }

  public ObjectProperty<Date> newEndDateProperty()
  {
    return newEndDate;
  }

  public StringProperty messageProperty()
  {
    return message;
  }

  /**
   * PropertyChange method to handle events from the BookingClient
   * This method listens for changes in the booking extension process,
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("extend"))
    {
      BookingHistory bookingHistory = JsonParser.convertPayload(
          evt.getNewValue(), BookingHistory.class);
      message.set("Booking extended to: " + bookingHistory.getEndDate());
    }
    else if (evt.getPropertyName().equals("error"))
    {
      ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
      String errorMessage = errorResponse.errorMessage();
      message.set(errorMessage);
    }
  }
}