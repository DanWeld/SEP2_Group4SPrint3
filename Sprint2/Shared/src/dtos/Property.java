package Shared.src.dtos;

import dtos.Facilities;

public class Property
{
  private int id;
  private String location;
  private double pricePerNight;
  private Facilities facilities;

  public Property(int id, String location, double pricePerNight, Facilities facilities)
  {
    this.id = id;
    this.location = location;
    this.pricePerNight = pricePerNight;
    this.facilities = facilities;
  }

  public int id()
  {
    return id;
  }

  public String location()
  {
    return location;
  }

  public double pricePerNight()
  {
    return pricePerNight;
  }

  public Facilities facilities()
  {
    return facilities;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("id: ").append(id)
        .append(", Location: ").append(location)
        .append(", Price per night: ").append(pricePerNight)
        .append(", Facilities: ")
        .append(facilities);
    return sb.toString();
  }
  // added by DW
  public String getLocation()
  {
    return location;
  }
  // added by DW
  public void setLocation(String newLocation)
  {
    this.location = newLocation;

  }
  // added by DW
  public double getPricePerNight()
  {
    return pricePerNight;
  }
  // added by DW
  public void setPricePerNight(double v)
  {
    this.pricePerNight = v;
  }
  // added by DW
  public boolean getAvailability()
  {
    return true;
  }
  // added by DW
  public void setAvailability(boolean b)
  {
    // TODO Auto-generated method stub
  }
  // added by DW
  public Facilities getFacilities()
  {
    return facilities;
  }
}
