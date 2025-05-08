package model.bookingHistory;

import observer.PropertyChangeSubject;

public interface BookingHistoryModel extends PropertyChangeSubject
{
  void getBookingHistory(String username);
}
