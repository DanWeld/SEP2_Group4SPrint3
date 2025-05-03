package networking.propertyListClient;

import java.sql.Date;

public interface PropertyListClient
{
  void getAvailableProperties(Date startDate, Date endDate) throws Exception;
  void isAvailable(Date startDate, Date endDate, int id) throws Exception;
  void getByID(int id) throws Exception;
}
