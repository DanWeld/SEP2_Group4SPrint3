package dtos;

import java.sql.Date;

public class BookingHistory
{
  private String location;
  private Date startDate;
  private Date endDate;
  private double pricePerNight;

  public BookingHistory(String location, Date startDate, Date endDate, double pricePerNight)
  {
    this.location = location;
    this.startDate = startDate;
    this.endDate = endDate;
    this.pricePerNight = pricePerNight;
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
}
