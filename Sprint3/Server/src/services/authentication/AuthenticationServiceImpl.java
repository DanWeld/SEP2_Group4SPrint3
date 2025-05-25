package services.authentication;

import dtos.ErrorResponse;
import dtos.Response;
import dtos.User;
import persistence.daos.user.UserDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;

/**
 * Implementation of the AuthenticationService interface
 */
public class AuthenticationServiceImpl implements AuthenticationService
{
  private final UserDAO userDAO;
  private PropertyChangeSupport support;

  /**
   * Constructor for AuthenticationServiceImpl
   *
   * @param userDAO UserDAO instance for database operations
   */
  public AuthenticationServiceImpl(UserDAO userDAO)
  {
    this.userDAO = userDAO;
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * Constructor for AuthenticationServiceImpl with PropertyChangeSupport
   *
   * @param email User's email
   * @param password User's password
   */
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

  /**
   * Registers a new user after validating email and password
   *
   * @param user User to register
   */
  @Override public void registerUser(User user)
  {
    // Validate the email
    String emailValidationResult = validateEmail(user.getEmail());
    if (!emailValidationResult.equals("OK"))
    {
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(emailValidationResult)));
      return;
    }

    // Validate the password
    String passwordValidationResult = validatePassword(user.getPassword());
    if (!passwordValidationResult.equals("OK"))
    {
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(passwordValidationResult)));
      return;
    }

    // take it to the DAO
    try
    {
      User newUser = userDAO.create(user.getUsername(), user.getEmail(),
          user.getPassword());
      support.firePropertyChange("registerSuccess", null,
          new Response("SUCCESS", newUser));
    }
    catch (SQLException e)
    {
      support.firePropertyChange("registerFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

  /**
   * Validates if the password contains at least one upper case and one lower case character
   * @param password Password to validate
   * @return true if valid, false otherwise
   */
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

  /**
   * Validates if the password contains at least one number, one letter and one symbol
   * @param pw Password to validate
   * @return true if valid, false otherwise
   */
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

  /**
   * Validates the email format
   * @param email Email to validate
   * @return "OK" if valid, error message otherwise
   */
  private String validateEmail(String email)
  {
    if (!email.contains("@"))
    {
      return "Email must be in a correct format.";
    }
    return "OK";
  }

  /**
   * Validates the password format
   * @param newPassword Password to validate
   * @return "OK" if valid, error message otherwise
   */
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

  /**
   * Adds a PropertyChangeListener to the service
   * @param listener PropertyChangeListener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
