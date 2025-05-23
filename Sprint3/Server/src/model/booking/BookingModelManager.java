package model.booking;

import dtos.Booking;
import dtos.ErrorResponse;
import dtos.Response;
import persistence.daos.bookings.BookingDAO;
import observer.PropertyChangeSubject;
import utilities.readerWriterLock.PriorityWriterLockImpl;
import utilities.readerWriterLock.ReaderWriterLock;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;
import java.sql.SQLException;

public class BookingModelManager implements BookingModel, PropertyChangeSubject
{
  private final PropertyChangeSupport support;
  BookingDAO bookingDAO;
  private ReaderWriterLock lock = new PriorityWriterLockImpl();

  public BookingModelManager(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    this.support = new PropertyChangeSupport(this);
  }

  @Override public void createBooking(int propertyID, Date startDate,
      Date endDate, String username)
  {
    try
    {
      lock.lockWrite();
      Booking newBooking = bookingDAO.create(startDate, endDate, propertyID,
          username);
      Response response = new Response("SUCCESS", newBooking);
      support.firePropertyChange("bookingCreationSuccess", null, response);
    }
    catch (SQLException | InterruptedException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      support.firePropertyChange("bookingCreationFailure", null, response);
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  @Override public void isAvailable(Date startDate, Date endDate,
      int propertyId)
  {
    try
    {
      lock.lockRead();
      boolean isAvailable = bookingDAO.isAvailable(startDate, endDate,
          propertyId);
      Response response = new Response("SUCCESS", isAvailable);
      support.firePropertyChange("isAvailableSuccess", null, response);
    }
    catch (SQLException | InterruptedException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      support.firePropertyChange("isAvailableFailure", null, response);
    }
    finally
    {
      lock.unlockRead();
    }
  }

  @Override public void extendBooking(int propertyId, Date startDate,
      Date newEndDate, String username)
  {
    try
    {
      lock.lockWrite();
      Booking updatedBooking = bookingDAO.update(startDate, newEndDate,
          propertyId, username);
      Response response = new Response("SUCCESS", updatedBooking);
      support.firePropertyChange("bookingExtensionSuccess", null, response);
    }
    catch (SQLException | InterruptedException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      support.firePropertyChange("bookingExtensionFailure", null, response);
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  @Override public void deleteBooking(Date startDate, int propertyId,
      String username)
  {
    try
    {
      lock.lockWrite();
      bookingDAO.delete(startDate, propertyId, username);
      System.out.println("Booking deleted successfully");
      Response response = new Response("SUCCESS", null);
      support.firePropertyChange("bookingDeletionSuccess", null, response);
      System.out.println("Booking deletion success response sent" + response);
    }
    catch (SQLException | InterruptedException e)
    {
      ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
      Response response = new Response("ERROR", errorResponse);
      support.firePropertyChange("bookingDeletionFailure", null, response);
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
