package dtos;

import java.io.Serializable;

/**
 * A data transfer object for user profile information.
 * This class extends the basic User class with additional profile-related fields.
 */
public class UserProfile extends User implements Serializable
{
  private static final long serialVersionUID = 1L;
  
  private String bio;
  private String phoneNumber;
  private String address;
  
  public UserProfile(User user)
  {
    super(user.getUsername(), user.getEmail(), user.getPassword(), user.isAdmin());
    this.setName(user.getName());
    this.bio = "";
    this.phoneNumber = "";
    this.address = "";
  }
  
  public UserProfile(String name, String username, String email, String password, boolean isAdmin,
                     String bio, String phoneNumber, String address)
  {
    super(name, username, email, password, isAdmin);
    this.bio = bio;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }
  
  public String getBio()
  {
    return bio;
  }
  
  public void setBio(String bio)
  {
    this.bio = bio;
  }
  
  public String getPhoneNumber()
  {
    return phoneNumber;
  }
  
  public void setPhoneNumber(String phoneNumber)
  {
    this.phoneNumber = phoneNumber;
  }
  
  public String getAddress()
  {
    return address;
  }
  
  public void setAddress(String address)
  {
    this.address = address;
  }
  
  @Override
  public String toString()
  {
    return "UserProfile{" +
      "name='" + getName() + '\'' +
      ", username='" + getUsername() + '\'' +
      ", email='" + getEmail() + '\'' +
      ", isAdmin=" + isAdmin() +
      ", bio='" + bio + '\'' +
      ", phoneNumber='" + phoneNumber + '\'' +
      ", address='" + address + '\'' +
      '}';
  }
}
