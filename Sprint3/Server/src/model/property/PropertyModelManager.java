package model.property;

import dtos.Property;
import services.property.PropertyReader;
import services.property.PropertyWriter;
import util.ReaderWriterLock;
import util.ReaderWriterLockImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class PropertyModelManager implements PropertyModel, PropertyChangeListener
{
  private final PropertyReader reader;
  private final PropertyWriter writer;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final ReaderWriterLock lock = new ReaderWriterLockImpl();

  public PropertyModelManager(PropertyReader reader, PropertyWriter writer)
  {
    this.reader = reader;
    this.writer = writer;
    this.writer.addPropertyChangeListener(this);
  }

  public void createProperty(Property p)
  {
    try {
      lock.lockWrite();
      writer.createProperty(p);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockWrite();
    }
  }

  public void updateProperty(Property p)
  {
    try {
      lock.lockWrite();
      writer.updateProperty(p);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockWrite();
    }
  }

  public void deleteProperty(int id)
  {
    try {
      lock.lockWrite();
      writer.deleteProperty(id);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockWrite();
    }
  }

  public void getAllProperties()
  {
    try {
      lock.lockRead();
      writer.getAllProperties();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockRead();
    }
  }

  public void getAvailableProperties(Date start, Date end)
  {
    try {
      lock.lockRead();
      reader.getAvailableProperties(start, end);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlockRead();
    }
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
