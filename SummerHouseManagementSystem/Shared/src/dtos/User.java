package dtos;

/**
 * Represents a user with credentials and admin status.
 *
 * @author Group 4
 * @version 1.0
 */
public class User
{
  private String username;
  private String email;
  private String password;
  private boolean isAdmin;

  /**
   * Constructs a User with the specified username, email, and password.
   * The user is not an admin by default.
   *
   * @param username the user's username
   * @param email the user's email address
   * @param password the user's password
   */
  public User(String username, String email, String password)
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = false;
  }

  /**
   * Constructs a User with the specified username, email, password, and admin status.
   *
   * @param username the user's username
   * @param email the user's email address
   * @param password the user's password
   * @param isAdmin true if the user is an admin, false otherwise
   */
  public User(String username, String email, String password, boolean isAdmin)
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  /**
   * Returns the username of the user.
   *
   * @return the username
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Returns the email address of the user.
   *
   * @return the email address
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * Returns the password of the user.
   *
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * Checks if the user is an admin.
   *
   * @return true if the user is an admin, false otherwise
   */
  public boolean isAdmin()
  {
    return isAdmin;
  }

  /**
   * Sets the username of the user.
   *
   * @param username the new username
   */
  public void setUsername(String username)
  {
    this.username = username;
  }

  /**
   * Sets the email address of the user.
   *
   * @param email the new email address
   */
  public void setEmail(String email)
  {
    this.email = email;
  }

  /**
   * Sets the password of the user.
   *
   * @param password the new password
   */
  public void setPassword(String password)
  {
    this.password = password;
  }

  /**
   * Sets the admin status of the user.
   *
   * @param admin true to set as admin, false otherwise
   */
  public void setAdmin(boolean admin)
  {
    isAdmin = admin;
  }

  /**
   * Returns a string representation of the user.
   *
   * @return a string describing the user
   */
  public String toString()
  {
    return "User{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
  }

  public boolean validatePassword(String password)
  {
    // Simple validation: check if the password matches the user's password
    return this.password.equals(password);
  }

  public void logout()
  {
    System.out.println("User " + username + " has logged out.");
  }
}