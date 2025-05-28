package dtos;

import java.io.Serializable;

/**
 * Represents an error response with a message.
 * This class is used to encapsulate error messages
 * that can be returned to the client
 *
 * @author Group 4
 * @version 1.0
 */
public class ErrorResponse implements Serializable
{
  private final String errorMessage;

  /**
   * Constructs an ErrorResponse with the specified error message.
   *
   * @param errorMessage the error message to be included in the response
   */
  public ErrorResponse(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  /**
   * Returns the error message contained in this response.
   *
   * @return the error message
   */
  public String errorMessage()
  {
    return errorMessage;
  }
}
