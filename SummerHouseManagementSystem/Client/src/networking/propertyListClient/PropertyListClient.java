package networking.propertyListClient;

import observer.PropertyChangeSubject;

import java.sql.Date;

/**
 * Interface for PropertyListClient, which handles property-related operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 *
 * @author Group 4
 * @version 1.0
 */
public interface PropertyListClient  extends PropertyChangeSubject
{
  /**
   * Get available properties within a specified date range.
   *
   * @param startDate the start date of the range
   * @param endDate the end date of the range
   */
  void getAvailableProperties(Date startDate, Date endDate);

  /**
   * Get all properties.
   */
  void getAllProperties();
}
