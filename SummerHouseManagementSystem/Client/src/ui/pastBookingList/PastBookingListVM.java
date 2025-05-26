package ui.pastBookingList;

import dtos.BookingHistory;
import dtos.User;
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
 * ViewModel for displaying past bookings of a user.
 * It listens for property changes from the BookingHistoryClient
 * and updates the list of past bookings accordingly.
 */
public class PastBookingListVM implements PropertyChangeListener
{
  private ObservableList<BookingHistory> bookings;
  private User user;
  private BookingHistoryClient bookingHistoryClient;

  /**
   * Constructor initializes the BookingHistoryClient and the list of bookings.
   * It also retrieves the current user from the UserSession.
   */
  public PastBookingListVM()
  {
    this.bookings = FXCollections.observableArrayList();
    try
    {
      bookingHistoryClient = new BookingHistoryClientImpl(new ClientSocket());
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    this.user = UserSession.getInstance().getCurrentUser();
  }

  /**
   * Retrieves the past bookings for the current user.
   * It calls the BookingHistoryClient to fetch the data.
   *
   * @return an ObservableList of BookingHistory objects representing past bookings.
   */
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

  /**
   * Property change listener that updates the bookings list
   * when the "pastBookings" property changes.
   * It also handles error messages if the "error" property changes.
   * @param evt A PropertyChangeEvent object describing the event source
   *          and the property that has changed.
   */
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
