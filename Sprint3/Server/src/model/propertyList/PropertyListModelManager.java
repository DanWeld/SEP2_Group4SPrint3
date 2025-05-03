package model.propertyList;

import dtos.PropertyList;
import persistence.daos.properties.PropertyDAO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;

import dtos.Property;

public class PropertyListModelManager implements PropertyListModel
{
  private ArrayList<Property> properties = new ArrayList<>();
  private PropertyDAO propertyDAO;
  private PropertyChangeSupport support;

  public PropertyListModelManager(PropertyDAO propertyDAO)
  {
    this.propertyDAO = propertyDAO;
    this.support = new PropertyChangeSupport(this);
  }

  @Override public Property getByID(int id)
  {
    //TODO: Watch out for the difference between id and index (ID does not represent the index of the list)
    return properties.get(id);
  }

  public void getAvailableProperties(Date startDate, Date endDate) throws SQLException
  {
    properties = (ArrayList<Property>) propertyDAO.getAvailableProperties(startDate, endDate);
    PropertyList propertyList = new PropertyList();
    propertyList.setProperties(properties);

    // Notify listeners about the change in the property list
    support.firePropertyChange("propertyList", null, propertyList);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }
}