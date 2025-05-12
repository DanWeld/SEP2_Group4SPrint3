package dtos;

public class Property
{
  private int id;
  private String location;
  private double pricePerNight;
  private boolean availability;
  private Facilities facilities;
  private String newLocation;

  public Property(int id, String location, double pricePerNight, boolean b,
      Facilities facilities)
  {
    this.id = id;
    this.location = location;
    this.pricePerNight = pricePerNight;
    this.facilities = facilities;
  }

  public Property(int id, String testLocation, double pricePerNight, Facilities mockFacilities)
  {
    this.id = id;
    this.location = testLocation;
    this.pricePerNight = pricePerNight;
    this.facilities = mockFacilities;
  }

  public int getId()
  {
    return id;
  }

  public String getName()
  {
    return location; // Using location as name for compatibility
  }

  public String getDescription()
  {
    return "Description not available"; // Placeholder
  }

  public String getAddress()
  {
    return "Address not available"; // Placeholder
  }

  public int getBedrooms()
  {
    return 1; // Default value
  }

  public int getBathrooms()
  {
    return 1; // Default value
  }

  public double getPrice()
  {
    return pricePerNight;
  }

  public void setName(String name)
  {
    this.location = name; // Using location as name for compatibility
  }

  public void setDescription(String description)
  {
    // Placeholder - not stored in the current model
  }

  public void setAddress(String address)
  {
    // Placeholder - not stored in the current model
  }

  public void setBedrooms(int bedrooms)
  {
    // Placeholder - not stored in the current model
  }

  public void setBathrooms(int bathrooms)
  {
    // Placeholder - not stored in the current model
  }

  public void setPrice(double price)
  {
    this.pricePerNight = price;
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

  public boolean getAvailability()
  {
    return true; // Assuming availability is always true for this example
  }

  public void setAvailability(boolean b)
  {
    // Set availability logic here.
  }

  public Facilities getFacilities()
  {
    return facilities;
  }
}
