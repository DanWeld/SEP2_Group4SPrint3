package ui.futureBookingList;

import dtos.Booking;
import dtos.BookingHistory;
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

import java.io.IOException;
import java.util.List;

public class FutureBookingListVM
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
      bookingHistoryClient = new BookingHistoryClientImpl(new Client());
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    this.user = UserSession.getInstance().getCurrentUser();
  }

  public ObservableList<BookingHistory> getFutureBookings()
  {
    try
    {
      List<BookingHistory> bookingHistoryList = bookingHistoryClient.getFutureBookings(user.getUsername());
      bookings = FXCollections.observableArrayList(bookingHistoryList);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    return bookings;
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
      if (selectedBooking.get().getStartDate().getTime() - System.currentTimeMillis() < 6 * 24 * 60 * 60 * 1000)
      {
        errMsg.set("You cannot cancel a booking that starts in less than 7 days");
        return;
      }
      try
      {
        bookingHistoryClient.cancelBooking(booking);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
      bookings.remove(booking);
    }
    else
    {
      errMsg.set("Please select a booking to cancel");
    }
  }
}
