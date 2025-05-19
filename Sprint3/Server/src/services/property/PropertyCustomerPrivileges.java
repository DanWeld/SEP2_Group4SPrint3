package services.property;

import observer.PropertyChangeSubject;

import java.sql.Date;

public interface PropertyCustomerPrivileges extends PropertyChangeSubject
{
  void getAvailableProperties(Date startDate, Date endDate);
}
