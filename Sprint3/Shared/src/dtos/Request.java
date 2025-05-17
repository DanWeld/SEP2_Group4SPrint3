package dtos;

import java.io.Serializable;

public class Request implements
    Serializable
{
    private final String handler;
    private final String action;
    private final Object payload;
    private User user;

  public Request(String handler, String action, Object payload)
    {
        this.handler = handler;
        this.action = action;
        this.payload = payload;
    }

    public Request(String handler, String action, Object payload, User user)
    {
        this.handler = handler;
        this.action = action;
        this.payload = payload;
        this.user = user;
    }

    public String handler()
    {
        return handler;
    }

    public String action()
    {
        return action;
    }

    public Object payload()
    {
        return payload;
    }

    public User user()
    {
        return user;
    }

    public String toString()
    {
        return "Request{" +
            "handler='" + handler + '\'' +
            ", action='" + action + '\'' +
            ", payload=" + payload +
            '}';
    }
}
