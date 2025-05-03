package networking.bookingHandler;

import dtos.Booking;
import model.booking.BookingModel;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;

public class BookingHandlerImpl
    implements BookingHandler, PropertyChangeListener
{
  private Socket socket;
  private PrintWriter out;
  private final BookingModel bookingModel;

  public BookingHandlerImpl(Socket socket, BookingModel bookingModel)
  {
    // Initialize the socket and reader/writer
    this.socket = socket;
    try
    {
      out = new PrintWriter(socket.getOutputStream(), true);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    this.bookingModel = bookingModel;
    bookingModel.addPropertyChangeListener(this);
  }

  @Override public void createBooking(int propertyID, Date startDate,
      Date endDate, String username) throws SQLException
  {
    bookingModel.createBooking(propertyID, startDate, endDate, username);
  }

  @Override
  public void isAvailable(Date startDate, Date endDate, int propertyId)
  {
    bookingModel.isAvailable(startDate,endDate,propertyId);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    // Handle the property change event
    String name = evt.getPropertyName();

    if (name.equals("bookingCreated"))
    {
      // Get the booking details
      Booking booking = (Booking) evt.getNewValue();

      // Convert the booking details to JSON
      String jsonResponse = JsonParser.bookingToJson(booking);

      // Send the JSON response to the client
      out.println(jsonResponse);
      out.flush();
    }
    else if (name.equals("isAvailable"))
    {
      // Get the availability status
      boolean isAvailable = (boolean) evt.getNewValue();

      // Send the availability status to the client
      out.println(isAvailable);
      out.flush();
    }
    else
    {
      System.out.println("Unknown property change event: " + name);
    }
  }
}
