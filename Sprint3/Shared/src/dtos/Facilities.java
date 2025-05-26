package dtos;

  /**
   * Represents the facilities available in a property.
   *
   * @author group 4
   * @version 1.0
   */
  public class Facilities
  {
    private boolean kitchen;
    private boolean internet;
    private boolean dishwasher;
    private boolean laundryMachine;
    private boolean swimmingPool;
    private boolean availability;

    /**
     * Constructs a Facilities object with the specified facility availability.
     *
     * @param kitchen        true if a kitchen is available
     * @param internet       true if internet is available
     * @param dishwasher     true if a dishwasher is available
     * @param laundryMachine true if a laundry machine is available
     * @param swimmingPool   true if a swimming pool is available
     */
    public Facilities(boolean kitchen, boolean internet, boolean dishwasher,
                     boolean laundryMachine, boolean swimmingPool)
    {
      this.kitchen = kitchen;
      this.internet = internet;
      this.dishwasher = dishwasher;
      this.laundryMachine = laundryMachine;
      this.swimmingPool = swimmingPool;
    }

    /**
     * Checks if a kitchen is available.
     *
     * @return true if a kitchen is available, false otherwise
     */
    public boolean kitchen()
    {
      return kitchen;
    }

    /**
     * Checks if internet is available.
     *
     * @return true if internet is available, false otherwise
     */
    public boolean internet()
    {
      return internet;
    }

    /**
     * Checks if a dishwasher is available.
     *
     * @return true if a dishwasher is available, false otherwise
     */
    public boolean dishwasher()
    {
      return dishwasher;
    }

    /**
     * Checks if a laundry machine is available.
     *
     * @return true if a laundry machine is available, false otherwise
     */
    public boolean laundryMachine()
    {
      return laundryMachine;
    }

    /**
     * Checks if a swimming pool is available.
     *
     * @return true if a swimming pool is available, false otherwise
     */
    public boolean swimmingPool()
    {
      return swimmingPool;
    }

    /**
     * Checks if the facilities are available.
     *
     * @return true if any facility is available, false otherwise
     */
    public boolean isAvailable()
    {
      return kitchen || internet || dishwasher || laundryMachine || swimmingPool;
    }

    /**
     * Returns a string representation of the available facilities.
     *
     * @return a string listing the available facilities
     */
    public String toString()
    {
      StringBuilder sb = new StringBuilder();
      sb.append("Facilities: ");
      if (kitchen)
        sb.append("Kitchen, ");
      if (internet)
        sb.append("Internet, ");
      if (dishwasher)
        sb.append("Dishwasher, ");
      if (laundryMachine)
        sb.append("Laundry Machine, ");
      if (swimmingPool)
        sb.append("Swimming Pool, ");

      // Remove the last comma and space
      if (sb.length() > 12) {
        sb.setLength(sb.length() - 2);
      }
      return sb.toString();
    }
  }