package model.property;

import dtos.Property;
import services.property.PropertyCustomerPrivileges;
import services.property.PropertyAdminPrivileges;
import utilities.readerWriterLock.ReaderWriterLock;
import utilities.readerWriterLock.PriorityReaderLockImpl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Date;

public class PropertyModelManager
    implements PropertyModel, PropertyChangeListener
{
  private final PropertyCustomerPrivileges customer;
  private final PropertyAdminPrivileges admin;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final ReaderWriterLock lock = new PriorityReaderLockImpl();

  public PropertyModelManager(PropertyCustomerPrivileges customer,
      PropertyAdminPrivileges admin)
  {
    this.customer = customer;
    this.admin = admin;
    this.admin.addPropertyChangeListener(this);
  }

  public void createProperty(Property p)
  {
    try
    {
      lock.lockWrite();
      admin.createProperty(p);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  public void updateProperty(Property p)
  {
    try
    {
      lock.lockWrite();
      admin.updateProperty(p);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  public void deleteProperty(int id)
  {
    try
    {
      lock.lockWrite();
      admin.deleteProperty(id);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    finally
    {
      lock.unlockWrite();
    }
  }

  public void getAllProperties()
  {
    try
    {
      lock.lockRead();
      admin.getAllProperties();
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    finally
    {
      lock.unlockRead();
    }
  }

  public void getAvailableProperties(Date start, Date end)
  {
    try
    {
      lock.lockRead();
      customer.getAvailableProperties(start, end);
    }
    catch (InterruptedException e)
    {
      Thread.currentThread().interrupt();
    }
    finally
    {
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
