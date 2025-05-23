package ui.futureBookingList;

import com.google.gson.reflect.TypeToken;
import dtos.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import services.UserSession;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class FutureBookingListVM implements PropertyChangeListener
{

  private ObservableList<BookingHistory> bookings;
  private ObjectProperty<BookingHistory> selectedBooking;
  private StringProperty errMsg;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  public FutureBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    this.selectedBooking = new SimpleObjectProperty<>();
    this.errMsg = new SimpleStringProperty();
    try
    {
      Client client = new Client();
      bookingHistoryClient = new BookingHistoryClientImpl(client);
      bookingHistoryClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public ObservableList<BookingHistory> getFutureBookings()
  {
    bookingHistoryClient.getFutureBookings(getUsername());
    return bookings;
  }

  private String getUsername()
  {
    if (UserSession.getInstance().isLoggedIn())
    {
      user = UserSession.getInstance().getCurrentUser();
      return user.getUsername();
    }
    return null;
  }

  public ObjectProperty<BookingHistory> selectedBookingProperty()
  {
    return selectedBooking;
  }

  public StringProperty errMsgProperty()
  {
    return errMsg;
  }

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