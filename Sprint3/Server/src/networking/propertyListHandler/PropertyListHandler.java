package networking.propertyListHandler;

import java.sql.Date;
import java.sql.SQLException;

public interface PropertyListHandler
{
  void getAvailableProperties() throws SQLException;

  void setDates(Date[] dates);
}
