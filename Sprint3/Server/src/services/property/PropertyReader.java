package services.property;

import observer.PropertyChangeSubject;

import java.sql.Date;

public interface PropertyReader extends PropertyChangeSubject
{
  void getAvailableProperties(Date startDate, Date endDate);
}
