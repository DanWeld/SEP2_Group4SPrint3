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

/**
 * Manages booking operations such as creation, availability checks, extension, and deletion.
 * Handles concurrency using a reader-writer lock and notifies listeners of operation results
 * via property change events. Delegates data access to the BookingDAO.
 *
 * <p>Fires property change events with a {@link dtos.Response} object indicating success or failure.</p>
 *
 * @author Group 4
 * @version 1.0
 */
public class BookingModelManager implements BookingModel, PropertyChangeSubject
{
  private final PropertyChangeSupport support;
  BookingDAO bookingDAO;
  private ReaderWriterLock lock = new PriorityWriterLockImpl();

  /**
   * Constructs a BookingModelManager with the specified BookingDAO.
   *
   * @param bookingDAO the DAO used for booking operations
   */
  public BookingModelManager(BookingDAO bookingDAO)
  {
    this.bookingDAO = bookingDAO;
    this.support = new PropertyChangeSupport(this);
  }

  /**
   * Creates a new booking for a property and notifies listeners of the result.
   *
   * @param propertyID the ID of the property to book
   * @param startDate the start date of the booking
   * @param endDate the end date of the booking
   * @param username the username of the person booking
   */
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

  /**
   * Checks if a property is available for booking within the specified date range.
   *
   * @param startDate the start date of the booking
   * @param endDate the end date of the booking
   * @param propertyId the ID of the property to check
   */
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

  /**
   * Extends an existing booking for a property and notifies listeners of the result.
   *
   * @param propertyId the ID of the property to extend the booking for
   * @param startDate the start date of the booking
   * @param newEndDate the new end date of the booking
   * @param username the username of the person extending the booking
   */
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

  /**
   * Deletes a booking for a property and notifies listeners of the result.
   *
   * @param startDate the start date of the booking to delete
   * @param propertyId the ID of the property to delete the booking for
   * @param username the username of the person who made the booking
   */
  @Override public void deleteBooking(Date startDate, int propertyId,
      String username)
  {
    try
    {
      lock.lockWrite();
      bookingDAO.delete(startDate, propertyId, username);
      Response response = new Response("SUCCESS", null);
      support.firePropertyChange("bookingDeletionSuccess", null, response);
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

  /**
   * Adds a property change listener to this model.
   *
   * @param listener the listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}
