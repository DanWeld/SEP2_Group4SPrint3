package dtos;

import java.io.Serializable;

public class ErrorResponse implements Serializable
{
  private final String errorMessage;

  public ErrorResponse(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }

  public String errorMessage()
  {
    return errorMessage;
  }
}
