package Server.networking;

import model.booking.BookingModel;
import networking.bookingHandler.BookingHandler;
import networking.bookingHandler.BookingHandlerImpl;
import networking.propertyListHandler.PropertyListHandler;
import utils.JsonParser;
import model.propertyList.PropertyListModel;
import networking.propertyListHandler.PropertyListHandlerImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Date;
import java.sql.SQLException;

public class MainSocketHandler implements Runnable
{
  private final Socket socket;
  private final PropertyListModel propertyListModel;
  private final BookingModel bookingModel;
  private final BufferedReader in;
  private final PrintWriter out;
  private PropertyListHandler propertyListHandler;
  private BookingHandler bookingHandler;

  public MainSocketHandler(Socket socket, PropertyListModel propertyListModel,
      BookingModel bookingModel) throws IOException, SQLException
  {
    // Initialize the socket
    this.socket = socket;

    // Initialize the input and output streams
    in = new BufferedReader(
        new java.io.InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream(), true);

    // Initialize the property list model and booking model
    this.propertyListModel = propertyListModel;
    this.bookingModel = bookingModel;

    // Initialize the handlers
    propertyListHandler = new PropertyListHandlerImpl(socket,
        propertyListModel);
    bookingHandler = new BookingHandlerImpl(socket, bookingModel);
  }

  @Override public void run()
  {
    try
    {
      String clientRequest;
      try
      {
        while ((clientRequest = in.readLine()) != null)
        {
          switch (clientRequest)
          {
            case "getAvailableProperties" ->
            {
              // Read the dates from the client
              String datesJson = in.readLine();
              // Parse the dates from JSON
              Date[] dates = JsonParser.jsonToDates(datesJson);

              propertyListHandler.setDates(dates);
              propertyListHandler.getAvailableProperties();
            }
            case "isAvailable" ->
            {
              int propertyID = Integer.parseInt(in.readLine());

              // Read the start and end dates from the client
              String datesJson = in.readLine();

              // Parse the dates from JSON
              Date[] dates = JsonParser.jsonToDates(datesJson);

              // Check if the property is available
              bookingHandler.isAvailable(dates[0], dates[1], propertyID);
            }
            case "getPropertyByID" ->
            {
              //read the property ID from the client
              String propertyID = in.readLine();

              //TODO get the property by ID
            }
            case "createBooking" ->
            {
              int propertyId = Integer.parseInt(in.readLine());
              String username = in.readLine();
              String startDateJson = in.readLine();

              //Convert the dates from Json
              Date[] dates = JsonParser.jsonToDates(startDateJson);
              Date startDate = dates[0];
              Date endDate = dates[1];

              // Create the booking
              bookingHandler.createBooking(propertyId, startDate, endDate,
                  username);
            }
          }
        }
      }
      catch (SQLException e)
      {
        throw new RuntimeException(e);
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
    finally
    {
      // clean up socket, streams, etc.
      try
      {
        socket.close();
        in.close();
        out.close();
      }
      catch (IOException e)
      {
        throw new RuntimeException(e);
      }
    }
  }
}