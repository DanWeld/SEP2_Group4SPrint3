package networking.propertyListClient;

import observer.PropertyChangeSubject;

import java.sql.Date;

public interface PropertyListClient  extends PropertyChangeSubject
{
  void getAvailableProperties(Date startDate, Date endDate) throws Exception;
}
