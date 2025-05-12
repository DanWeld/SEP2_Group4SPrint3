package model.booking;

import observer.PropertyChangeSubject;

import java.sql.Date;

public interface BookingModel extends PropertyChangeSubject
{
  void createBooking(int propertyID, Date startDate, Date endDate, String username);
  void isAvailable(Date startDate, Date endDate, int propertyId);
  boolean extendBooking(int propertyId, Date currentEndDate, Date newEndDate, String username) throws Exception;
  boolean isAvailableForExtension(Date startDate, Date endDate, int propertyId);
}
