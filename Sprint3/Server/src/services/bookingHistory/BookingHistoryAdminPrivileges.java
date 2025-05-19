package services.bookingHistory;

import observer.PropertyChangeSubject;

public interface BookingHistoryAdminPrivileges extends PropertyChangeSubject
{
  void getAllBookings(int propertyId);
}
