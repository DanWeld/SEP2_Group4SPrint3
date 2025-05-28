package ui.currentBookingList;

import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.User;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.ClientSocket;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import services.UserSession;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

/**
 * ViewModel for the Current Booking List view.
 * This class handles the logic for displaying the current bookings of a user.
 */
public class CurrentBookingListVM implements PropertyChangeListener
{
  private final ObservableList<BookingHistory> bookings;
  private final BookingHistoryClient bookingHistoryClient;
  private final StringProperty msgProp;
  private final ObjectProperty<BookingHistory> selectedBooking;

  /**
   * Default constructor for CurrentBookingListVM.
   * Initializes the BookingHistoryClient and sets up the properties.
   */
  public CurrentBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    this.msgProp = new SimpleStringProperty();
    this.selectedBooking = new SimpleObjectProperty<>();
    try
    {
      bookingHistoryClient = new BookingHistoryClientImpl(new ClientSocket());
      bookingHistoryClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Returns the list of current bookings for the user.
   * This method fetches the current bookings from the BookingHistoryClient.
   *
   * @return an ObservableList of BookingHistory objects
   */
  public ObservableList<BookingHistory> getBookingHistory()
  {
    bookingHistoryClient.getCurrentBookings(getUser());
    return bookings;
  }

  /**
   * get the current user from the UserSession.
   *
   * @return the username of the current user
   */
  private String getUser()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    return currentUser.getUsername();
  }

  /**
   * Getters for the properties.
   * These properties are bound to the UI components in the AddProperty view.
   *
   * @return the property values as JavaFX properties
   */
  public StringProperty msgProperty()
  {
    return msgProp;
  }

  public ObjectProperty<BookingHistory> selectedBookingProperty()
  {
    return selectedBooking;
  }

  /**
   * Property change listener method.
   * This method is called when a property change event occurs in the BookingHistoryClient.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("getCurrentBookings"))
    {
      List<BookingHistory> bookingHistoryList = JsonParser.toList(
          evt.getNewValue(), BookingHistory[].class);
      bookings.clear();
      bookings.addAll(bookingHistoryList);
    }
    else if (evt.getPropertyName().equals("error"))
    {
      ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
      msgProp.set(errorResponse.errorMessage());
    }
  }
}
