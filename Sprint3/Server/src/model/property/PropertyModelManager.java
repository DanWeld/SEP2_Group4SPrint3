package model.property;

import dtos.Property;
import services.property.PropertyReader;
import services.property.PropertyWriter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class PropertyModelManager implements PropertyModel, PropertyChangeListener
{
  private final PropertyReader reader;
  private final PropertyWriter writer;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);


  public PropertyModelManager(PropertyReader reader, PropertyWriter writer)
  {
    this.reader = reader;
    this.writer = writer;
    this.writer.addPropertyChangeListener(this);
  }

  public void createProperty(Property p)
  {
    writer.createProperty(p);
  }

  public void updateProperty(Property p)
  {
    writer.updateProperty(p);
  }

  public void deleteProperty(int id)
  {
    writer.deleteProperty(id);
  }

  public void getAllProperties()
  {
    writer.getAllProperties();
  }

  public void getAvailableProperties(Date start, Date end)
  {
    reader.getAvailableProperties(start, end);
  }

  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
