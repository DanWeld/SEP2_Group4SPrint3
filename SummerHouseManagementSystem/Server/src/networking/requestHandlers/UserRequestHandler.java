package networking.requestHandlers;

import dtos.ErrorResponse;
import dtos.Response;
import dtos.User;
import model.user.UserModel;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;

/**
 * Handles user-related requests such as promoting a user to admin, updating user details,
 * deleting a user, and retrieving all users.
 *
 * @author Group 4
 * @version 1.0
 */
public class UserRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private final UserModel userModel;
  private final Logger logger;
  private PrintWriter out;

  /**
   * Constructs a UserRequestHandler with the specified UserModel and Logger.
   *
   * @param userModel the UserModel to handle user operations
   * @param logger    the Logger to log messages
   */
  public UserRequestHandler(UserModel userModel, Logger logger)
  {
    this.userModel = userModel;
    userModel.addPropertyChangeListener(this);
    this.logger = logger;
  }

  /**
   * Checks if this handler can handle the specified action for the user handler.
   *
   * @param handler the name of the handler
   * @param action  the action to be performed
   * @return true if this handler can handle the action, false otherwise
   */
  @Override public boolean canHandle(String handler, String action)
  {
    if (handler.equalsIgnoreCase("user") && (action.equalsIgnoreCase("promote")
        || action.equalsIgnoreCase("update") || action.equalsIgnoreCase(
        "delete") || action.equalsIgnoreCase("getAllUsers")))
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
      case "promote":
        userModel.promoteToAdmin(payload);
        break;
      case "update":
        User user = (User) JsonParser.jsonToObject(payload, User.class);
        userModel.updateUser(user);
        break;
      case "delete":
        userModel.deleteUser(payload);
        break;
      case "getAllUsers":
        userModel.getAllUsers();
        break;
      default:
        out.println("Invalid action");
        out.flush();
    }
  }

  /**
   * Handles property change events from the UserModel.
   *
   * @param evt the PropertyChangeEvent containing the details of the change
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String propertyName = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();
    switch (propertyName)
    {
      case "promoteSuccess":
        logger.log("User promoted successfully: " + response.payload(),
            LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "promoteFailure":
        logger.log("User promotion failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "updateSuccess":
        logger.log("User updated successfully: "
            + ((User) response.payload()).getUsername(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "updateFailure":
        logger.log("User update failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "deleteSuccess":
        logger.log("User deleted successfully: " + response.payload(),
            LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "deleteFailure":
        logger.log("User deletion failed: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "getAllUsersSuccess":
        logger.log("All users retrieved successfully: "+ JsonParser.toJson(response), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
      case "getAllUsersFailure":
        logger.log("Failed to retrieve all users: "
                + ((ErrorResponse) response.payload()).errorMessage(),
            LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
        break;
    }
  }
}
