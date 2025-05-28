package networking.propertyClient;

import dtos.Property;
import observer.PropertyChangeSubject;

/**
 * Interface for PropertyClient, which handles property-related operations.
 * It extends PropertyChangeSubject to allow for property change notifications.
 * This interface defines methods for creating, updating, and deleting properties.
 *
 * @author Group 4
 * @version 1.0
 */
public interface PropertyClient extends PropertyChangeSubject
{
  /**
   * Create a new property.
   *
   * @param property the property to create
   */
  void createProperty(Property property);

  /**
   * Update an existing property.
   *
   * @param property the property to update
   */
  void updateProperty(Property property);

  /**
   * Delete a property by its ID.
   *
   * @param propertyId the ID of the property to delete
   */
  void deleteProperty(int propertyId);
}
