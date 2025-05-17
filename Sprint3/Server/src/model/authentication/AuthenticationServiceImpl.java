package model.authentication;

import dtos.ErrorResponse;
import dtos.Response;
import dtos.User;
import observer.PropertyChangeSubject;
import persistence.daos.user.UserDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the AuthenticationService interface
 */
public class AuthenticationServiceImpl implements AuthenticationService
{
  private final UserDAO userDAO;
  private PropertyChangeSupport support;

  public AuthenticationServiceImpl(UserDAO userDAO)
  {
    this.userDAO = userDAO;
    this.support = new PropertyChangeSupport(this);
  }

  @Override public void authenticate(String email, String password)
  {
    try
    {
      User user = userDAO.read(email, password);
      support.firePropertyChange("loginSuccess", null,
          new Response("SUCCESS", user));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("loginFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  @Override public void registerUser(User user)
  {
    // Validate the email
    String emailValidationResult = validateEmail(user.getEmail());
    System.out.println("emailValidationResult: " + emailValidationResult);
    if (!emailValidationResult.equals("OK"))
    {
      System.out.println("emailValidationResult: " + emailValidationResult);
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(emailValidationResult)));
      return;
    }

    // Validate the password
    String passwordValidationResult = validatePassword(user.getPassword());
    System.out.println("passwordValidationResult: " + passwordValidationResult);
    if (!passwordValidationResult.equals("OK"))
    {
      System.out.println("passwordValidationResult: " + passwordValidationResult);
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(passwordValidationResult)));
      return;
    }

    // take it to the DAO
    try
    {
      System.out.println(
          "AuthenticationServiceImpl: User creation: start" + user);
      User newUser = userDAO.create(user.getUsername(), user.getEmail(),
          user.getPassword());
      System.out.println(
          "AuthenticationServiceImpl: User created: firing event" + newUser);
      support.firePropertyChange("registerSuccess", null,
          new Response("SUCCESS", newUser));
    }
    catch (SQLException e)
    {
      System.out.println(
          "AuthenticationServiceImpl: User creation failed: firing event"
              + e.getMessage());
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

  // Helper methods for validation
  private boolean containsUpperCaseAndLowerCase(String password)
  {
    boolean hasUpperCase = false;
    boolean hasLowerCase = false;

    for (char c : password.toCharArray())
    {
      if (Character.isUpperCase(c))
      {
        hasUpperCase = true;
      }
      if (Character.isLowerCase(c))
      {
        hasLowerCase = true;
      }
      if (hasUpperCase && hasLowerCase)
      {
        return true;
      }
    }
    return false;
  }

  private boolean containsNumberLetterAndSymbol(String pw)
  {
    boolean hasNumber = false;
    boolean hasSymbol = false;
    boolean hasLetter = false;

    for (char c : pw.toCharArray())
    {
      if (Character.isDigit(c))
      {
        hasNumber = true;
      }
      if (Character.isLetter(c))
      {
        hasLetter = true;
      }
      if (!Character.isLetterOrDigit(c))
      {
        hasSymbol = true;
      }
      if (hasNumber && hasLetter && hasSymbol)
      {
        return true;
      }
    }
    return false;
  }

  private String validateEmail(String email)
  {
    if (!email.contains("@"))
    {
      return "Email must be in a correct format.";
    }
    return "OK";
  }

  private String validatePassword(String newPassword)
  {
    if (newPassword.length() < 8)
    {
      return "Passwords must be 8 or greater character length.";
    }
    if (!containsUpperCaseAndLowerCase(newPassword))
    {
      return "Passwords must have atleast one upper and atleast one lower character.";
    }
    if (!containsNumberLetterAndSymbol(newPassword))
    {
      return "password must have at least one number, one letter and one symbol";
    }
    return "OK";
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
