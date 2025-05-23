package ui.adminPropertyList;

import dtos.Property;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.bookingHistory.PropertyBookingHistoryModel;
import persistence.daos.AdminPanel.PropertyBookingHistoryDAO;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;

/**
 * ViewModel for the Admin Property List view
 */
public class AdminPropertyListVM implements PropertyChangeListener {
    private final ObservableList<Property> properties;
    private SimpleObjectProperty<Property> selectedProperty;
    private IntegerProperty selectedPropertyId;          //for any selected property.
    private StringProperty selectedPropertyLocation;
    private DoubleProperty selectedPropertyPricePerNight;
    private StringProperty errorMessage;
    private final PropertyBookingHistoryModel propertyBookingHistoryModel;

    public AdminPropertyListVM(PropertyBookingHistoryModel model) throws SQLException{
        this.propertyBookingHistoryModel = model;
        this.properties = FXCollections.observableArrayList();
        this.selectedProperty = new SimpleObjectProperty<>();
        this.selectedPropertyId = new SimpleIntegerProperty(0);
        this.selectedPropertyLocation = new SimpleStringProperty("");
        this.selectedPropertyPricePerNight = new SimpleDoubleProperty(0.00);

        this.errorMessage = new SimpleStringProperty();
        model.addPropertyChangeListener(this);
        Refresh();
        }

/*    private void loadAllAdminSideProperties() {
        List<Property> propertyList = propertyBookingHistoryModel.getAllProperties();
        properties.setAll(propertyList);
    }
 */


    public ObservableList<Property> getAllPropertiesList() {
        return properties;
    }
    public ObjectProperty<Property> getSelectedProperty(){
        return selectedProperty;
    }
    public void bindSelectedProperty(ReadOnlyObjectProperty<Property> selectedFromTable){
        selectedProperty.bind(selectedFromTable);
        // Update selectedPropertyId when selectedProperty changes
        selectedProperty.addListener((obs, oldValue, newValue) -> {
            selectedPropertyId.set(newValue != null ? newValue.id() : 0);
            selectedPropertyLocation.set(newValue!=null? newValue.location() : "");
            selectedPropertyPricePerNight.set(newValue!=null? newValue.pricePerNight() : 0.00);
        });
    }
 //  public int getSelectedPropertyId(){
   //     return selectedPropertyId.get();
   //}
  //  public Property getSelectedProperty(){
    // return selectedProperty.get();
    //}

    public IntegerProperty selectedPropertyIdProperty(){
        return selectedPropertyId;
    }
    public StringProperty selectedPropertyLocationProperty(){
        return selectedPropertyLocation;
    }
     public DoubleProperty SelectedPropertyPricePerNightProperty(){return selectedPropertyPricePerNight;}
    public StringProperty messageProperty() {
        return errorMessage;
    }

    public void Refresh() throws SQLException {
        this.properties.clear();
        List<Property> loadedProperties = propertyBookingHistoryModel.getAllProperties();
        if (loadedProperties != null) {
            properties.addAll(loadedProperties);
        }
        else {
            errorMessage.set("No properties found or failed to load data.");
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "getAllProperties":
                properties.clear();
                List<Property> propertyList = (List<Property>) evt.getNewValue();
                if(propertyList!=null){
                    properties.addAll(propertyList);
                    errorMessage.set("All properties are displayed!");
                }
                else {
                    errorMessage.set("Failed to fetch properties!");
                }
                break;

            case "getAllUpdatedProperties":
                properties.clear();
                List<Property> newList = (List<Property>) evt.getNewValue();
                if (newList != null) {
                    properties.addAll(newList);
                    errorMessage.set("Property list updated!");
                } else {
                    errorMessage.set("Failed to fetch updated property list.");
                }
                break;
            // You can handle other event types here if needed
        }
    }
    public void size(){
        errorMessage.set("System.out.println(\"Properties fetched: "+properties.size());
    }
}