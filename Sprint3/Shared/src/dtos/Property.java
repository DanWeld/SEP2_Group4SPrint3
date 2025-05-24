package dtos;

public class Property
{
  private int id;
  private String location;
  private double pricePerNight;
  private Facilities facilities;

  public Property(int id, String location, double pricePerNight,
      Facilities facilities)
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
    sb.append("id: ").append(id).append(", Location: ").append(location)
        .append(", Price per night: ").append(pricePerNight)
        .append(", Facilities: ").append(facilities);
    return sb.toString();
  }

  public String getLocation()
  {
    return location;
  }

  public void setLocation(String location)
  {
    // new location
    this.location = location;
  }

  public double getPricePerNight()
  {
    return pricePerNight;
  }

  public void setPricePerNight(double v)
  {
    this.pricePerNight = v;
  }

  public Facilities getFacilities()
  {
    return facilities;
  }
}
