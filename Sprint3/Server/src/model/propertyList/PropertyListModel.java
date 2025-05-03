package model.propertyList;

import dtos.Property;
import observer.PropertyChangeSubject;

import java.sql.SQLException;
import java.sql.Date;

public interface PropertyListModel extends PropertyChangeSubject
{
    Property getByID(int id);
    void getAvailableProperties(Date startDate, Date endDate) throws SQLException;
}