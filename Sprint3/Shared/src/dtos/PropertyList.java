package dtos;

import java.util.ArrayList;

public class PropertyList
{
  private ArrayList<Property> properties;

  public PropertyList()
  {
    properties = new ArrayList<>();
  }

  public void addProperty(Property property)
  {
    properties.add(property);
  }

  public int size()
  {
    return properties.size();
  }

  public Property getProperty(int index)
  {
    return properties.get(index);
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    for (Property property : properties)
    {
      sb.append(property.toString()).append("\n");
    }
    return sb.toString();
  }

  public PropertyList getProperties()
  {
    return this;
  }

  public void setProperties(ArrayList<Property> properties)
  {
    this.properties = properties;
  }
}
