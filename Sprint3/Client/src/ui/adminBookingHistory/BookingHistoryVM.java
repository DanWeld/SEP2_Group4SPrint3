package ui.adminBookingHistory;

import dtos.BookingHistory;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.bookingHistory.PropertyBookingHistoryModel;
import networking.Client;
import networking.bookingHistoryClient.BookingHistoryClient;
import networking.bookingHistoryClient.BookingHistoryClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BookingHistoryVM implements PropertyChangeListener
{
  private int propertyID;                   //no final because user can select any or random id, location properties.
  private final StringProperty propertyIDProperty;
  private final StringProperty locationProperty;
  private final StringProperty pricePerNightProperty;

  private final ObservableList<BookingHistory> propertyBookingHistories;
  private final BookingHistoryClient bookingHistoryClient;
  private final StringProperty errorMessage;

  public BookingHistoryVM()
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

    this.propertyBookingHistories = FXCollections.observableArrayList();
    this.propertyIDProperty = new SimpleStringProperty("");
    this.locationProperty = new SimpleStringProperty("");
    this.pricePerNightProperty = new SimpleStringProperty("");
    this.errorMessage = new SimpleStringProperty();
    //    propertyBookingHistoryModel.addPropertyChangeListener(this);
    Refresh();
  }

  public ObservableList<BookingHistory> getAllPropertyBookingHistory()
  {
    return propertyBookingHistories;
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
  {            //int propertyID
    this.propertyID = propertyid;
    try
    {
      Refresh();
    }
    catch (SQLException e)
    {
      throw new RuntimeException(e);
    }
  }

  public StringProperty getErrorMessage()
  {
    return errorMessage;
  }

  public void Refresh() throws SQLException
  {
    propertyBookingHistories.clear();
    List<BookingHistory> loadedHistories = propertyBookingHistoryModel.getAllBookingHistoryForProperty(
        propertyID);
    // List<Property> propertyList = propertyBookingHistoryModel.getAllProperties();
    if (loadedHistories != null)
    {
      propertyBookingHistories.addAll(loadedHistories);
      if (!propertyBookingHistories.isEmpty())
      {
        propertyIDProperty.set(String.valueOf(
            propertyBookingHistoryModel.getPropertyId(propertyID)));
        locationProperty.set(
            propertyBookingHistoryModel.getLocationOfPropertyId(propertyID));
        pricePerNightProperty.set(String.valueOf(
            propertyBookingHistoryModel.pricePerNightOfPropertyId(propertyID)));
        errorMessage.set(
            "All booking histories of this property are displayed!");
      }
      else if (propertyBookingHistories.isEmpty())
      {      //0, null did not displayed or worked for observableList so we use isEmpty() method.
        errorMessage.set("This property has no booking histories!");
        propertyIDProperty.set(String.valueOf(
            propertyBookingHistoryModel.getPropertyId(propertyID)));
        locationProperty.set(
            propertyBookingHistoryModel.getLocationOfPropertyId(propertyID));
        pricePerNightProperty.set(String.valueOf(
            propertyBookingHistoryModel.pricePerNightOfPropertyId(propertyID)));
        //this id, location pricePerNight will not be displayed because data are not fetched from DB
      }
    }
    System.out.println("Loaded " + propertyBookingHistories.size()
        + " booking histories of propertyId: " + propertyID);
    System.out.println("Click any property to view it's booking histories");
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {

  }

   /* @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()){
            case "getAllBookingHistories":
                propertyBookingHistories.clear();
                List<BookingHistory> bookingHistoryList = (List<BookingHistory>) evt.getNewValue();
                if(bookingHistoryList!=null) {
                    propertyBookingHistories.addAll(bookingHistoryList);
                    errorMessage.set("All booking histories of this property are displayed.");
                }
                else {
                    errorMessage.set("Fail to load booking histories of this property!");
                }
                break;
        }
    }
    */
}
