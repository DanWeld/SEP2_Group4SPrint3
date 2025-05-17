package services.property;

import dtos.Property;
import observer.PropertyChangeSubject;

import java.sql.Date;

public interface PropertyService extends PropertyChangeSubject
{
    void createProperty(Property property);
    void updateProperty(Property property);
    void deleteProperty(int id);
    void getAllProperties();
    void getAvailableProperties(Date startDate, Date endDate);
}