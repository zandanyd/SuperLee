package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.DataLayer.DriverDataMapper;
import SuperLee.HumenResource.DataLayer.DriverShiftDataMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class DriverShiftController {
    private static DriverShiftController instance = null;
    private DriverShiftDataMapper driverShiftMapper = DriverShiftDataMapper.getInstance();
    private DriverDataMapper driverMapper = DriverDataMapper.getInstance();
    private static DriverShiftController single_instance = null;



    private DriverShiftController() {
//        DriverShiftHistory = new HashMap<AbstractMap.SimpleImmutableEntry<LocalDate, ShiftType>,DriverShift>();
//        DriverShiftArrangement = new HashMap<AbstractMap.SimpleImmutableEntry<LocalDate, ShiftType>,DriverShift>();
    }
    public static synchronized DriverShiftController getInstance()  {
        if(instance == null){
            instance = new DriverShiftController();
        }
        return instance;
    }
    public GenericShift findShift(LocalDate date, ShiftType type) throws SQLException {
        return driverShiftMapper.get(date, type);
    }

    public boolean isExist(LocalDate date, ShiftType type) throws SQLException {
        return driverShiftMapper.isInDataBase(date, type);
    }

    public void printAllShiftWorkers(LocalDate date, ShiftType type) throws SQLException {
        findShift(date,type).PrintShiftDetails();
    }
    public void RemoveDriverFromShift(String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        driverShiftMapper.deleteDriverFromShift(workerID, localDate, shiftType);
        driverMapper.updateNumberOfShiftsPerWeek(workerID, driverMapper.getDriver(workerID).getNumberOfShiftsPerWeek() - 1);

    }
    public void addDriverToShift(String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        driverShiftMapper.insertDriverToShift(workerID, localDate, shiftType);
        driverMapper.updateNumberOfShiftsPerWeek(workerID, driverMapper.getDriver(workerID).getNumberOfShiftsPerWeek() + 1);

    }
    public boolean isDriverInShift(String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        return driverShiftMapper.isDriverInShift(workerID, localDate, shiftType);
    }

    public void printShift(LocalDate localDate, ShiftType shiftType) throws SQLException {
        findShift(localDate, shiftType).PrintShiftDetails();
    }
    public void createShiftAndAddDriver(LocalDate localDate, ShiftType shiftType, String id) throws SQLException {
        if(!driverShiftMapper.isInDataBase(localDate, shiftType)){
            GenericShift shift = new GenericShift(localDate,shiftType);
            driverShiftMapper.insert(id, shift);
            driverMapper.updateNumberOfShiftsPerWeek(id, driverMapper.getDriver(id).getNumberOfShiftsPerWeek() + 1);
        }
        else {
            driverShiftMapper.insertDriverToShift(id,localDate,shiftType);
        }
    }

    public void PrintShiftDetails(LocalDate date, ShiftType shiftType) throws SQLException {
        GenericShift shift = findShift(date,shiftType);
        shift.PrintShiftDetails();
        ArrayList<Driver> ListOfDrivers = driverMapper.getDriversFromShift(date,shiftType);
        for (Driver driver : ListOfDrivers) {
            System.out.println("Name : " + driver.getFirstName() + " " + driver.getLastName() + " ID : " + driver.getID() +
                    " License : " + driver.getLicenseType() + " Trainings : " + driver.getTrainings());
        }

    }

    public void EndShift(LocalDate date, ShiftType type) throws SQLException {
        if (!driverShiftMapper.isInDataBase(date, type)) {
            System.out.println("Shift does not exist");
        }
        GenericShift shift = driverShiftMapper.get(date, type);
        double WorkHours = 0;
        double minutes = Math.abs(shift.getFinishHour().getMinute() - shift.getStartHour().getMinute()) / 60.0;
        double hours = shift.getFinishHour().getHour() - shift.getStartHour().getHour();
        WorkHours = hours + minutes;
        for (Driver driver : driverMapper.getDriversFromShift(date, type)) {
            driverMapper.updateWorkHours(driver.getID(), WorkHours+driver.getWorkHours());
        }
    }

}
