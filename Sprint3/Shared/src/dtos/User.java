package dtos;

public class User
{
  private String username;
  private String email;
  private String password;
  private boolean isAdmin;

  public User(String username, String email, String password)
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = false;
  }
  
  public User(String username, String email, String password, boolean isAdmin)
  {
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  public String getUsername()
  {
    return username;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPassword()
  {
    return password;
  }

  public boolean isAdmin()
  {
    return isAdmin;
  }

  public void setUsername(String username)
  {
    this.username = username;
  }

  public void setEmail(String email)
  {
    this.email = email;
  }

  public void setPassword(String password)
  {
    this.password = password;
  }

  public void setAdmin(boolean admin)
  {
    isAdmin = admin;
  }

  public String toString()
  {
    return "User{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
  }


}
