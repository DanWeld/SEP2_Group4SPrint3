package networking.propertyClient;

import dtos.Property;
import dtos.Request;
import networking.ClientSocket;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Implementation of PropertyClient for handling property operations
 * and communication with the server.
 * This class listens for
 * property changes from the client
 * and forwards them to registered listeners.
 *
 * @author Group 4
 * @version 1.0
 */
public class PropertyClientImpl
    implements PropertyClient, PropertyChangeListener
{
  private ClientSocket client;
  private PropertyChangeSupport support;

  /**
   * Constructor for PropertyClientImpl
   * Initializes the client and adds a property change listener.
   *
   * @param client The client used for communication with the server
   */
  public PropertyClientImpl(ClientSocket client)
  {
    this.client = client;
    client.addPropertyChangeListener(this);
    support = new PropertyChangeSupport(this);
  }

  /**
   * Create a new property.
   *
   * @param property the property to create
   */
  @Override public void createProperty(Property property)
  {
    client.sendRequest(new Request("property", "create", property));
  }

  /**
   * Update an existing property.
   *
   * @param property the property to update
   */
  @Override public void updateProperty(Property property)
  {
    client.sendRequest(new Request("property", "update", property));
  }

  /**
   * Delete a property by its ID.
   *
   * @param propertyId the ID of the property to delete
   */
  @Override public void deleteProperty(int propertyId)
  {
    client.sendRequest(new Request("property", "delete", propertyId));
  }

  /**
   * Handles property change events and forwards them to registered listeners.
   *
   * @param evt the property change event
   */
  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    support.firePropertyChange(evt);
  }

  /**
   * Adds a property change listener to this client.
   *
   * @param listener the listener to add
   */
  @Override public void addPropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(listener);
  }

  /**
   * Removes a property change listener from this client.
   *
   * @param listener the listener to remove
   */
  @Override public void removePropertyChangeListener(
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(listener);
  }
}
