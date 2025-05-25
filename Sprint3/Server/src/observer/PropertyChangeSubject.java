package observer;

import java.beans.PropertyChangeListener;

/**
 * Interface for subjects that can notify listeners about property changes.
 * Classes implementing this interface should provide a way to add property change listeners.
 */
public interface PropertyChangeSubject
{
  void addPropertyChangeListener(PropertyChangeListener listener);
}