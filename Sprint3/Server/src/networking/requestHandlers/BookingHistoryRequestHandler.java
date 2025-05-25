package networking.requestHandlers;

import dtos.Response;
import model.bookingHistory.BookingHistoryModel;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;

/**
 * Handles requests related to booking history, including current, past, future,
 * and all bookings.
 */
public class BookingHistoryRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private final BookingHistoryModel bookingHistoryModel;
  private final Logger logger;
  private PrintWriter out;

  /**
   * Constructs a BookingHistoryRequestHandler with the specified model and logger.
   *
   * @param bookingHistoryModel the model to handle booking history requests
   * @param logger              the logger for logging events
   */
  public BookingHistoryRequestHandler(BookingHistoryModel bookingHistoryModel,
      Logger logger)
  {
    this.bookingHistoryModel = bookingHistoryModel;
    this.bookingHistoryModel.addPropertyChangeListener(this);
    this.logger = logger;
  }

  /**
   * Checks if this handler can handle the specified action for the booking history.
   *
   * @param handler the handler name
   * @param action  the action to check
   * @return true if this handler can handle the action, false otherwise
   */
  @Override public boolean canHandle(String handler, String action)
  {
    if (handler.equalsIgnoreCase("bookingHistory") && (
        action.equalsIgnoreCase("getCurrentBookings")
            || action.equalsIgnoreCase("getPastBookings")
            || action.equalsIgnoreCase("getFutureBookings")
            || action.equalsIgnoreCase("getAllBookings")))
    {
      return true;
    }
    return false;
  }

  /**
   * Handles the specified action with the given payload.
   *
   * @param action  the action to handle
   * @param payload the payload for the action
   * @param out     the PrintWriter to send responses to the client
   */
  @Override public void handle(String action, String payload, PrintWriter out)
  {
    this.out = out;
    switch (action)
    {
      case "getCurrentBookings":
        bookingHistoryModel.getCurrentBookings(payload);
        break;
      case "getPastBookings":
        bookingHistoryModel.getPastBookings(payload);
        break;
      case "getFutureBookings":
        bookingHistoryModel.getFutureBookings(payload);
        break;
      case "getAllBookings":
        bookingHistoryModel.getAllBookings(Integer.valueOf(payload));
        break;
      default:
        out.println("Invalid action");
    }
  }

  /**
   * Handles property change events from the BookingHistoryModel.
   *
   * @param evt the property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String name = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();

    switch (name)
    {
      case "pastBookingsSuccess":
        logger.log("Past bookings retrieved successfully", LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "pastBookingsFailure":
        logger.log("Failed to retrieve past bookings", LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "currentBookingsSuccess":
        logger.log("Current bookings retrieved successfully", LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "currentBookingsFailure":
        logger.log("Failed to retrieve current bookings", LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "futureBookingsSuccess":
        logger.log("Future bookings retrieved successfully", LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "futureBookingsFailure":
        logger.log("Failed to retrieve future bookings", LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "allBookingsSuccess":
        logger.log("All bookings retrieved successfully", LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "allBookingsFailure":
        logger.log("Failed to retrieve all bookings", LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      default:
        logger.log("Unknown event: " + name, LogLevel.ERROR);
        out.println(JsonParser.toJson(new Response("ERROR", "Unknown event")));
        out.flush();
        break;
    }
  }
}
