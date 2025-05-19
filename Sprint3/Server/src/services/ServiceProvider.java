package services;

import dtos.User;
import model.booking.BookingModel;
import model.booking.BookingModelManager;
import model.bookingHistory.BookingHistoryModel;
import model.bookingHistory.BookingHistoryModelManager;
import model.property.PropertyModel;
import model.property.PropertyModelManager;
import model.user.UserModel;
import model.user.UserModelManager;
import networking.requestHandlers.*;
import persistence.daos.bookings.BookingDAO;
import persistence.daos.bookings.BookingDAOImpl;
import persistence.daos.properties.PropertyDAO;
import persistence.daos.properties.PropertyDAOImpl;
import persistence.daos.user.UserDAO;
import persistence.daos.user.UserDAOImpl;
import services.bookingHistory.BookingHistoryAdminPrivileges;
import services.bookingHistory.BookingHistoryCustomerPrivileges;
import services.bookingHistory.BookingHistoryService;
import services.bookingHistory.BookingHistoryServiceImpl;
import services.bookingHistory.security.AdminBookingHistoryProxy;
import services.property.PropertyCustomerPrivileges;
import services.property.PropertyService;
import services.property.PropertyServiceImpl;
import services.property.PropertyAdminPrivileges;
import services.property.security.AdminPropertyProxy;
import services.user.UserAdminPrivileges;
import services.user.UserCustomerPrivileges;
import services.user.UserService;
import services.user.UserServiceImpl;
import services.user.security.AdminUserProxy;
import utilities.logging.ConsoleLogger;
import utilities.logging.LogLevel;
import utilities.logging.Logger;
import services.authentication.AuthenticationService;
import services.authentication.AuthenticationServiceImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceProvider
{
  private Logger logger;
  private User user;

  public List<RequestHandler> getAllHandlers()
  {
    List<RequestHandler> handlers = new ArrayList<>();
    handlers.add(getAuthenticationRequestHandler());
    handlers.add(getPropertyRequestHandler(user));
    handlers.add(getBookingRequestHandler());
    handlers.add(getBookingHistoryRequestHandler(user));
    handlers.add(getUserRequestHandler());
    return handlers;
  }

  // ------------------ Request Handlers ------------------

  public RequestHandler getAuthenticationRequestHandler()
  {
    return new AuthenticationRequestHandler(getAuthenticationService(),
        getLogger());
  }

  public RequestHandler getPropertyRequestHandler(User user)
  {
    return new PropertyRequestHandler(getPropertyModel(user), getLogger());
  }

  public RequestHandler getBookingRequestHandler()
  {
    return new BookingRequestHandler(getBookingModel(), getLogger());
  }

  public RequestHandler getBookingHistoryRequestHandler(User user)
  {
    return new BookingHistoryRequestHandler(getBookingHistoryModel(user),
        getLogger());
  }

  public RequestHandler getUserRequestHandler()
  {
    return new UserRequestHandler(getUserModel(), getLogger());
  }

  // ------------------ Models ------------------

  private PropertyModel getPropertyModel(User user)
  {
    return new PropertyModelManager((PropertyCustomerPrivileges) getPropertyService(),
        getAdminPropertyWriter(user));
  }

  private BookingModel getBookingModel()
  {
    return new BookingModelManager(gettBookingDAO());
  }

  private BookingHistoryModel getBookingHistoryModel(User user)
  {
    return new BookingHistoryModelManager(getAdminBookingHistoryService(user),
        (BookingHistoryCustomerPrivileges) getBookingHistoryService());
  }

  private UserModel getUserModel()
  {
    return new UserModelManager((UserCustomerPrivileges) getUserService(),
        getAdminUserService(user));
  }

  // ------------------ Services ------------------

  private PropertyService getPropertyService()
  {
    return new PropertyServiceImpl(getPropertyDAO());
  }

  private PropertyAdminPrivileges getAdminPropertyWriter(User user)
  {
    return new AdminPropertyProxy((PropertyAdminPrivileges) getPropertyService(),
        user);
  }

  private AuthenticationService getAuthenticationService()
  {
    return new AuthenticationServiceImpl(getUserDAO());
  }

  private BookingHistoryAdminPrivileges getAdminBookingHistoryService(User user)
  {
    return new AdminBookingHistoryProxy(
        (BookingHistoryAdminPrivileges) getBookingHistoryService(), user);
  }

  private BookingHistoryService getBookingHistoryService()
  {
    return new BookingHistoryServiceImpl(gettBookingDAO());
  }

  private UserService getUserService()
  {
    return new UserServiceImpl(getUserDAO());
  }

  private UserAdminPrivileges getAdminUserService(User user)
  {
    return new AdminUserProxy((UserAdminPrivileges) getUserService(), user);
  }

  // ------------------ DAOs ------------------

  private PropertyDAO getPropertyDAO()
  {
    try
    {
      PropertyDAO propertyDAO = PropertyDAOImpl.getInstance();
      return propertyDAO;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  private UserDAO getUserDAO()
  {
    try
    {
      UserDAO userDAO = UserDAOImpl.getInstance();
      return userDAO;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  private BookingDAO gettBookingDAO()
  {
    try
    {
      BookingDAO bookingDAO = BookingDAOImpl.getInstance();
      return bookingDAO;
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  // ------------------ Logging ------------------

  public Logger getLogger()
  {
    if (logger == null)
    {
      logger = new ConsoleLogger(LogLevel.INFO);
    }
    return logger;
  }

  // ------------------ User ------------------
  public void setUser(User user)
  {
    this.user = user;
  }
}
