package networking.bookingHistoryHandler;

import dtos.Booking;
import dtos.BookingHistory;
import model.bookingHistory.BookingHistoryModel;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class BookingHistoryHandlerImpl
    implements BookingHistoryHandler, PropertyChangeListener
{
  private Socket socket;
  private PrintWriter out;
  private BookingHistoryModel bookingHistoryModel;

  public BookingHistoryHandlerImpl(Socket socket,
      BookingHistoryModel bookingHistoryModel)
  {
    this.socket = socket;
    try
    {
      out = new PrintWriter(socket.getOutputStream(), true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    this.bookingHistoryModel = bookingHistoryModel;
    bookingHistoryModel.addPropertyChangeListener(this);
  }

  public void getPastBookings(String username)
  {
    bookingHistoryModel.getPastBookings(username);
  }

  public void getCurrentBookings(String username)
  {
    bookingHistoryModel.getCurrentBookings(username);
  }

  public void getFutureBookings(String username)
  {
    bookingHistoryModel.getFutureBookings(username);
  }

  @Override public void cancelBooking(BookingHistory booking)
  {
    bookingHistoryModel.cancelBooking(booking);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String name = evt.getPropertyName();
    if (name.equals("bookingHistory"))
    {
      // Convert the list of properties to PropertyList DTO
      @SuppressWarnings("unchecked")
      ArrayList<BookingHistory> bookingHistory = (ArrayList<BookingHistory>) evt.getNewValue();

      // Convert the list of properties to JSON
      String bookingHistoryJson = JsonParser.toJson(bookingHistory);

      // Send the JSON string to the client
      out.println(bookingHistoryJson);
      out.flush();
    }
  }
}
