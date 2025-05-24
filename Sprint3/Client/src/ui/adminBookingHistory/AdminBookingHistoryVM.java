package ui.adminBookingHistory;

import dtos.BookingHistory;
import dtos.ErrorResponse;
import dtos.Property;
import dtos.Response;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;
import utils.JsonParser;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.List;

public class AdminBookingHistoryVM implements PropertyChangeListener
{
  private Property property;
  private final StringProperty propertyIDProperty;
  private final StringProperty locationProperty;
  private final StringProperty pricePerNightProperty;

  private final ObservableList<BookingHistory> bookingHistoryList;
  private final BookingHistoryClient bookingHistoryClient;
  private final StringProperty errorMessage;

  public AdminBookingHistoryVM()
  {
    try
    {
      this.bookingHistoryClient = new BookingHistoryClientImpl(new Client());
      bookingHistoryClient.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }

    this.bookingHistoryList = FXCollections.observableArrayList();
    this.propertyIDProperty = new SimpleStringProperty("");
    this.locationProperty = new SimpleStringProperty("");
    this.pricePerNightProperty = new SimpleStringProperty("");
    this.errorMessage = new SimpleStringProperty();
  }

  public ObservableList<BookingHistory> getAllPropertyBookingHistory()
  {
    return bookingHistoryList;
  }

  public StringProperty getPropertyIDProperty()
  {
    return propertyIDProperty;
  }

  public StringProperty getPricePerNightProperty()
  {
    return pricePerNightProperty;
  }

  public StringProperty getLocationProperty()
  {
    return locationProperty;
  }

  public void setProperty(Property property)
  {
    this.property = property;
    refresh();
  }

  public StringProperty getErrorMessage()
  {
    return errorMessage;
  }

  public void refresh()
  {
    System.out.println("Refreshing booking history for property: " + property);
    propertyIDProperty.set(String.valueOf(property.id()));
    locationProperty.set(property.location());
    pricePerNightProperty.set(String.valueOf(property.pricePerNight()));
    int propertyId = property.id();
    bookingHistoryList.clear();
    bookingHistoryClient.getBookingHistory(propertyId);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String propertyName = evt.getPropertyName();

    switch (propertyName)
    {
      case "getAllBookings":
      {
        bookingHistoryList.clear();
        List<BookingHistory> newBookingHistoryList = JsonParser.toList(evt.getNewValue(), BookingHistory[].class);

        bookingHistoryList.addAll(newBookingHistoryList);
        break;
      }
      case "error":
      {
        ErrorResponse errorResponse = (ErrorResponse) evt.getNewValue();
        errorMessage.setValue(errorResponse.errorMessage());
        break;
      }
    }
  }
}
