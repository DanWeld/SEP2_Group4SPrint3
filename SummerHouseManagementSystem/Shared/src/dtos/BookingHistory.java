package dtos;

import java.sql.Date;

/**
 * Represents a booking made by a user for a property.
 * Contains details about the booking date, start date, end date, property ID, and username.
 */
public class BookingHistory
{
  private final String username;
  private String email;
  private final Date startDate;
  private final Date endDate;
  private double pricePerNight;
  private Date bookingDate;
  private Booking booking;
  private Property property;
  private int propertyId;
  private String location;

  /**
   * Constructor for BookingHistory.
   *
   * @param username      the username of the user who made the booking
   * @param location      the location of the property booked
   * @param startDate     the start date of the booking
   * @param endDate       the end date of the booking
   * @param pricePerNight the price per night for the booking
   * @param propertyId    the ID of the booked property
   */
  public BookingHistory(String username, String location, Date startDate,
      Date endDate, double pricePerNight, int propertyId)
  {
    this.username = username;
    this.location = location;
    this.startDate = startDate;
    this.endDate = endDate;
    this.pricePerNight = pricePerNight;
    this.propertyId = propertyId;
  }

  /**
   * Constructor for BookingHistory with booking date.
   *
   * @param username    the username of the user who made the booking
   * @param email       the email of the user who made the booking
   * @param startDate   the start date of the booking
   * @param endDate     the end date of the booking
   * @param bookingDate the date when the booking was made
   */
  public BookingHistory(String username, String email, Date startDate,
      Date endDate, Date bookingDate)
  {

    this.username = username;
    this.email = email;
    this.startDate = startDate;
    this.endDate = endDate;
    this.bookingDate = bookingDate;
  }

  /**
   * gets the username of the user who made the booking.
   *
   * @return the username of the user
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * Gets the location of the booked property.
   *
   * @return the location of the property
   */
  public String getLocation()
  {
    return location;
  }

  /**
   * Gets the price per night for the booking.
   *
   * @return the price per night
   */
  public double getPricePerNight()
  {
    return pricePerNight;
  }

  /**
   * Gets the end date of the booking.
   *
   * @return the end date of the booking
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * Gets the start date of the booking.
   *
   * @return the start date of the booking
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * Gets the property ID of the booked property.
   *
   * @return the property ID
   */
  public int getPropertyId()
  {
    return propertyId;
  }

  /**
   * Gets the booking date.
   *
   * @return the booking date
   */
  public Booking getBooking()
  {
    return booking;
  }

  /**
   * Sets the booking details.
   * @param booking the Booking object containing booking details
   *
   */
  public void setBooking(Booking booking)
  {
    this.booking = booking;
  }

  /**
   * set email of the user who made the booking.
   *
   * @return the email of the user
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * get booking date of the booking.
   *
   * @return the booking date
   */
  public Date getBookingDate()
  {
    return bookingDate;
  }

  /**
   * String representation of the BookingHistory object.
   * @return a string containing the details of the booking history
   */
  public String toString()
  {
    return "BookingHistory{" + "username='" + username + '\'' + ", location='"
        + location + '\'' + ", startDate=" + startDate + ", endDate=" + endDate
        + ", pricePerNight=" + pricePerNight + ", propertyId=" + propertyId
        + '}';
  }
}
