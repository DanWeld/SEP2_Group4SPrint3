package model.bookingHistory;

import dtos.Booking;
import dtos.BookingHistory;
import observer.PropertyChangeSubject;

/**
 * BookingHistoryModel interface defines the methods for managing booking history.
 * It extends PropertyChangeSubject to allow listeners to be notified of changes.
 *
 * @author Group 4
 * @version 1.0
 */
public interface BookingHistoryModel extends PropertyChangeSubject
{
  void getPastBookings(String username);
  void getCurrentBookings(String username);
  void getFutureBookings(String username);
  void getAllBookings(int propertyId);
}
