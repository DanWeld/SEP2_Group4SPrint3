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

public class UserRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private final UserModel userModel;
  private final Logger logger;
  private PrintWriter out;

  public UserRequestHandler(UserModel userModel, Logger logger)
  {
    this.userModel = userModel;
    userModel.addPropertyChangeListener(this);
    this.logger = logger;
  }

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
