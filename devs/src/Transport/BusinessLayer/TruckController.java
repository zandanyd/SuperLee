package SuperLee.Transport.BusinessLayer;
import SuperLee.Transport.DataLayer.TrucksDAO;

import java.time.LocalDate;
import java.util.ArrayList;

public class TruckController { // Singleton

    private TrucksDAO trucksDAO;
    private static TruckController single_instance = null;

    private TruckController()  {
        trucksDAO = TrucksDAO.getInstance();
    }

    public static synchronized TruckController getInstance() {
        if(single_instance == null){
            single_instance = new TruckController();
        }
        return single_instance;
    }

    public ArrayList<Truck> getAvailableTrucksByType(TrainingType type, LocalDate date){
        return trucksDAO.getAvailableTrucksByType(type, date);
    }

    public void addTruck(TrainingType type, Truck truck){
        trucksDAO.addTruck(type, truck);
    }

    // Receive args from the user and create truck object and add it to system and return the truck.
    public boolean createTruck(int licenseNumber,String model,double netWeight,double maxWeight, String truckType) {
        if(trucksDAO.getTruckByLicense(licenseNumber) != null){
            return false;
        }
        TrainingType type = TrainingType.FROZEN;
        Truck truck = null;
        if(truckType.equalsIgnoreCase( "DRY")){
            type = TrainingType.DRY;
            truck = new DryTruck(licenseNumber, model,netWeight, maxWeight);
            addTruck(type, truck);
            return true;
        }
        if(truckType.equals( "COOL")){
            truck = new CoolTruck(licenseNumber, model,netWeight, maxWeight);
            type = TrainingType.REFRIGERATED;

            addTruck(type, truck);
            return true;
        }

        truck = new FrozenTruck(licenseNumber, model,netWeight, maxWeight);
        addTruck(type, truck);
        return true;
    }

    public void printTruck(int licenseTruck){
        Truck truck = getTruckByLicense(licenseTruck);
        System.out.println(truck.toString());
    }

    public void printAvailableTrucks(TrainingType type, LocalDate date){
        int i = 0;
        for (Truck truck : getAvailableTrucksByType(type, date)) {
            System.out.println((i + 1) + ". " + truck.toString());
            i++;
        }
    }


    public Truck getTruckByLicense(int licenseTruck){
        return trucksDAO.getTruckByLicense(licenseTruck);
    }

    public boolean checkAvailableTrucksByType(TrainingType truckType, LocalDate date){
        return (trucksDAO.getAvailableTrucksByType(truckType, date).size() != 0);
    }

    public boolean checkExistTrucks(){
        return trucksDAO.checkIfExistTrucks();
    }

    public boolean truckExist(int licenseNumber){
        if(trucksDAO.getTruckByLicense(licenseNumber) == null){
            return false;
        }
        return true;
    }

    public void setTruckAvailable(Truck truck){
        truck.setTruckAvailableStatus(true);
        trucksDAO.updateTruck(truck);
    }

    public void setTruckUnAvailable(Truck truck) {
        truck.setTruckAvailableStatus(false);
        trucksDAO.updateTruck(truck);
    }

    public void addDateToTruck(LocalDate date, Truck truck){
        truck.addDate(date);
        trucksDAO.updateTruck(truck);
    }
    public void removeDateToTruck(LocalDate date, Truck truck){
        truck.removeDate(date);
        trucksDAO.updateTruck(truck);
    }

    public void deleteTruck(Truck truck){
        trucksDAO.deleteTruck(truck);
    }
}
