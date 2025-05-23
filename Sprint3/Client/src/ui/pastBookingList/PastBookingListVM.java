package ui.pastBookingList;

import dtos.BookingHistory;
import dtos.User;
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

public class PastBookingListVM implements PropertyChangeListener
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
    bookingHistoryClient.getPastBookings(getUser());
    return bookings;
  }

  private String getUser()
  {
    User currentUser = UserSession.getInstance().getCurrentUser();
    if (currentUser == null)
    {
      throw new IllegalStateException("User not logged in");
    }
    return currentUser.getUsername();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("pastBookings"))
    {
      List<BookingHistory> bookingHistoryList = JsonParser.toList(
          evt.getNewValue(), BookingHistory[].class);
      bookings.clear();
      bookings.addAll(bookingHistoryList);
    }
    else if (evt.getPropertyName().equals("error"))
    {
      System.out.println("Error: " + evt.getNewValue());
    }
  }
}
