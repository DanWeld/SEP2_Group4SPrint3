package networking.propertyListClient;

import java.sql.Date;

public interface PropertyListClient
{
  void getAvailableProperties(Date startDate, Date endDate) throws Exception;
}
