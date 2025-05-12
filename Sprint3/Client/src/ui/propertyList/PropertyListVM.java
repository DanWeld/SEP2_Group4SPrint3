package ui.propertyList;

import dtos.PropertyList;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import dtos.Property;
import networking.Client;
import networking.propertyListClient.PropertyListClient;
import networking.propertyListClient.PropertyListClientImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Date;

/**
 * ViewModel for the PropertyList view.
 * This class handles the logic for displaying a list of properties.
 *
 * @author Group 4
 * @version 1.0
 */
public class PropertyListVM implements PropertyChangeListener
{
  private ObservableList<Property> properties;
  private IntegerProperty propertyID;
  private SimpleObjectProperty<Property> selectedProperty;
  private StringProperty errorMsg;
  private Date startDate;
  private Date endDate;
  private PropertyListClient propertyListClient;

  /**
   * Constructor for PropertyListVM.
   * Initializes the properties and the client.
   */
  public PropertyListVM()
  {
    this.properties = FXCollections.observableArrayList();
    this.propertyID = new SimpleIntegerProperty(-1);
    this.selectedProperty = new SimpleObjectProperty<>();
    this.errorMsg = new SimpleStringProperty();

    try
    {
      Client client = new Client();
      this.propertyListClient = new PropertyListClientImpl(client);
      client.addPropertyChangeListener(this);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Sets the start and end dates for the property list.
   *
   * @param startDate The start date for the property list.
   * @param endDate The end date for the property list.
   */
  public void setDates(Date startDate, Date endDate)
  {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  /**
   * Gets the list of properties available for booking.
   *
   * @return An ObservableList of Property objects.
   * @throws Exception If an error occurs while getting the properties.
   */
  public ObservableList<Property> getPropertyList() throws Exception
  {
    //Call the client controller to get the properties
    propertyListClient.getAvailableProperties(startDate, endDate);
    return properties;
  }

  /**
   * Gets the property ID.
   *
   * @return An IntegerProperty.
   */
  public IntegerProperty getSelectedIndexProperty()
  {
    return propertyID;
  }

  /**
   * Binds the selected property from the table to the View.
   * @param selectedPropertyFromTable The selected property from the table.
   */
  public void bindSelectedProperty(
      ReadOnlyObjectProperty<Property> selectedPropertyFromTable)
  {
    selectedProperty.bind(selectedPropertyFromTable);
  }

  /**
   * Gets the selected property.
   *
   * @return A SimpleObjectProperty of the selected Property.
   */
  public SimpleObjectProperty<Property> getSelectedProperty()
  {
    errorMsg.set("");
    if (selectedProperty.getValue() == null)
    {
      errorMsg.set("No property selected");
    }
    return selectedProperty;
  }

  public StringProperty getErrorMsgProperty()
  {
    return errorMsg;
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    if (evt.getPropertyName().equals("getAllProperties"))
    {
      properties.clear();
      PropertyList propertyArray = (PropertyList) evt.getNewValue();
      for (int i = 0; i < propertyArray.size(); i++)
      {
        Property property = propertyArray.getProperty(i);
        properties.add(property);
      }
    }

    else if (evt.getPropertyName().equals("getPropertyByID"))
    {
      selectedProperty.set((Property) evt.getNewValue());
    }
  }
}