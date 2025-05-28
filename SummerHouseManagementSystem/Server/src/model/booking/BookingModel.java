package model.booking;

import observer.PropertyChangeSubject;

import java.sql.Date;

/**
 * Interface for booking-related operations and property change support.
 *
 * @author Group 4
 * @version 1.0
 */
public interface BookingModel extends PropertyChangeSubject
{
  void createBooking(int propertyID, Date startDate, Date endDate, String username);
  void isAvailable(Date startDate, Date endDate, int propertyId);
  void extendBooking(int propertyId, Date startDate, Date newEndDate, String username);
  void deleteBooking(Date startDate, int propertyId, String username);
}
