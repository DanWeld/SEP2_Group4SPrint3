package services.property;

import observer.PropertyChangeSubject;

import java.sql.Date;

/**
 * Interface for customer privileges related to property management.
 * It extends PropertyChangeSubject to allow for property change notifications.
 */
public interface PropertyCustomerPrivileges extends PropertyChangeSubject
{
  void getAvailableProperties(Date startDate, Date endDate);
}
