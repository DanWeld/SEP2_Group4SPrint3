package ui.pastBookingList;

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
    this.user = UserSession.getInstance().getCurrentUser();
  }

  public ObservableList<BookingHistory> getBookingHistory()
  {
    new Thread(() -> {
      try
      {
        List<BookingHistory> list = bookingHistoryClient.getPastBookings(getUser());
        javafx.application.Platform.runLater(() -> {
          bookings.setAll(list);
        });
      }
      catch (IOException e)
      {
        javafx.application.Platform.runLater(() -> {
          System.out.println("Failed: " + e.getMessage());
        });
      }
    }).start();
    return bookings;
  }

  private String getUser() {
    User currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser == null) {
      throw new IllegalStateException("User not logged in");
    }
    return currentUser.getUsername();
  }
}
