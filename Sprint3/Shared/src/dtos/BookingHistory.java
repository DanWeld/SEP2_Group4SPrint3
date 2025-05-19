package dtos;

import java.sql.Date;

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

  public BookingHistory(String username, String email, Date startDate, Date endDate, Date bookingDate){

    this.username = username;
    this.email = email;
    this.startDate = startDate;
    this.endDate = endDate;
    this.bookingDate = bookingDate;
  }

  public String getUsername()
  {
    return username;
  }

  public String getLocation()
  {
    return location;
  }

  public double getPricePerNight()
  {
    return pricePerNight;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public int getPropertyId()
  {
    return propertyId;
  }

  public Booking getBooking()
  {
    return booking;
  }

  public String getEmail()
  {
    return email;
  }

  public Date getBookingDate()
  {
    return bookingDate;
  }

  public String toString()
  {
    return "BookingHistory{" +
        "username='" + username + '\'' +
        ", location='" + location + '\'' +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        ", pricePerNight=" + pricePerNight +
        ", propertyId=" + propertyId +
        '}';
  }
}
