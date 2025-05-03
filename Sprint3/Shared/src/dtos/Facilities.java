package dtos;

public class Facilities
{
  private boolean kitchen;
  private boolean internet;
  private boolean dishwasher;
  private boolean laundryMachine;
  private boolean swimmingPool;

  public Facilities( boolean kitchen, boolean internet, boolean dishwasher,
      boolean laundryMachine, boolean swimmingPool)
  {
    this.kitchen = kitchen;
    this.internet = internet;
    this.dishwasher = dishwasher;
    this.laundryMachine = laundryMachine;
    this.swimmingPool = swimmingPool;
  }

  public boolean kitchen()
  {
    return kitchen;
  }

  public boolean internet()
  {
    return internet;
  }

  public boolean dishwasher()
  {
    return dishwasher;
  }

  public boolean laundryMachine()
  {
    return laundryMachine;
  }

  public boolean swimmingPool()
  {
    return swimmingPool;
  }

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
