package networking.propertyListClient;

import com.google.gson.Gson;
import dtos.Request;
import networking.Client;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

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
    // Dates to a list
    List<Date> dates = List.of(startDate, endDate);
    // Send the request to the server
    client.sendRequest(new Request("property", "readAvailable", dates));
  }
}
