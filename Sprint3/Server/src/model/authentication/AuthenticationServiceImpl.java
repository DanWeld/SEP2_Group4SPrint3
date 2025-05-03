package model.authentication;

import dtos.LoginRequest;
import dtos.User;

public class AuthenticationServiceImpl implements AuthenticationService
{
  @Override public String loginUser(LoginRequest request)
  {

    //    for (User user : users)
    return "Email not found.";
  }

  @Override public String registerUser(User newUser)
  {
    String newEmail = newUser.getEmail();
    String newPassword = newUser.getPassword();
    String emailValidation = validateEmail(newEmail);
    String passwordValidation = validatePassword(newPassword);

    if (emailValidation.equals("OK") && passwordValidation.equals("OK"))
    {
      //TODO: send the request to DAO
    }
    else if (!emailValidation.equals("OK"))
    {
      return emailValidation;
    }
    else
    {
      return passwordValidation;
    }
    return "Ok";
  }

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
    if (email == null || email.isEmpty())
    {
      return "Email cannot be empty";
    }
    if (!email.contains("@") && !email.endsWith(".com"))
    {
      return "Email must be in a correct format.";
    }
    return "OK";
  }

  private String validatePassword(String newPassword)
  {
    if (newPassword == null || newPassword.isEmpty())
    {
      return "Password cannot be empty";
    }
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
