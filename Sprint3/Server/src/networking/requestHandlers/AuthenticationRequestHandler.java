package networking.requestHandlers;

import dtos.ErrorResponse;
import dtos.LoginRequest;
import dtos.Response;
import dtos.User;
import services.authentication.AuthenticationService;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;

/**
 * Handles authentication requests such as login and registration.
 * It listens for property changes from the AuthenticationService to respond
 * to the client with appropriate messages.
 */
public class AuthenticationRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private AuthenticationService authService;
  private PrintWriter out;
  private Logger logger;

  /**
   * Constructs an AuthenticationRequestHandler with the given authentication service
   * and logger.
   *
   * @param authService the authentication service to handle login and registration
   * @param logger      the logger to log events
   */
  public AuthenticationRequestHandler(AuthenticationService authService,
      Logger logger)
  {
    this.authService = authService;
    authService.addPropertyChangeListener(this);
    this.logger = logger;
  }

  /**
   * Checks if this handler can handle the given action for the specified handler.
   *
   * @param handler the name of the handler
   * @param action  the action to be handled
   * @return true if this handler can handle the action, false otherwise
   */
  @Override public boolean canHandle(String handler, String action)
  {
    return handler.equals("auth") && (action.equals("login") || action.equals(
        "register"));
  }

  /**
   * Handles the authentication request based on the action and payload.
   * It processes login and registration requests and sends responses back to the client.
   *
   * @param action  the action to be handled (login or register)
   * @param payload the payload containing request data
   * @param out     the PrintWriter to send responses back to the client
   */
  @Override public void handle(String action, String payload, PrintWriter out)
  {
    this.out = out;
    switch (action)
    {
      case "login" ->
      {
        LoginRequest request = (LoginRequest) JsonParser.jsonToObject(payload,
            LoginRequest.class);
            authService.authenticate(request.getEmail(), request.getPassword());
      }
      case "register" ->
      {
        User newUser = (User) JsonParser.jsonToObject(payload, User.class);
        authService.registerUser(newUser);
      }
    }
  }

  /**
   * Handles property change events from the AuthenticationService.
   * It processes login and registration success or failure events and sends
   * appropriate responses back to the client.
   *
   * @param evt the property change event containing the response data
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String eventName = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();
    switch (eventName)
    {
      case "loginSuccess" ->
      {
        logger.log(
            "Login success: " + ((User) response.payload()).getUsername(),
            LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "loginFailure" ->
      {
        logger.log("Login failed: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "registerSuccess" ->
      {
        logger.log(
            "Register success: " + ((User) response.payload()).getUsername(),
            LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "registerFailure" ->
      {
        logger.log("Register failed: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
    }
  }
}
