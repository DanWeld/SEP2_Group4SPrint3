package services;

import dtos.User;
import model.property.PropertyModel;
import model.property.PropertyModelManager;
import networking.requestHandlers.PropertyRequestHandler;
import networking.requestHandlers.AuthenticationRequestHandler;
import networking.requestHandlers.RequestHandler;
import persistence.daos.properties.PropertyDAO;
import persistence.daos.properties.PropertyDAOImpl;
import persistence.daos.user.UserDAO;
import persistence.daos.user.UserDAOImpl;
import services.property.PropertyReader;
import services.property.PropertyService;
import services.property.PropertyServiceImpl;
import services.property.PropertyWriter;
import services.property.security.AdminPropertyWriterProxy;
import utilities.logging.FileLogger;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import model.authentication.AuthenticationService;
import model.authentication.AuthenticationServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceProvider
{

  // Logging
  private Logger logger;

  // DAOs
  private PropertyDAO propertyDAO;
  private UserDAO userDAO;

  // Services
  private PropertyService propertyService;
  private AuthenticationService authenticationService;

  // Request Handlers
  private PropertyRequestHandler propertyRequestHandler;
  private AuthenticationRequestHandler authenticationRequestHandler;
  private User user;

  public List<RequestHandler> getAllHandlers()
  {
    List<RequestHandler> handlers = new ArrayList<>();
    handlers.add(getAuthenticationRequestHandler());
    handlers.add(getPropertyRequestHandler(user));
    // Add more handlers here in future
    return handlers;
  }

  // ------------------ Request Handlers ------------------

  public RequestHandler getAuthenticationRequestHandler()
  {
    if (authenticationRequestHandler == null)
    {
      authenticationRequestHandler = new AuthenticationRequestHandler(
          getAuthenticationService());
    }
    return authenticationRequestHandler;
  }

  public RequestHandler getPropertyRequestHandler(User user)
  {
    if (propertyRequestHandler == null)
    {
      propertyRequestHandler = new PropertyRequestHandler(getPropertyModel(user));
    }
    return propertyRequestHandler;
  }

  // ------------------ Services ------------------

  private PropertyModel getPropertyModel(User user)
  {
    return new PropertyModelManager((PropertyReader) getPropertyService(), getAdminWriter(user));
  }

  public PropertyService getPropertyService()
  {
    if (propertyService == null)
    {
      propertyService = new PropertyServiceImpl(getPropertyDAO());
    }
    return propertyService;
  }

  public AuthenticationService getAuthenticationService()
  {
    if (authenticationService == null)
    {
      authenticationService = new AuthenticationServiceImpl(getUserDAO());
    }
    return authenticationService;
  }

  public PropertyWriter getAdminWriter(User user)
  {
    return new AdminPropertyWriterProxy((PropertyWriter) getPropertyService(),
        user);
  }

  // ------------------ DAOs ------------------

  private PropertyDAO getPropertyDAO()
  {
    if (propertyDAO == null)
    {
      try
      {
        propertyDAO = PropertyDAOImpl.getInstance();
      }
      catch (SQLException e)
      {
        throw new RuntimeException("Failed to create PropertyDAO", e);
      }
    }
    return propertyDAO;
  }

  private UserDAO getUserDAO()
  {
    if (userDAO == null)
    {
      try
      {
        userDAO = UserDAOImpl.getInstance();
      }
      catch (SQLException e)
      {
        throw new RuntimeException("Failed to create UserDAO", e);
      }
    }
    return userDAO;
  }

  // ------------------ Logging ------------------

  public Logger getLogger()
  {
    if (logger == null)
    {
      logger = new FileLogger(LogLevel.INFO, "logs.txt");
    }
    return logger;
  }


  // ------------------ User ------------------
  public void setUser(User user) {
    this.user = user;
  }
}
