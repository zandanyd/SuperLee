package SuperLee.Transport.BusinessLayer;
import SuperLee.Transport.BusinessLayer.TrainingType;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Truck {

    protected int licenseNumber;
    protected String model;
    protected double netWeight;
    protected double maxWeight;
    protected ArrayList<LocalDate> unAvailableDates;

    public void setTruckAvailableStatus(Boolean available) {
        this.available = available;
    }

    protected Boolean available; // check if the truck is available for driving

    public Truck(int licenseNumber, String model, double netWeight, double maxWeight) {
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.netWeight = netWeight;
        this.maxWeight = maxWeight;
        this.available = true;
        unAvailableDates = new ArrayList<>();
        String initialString = "1111-11-11";
        LocalDate initialDate = LocalDate.parse(initialString);
        unAvailableDates.add(initialDate);
    }

    public Truck() {
    }

    public void addDate(LocalDate date){
        unAvailableDates.add(date);
    }

    public void removeDate(LocalDate date){
        if(!unAvailableDates.contains(date)){
            return;
        }
        unAvailableDates.remove(date);
    }


    public boolean checkDate(LocalDate date){
        return unAvailableDates.contains(date);
    }

    public abstract TrainingType getTrainingType();

    public int getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public void setUnAvailableDates(ArrayList<LocalDate> unAvailableDates) {
        this.unAvailableDates = unAvailableDates;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public Boolean getAvailable() {
        return available;
    }

    public ArrayList<LocalDate> getUnAvailableDates() {
        return unAvailableDates;
    }

    @Override
    public abstract String toString();

}


