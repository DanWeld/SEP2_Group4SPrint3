package model.bookingHistory;

import dtos.Booking;
import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.Response;
import persistence.daos.bookings.BookingDAO;
import services.bookingHistory.BookingHistoryAdminPrivileges;
import services.bookingHistory.BookingHistoryCustomerPrivileges;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryModelManager implements BookingHistoryModel,
    PropertyChangeListener
{
  private BookingHistoryAdminPrivileges admin;
  private BookingHistoryCustomerPrivileges customer;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public BookingHistoryModelManager(BookingHistoryAdminPrivileges admin,
      BookingHistoryCustomerPrivileges customer)
  {
    this.admin = admin;
    this.customer = customer;
    this.admin.addPropertyChangeListener(this);
    this.customer.addPropertyChangeListener(this);
  }

  @Override
  public void getPastBookings(String username)
  {
    customer.getPastBookings(username);
  }

  @Override
  public void getCurrentBookings(String username)
  {
    customer.getCurrentBookings(username);
  }

  @Override
  public void getFutureBookings(String username)
  {
    customer.getFutureBookings(username);
  }

  @Override
  public void getAllBookings(int propertyId)
  {
    admin.getAllBookings(propertyId);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
