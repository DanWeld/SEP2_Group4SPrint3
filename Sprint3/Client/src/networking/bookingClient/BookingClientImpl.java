package networking.bookingClient;

import networking.Client;

import java.sql.Date;

public class BookingClientImpl implements BookingClient
{
  private Client client;

  public BookingClientImpl(Client client)
  {
    this.client = client;
  }

  @Override
  public void createBooking(int propertyID, Date startDate,
      Date endDate, String username)
  {
    client.createBooking(propertyID, startDate, endDate, username);
  }

  @Override public void isAvailable(Date startDate, Date endDate, int id)
  {
    client.getIsAvailable(startDate, endDate, id);
  }
  
  @Override
  public boolean extendBooking(int propertyId, Date currentEndDate, Date newEndDate, String username) throws Exception
  {
    try {
      System.out.println("DEBUG: BookingClientImpl.extendBooking called with:");
      System.out.println("  PropertyID: " + propertyId);
      System.out.println("  CurrentEndDate: " + currentEndDate);
      System.out.println("  NewEndDate: " + newEndDate);
      System.out.println("  Username: " + username);
      
      // Convert the dates to ensure they're properly serialized
      Object[] params = new Object[] { 
          propertyId,
          currentEndDate.getTime(),  // Using timestamp for better serialization
          newEndDate.getTime(),      // Using timestamp for better serialization
          username 
      };
      
      Boolean result = (Boolean) client.call("extendBooking", params, Boolean.class);
      System.out.println("DEBUG: extendBooking result: " + result);
      return result;
    } catch (Exception e) {
      System.err.println("ERROR in BookingClientImpl.extendBooking: " + e.getMessage());
      e.printStackTrace();
      throw new Exception("Failed to extend booking: " + e.getMessage(), e);
    }
  }
}
