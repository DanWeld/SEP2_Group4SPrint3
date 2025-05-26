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

/**
 * The PropertyModelManager class implements the PropertyModel interface and manages
 * property-related operations such as creating, updating, deleting, and retrieving properties.
 * It delegates administrative and customer operations to the respective privilege services.
 * This class also listens for property change events and notifies registered listeners.
 * A reader-writer lock is used to ensure thread-safe access to property data.
 *
 * @author Group4
 * @version 1.0
 */
public class PropertyModelManager
    implements PropertyModel, PropertyChangeListener
{
  private final PropertyCustomerPrivileges customer;
  private final PropertyAdminPrivileges admin;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private final ReaderWriterLock lock = new PriorityReaderLockImpl();

  /**
   * Constructs a PropertyModelManager with the specified customer and admin privileges.
   *
   * @param customer the customer privileges service
   * @param admin    the admin privileges service
   */
  public PropertyModelManager(PropertyCustomerPrivileges customer,
      PropertyAdminPrivileges admin)
  {
    this.customer = customer;
    this.admin = admin;
    this.admin.addPropertyChangeListener(this);
    this.customer.addPropertyChangeListener(this);
  }

  /**
   * Creates a new property.
   *
   * @param p the property to create
   */
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

  /**
   * Updates an existing property.
   *
   * @param p the property to update
   */
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

  /**
   * Deletes a property by its ID.
   *
   * @param id the ID of the property to delete
   */
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

  /**
   * Retrieves all properties.
   */
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

  /**
   * Retrieves properties available within a specified date range.
   *
   * @param start the start date of the range
   * @param end   the end date of the range
   */
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

  /**
   * Adds a property change listener to this model manager.
   *
   * @param listener the listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Notifies all registered listeners of a property change event.
   *
   * @param evt the property change event to notify listeners about
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }
}
