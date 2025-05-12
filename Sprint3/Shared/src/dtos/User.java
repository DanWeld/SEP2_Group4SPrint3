package dtos;

public class User
{
  private String name;
  private String username;
  private String email;
  private String password;
  private boolean isAdmin;

  public User(String username, String email, String password)
  {
    this.name = username; // Default name to username
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = false;
  }
  
  public User(String username, String email, String password, boolean isAdmin)
  {
    this.name = username; // Default name to username
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  // New constructor with name
  public User(String name, String username, String email, String password, boolean isAdmin)
  {
    this.name = name;
    this.username = username;
    this.email = email;
    this.password = password;
    this.isAdmin = isAdmin;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
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
            "name='" + name + '\'' +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", isAdmin=" + isAdmin +
            '}';
  }
}
