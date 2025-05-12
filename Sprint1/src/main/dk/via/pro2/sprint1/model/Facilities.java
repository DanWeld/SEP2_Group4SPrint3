package main.dk.via.pro2.sprint1.model;

public class Facilities {
    private boolean kitchen;
    private boolean internet;
    private boolean dishwasher;
    private boolean laundryMachine;
    private boolean swimmingPool;

    public Facilities(boolean kitchen, boolean internet, boolean dishwasher, 
                     boolean laundryMachine, boolean swimmingPool) {
        this.kitchen = kitchen;
        this.internet = internet;
        this.dishwasher = dishwasher;
        this.laundryMachine = laundryMachine;
        this.swimmingPool = swimmingPool;
    }

    public boolean getKitchen() {
        return kitchen;
    }

    public boolean getInternet() {
        return internet;
    }

    public boolean getDishWasher() {
        return dishwasher;
    }

    public boolean getLaundryMachine() {
        return laundryMachine;
    }

    public boolean getSwimmingPool() {
        return swimmingPool;
    }

    public void setAll(boolean kitchen, boolean internet, boolean dishwasher, 
                      boolean laundryMachine, boolean swimmingPool) {
        this.kitchen = kitchen;
        this.internet = internet;
        this.dishwasher = dishwasher;
        this.laundryMachine = laundryMachine;
        this.swimmingPool = swimmingPool;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (kitchen) sb.append("Kitchen, ");
        if (internet) sb.append("Internet, ");
        if (dishwasher) sb.append("Dishwasher, ");
        if (laundryMachine) sb.append("Laundry Machine, ");
        if (swimmingPool) sb.append("Swimming Pool, ");
        
        // facilities
        String result = sb.toString();
        if (!result.isEmpty()) {
            result = result.substring(0, result.length() - 2);
        } else {
            result = "No facilities";
        }
        
        return result;
    }
}