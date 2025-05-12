package ui.adminPropertyList;

import dtos.Facilities;
import dtos.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel for the Admin Property List view
 */
public class AdminPropertyListVM {
    private final ObservableList<Property> properties;
    private final StringProperty message;
    private Property selectedProperty;
    
    public AdminPropertyListVM() {
        properties = FXCollections.observableArrayList();
        message = new SimpleStringProperty("");
    }
      /**
     * Loads the list of properties
     */
    public void loadProperties() {
        // TODO: Implement fetching properties from server
        // For now, use dummy data
        properties.clear();
        
        // Create facilities for sample properties
        Facilities beachFacilities = new Facilities(true, true, true, false, true);
        Facilities mountainFacilities = new Facilities(true, true, false, false, false);
        Facilities cityFacilities = new Facilities(true, true, false, true, false);
        
        // Add some sample properties
        Property p1 = new Property(1, "Beach House", 150.00, true, beachFacilities);
        Property p2 = new Property(2, "Mountain Cabin", 200.00, true, mountainFacilities);
        Property p3 = new Property(3, "City Apartment", 100.00, true, cityFacilities);
                
        properties.addAll(p1, p2, p3);
    }
    
    /**
     * Adds a new property
     * @param property The property to add
     * @return true if successful, false otherwise
     */
    public boolean addProperty(Property property) {
        if (property == null) {
            message.set("Invalid property data");
            return false;
        }
        
        // TODO: Implement adding property to server
        // For now, just add to local list
        properties.add(property);
        message.set("Property added successfully");
        return true;
    }
    
    /**
     * Updates an existing property
     * @param property The property with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateProperty(Property property) {
        if (property == null) {
            message.set("No property selected");
            return false;
        }
        
        // TODO: Implement updating property on server
        // For now, just update in local list
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).getId() == property.getId()) {
                properties.set(i, property);
                message.set("Property updated successfully");
                return true;
            }
        }
        
        message.set("Property not found");
        return false;
    }
    
    /**
     * Deletes a property
     * @param property The property to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteProperty(Property property) {
        if (property == null) {
            message.set("No property selected");
            return false;
        }
        
        // TODO: Implement deleting property on server
        // For now, just remove from local list
        boolean removed = properties.removeIf(p -> p.getId() == property.getId());
        if (removed) {
            message.set("Property deleted successfully");
            return true;
        } else {
            message.set("Property not found");
            return false;
        }
    }
    
    public ObservableList<Property> getProperties() {
        return properties;
    }
    
    public StringProperty messageProperty() {
        return message;
    }
    
    public void setSelectedProperty(Property property) {
        this.selectedProperty = property;
    }
    
    public Property getSelectedProperty() {
        return selectedProperty;
    }
}
