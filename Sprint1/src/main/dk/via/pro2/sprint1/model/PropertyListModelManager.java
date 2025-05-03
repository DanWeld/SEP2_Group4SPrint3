package main.dk.via.pro2.sprint1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertyListModelManager implements PropertyListModel {
    private final ArrayList<Property> properties;

    public PropertyListModelManager() {
        this.properties = new ArrayList<>();
        // Initialize with some sample properties
        properties.add(new Property(0, "Vejle", 150.0,
            new Facilities(true, true, true, false, false), true));
        properties.add(new Property(1, "Horsens", 200.0,
            new Facilities(true, true, true, true, true), true));
        properties.add(new Property(2, "Aarhus", 120.0,
            new Facilities(true, true, false, false, false), true));
        properties.add(new Property(3, "Aalborg", 180.0,
            new Facilities(true, true, false, true, true), true));
        properties.add(new Property(4, "Copenhagen", 250.0,
            new Facilities(true, true, true, true, false), true));
    }

    @Override
    public Property getByID(int id) {
        //TODO: Watch out for the difference between id and index (ID does not represent the index of the list)
        return properties.get(id);
    }

    @Override
    public boolean isAvailable(Date startDate, Date endDate, int id) {
        Property property = getByID(id);
        if (property != null) {
            return property.getAvailability();
        }
        return false;
    }

    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }
}