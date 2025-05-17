package networking.requestHandlers;

import dtos.ErrorResponse;
import dtos.Property;
import dtos.Response;
import dtos.User;
import model.property.PropertyModel;
import model.property.PropertyModelManager;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.List;

public class PropertyRequestHandler
    implements PropertyChangeListener, RequestHandler
{
  private final PropertyModel propertyModel;
  private PrintWriter out;

  public PropertyRequestHandler(PropertyModel propertyModel)
  {
    this.propertyModel = propertyModel;
    propertyModel.addPropertyChangeListener(this);
  }

  @Override public boolean canHandle(String handler, String action)
  {
    return handler.equals("property") && (action.equals("create")
        || action.equals("update") || action.equals("readAll") || action.equals(
        "readAvailable") || action.equals("delete"));
  }

  @Override public void handle(String action, String payload, PrintWriter out)
  {
    this.out = out;
    switch (action)
    {
      case "create" ->
      {
        Property property = (Property) JsonParser.jsonToObject(payload,
            Property.class);
        propertyModel.createProperty(property);
      }
      case "update" ->
      {
        Property property = (Property) JsonParser.jsonToObject(payload,
            Property.class);
        propertyModel.updateProperty(property);
      }
      case "readAll" ->
      {
        propertyModel.getAllProperties();
      }
      case "readAvailable" ->
      {
        List<Date> dates = JsonParser.jsonToList(payload, Date[].class);
        propertyModel.getAvailableProperties(dates.getFirst(), dates.getLast());
      }
      case "delete" ->
      {
        int propertyId = Integer.parseInt(payload);
        propertyModel.deleteProperty(propertyId);
      }
    }
  }

  public void propertyChange(PropertyChangeEvent evt)
  {
    String eventName = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();
    switch (eventName)
    {
      case "propertyCreationSuccess" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property creation success: "
            + ((Property) response.payload()).id());
      }
      case "propertyCreationFailure" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property creation failure: "
            + ((ErrorResponse) response.payload()).errorMessage());
      }
      case "propertyUpdateSuccess" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println(
            "Property update success: " + ((Property) response.payload()).id());
      }
      case "propertyUpdateFailure" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property update failure: "
            + ((ErrorResponse) response.payload()).errorMessage());
      }
      case "propertyDeletionSuccess" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property deletion success: " + response.payload());
      }
      case "propertyDeletionFailure" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property deletion failure: "
            + ((ErrorResponse) response.payload()).errorMessage());
      }
      case "propertyListSuccess" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property list success, count: "
            + ((List<Property>) response.payload()).size());
      }
      case "propertyListFailure" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Property list failure: "
            + ((ErrorResponse) response.payload()).errorMessage());
      }
      case "availablePropertiesSuccess" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Available properties success, count: "
            + ((List<Property>) response.payload()).size());
      }
      case "availablePropertiesFailure" ->
      {
        out.println(JsonParser.toJson(response));
        out.flush();
        System.out.println("Available properties failure: "
            + ((ErrorResponse) response.payload()).errorMessage());
      }
    }
  }
}
