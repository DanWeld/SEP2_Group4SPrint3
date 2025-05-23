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
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import services.UserSession;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class CurrentBookingListVM implements PropertyChangeListener
{
  private final ObservableList<BookingHistory> bookings;
  private final BookingHistoryClient bookingHistoryClient;
  private final StringProperty msgProp;
  private final ObjectProperty<BookingHistory> selectedBooking;


  public CurrentBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    this.msgProp = new SimpleStringProperty();
    this.selectedBooking = new SimpleObjectProperty<>();
    try
    {
      bookingHistoryClient = new BookingHistoryClientImpl(new Client());
      bookingHistoryClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }

  public ObservableList<BookingHistory> getBookingHistory()
  {
    bookingHistoryClient.getCurrentBookings(getUser());
    return bookings;
  }

  private String getUser()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    return currentUser.getUsername();
  }

  public StringProperty msgProperty()
  {
    return msgProp;
  }

  public ObjectProperty<BookingHistory> selectedBookingProperty()
  {
    return selectedBooking;
  }

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
