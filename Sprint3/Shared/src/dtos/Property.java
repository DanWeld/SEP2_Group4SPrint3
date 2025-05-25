package dtos;

  /**
   * Represents a property listing with location, price, and facilities.
   *
   * @author Group 4
   * @version 1.0
   */
  public class Property
  {
    private int id;
    private String location;
    private double pricePerNight;
    private Facilities facilities;

    /**
     * Constructs a Property with the specified details.
     *
     * @param id the unique identifier of the property
     * @param location the location of the property
     * @param pricePerNight the price per night for the property
     * @param facilities the facilities available at the property
     */
    public Property(int id, String location, double pricePerNight,
        Facilities facilities)
    {
      this.id = id;
      this.location = location;
      this.pricePerNight = pricePerNight;
      this.facilities = facilities;
    }

    /**
     * Returns the property ID.
     * @return the property ID
     */
    public int id()
    {
      return id;
    }

    /**
     * Returns the property location.
     * @return the location string
     */
    public String location()
    {
      return location;
    }

    /**
     * Returns the price per night.
     * @return the price per night
     */
    public double pricePerNight()
    {
      return pricePerNight;
    }

    /**
     * Returns the facilities available at the property.
     * @return the Facilities object
     */
    public Facilities facilities()
    {
      return facilities;
    }

    /**
     * Returns a string representation of the property.
     * @return a string with property details
     */
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("id: ").append(id).append(", Location: ").append(location)
          .append(", Price per night: ").append(pricePerNight)
          .append(", Facilities: ").append(facilities);
      return sb.toString();
    }

    /**
     * Gets the property location.
     * @return the location string
     */
    public String getLocation()
    {
      return location;
    }

    /**
     * Sets the property location.
     * @param location the new location
     */
    public void setLocation(String location)
    {
      this.location = location;
    }

    /**
     * Gets the price per night.
     * @return the price per night
     */
    public double getPricePerNight()
    {
      return pricePerNight;
    }

    /**
     * Sets the price per night.
     * @param v the new price per night
     */
    public void setPricePerNight(double v)
    {
      this.pricePerNight = v;
    }

    /**
     * Gets the facilities available at the property.
     * @return the Facilities object
     */
    public Facilities getFacilities()
    {
      return facilities;
    }
  }