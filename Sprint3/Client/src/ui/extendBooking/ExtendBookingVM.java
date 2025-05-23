package ui.extendBooking;

import dtos.Booking;
import dtos.BookingHistory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import networking.Client;
import networking.bookingClient.BookingClient;
import networking.bookingClient.BookingClientImpl;
import services.UserSession;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;

/**
 * ViewModel for extending a booking
 */
public class ExtendBookingVM implements PropertyChangeListener
{
  private final ObjectProperty<BookingHistory> selectedBooking;
  private final ObjectProperty<Date> newEndDate;
  private final StringProperty message;
  private final BookingClient bookingClient;

  public ExtendBookingVM()
  {
    try
    {
      Client client = new Client();
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

  public void setBooking(BookingHistory booking) {
    if (booking == null) {
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
   *
   * @return true if extension was successful, false otherwise
   */
  public void extendBooking()
  {
    if (newEndDate.get() == null)
    {
      message.set("end date isn't selected");
    }

    // Validate that new end date is after the current end date
    if (newEndDate.get().before(selectedBooking.get().getEndDate()))
    {
      message.set("New end date must be after current end date");
    }

    // Call service to extend booking
    int propertyId = selectedBooking.get().getPropertyId();
    Date currentEndDate = selectedBooking.get().getEndDate();
    String username = selectedBooking.get().getUsername();

    Booking booking = new Booking(currentEndDate, newEndDate.get(), propertyId,
        username);
    bookingClient.extendBooking(booking);
  }

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

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("extend"))
    {
      message.set("Booking extended successfully");
    }
    else if (evt.getPropertyName().equals("error"))
    {
      String errorMessage = (String) evt.getNewValue();
      message.set(errorMessage);
    }
  }
}