package services.property;

import dtos.ErrorResponse;
import dtos.Facilities;
import dtos.Response;
import persistence.daos.properties.PropertyDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import dtos.Property;

/**
 * Implementation of the PropertyService interface, providing methods
 * for managing properties, including creation, updating, deletion,
 * and retrieval of properties.
 */
public class PropertyServiceImpl implements PropertyService,
    PropertyCustomerPrivileges, PropertyAdminPrivileges
{
  private PropertyDAO propertyDAO;
  private PropertyChangeSupport support;

  /**
   * Constructor for PropertyServiceImpl.
   *
   * @param propertyDAO the PropertyDAO instance to use for database operations
   */
  public PropertyServiceImpl(PropertyDAO propertyDAO)
  {
    this.propertyDAO = propertyDAO;
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * Creates a new property in the database.
   * @param property the Property object containing details of the property to be created
   */
  @Override public void createProperty(Property property)
  {
    int id = property.id();
    String location = property.location();
    double pricePerNight = property.pricePerNight();
    Facilities facilities = property.facilities();

    Property createdProperty = null;
    try
    {
      createdProperty = propertyDAO.create(id, location, pricePerNight,
          facilities);
      support.firePropertyChange("propertyCreationSuccess", null,
          new Response("SUCCESS", createdProperty));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("propertyCreationFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  /**
   * Updates an existing property in the database.
   *
   * @param property the Property object containing updated details of the property
   */
  @Override public void updateProperty(Property property)
  {
    try
    {
      propertyDAO.update(property);
      support.firePropertyChange("propertyUpdateSuccess", null,
          new Response("SUCCESS", property));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("propertyUpdateFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  /**
   * Deletes a property from the database by its ID.
   *
   * @param id the ID of the property to be deleted
   */
  @Override public void deleteProperty(int id)
  {
    try
    {
      propertyDAO.delete(id);
      support.firePropertyChange("propertyDeletionSuccess", null,
          new Response("SUCCESS", id));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("propertyDeletionFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  /**
   * Retrieves all properties from the database.
   */
  @Override public void getAllProperties()
  {
    try
    {
      List<Property> properties = propertyDAO.readAll();
      support.firePropertyChange("propertyListSuccess", null,
          new Response("SUCCESS", properties));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("propertyListFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  /**
   * Retrieves available properties within a specified date range.
   *
   * @param startDate the start date of the range
   * @param endDate   the end date of the range
   */
  @Override public void getAvailableProperties(Date startDate, Date endDate)
  {
    // Fetch available properties from the database
    List<Property> properties;
    try
    {
      properties = propertyDAO.getAvailableProperties(startDate, endDate);
      support.firePropertyChange("availablePropertiesSuccess", null,
          new Response("SUCCESS", properties));
    }
    catch (SQLException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      support.firePropertyChange("availablePropertiesFailure", null,
          new Response("ERROR", errorResponse));
    }
  }

  /**
   * Adds a PropertyChangeListener to the service.
   *
   * @param listener the PropertyChangeListener to be added
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}