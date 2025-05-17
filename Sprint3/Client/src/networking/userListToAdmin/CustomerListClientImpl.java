package networking.userListToAdmin;

import com.google.gson.Gson;
import dtos.User;
import dtos.UserList;
import networking.Client;

import java.util.List;

public class CustomerListClientImpl implements CustomerListClient
{
  private Client client;
  public CustomerListClientImpl(Client client) throws Exception
  {
    this.client=client;
  }



  @Override public void upgradeToAdmin(String userName) throws Exception
  {
      client.upgradeToAdmin(userName);
  }

  @Override public void updateUser(User selectedItem)
  {
    try
    {
      client.updateUser(selectedItem);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  @Override public void getAllUsers() throws Exception
  {
    client.getAllUsers();
  }

}
