package networking.bookingClient;

import dtos.Booking;
import observer.PropertyChangeSubject;

import java.sql.Date;

public interface BookingClient extends PropertyChangeSubject
{
  void createBooking(Booking booking);
  void isAvailable(Booking booking);
  void extendBooking(Booking booking);
}
