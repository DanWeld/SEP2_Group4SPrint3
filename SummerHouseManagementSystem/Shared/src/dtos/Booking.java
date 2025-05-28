package dtos;

import java.sql.Date;

/**
 * Represents a booking made by a user for a property.
 * Contains details about the booking date, start date, end date, property ID, and username.
 *
 * @author Group 4
 * @version 1.0
 */
public class Booking
{
  private Date startDate;
  private Date endDate;
  private Date bookingDate;
  private int propertyId;
  private String username;

  /**
   * Constructs a Booking object with the specified booking date, start date, end date, property ID, and username.
   *
   * @param bookingDate the date when the booking was made
   * @param startDate the start date of the booking
   * @param endDate the end date of the booking
   * @param propertyId the ID of the property being booked
   * @param username the username of the person making the booking
   */
  public Booking(Date bookingDate, Date startDate, Date endDate, int propertyId, String username)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.propertyId = propertyId;
    this.username = username;
    this.bookingDate = bookingDate;
  }

  /**
   * Constructs a Booking object with the specified start date, end date, and property ID.
   *
   * @param startDate the start date of the booking
   * @param endDate the end date of the booking
   * @param propertyId the ID of the property being booked
   */
  public Booking(Date startDate, Date endDate, int propertyId, String username)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.propertyId = propertyId;
    this.username = username;
  }

  /**
   * Constructs a Booking object with the specified start date, end date, and property ID.
   *
   * @param startDate the start date of the booking
   * @param endDate the end date of the booking
   * @param propertyId the ID of the property being booked
   */
  public Booking(Date startDate, Date endDate, int propertyId)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.propertyId = propertyId;
  }

  /**
   * get the booking date
   * @return the date when the booking was made
   */
  public Date getBookingDate()
  {
    return bookingDate;
  }

  /**
   * get the start date of the booking
   * @return the start date of the booking
   */
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * set the start date of the booking
   * @param startDate the start date of the booking
   */
  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * get the end date of the booking
   * @return the end date of the booking
   */
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * set the end date of the booking
   * @param endDate the end date of the booking
   */
  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  /**
   * get the property ID of the booking
   * @return the ID of the property being booked
   */
  public int getPropertyId()
  {
    return propertyId;
  }

  /**
   * get the username of the person making the booking
   *
   * @return the username of the person making the booking
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * String representation of the Booking object.
   *
   * @return a string containing the booking details
   */
  @Override public String toString()
  {
    return "Booking{" +
        "bookingDate=" + bookingDate +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", propertyId=" + propertyId +
        ", username='" + username + '\'' +
        '}';
  }
}
