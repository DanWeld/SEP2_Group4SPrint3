package dtos;

import java.sql.Date;

public class Booking
{
  private Date startDate;
  private Date endDate;
  private Date bookingDate;
  private int propertyId;
  private String username;

  public Booking(Date bookingDate, Date startDate, Date endDate, int propertyId, String username)
  {
    this.startDate = startDate;
    this.endDate = endDate;
    this.propertyId = propertyId;
    this.username = username;
    this.bookingDate = bookingDate;
  }

  public Date getBookingDate()
  {
    return bookingDate;
  }

  public Date getStartDate()
  {
    return startDate;
  }

  public void setStartDate(Date startDate)
  {
    this.startDate = startDate;
  }

  public Date getEndDate()
  {
    return endDate;
  }

  public void setEndDate(Date endDate)
  {
    this.endDate = endDate;
  }

  public int getPropertyId()
  {
    return propertyId;
  }

  public String getUsername()
  {
    return username;
  }

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
