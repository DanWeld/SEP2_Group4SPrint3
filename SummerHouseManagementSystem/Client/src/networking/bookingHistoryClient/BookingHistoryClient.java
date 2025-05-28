package networking.bookingHistoryClient;

import dtos.BookingHistory;
import observer.PropertyChangeSubject;

/**
 * Interface for BookingHistoryClient, which handles booking history operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 * This interface defines methods for retrieving past, current, and future bookings,
 * canceling bookings, and getting booking history for a specific property.
 *
 * @author Group 4
 * @version 1.0
 */
public interface BookingHistoryClient extends PropertyChangeSubject
{
  /**
   * get past bookings for a user
   * @param username the username of the user
   */
  void getPastBookings(String username);

  /**
   * get current bookings for a user
   * @param username the username of the user
   */
  void getCurrentBookings(String username);

  /**
   * get future bookings for a user
   * @param username the username of the user
   */
  void getFutureBookings(String username);

  /**
   * Cancel a booking
   * @param booking the booking to cancel
   */
  void cancelBooking(BookingHistory booking);

  /**
   * Get booking history for a specific property
   * @param propertyId the ID of the property
   */
  void getBookingHistory(int propertyId);
}
