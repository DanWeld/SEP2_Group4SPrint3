package networking.requestHandlers;

import dtos.ErrorResponse;
import dtos.LoginRequest;
import dtos.Response;
import dtos.User;
import model.authentication.AuthenticationService;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;

public class AuthenticationRequestHandler
    implements RequestHandler, PropertyChangeListener
{
  private AuthenticationService authService;
  private PrintWriter out;
  private Logger logger;

  public AuthenticationRequestHandler(AuthenticationService authService,
      Logger logger)
  {
    this.authService = authService;
    authService.addPropertyChangeListener(this);
    this.logger = logger;
  }

  @Override public boolean canHandle(String handler, String action)
  {
    return handler.equals("auth") && (action.equals("login") || action.equals(
        "register"));
  }

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
