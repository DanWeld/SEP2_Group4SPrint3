package ui.pastBookingList;

import dtos.BookingHistory;
import dtos.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;

import java.io.IOException;
import java.util.List;

public class PastBookingListVM
{
  private ObservableList<BookingHistory> bookings;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  public PastBookingListVM()
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
    this.user = new User("YoussefTopaji", "email", "password");
  }

  public ObservableList<BookingHistory> getBookingHistory()
  {
    try
    {
      List<BookingHistory> bookingHistoryList = bookingHistoryClient.getPastBookings(user.getUsername());
      bookings = FXCollections.observableArrayList(bookingHistoryList);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    return bookings;
  }
}
