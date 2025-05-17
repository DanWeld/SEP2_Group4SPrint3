package dtos;

import java.io.Serializable;

public class Response implements Serializable
{
  private final String status;
  private final Object payload;

  public Response(String status, Object payload)
  {
    this.status = status;
    this.payload = payload;
  }

  public String status()
  {
    return status;
  }

  public Object payload()
  {
    return payload;
  }
}
