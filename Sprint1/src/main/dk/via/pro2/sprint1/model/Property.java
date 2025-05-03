package main.dk.via.pro2.sprint1.model;

public class Property {
    private int id;
    private String location;
    private double pricePerNight;
    private boolean availability;
    private Facilities facilities;

    public Property(int id, String location, double pricePerNight, Facilities facilities, boolean availability) {
        this.id = id;
        this.location = location;
        this.pricePerNight = pricePerNight;
        this.facilities = facilities;
        this.availability = availability;
    }

    public int getId()
    {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public Facilities getFacilities() {
        return facilities;
    }

    public String toString(){
        return "id: "+ id +", Location: " + location + ", Price per night: " + pricePerNight + ", Facilities: " + facilities.toString() + ", Availability: " + availability;
    }
}