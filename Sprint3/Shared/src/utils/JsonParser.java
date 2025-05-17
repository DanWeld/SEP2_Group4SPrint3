package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.Booking;
import dtos.LoginRequest;
import dtos.Property;
import dtos.PropertyList;
import dtos.User;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

  /**
   * Converts a User object to JSON
   * @param user The user to convert
   * @return JSON string representation of the user
   */
  public static String toJson(User user)
  {
    Gson gson = new Gson();
    return gson.toJson(user);
  }

  /**
   * Converts JSON to a User object
   * @param json The JSON string
   * @return User object
   */
  public static User jsonToUser(String json)
  {
    Gson gson = new Gson();
    return gson.fromJson(json, User.class);
  }

  /**
   * Converts a LoginRequest object to JSON
   * @param loginRequest The login request to convert
   * @return JSON string representation of the login request
   */
  public static String toJson(LoginRequest loginRequest)
  {
    Gson gson = new Gson();
    return gson.toJson(loginRequest);
  }

  /**
   * Converts JSON to a LoginRequest object
   * @param request The JSON string
   * @return LoginRequest object
   */
  public static LoginRequest jsonToLoginRequest(String request)
  {
    Gson gson = new Gson();
    return gson.fromJson(request, LoginRequest.class);
  }

  /**
   * Converts a list of users to JSON
   * @param users The list of users to convert
   * @return JSON string representation of the user list
   */
  public static String userListToJson(List<User> users)
  {
    Gson gson = new Gson();
    return gson.toJson(users);
  }

  /**
   * Converts JSON to a list of User objects
   * @param json The JSON string
   * @return List of User objects
   */
  public static List<User> jsonToUserList(String json)
  {
    Gson gson = new Gson();
    User[] userArray = gson.fromJson(json, User[].class);
    List<User> userList = new ArrayList<>();
    if (userArray != null) {
      for (User user : userArray) {
        userList.add(user);
      }
    }
    return userList;
  }

  public static User parseUser(String jsonResponse)
  {
    Gson gson = new Gson();
    return gson.fromJson(jsonResponse, User.class);
  }
}