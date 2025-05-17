package networking.requestHandlers;


import dtos.ErrorResponse;
import dtos.LoginRequest;
import dtos.Response;
import dtos.User;
import model.authentication.AuthenticationService;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;

public class AuthenticationRequestHandler implements RequestHandler,
    PropertyChangeListener
{
  private AuthenticationService authService;
  private PrintWriter out;

  public AuthenticationRequestHandler(AuthenticationService authService)
  {
    this.authService = authService;
    authService.addPropertyChangeListener(this);
  }

  @Override public boolean canHandle(String handler, String action)
  {
    return handler.equals("auth") &&
        (action.equals("login") || action.equals("register"));
  }

  @Override public void handle(String action, String payload, PrintWriter out)
  {
    this.out = out;
    switch (action) {
      case "login" -> {
        LoginRequest request = (LoginRequest) JsonParser.jsonToObject(payload, LoginRequest.class);
        authService.authenticate(request.getEmail(), request.getPassword());
      }
      case "register" -> {
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
      case "loginSuccess" -> {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Login success: " + ((User) response.payload()).getUsername());
      }
      case "loginFailure" -> {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Login failed: " + ((ErrorResponse) response.payload()).errorMessage());
      }
      case "registerSuccess" -> {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Register success: " + ((User) response.payload()).getUsername());
      }
      case "registerFailure" -> {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Register failed: " + ((ErrorResponse) response.payload()).errorMessage());
      }
    }
  }
}
