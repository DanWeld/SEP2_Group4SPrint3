package services.user;

import dtos.ErrorResponse;
import dtos.Response;
import dtos.User;
import persistence.daos.user.UserDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl
    implements UserService, UserAdminPrivileges, UserCustomerPrivileges
{
  private UserDAO userDAO;
  private PropertyChangeSupport support;

  public UserServiceImpl(UserDAO userDAO)
  {
    this.userDAO = userDAO;
    this.support = new PropertyChangeSupport(this);
  }

  @Override public void promoteToAdmin(String username)
  {
    try
    {
      userDAO.promoteToAdmin(username);
      support.firePropertyChange("promoteSuccess", null,
          new Response("SUCCESS", username));
    }
    catch (Exception e)
    {
      support.firePropertyChange("promoteFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

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

  @Override public void deleteUser(String username)
  {
    try
    {
      userDAO.delete(username);
      support.firePropertyChange("deleteSuccess", null,
          new Response("SUCCESS", username));
    }
    catch (SQLException e)
    {
      support.firePropertyChange("deleteFailure", null,
          new Response("ERROR", new ErrorResponse(e.getMessage())));
    }
  }

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

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
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
