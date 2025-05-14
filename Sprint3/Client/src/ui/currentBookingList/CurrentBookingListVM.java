package ui.currentBookingList;

import dtos.BookingHistory;
import dtos.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import services.UserSession;

import java.io.IOException;
import java.util.List;

public class CurrentBookingListVM
{
  private ObservableList<BookingHistory> bookings;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  public CurrentBookingListVM()
  {

    this.bookings = FXCollections.observableArrayList();
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

  public ObservableList<BookingHistory> getBookingHistory()
  {
    try
    {
      List<BookingHistory> bookingHistoryList = bookingHistoryClient.getCurrentBookings(user.getUsername());
      bookings = FXCollections.observableArrayList(bookingHistoryList);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    return bookings;
  }
}
