package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.DataLayer.ConstraintDataMapper;
import SuperLee.HumenResource.DataLayer.DriverDataMapper;
import SuperLee.Transport.BusinessLayer.LicenseType;
import SuperLee.Transport.BusinessLayer.TrainingType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class DriverController {
    private static DriverController instance = null;
    private ConstraintDataMapper constraintMapper = ConstraintDataMapper.getInstance();
    private DriverDataMapper driverMapper = DriverDataMapper.getInstance();

    private DriverController()  {};
    public static synchronized DriverController getInstance()  {
        if (instance == null){
            instance = new DriverController();
        }
        return instance;
    }

    public void AddTraining(String driverID, TrainingType type) throws SQLException {
        driverMapper.insertTraining(driverMapper.getDriver(driverID), type);
    }

    public void createNewDriver(String NewWorkerFirstName, String NewWorkerLastName, String NewWorkerID, int NewWorkerBankAccount, double NewWorkerWage, LocalDate NewWorkerHireDate, String NewWorkerEmploymentCondition, LicenseType NewDriverLicenseType, ArrayList<TrainingType> NewDriverTrainingType) throws SQLException {
        Driver driver = new Driver( NewWorkerFirstName,NewWorkerLastName,NewWorkerID, generateRandomPassword(4), NewWorkerBankAccount,NewWorkerWage,NewWorkerHireDate,NewWorkerEmploymentCondition,NewDriverLicenseType, NewDriverTrainingType);
        driverMapper.insert(driver);
    }
    public static String generateRandomPassword(int length) {
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generate a random digit (0-9)
            passwordBuilder.append(digit);
        }

        return passwordBuilder.toString();
    }
    public Driver SearchDriver(String id) throws SQLException {
        return driverMapper.getDriver(id);
    }
    public void PrintDriver(String id) throws SQLException {
        System.out.println(SearchDriver(id));
    }

    public void ShowAllDrivers() throws SQLException {
        ArrayList<Driver> drivers = driverMapper.getAllDrivers();
        for (Driver driver:drivers){
            System.out.println(driver+ "\n");
        }
    }
    public ArrayList<Driver> getAllDrivers() throws SQLException {
        return driverMapper.getAllDrivers();
    }
    public void resetWorkHours() throws SQLException {
        for(Driver worker : driverMapper.getAllDrivers()){
            driverMapper.updateWorkHours(worker.getID(),0);
        }
    }
    public void EndWeek() throws SQLException {
        for(Driver worker : driverMapper.getAllDrivers()){
            driverMapper.updateNumberOfShiftsPerWeek(worker.getID(),0);
        }
    }
    // this method prints for each worker his salary for the current month (without bonuses) and resets his work hours field
    public void EndMonth(ArrayList<String> RewardedWorkersID, ArrayList<Double> Bonuses) throws SQLException {
        int index = 0;
        for (Driver driver : driverMapper.getAllDrivers()) {
            if(!RewardedWorkersID.contains(driver.getID())) {
                System.out.println("Name: " + driver.getFirstName() + " " + driver.getLastName() + "\n" + "this month payment is - " + calculateWage(driver.getID(),0,false));
                driverMapper.updateWorkHours(driver.getID(), 0);
            }
            else{
                index = RewardedWorkersID.indexOf(driver.getID());
                System.out.println(driver.getFirstName() + " " + driver.getLastName() + "\n" + " this month payment after the bonus is " + calculateWage(driver.getID(), Bonuses.get(index), true));
            }
        }
    }
    public boolean isExist(String id) throws SQLException {
        return driverMapper.isInDataBase(id);
    }
    public boolean VerifyId(String id, String password) throws SQLException {
        return Objects.equals(driverMapper.getDriver(id).getPassword(), password);
    }
    public Driver findDriver(String id) throws SQLException {
        if(!driverMapper.isInDataBase(id)){
            System.out.println("There is not a driver with this ID");
            return null;
        }
        return driverMapper.getDriver(id);
    }
    public String getDriverFirstName(String id) throws SQLException {
        return findDriver(id).getFirstName();
    }
    public String getDriverLastName(String id) throws SQLException {
        return findDriver(id).getLastName();
    }
    public void setDriverPassword(String id, String password) throws SQLException {
        driverMapper.updatePassword(id, password);
    }
    public void setEmploymentCondition(String id,String text) throws SQLException {
        driverMapper.updateEmploymentCondition(id,text);
    }
    public void setWage(String id, double wage) throws SQLException {
        driverMapper.updateWage(id, wage);
    }
    public void AddConstraint(String id, LocalDate localDate, ShiftType shiftType) throws SQLException {
        if(constraintMapper.isInDataBase(id, localDate, shiftType)){
            System.out.println("This driver already has this constraint!");
            return;
        }
        Constraints NewConstraint = new Constraints(localDate, shiftType,id);
        constraintMapper.insert(NewConstraint);
    }

    public double calculateWage(String id, double bonus, boolean rewarded) throws SQLException {
        if(rewarded)
            return findDriver(id).CalculateMonthlyWage() + bonus;
        else
            return findDriver(id).CalculateMonthlyWage();
    }

    public boolean MoreShiftThen(String ID, int NumOfShift)throws SQLException{
        return findDriver(ID).getNumberOfShiftsPerWeek() >= NumOfShift;
    }

    public boolean LessShiftThen(String ID, int NumOfShift)throws SQLException{
        return findDriver(ID).getNumberOfShiftsPerWeek() < NumOfShift;
    }

    public ArrayList<Driver> getAvailableDriversByDate(LocalDate date, ShiftType type) throws SQLException {
        ArrayList<Driver> AvailableDrivers = new ArrayList<>();
        for (Driver driver  : driverMapper.getAllDrivers()) {
            if(constraintMapper.isInDataBase(driver.getID(), date, type )){
                AvailableDrivers.add(driver);
            }
        }
        return AvailableDrivers;
    }
    public void addTraining(String id, TrainingType trainingType) throws SQLException {
        Driver driver = driverMapper.getDriver(id);
        driverMapper.insertTraining(driver,trainingType);
    }

    public void updateWorkHours(String workerID, Double time) throws SQLException {
        driverMapper.updateWorkHours(workerID,time+driverMapper.getDriver(workerID).getWorkHours());
    }
    public void addDriverToShift(LocalDate date, ShiftType type, String id){

    }

    public boolean isTrainingExist(String driverID, TrainingType type) throws  SQLException {
        return driverMapper.isTrainingExist(driverID,type);
    }

    public void printDriver(String id){
        try {
            System.out.println(driverMapper.getDriver(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
