package networking.userListToAdmin;

import dtos.User;
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
}
