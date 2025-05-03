package networking.propertyListClient;

import com.google.gson.Gson;
import networking.Client;

import java.io.IOException;
import java.sql.Date;

public class PropertyListClientImpl implements PropertyListClient
{
  private Client client;
  private Date startDate;
  private Date endDate;

  public PropertyListClientImpl(Client client) throws IOException
  {
    this.client = client;
  }

  @Override
  public void getAvailableProperties(Date startDate, Date endDate) throws IOException
  {
    this.startDate = startDate;
    this.endDate = endDate;

    // Convert the dates to JSON
    String datesJson = new Gson().toJson(new Date[]{startDate, endDate});

    // Send the request to the server
    client.requestAvailableProperties(datesJson);
  }

  @Override public void isAvailable(Date startDate, Date endDate, int id)
      throws Exception
  {
    client.getIsAvailable(startDate, endDate, id);
  }

  @Override public void getByID(int id) throws Exception
  {
    client.getPropertyByID(id);
  }
}
