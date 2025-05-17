package dtos;

import java.util.ArrayList;
import java.util.List;

public class UserList
{
  private List<User> users;

  public UserList()
  {
    users = new ArrayList<>();
  }
  public void addUser(User user)
  {
    users.add(user);
  }

  public List<User> getUsers()
  {
    return users;
  }
}
