package networking.requestHandlers;

import dtos.ErrorResponse;
import dtos.Property;
import dtos.Response;
import dtos.User;
import model.property.PropertyModel;
import model.property.PropertyModelManager;
import services.ServiceProvider;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
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
  private Logger logger;
  public PropertyRequestHandler(PropertyModel propertyModel, Logger logger)
  {
    this.propertyModel = propertyModel;
    propertyModel.addPropertyChangeListener(this);
    this.logger = logger;
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
        logger.log("Property creation success: " + ((Property) response.payload()).id(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyCreationFailure" ->
      {
        logger.log("Property creation failure: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyUpdateSuccess" ->
      {
        logger.log("Property update success: " + ((Property) response.payload()).id(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
       }
      case "propertyUpdateFailure" ->
      {
        logger.log("Property update failure: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyDeletionSuccess" ->
      {
        logger.log("Property deletion success: " + ((Property) response.payload()).id(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyDeletionFailure" ->
      {
        logger.log("Property deletion failure: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyListSuccess" ->
      {
        logger.log("Property list success, count: " + ((List<Property>) response.payload()).size(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "propertyListFailure" ->
      {
        logger.log("Property list failure: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "availablePropertiesSuccess" ->
      {
        logger.log("Available properties success, count: "
            + ((List<Property>) response.payload()).size(), LogLevel.INFO);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
      case "availablePropertiesFailure" ->
      {
        logger.log("Available properties failure: "
            + ((ErrorResponse) response.payload()).errorMessage(), LogLevel.ERROR);
        out.println(JsonParser.toJson(response));
        out.flush();
      }
    }
  }
}
