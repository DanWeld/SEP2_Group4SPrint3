package ui.adminBookingHistory;

import dtos.BookingHistory;
import dtos.Response;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class AdminBookingHistoryVM implements PropertyChangeListener
{
  private int propertyID;
  private final StringProperty propertyIDProperty;
  private final StringProperty locationProperty;
  private final StringProperty pricePerNightProperty;

  private final ObservableList<BookingHistory> propertyBookingList;
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

    this.propertyBookingList = FXCollections.observableArrayList();
    this.propertyIDProperty = new SimpleStringProperty("");
    this.locationProperty = new SimpleStringProperty("");
    this.pricePerNightProperty = new SimpleStringProperty("");
    this.errorMessage = new SimpleStringProperty();
    Refresh();
  }

  public ObservableList<BookingHistory> getAllPropertyBookingHistory()
  {
    return propertyBookingList;
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

  public void setPropertyID(int propertyid)
  {
    this.propertyID = propertyid;
    Refresh();
  }

  public StringProperty getErrorMessage()
  {
    return errorMessage;
  }

  public void Refresh()
  {
    propertyBookingList.clear();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    String propertyName = evt.getPropertyName();
    Response response = (Response) evt.getNewValue();

    switch (propertyName)
    {
      case "asd":
      {
        break;
      }
    }
  }
}
