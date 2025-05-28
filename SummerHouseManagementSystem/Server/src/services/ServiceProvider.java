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

/**
 * ServiceProvider is a factory class that provides various request handlers
 * and models for the application. It encapsulates the creation of services,
 * DAOs, and request handlers, allowing for easy access and management of
 * application components.
 */
public class ServiceProvider
{
  private Logger logger;
  private User user;

  /**
   * Returns a singleton instance of ServiceProvider.
   *
   * @return the singleton instance of ServiceProvider
   */
  public List<RequestHandler> getAllHandlers()
  {
    List<RequestHandler> handlers = new ArrayList<>();
    handlers.add(getAuthenticationRequestHandler());
    handlers.add(getPropertyRequestHandler(user));
    handlers.add(getBookingRequestHandler());
    System.out.println("User: " + user);
    handlers.add(getBookingHistoryRequestHandler(user));
    handlers.add(getUserRequestHandler());
    return handlers;
  }

  // ------------------ Request Handlers ------------------

  /**
   * Returns a RequestHandler for authentication requests.
   *
   * @return a RequestHandler for authentication requests
   */
  public RequestHandler getAuthenticationRequestHandler()
  {
    return new AuthenticationRequestHandler(getAuthenticationService(),
        getLogger());
  }

  /**
   * Returns a RequestHandler for property requests.
   *
   * @param user the user making the request
   * @return a RequestHandler for property requests
   */
  public RequestHandler getPropertyRequestHandler(User user)
  {
    return new PropertyRequestHandler(getPropertyModel(user), getLogger());
  }

  /**
   * Returns a RequestHandler for booking requests.
   *
   * @return a RequestHandler for booking requests
   */
  public RequestHandler getBookingRequestHandler()
  {
    return new BookingRequestHandler(getBookingModel(), getLogger());
  }

  /**
   * Returns a RequestHandler for booking history requests.
   *
   * @param user the user making the request
   * @return a RequestHandler for booking history requests
   */
  public RequestHandler getBookingHistoryRequestHandler(User user)
  {
    return new BookingHistoryRequestHandler(getBookingHistoryModel(user),
        getLogger());
  }

  /**
   * Returns a RequestHandler for user requests.
   *
   * @return a RequestHandler for user requests
   */
  public RequestHandler getUserRequestHandler()
  {
    return new UserRequestHandler(getUserModel(), getLogger());
  }

  // ------------------ Models ------------------

  /**
   * Returns a PropertyModel for managing properties.
   *
   * @param user the user making the request
   * @return a PropertyModel for managing properties
   */
  private PropertyModel getPropertyModel(User user)
  {
    return new PropertyModelManager((PropertyCustomerPrivileges) getPropertyService(),
        getAdminPropertyWriter(user));
  }

  /**
   * Returns a BookingModel for managing bookings.
   *
   * @return a BookingModel for managing bookings
   */
  private BookingModel getBookingModel()
  {
    return new BookingModelManager(gettBookingDAO());
  }

  /**
   * Returns a BookingHistoryModel for managing booking history.
   *
   * @param user the user making the request
   * @return a BookingHistoryModel for managing booking history
   */
  private BookingHistoryModel getBookingHistoryModel(User user)
  {
    return new BookingHistoryModelManager(getAdminBookingHistoryService(user),
        (BookingHistoryCustomerPrivileges) getBookingHistoryService());
  }

  /**
   * Returns a UserModel for managing users.
   *
   * @return a UserModel for managing users
   */
  private UserModel getUserModel()
  {
    return new UserModelManager((UserCustomerPrivileges) getUserService(),
        getAdminUserService(user));
  }

  // ------------------ Services ------------------

  /**
   * Returns a PropertyService for managing properties.
   *
   * @return a PropertyService for managing properties
   */
  private PropertyService getPropertyService()
  {
    return new PropertyServiceImpl(getPropertyDAO());
  }

  /**
   * Returns a PropertyAdminPrivileges for managing properties with admin privileges.
   *
   * @param user the user making the request
   * @return a PropertyAdminPrivileges for managing properties with admin privileges
   */
  private PropertyAdminPrivileges getAdminPropertyWriter(User user)
  {
    return new AdminPropertyProxy((PropertyAdminPrivileges) getPropertyService(),
        user);
  }

  /**
   * Returns an AuthenticationService for managing authentication.
   *
   * @return an AuthenticationService for managing authentication
   */
  private AuthenticationService getAuthenticationService()
  {
    return new AuthenticationServiceImpl(getUserDAO());
  }

  /**
   * Returns a BookingHistoryAdminPrivileges for managing booking history with admin privileges.
   *
   * @param user the user making the request
   * @return a BookingHistoryAdminPrivileges for managing booking history with admin privileges
   */
  private BookingHistoryAdminPrivileges getAdminBookingHistoryService(User user)
  {
    return new AdminBookingHistoryProxy(
        (BookingHistoryAdminPrivileges) getBookingHistoryService(), user);
  }

  /**
   * Returns a BookingHistoryService for managing booking history.
   *
   * @return a BookingHistoryService for managing booking history
   */
  private BookingHistoryService getBookingHistoryService()
  {
    return new BookingHistoryServiceImpl(gettBookingDAO());
  }

  /**
   * Returns a UserService for managing users.
   *
   * @return a UserService for managing users
   */
  private UserService getUserService()
  {
    return new UserServiceImpl(getUserDAO());
  }

  /**
   * Returns a UserAdminPrivileges for managing users with admin privileges.
   *
   * @param user the user making the request
   * @return a UserAdminPrivileges for managing users with admin privileges
   */
  private UserAdminPrivileges getAdminUserService(User user)
  {
    return new AdminUserProxy((UserAdminPrivileges) getUserService(), user);
  }

  // ------------------ DAOs ------------------

  /**
   * Returns a PropertyDAO for managing properties.
   *
   * @return a PropertyDAO for managing properties
   */
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

  /**
   * Returns a UserDAO for managing users.
   *
   * @return a UserDAO for managing users
   */
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

  /**
   * Returns a BookingDAO for managing bookings.
   *
   * @return a BookingDAO for managing bookings
   */
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

  /**
   * Returns a Logger for logging messages.
   *
   * @return a Logger for logging messages
   */
  public Logger getLogger()
  {
    if (logger == null)
    {
      logger = new ConsoleLogger(LogLevel.INFO);
    }
    return logger;
  }

  // ------------------ User ------------------

  /**
   * Returns the current user.
   *
   * @return the current user
   */
  public void setUser(User user)
  {
    this.user = user;
  }
}
