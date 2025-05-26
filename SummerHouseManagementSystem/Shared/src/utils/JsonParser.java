package utils;

   import com.google.gson.Gson;
   import dtos.*;
   import java.util.ArrayList;
   import java.util.List;

   /**
    * Utility class for converting Java objects to and from JSON using Gson.
    * Supports serialization and deserialization of single objects and lists.
    *
    * @author Group 4
    * @version 1.0
    */
   public class JsonParser
   {
     private static final Gson gson = new Gson();

     /**
      * Converts a Java object to its JSON string representation.
      * @param object the object to convert
      * @return JSON string representation of the object
      */
     public static String toJson(Object object)
     {
       return gson.toJson(object);
     }

     /**
      * Converts a payload object to a specific class type.
      * @param payload the object to convert
      * @param clazz the class to convert to
      * @param <T> the type of the class
      * @return the converted object of type T
      */
     public static <T> T convertPayload(Object payload, Class<T> clazz) {
       String json = toJson(payload);
       return gson.fromJson(json, clazz);
     }

     /**
      * Converts a JSON string to a list of objects of the specified type.
      * @param json the JSON string
      * @param clazz the array class of the type
      * @param <T> the type of the list elements
      * @return a list of objects of type T
      */
     public static <T> List<T> jsonToList(String json, Class<T[]> clazz)
     {
       T[] array = gson.fromJson(json, clazz);
       List<T> list = new ArrayList<>();
       if (array != null) {
         for (T item : array) {
           list.add(item);
         }
       }
       return list;
     }

     /**
      * Converts a payload object to a list of objects of the specified type.
      * @param payload the object to convert
      * @param arrayClass the array class of the type
      * @param <T> the type of the list elements
      * @return a list of objects of type T
      */
     public static <T> List<T> toList(Object payload, Class<T[]> arrayClass) {
       String json = toJson(payload);
       return jsonToList(json, arrayClass);
     }

     /**
      * Converts a JSON string to an object of the specified class.
      * @param json the JSON string
      * @param clazz the class to convert to
      * @return the converted object
      */
     public static Object jsonToObject(String json, Class<?> clazz)
     {
       return gson.fromJson(json, clazz);
     }
   }