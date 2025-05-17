package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dtos.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class JsonParser
{
  private static final Gson gson = new Gson();

  public static <T> T jsonToObject(String json, Type type) {
    return gson.fromJson(json, type);
  }

  /**
   * Converts a User object to JSON
   * @param object The user to convert
   * @return JSON string representation of the user
   */
  public static String toJson(Object object)
  {
    return gson.toJson(object);
  }

  public static <T> T convertPayload(Object payload, Class<T> clazz) {
    String json = toJson(payload);
    return gson.fromJson(json, clazz);
  }

  public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
  {
    Gson gson = new Gson();
    T[] array = gson.fromJson(json, clazz);
    List<T> list = new ArrayList<>();
    if (array != null) {
      for (T item : array) {
        list.add(item);
      }
    }
    return list;
  }

  public static <T> List<T> toList(Object payload, Class<T[]> arrayClass) {
    String json = toJson(payload);
    return jsonToList(json, arrayClass);
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

  public static String toJson(ArrayList<BookingHistory> bookingHistory)
  {
    Gson gson = new Gson();
    return gson.toJson(bookingHistory);
  }

  public static List<BookingHistory> jsonToBookingHistory(String jsonResponse)
  {
    Gson gson = new Gson();
    BookingHistory[] bookingHistoryArray = gson.fromJson(jsonResponse, BookingHistory[].class);
    List<BookingHistory> bookingHistoryList = new ArrayList<>();
    if (bookingHistoryArray != null) {
      for (BookingHistory bookingHistory : bookingHistoryArray) {
        bookingHistoryList.add(bookingHistory);
      }
    }
    return bookingHistoryList;
  }

  public static Object jsonToObject(String json, Class<?> clazz)
  {
    Gson gson = new Gson();
    return gson.fromJson(json, clazz);
  }
}