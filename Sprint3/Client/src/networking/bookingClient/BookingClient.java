package networking.bookingClient;

import dtos.Booking;
import observer.PropertyChangeSubject;

/**
 * Interface for BookingClient, which handles booking-related operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 * This interface defines methods for creating bookings, checking availability,
 * and extending existing bookings.
 *
 * @author Group 4
 * @version 1.0
 */

public interface BookingClient extends PropertyChangeSubject
{
  /**
   * Creates a new booking
   * @param booking The booking to create
   */
  void createBooking(Booking booking);

  /**
   * Checks if a booking is available
   * @param booking The booking to check
   */
  void isAvailable(Booking booking);

  /**
   * Extends an existing booking
   * @param booking The booking to extend
   */
  void extendBooking(Booking booking);
}
