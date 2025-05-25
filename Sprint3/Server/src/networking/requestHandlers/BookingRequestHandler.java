package networking.requestHandlers;

import dtos.Booking;
import dtos.ErrorResponse;
import dtos.Response;
import model.booking.BookingModel;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles booking-related requests such as creating, deleting, extending bookings,
 * and checking availability.
 */
public class BookingRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private final BookingModel bookingModel;
  private final Logger logger;
  private PrintWriter out;

  /**
   * Constructs a BookingRequestHandler with the specified BookingModel and Logger.
   *
   * @param bookingModel the model to handle booking operations
   * @param logger       the logger for logging events
   */
  public BookingRequestHandler(BookingModel bookingModel, Logger logger)
  {
    this.bookingModel = bookingModel;
    bookingModel.addPropertyChangeListener(this);
    this.logger = logger;
  }

  /**
   * Checks if this handler can handle the specified request.
   *
   * @param handler the name of the handler
   * @param action  the action to be performed
   * @return true if this handler can handle the request, false otherwise
   */
  @Override public boolean canHandle(String handler, String action)
  {
    if (handler.equalsIgnoreCase("Booking") && (
        action.equalsIgnoreCase("create") || action.equalsIgnoreCase("delete")
            || action.equalsIgnoreCase("extend") || action.equalsIgnoreCase(
            "isAvailable")))
    {
      return true;
    }
    return false;
  }

  /**
   * Handles the specified action with the provided payload.
   *
   * @param action  the action to be performed
   * @param payload the data associated with the action
   * @param out     the PrintWriter to send responses back to the client
   */
  @Override public void handle(String action, String payload, PrintWriter out)
  {
    this.out = out;
    switch (action)
    {
      case "create":
        Booking booking = (Booking) JsonParser.jsonToObject(payload,
            Booking.class);
        bookingModel.createBooking(booking.getPropertyId(),
            booking.getStartDate(), booking.getEndDate(),
            booking.getUsername());
        break;
      case "delete":
        Booking deleteBooking = (Booking) JsonParser.jsonToObject(payload,
            Booking.class);
        bookingModel.deleteBooking(deleteBooking.getStartDate(),
            deleteBooking.getPropertyId(), deleteBooking.getUsername());
        break;
      case "extend":
        Booking extendBooking = (Booking) JsonParser.jsonToObject(payload,
            Booking.class);
        bookingModel.extendBooking(extendBooking.getPropertyId(),
            extendBooking.getStartDate(), extendBooking.getEndDate(),
            extendBooking.getUsername());
        break;
      case "isAvailable":
        Booking checkBooking = (Booking) JsonParser.jsonToObject(payload,
            Booking.class);
        bookingModel.isAvailable(checkBooking.getStartDate(),
            checkBooking.getEndDate(), checkBooking.getPropertyId());
        break;
      default:
        out.println("Invalid action");
    }
  }

  /**
   * Handles property change events from the BookingModel.
   *
   * @param evt the property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    // Handle the property change event
    String eventName = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();
    switch (eventName)
    {
      case "bookingCreationSuccess":
      {
        logger.log("Booking created successfully by: "
            + ((Booking) response.payload()).getUsername(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "bookingCreationFailure":
      {
        logger.log("Booking creation failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "bookingExtensionSuccess":
      {
        logger.log("Booking extended successfully by: "
            + ((Booking) response.payload()).getUsername(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "bookingExtensionFailure":
      {
        logger.log("Booking extension failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "isAvailableSuccess":
      {
        logger.log("Availability check successful: "
            + ((Boolean) response.payload()).toString(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "isAvailableFailure":
      {
        logger.log("Availability check failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "bookingDeletionSuccess":
      {
        logger.log("Booking deleted successfully" , LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      case "bookingDeletionFailure":
      {
        logger.log("Booking deletion failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      }
      default:
        logger.log("Unknown event: " + eventName, LogLevel.WARNING);
        out.println(JsonParser.toJson(new ErrorResponse("Unknown event")));
        out.flush();
        break;
    }
  }
}
