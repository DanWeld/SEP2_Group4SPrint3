package ui.futureBookingList;

import dtos.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
 * ViewModel for the Future Booking List view
 * Handles the logic for displaying future bookings and cancelling them
 *
 * @author Group 4
 * @version 1.0
 */
public class FutureBookingListVM implements PropertyChangeListener
{

  private ObservableList<BookingHistory> bookings;
  private ObjectProperty<BookingHistory> selectedBooking;
  private StringProperty errMsg;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  /**
   * Constructor for FutureBookingListVM
   * Initializes the observable list of bookings and the selected booking property
   * Sets up the booking history client to listen for property changes
   */
  public FutureBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    this.selectedBooking = new SimpleObjectProperty<>();
    this.errMsg = new SimpleStringProperty();
    try
    {
      ClientSocket client = new ClientSocket();
      bookingHistoryClient = new BookingHistoryClientImpl(client);
      bookingHistoryClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves future bookings for the logged-in user
   * Calls the booking history client to fetch bookings and returns the observable list
   *
   * @return ObservableList of BookingHistory objects representing future bookings
   */
  public ObservableList<BookingHistory> getFutureBookings()
  {
    bookingHistoryClient.getFutureBookings(getUsername());
    return bookings;
  }

  /**
   * Gets the username of the currently logged-in user
   *
   * @return String representing the username, or null if not logged in
   */
  private String getUsername()
  {
    if (UserSession.getInstance().isLoggedIn())
    {
      user = UserSession.getInstance().getCurrentUser();
      return user.getUsername();
    }
    return null;
  }

  /**
   * Returns the property for the selected booking
   *
   * @return ObjectProperty of BookingHistory representing the selected booking
   */
  public ObjectProperty<BookingHistory> selectedBookingProperty()
  {
    return selectedBooking;
  }

  public StringProperty errMsgProperty()
  {
    return errMsg;
  }

  /**
   * Cancels the selected booking
   * Checks if a booking is selected and if it can be cancelled (not starting in less than 7 days)
   * If valid, shows a confirmation dialog and cancels the booking
   */
  public void cancelBooking()
  {
    BookingHistory booking = selectedBooking.get();
    errMsg.set("");
    if (booking != null)
    {
      // if the booking starts in less than 7 days, show an error message
      if (selectedBooking.get().getStartDate().getTime()
          - System.currentTimeMillis() < 6 * 24 * 60 * 60 * 1000)
      {
        errMsg.set(
            "You cannot cancel a booking that starts in less than 7 days");
        return;
      }
      new Alert(Alert.AlertType.CONFIRMATION,
          "Are you sure you want to cancel this booking?").showAndWait()
          .ifPresent(response -> {
            if (response == ButtonType.OK)
            {
              bookingHistoryClient.cancelBooking(booking);
              bookings.remove(booking);
            }
          });
    }
    else
    {
      errMsg.set("Please select a booking to cancel");
    }
  }

  /**
   * Property change listener for the booking history client
   * Handles events such as fetching future bookings,
   * cancelling bookings, and error responses
   *
   * @param evt A PropertyChangeEvent object describing the event source
   *            and the property that has changed.
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String eventName = evt.getPropertyName();
    switch (eventName)
    {
      case "getFutureBookings":
      {
        List<BookingHistory> bookingHistoryList = JsonParser.toList(
            evt.getNewValue(), BookingHistory[].class);
        bookings.clear();
        bookings.addAll(bookingHistoryList);
        break;
      }
      case "delete":
      {
        BookingHistory booking = (JsonParser.convertPayload(evt.getNewValue(),
            BookingHistory.class));
        errMsg.set("Booking cancelled");
        break;
      }
      case "error":
      {
        ErrorResponse errorResponse = (JsonParser.convertPayload(
            evt.getNewValue(), ErrorResponse.class));
        errMsg.set(errorResponse.errorMessage());
        break;
      }
    }
  }
}