package SuperLee;

import SuperLee.HumenResource.BusinessLayer.*;
import SuperLee.Transport.BusinessLayer.Site;
import SuperLee.Transport.BusinessLayer.Transport;
import SuperLee.Transport.BusinessLayer.TransportsController;
import SuperLee.Transport.BusinessLayer.Truck;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class TransportWorkersController {

    DriverController driverController = DriverController.getInstance();
    DriverShiftController driverShiftController = DriverShiftController.getInstance();
    WorkerController workerController = WorkerController.getInstance();
    BranchController branchController = BranchController.getInstance();
    TransportsController transportController = TransportsController.getInstance();
    private static TransportWorkersController single_instance = null;
    private TransportWorkersController()  {
    }
    public static synchronized TransportWorkersController getInstance()  {
        if(single_instance == null){
            return new TransportWorkersController();
        }
        return single_instance;
    }

    // TODO: check
    public String displayAllTransportsBySiteAndDate(String address, String date){
        String transportsStr = null;
        ArrayList<Transport> allTransports = transportController.getAllTransports();
        for(Transport transport : allTransports){
            String[] parts1 = transport.getStartLocalDateTime().split(" "); // Split the string at the space delimiter
            String[] parts2 = date.split("-"); // Split the string at the space delimiter
            String[] parts11 = parts1[0].split("-");
            if(parts11[0].equals(parts2[0]) && parts11[1].equals(parts2[1]) && parts11[2].equals(parts2[2])) {
                ArrayList<Site> sitesOfTransport = transport.getTransportDocument().getDestinationList();
                for (Site site : sitesOfTransport) {
                    if (site.getAddress().equals(address))
                        transportsStr = transport.toString();

                }
            }
        }
        return transportsStr;
    }


    public ArrayList<String> getAvailableSitesByDate(String date) throws SQLException {
        String[] arr1 = date.split(" ", 2);
        String[] arr2 = arr1[0].split("-", 3);
        String[] arr3 = arr1[1].split(":", 3);
        LocalDate Date = LocalDate.of(Integer.parseInt(arr2[0]),Integer.parseInt(arr2[1]),Integer.parseInt(arr2[2]));
        int hour = Integer.parseInt(arr3[0]);
        int min = Integer.parseInt(arr3[1]);
        LocalTime StartTime = LocalTime.of(hour, min);
        LocalDate localDate = LocalDate.of(Integer.parseInt(arr2[0]), Integer.parseInt(arr2[1]), Integer.parseInt(arr2[2]));
        boolean DifferentWorkers = false;
        StartTime = LocalTime.of(hour, min);
        LocalTime branchStartHour = null;
        ArrayList<String> AvailableAtMorning = new ArrayList<String>();
        ArrayList<String> AvailableAtEvening = new ArrayList<String>();
        ArrayList<String> AllAvailableSites = new ArrayList<String>();
        if(localDate.isBefore(LocalDate.now())){
            return null;
        }
        String str;
        int RightDay = 0;
        if(localDate.getDayOfWeek().getValue() == 1) {
            RightDay = 7;
        }
        else {
            RightDay = localDate.getDayOfWeek().getValue() - 1;
        }
        ArrayList<Branch> AllBranches = branchController.getListOfAllBranches();
        for(Branch branch : AllBranches){
            AvailableAtEvening.clear();
            AvailableAtMorning.clear();
            str = branchController.getStartHour(branch.getAddress(),RightDay,1);
            String[] Weekly = str.split(":", 2);
            hour = Integer.parseInt(Weekly[0]);
            min = Integer.parseInt(Weekly[1]);
            branchStartHour = LocalTime.of(hour, min);
            if(StartTime.isAfter(branchStartHour)) {
                for(BranchWorker worker : workerController.getAllWorkerFromBranch(branch.getAddress())){
                    if(workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Evening) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6){
                        AllAvailableSites.add(branch.getAddress());
                        break;
                    }
                }
            }
            else{
                for(BranchWorker worker : workerController.getAllWorkerFromBranch(branch.getAddress())){
                    if(workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Evening) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6){
                        AvailableAtEvening.add(worker.getID());
                    }
                    if(workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Morning) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6){
                        AvailableAtMorning.add(worker.getID());
                    }
                }
                if(AvailableAtEvening.size() != 0 && AvailableAtMorning.size() != 0) {
                    if (AvailableAtEvening.size() != AvailableAtMorning.size()) {
                        AllAvailableSites.add(branch.getAddress());
                    } else {
                        if(AvailableAtEvening.size() == 1 && AvailableAtMorning.size() == 1){
                            if(!AvailableAtEvening.get(0).equals(AvailableAtMorning.get(0)))
                            {
                                AllAvailableSites.add(branch.getAddress());
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return AllAvailableSites;
    }

    public boolean getAvailableSiteByDate(String date, String address) throws SQLException {
        String[] arr1 = date.split(" ", 2);
        String[] arr2 = arr1[0].split("-", 3);
        String[] arr3 = arr1[1].split(":", 3);
        LocalDate Date = LocalDate.of(Integer.parseInt(arr2[0]), Integer.parseInt(arr2[1]), Integer.parseInt(arr2[2]));
        int hour = Integer.parseInt(arr3[0]);
        int min = Integer.parseInt(arr3[1]);
        LocalTime StartTime = LocalTime.of(hour, min);
        LocalDate localDate = LocalDate.of(Integer.parseInt(arr2[0]), Integer.parseInt(arr2[1]), Integer.parseInt(arr2[2]));
        StartTime = LocalTime.of(hour, min);
        LocalTime branchStartHour = null;
        String str;
        ArrayList<String> AvailableAtEvening = new ArrayList<>();
        ArrayList<String> AvailableAtMorning = new ArrayList<>();
        int RightDay = 0;
        if (localDate.getDayOfWeek().getValue() == 1) {
            RightDay = 7;
        } else {
            RightDay = localDate.getDayOfWeek().getValue() - 1;
        }
        str = branchController.getStartHour(address, RightDay, 1);
        String[] Weekly = str.split(":", 2);
        hour = Integer.parseInt(Weekly[0]);
        min = Integer.parseInt(Weekly[1]);
        branchStartHour = LocalTime.of(hour, min);
        if (StartTime.isAfter(branchStartHour)) {
            for (BranchWorker worker : workerController.getAllWorkerFromBranch(address)) {
                if (workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Evening) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6) {
                    return true;
                }
            }
            return false;
        } else {
            for (BranchWorker worker : workerController.getAllWorkerFromBranch(address)) {
                if (workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Evening) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6) {
                    AvailableAtEvening.add(worker.getID());
                }
                if (workerController.isStockKeeper(worker.getID()) && workerController.isAvailable(worker.getID(), localDate, ShiftType.Morning) && (workerController.findWorker(worker.getID())).getNumberOfShiftsPerWeek() < 6) {
                    AvailableAtMorning.add(worker.getID());
                }
            }
            if (AvailableAtEvening.size() > 0 && AvailableAtMorning.size() > 0) {
                if (AvailableAtEvening.size() != AvailableAtMorning.size()) {
                    return true;
                } else {
                    if (AvailableAtEvening.size() == 1 && AvailableAtMorning.size() == 1) {
                        if (!AvailableAtEvening.get(0).equals(AvailableAtMorning.get(0))) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
    }



    public void getAllAvailableDriverByDate(LocalDate date, ShiftType type)  {
        try {
            ArrayList<Driver> drivers =  driverController.getAvailableDriversByDate(date, type);
            int i = 0;
            for (Driver driver : drivers){

                System.out.println((i + 1) + ". ID:" + driver.getID() + " Name: "+ driver.getName() + " LicenseType: "+ driver.getLicenseType()+ " Trainings: "+driver.getTrainings());
                i++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }
    public void addDriverToShift(LocalDate date, String type, String driverID) throws  SQLException{
        ShiftType Type = null;
        if(type.equals("Morning")){
            Type = ShiftType.Morning;
        }
        if(type.equals("Evening")){
            Type = ShiftType.Evening;
        }
        if(!driverShiftController.isExist(date,Type)){
            driverShiftController.createShiftAndAddDriver(date,Type,driverID);
        }
        else {
            driverController.addDriverToShift(date, Type, driverID);
        }
    }

    public void UpdateDriverWorkHours(LocalDateTime start, LocalDateTime end, Driver driver)  {
        double WorkHours = 0;
        double minutes = Math.abs(end.getMinute() - start.getMinute()) / 60.0;
        double hours = end.getHour() - start.getHour();
        WorkHours = hours + minutes;
        try {
            driverController.updateWorkHours(driver.getID(), WorkHours);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public boolean checkLicenseDriverToTruck(Driver driver, Truck truck){
        return (driver.getLicenseType().getWeightLimit() >= truck.getNetWeight());
    }

    // Returns true if the driver has good training to this truck.
    public boolean checkTrainingDriverToTruck(Driver driver, Truck truck){
        return driver.checkTraining(truck.getTrainingType());
    }
    public Integer checkDriverToTruck(Truck truck, Driver driver){
        if (!driver.checkTraining(truck.getTrainingType())) {
            return 1;
        } else if (driver.getLicenseType().getWeightLimit() < truck.getNetWeight()) {
            return 2;
        }
        return 0;
    }

    public boolean CheckAvailableDriverByDate(LocalDate date, ShiftType type){
        ArrayList<Driver> drivers = null;
        try {
            drivers = driverController.getAvailableDriversByDate(date, type);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return drivers.size() != 0;
    }


}


