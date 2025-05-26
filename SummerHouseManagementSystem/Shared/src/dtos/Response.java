package dtos;

import java.io.Serializable;

/**
 * Represents a generic response sent from a handler, containing a status and a payload.
 *
 * @author Group 4
 * @version 1.0
 */
public class Response implements Serializable
{
  private final String status;
  private final Object payload;

  /**
   * Constructs a Response with the specified status and payload.
   *
   * @param status  the status of the response (e.g., "success", "error")
   * @param payload the data associated with the response
   */
  public Response(String status, Object payload)
  {
    this.status = status;
    this.payload = payload;
  }

  /**
   * Returns the status of the response.
   *
   * @return the status string
   */
  public String status()
  {
    return status;
  }

  /**
   * Returns the payload of the response.
   *
   * @return the payload object
   */
  public Object payload()
  {
    return payload;
  }
}