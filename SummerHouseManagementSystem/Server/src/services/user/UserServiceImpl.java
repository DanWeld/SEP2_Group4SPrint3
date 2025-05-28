package services.user;

import dtos.ErrorResponse;
import dtos.Response;
import dtos.User;
import persistence.daos.user.UserDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.List;

/**
 * UserServiceImpl is the implementation of the UserService interface,
 * providing methods for user management, including promoting users to admin,
 * updating user details, deleting users, and retrieving all users.
 * It also implements UserAdminPrivileges and UserCustomerPrivileges interfaces
 * to handle admin and customer-specific operations.
 */
public class UserServiceImpl
    implements UserService, UserAdminPrivileges, UserCustomerPrivileges
{
  private UserDAO userDAO;
  private PropertyChangeSupport support;

  /**
   * Constructor for UserServiceImpl.
   *
   * @param userDAO the UserDAO instance used for database operations
   */
  public UserServiceImpl(UserDAO userDAO)
  {
    this.userDAO = userDAO;
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * Promotes a user to admin status.
   *
   * @param username the username of the user to be promoted
   */
  @Override public void promoteToAdmin(String username)
  {
    try
    {
      userDAO.promoteToAdmin(username.replace("\"", ""));
      support.firePropertyChange("promoteSuccess", null,
          new Response("SUCCESS", username));
    }
    catch (Exception e)
    {
      support.firePropertyChange("promoteFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

  /**
   * Updates the details of a user.
   *
   * @param user the User object containing updated user details
   */
  @Override public void updateUser(User user)
  {
    String passwordValidationResult = validatePassword(user.getPassword());
    if (!passwordValidationResult.equals("OK"))
    {
      support.firePropertyChange("updateFailure", null,
          new Response("ERROR", new ErrorResponse(passwordValidationResult)));
    }
    else
    {
      try
      {
        userDAO.update(user);
        support.firePropertyChange("updateSuccess", null,
            new Response("SUCCESS", user));
      }
      catch (Exception e)
      {
        support.firePropertyChange("updateFailure", null,
            new Response("ERROR", new ErrorResponse(e.getMessage())));
      }
    }
  }

  /**
   * Deletes a user by username.
   *
   * @param username the username of the user to be deleted
   */
  @Override public void deleteUser(String username)
  {
    try
    {
      userDAO.delete(username.replace("\"", ""));
      support.firePropertyChange("deleteSuccess", null,
          new Response("SUCCESS", username));
    }
    catch (SQLException e)
    {
      support.firePropertyChange("deleteFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

  /**
   * Retrieves all users from the database.
   */
  @Override public void getAllUsers()
  {
    try
    {
      List<User> users = userDAO.getAllUsers();
      support.firePropertyChange("getAllUsersSuccess", null,
          new Response("SUCCESS", users));
    }
    catch (SQLException e)
    {
      support.firePropertyChange("getAllUsersFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

  /**
   * Adds a property change listener to the service.
   * @param listener the PropertyChangeListener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Checks if the password contains at least one upper case and one lower case character.
   * @param password the password to check
   * @return true if the password contains both upper and lower case characters, false otherwise
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
   * Checks if the password contains at least one number, one letter, and one symbol.
   * @param pw the password to check
   * @return true if the password contains at least one number, one letter, and one symbol, false otherwise
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
   * Validates the new password based on specific criteria.
   * @param newPassword the new password to validate
   * @return "OK" if the password is valid, otherwise an error message
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
}
