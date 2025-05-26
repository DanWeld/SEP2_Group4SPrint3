package observer;

import java.beans.PropertyChangeListener;

/**
 * Interface for subjects that can notify listeners of property changes.
 * Classes implementing this interface should provide methods to add and remove
 * PropertyChangeListeners.
 */
public interface PropertyChangeSubject
{
  /**
   * Adds a PropertyChangeListener to the subject.
   *
   * @param listener The listener to add
   */
  void addPropertyChangeListener(PropertyChangeListener listener);

  /**
   * Removes a PropertyChangeListener from the subject.
   *
   * @param listener The listener to remove
   */
  void removePropertyChangeListener(PropertyChangeListener listener);
}