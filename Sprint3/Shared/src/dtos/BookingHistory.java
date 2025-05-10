package dtos;

import java.sql.Date;

public class BookingHistory
{
  private String username;
  private String location;
  private Date startDate;
  private Date endDate;
  private double pricePerNight;
  private int propertyId;

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

  public String toString()
  {
    return "BookingHistory{" + "username='" + username + '\'' + ", location='"
        + location + '\'' + ", startDate=" + startDate + ", endDate=" + endDate
        + ", pricePerNight=" + pricePerNight + '}';
  }
}
