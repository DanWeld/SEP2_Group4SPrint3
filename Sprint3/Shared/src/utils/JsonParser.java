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

  public static Object jsonToObject(String json, Class<?> clazz)
  {
    Gson gson = new Gson();
    return gson.fromJson(json, clazz);
  }
}