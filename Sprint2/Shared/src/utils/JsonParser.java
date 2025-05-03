package Shared.src.utils;

import com.google.gson.Gson;
import dtos.Booking;
import dtos.Property;
import dtos.PropertyList;

import java.lang.reflect.Type;
import java.sql.Date;

public class JsonParser
{
  public static PropertyList jsonToProperties(String jsonResponse)
  {
    Gson gson = new Gson();
    return gson.fromJson(jsonResponse, PropertyList.class);
  }

  public static String propertiesToJson(PropertyList propertyList)
  {
    Gson gson = new Gson();
    return gson.toJson(propertyList.getProperties());
  }

  public static Property parseProperty(String jsonResponse)
  {
    Gson gson = new Gson();
    return gson.fromJson(jsonResponse, Property.class);
  }

  public static String datesToJson(Date startDate, Date endDate)
  {
    Gson gson = new Gson();
    return gson.toJson(new Date[] {startDate, endDate});
  }

  public static Date[] jsonToDates(String json)
  {
    Gson gson = new Gson();
    return gson.fromJson(json, Date[].class);
  }

  public static String bookingToJson(Booking booking)
  {
    Gson gson = new Gson();
    return gson.toJson(booking);
  }

  public static Booking jsonToBooking(String jsonResponse)
  {
    Gson gson = new Gson();
    return gson.fromJson(jsonResponse, Booking.class);
  }
}