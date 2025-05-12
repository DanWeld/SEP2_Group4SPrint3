package Client.src.ui.propertyList;

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

public class PropertyListVM implements PropertyChangeListener
{
  private ObservableList<Property> properties;
  private IntegerProperty propertyID;
  private SimpleObjectProperty<Property> selectedProperty;
  private StringProperty errorMsg;
  private Date startDate;
  private Date endDate;
  private PropertyListClient propertyListClient;

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

  public void setDates(Date startDate, Date endDate)
  {
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public ObservableList<Property> getPropertyList() throws Exception
  {
    //Call the client controller to get the properties
    propertyListClient.getAvailableProperties(startDate, endDate);
    return properties;
  }

  public IntegerProperty getSelectedIndexProperty()
  {
    return propertyID;
  }

  public void bindSelectedProperty(
      ReadOnlyObjectProperty<Property> selectedPropertyFromTable)
  {
    selectedProperty.bind(selectedPropertyFromTable);
  }

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