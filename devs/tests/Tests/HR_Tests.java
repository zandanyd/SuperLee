package Tests;

import SuperLee.HumenResource.BusinessLayer.*;
import SuperLee.HumenResource.DataLayer.BranchWorkerDataMapper;
import SuperLee.HumenResource.DataLayer.ConstraintDataMapper;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HRtests {
    @Test
    @Order(1)
    public void createNewWorker() throws  Exception {
        // 1. Create new worker and check if a matching worker object is in the database after the method
        String ID = "205593218";
        assertEquals(false, WorkerController.getInstance().isExist(ID));
        String FirstName = "Avi";
        String LastName = "Ron";
        String password = "1234";
        String username = "205593218";
        int bankAccount = 3450;
        double wage = 45;
        String employmentCondition = "Student";
        LocalDate HireDate = LocalDate.of(2022, 3, 12);
        String BranchAddress = "Eqron City";
        SimpleWorkerType type = SimpleWorkerType.Cashier;
        WorkerController.getInstance().createNewWorker(BranchAddress, FirstName, LastName, ID, bankAccount, wage, HireDate, employmentCondition, type);
        assertEquals(true, WorkerController.getInstance().isExist(ID));
        BranchWorkerDataMapper.getInstance().deleteBranchWorker(ID);
    }
    @Test
    @Order(2)
    void AddConstraint() throws  Exception{
        // 2. Add constraint to a worker and the checks if he is really available at this date
        String TestedWorkerID = "1313131313";
        LocalDate DayForConstraint = LocalDate.of(2023,9,25);
        ShiftType type = ShiftType.Evening;
        WorkerController.getInstance().AddConstraint(TestedWorkerID,DayForConstraint, type);
        assertEquals(true, WorkerController.getInstance().isAvailable(TestedWorkerID,DayForConstraint,type));
        ConstraintDataMapper.getInstance().deleteConstraint("1313131313",DayForConstraint, type);
    }
    @Test
    @Order(3)
    void addBranchShift() throws  Exception{
        // 3. Receives a list of workers id's, shift manager, branch address, date and shift type then the function creates the shift and
        // the db will be updated as well (Note : the given workers are all available at date in the test
        LocalDate DayOfShift = LocalDate.of(2023,9,25);
        LocalTime HourToStart = LocalTime.of(15,0);
        LocalTime HourToEnd = LocalTime.of(22,0);
        ShiftType TimeOfShift = ShiftType.Evening;
        String BranchAddress = "Eqron City";
        assertEquals(false, BranchShiftController.getInstance().isExist(BranchAddress,DayOfShift,TimeOfShift));
        WorkerController.getInstance().AddConstraint("111111111",DayOfShift, TimeOfShift);
        WorkerController.getInstance().AddConstraint("555555555",DayOfShift, TimeOfShift);
        WorkerController.getInstance().AddConstraint("444444444",DayOfShift, TimeOfShift);
        ArrayList<String> WorkersID = new ArrayList<>();
        WorkersID.add("111111111");
        WorkersID.add("555555555");
        assertEquals(true, WorkerController.getInstance().isShiftManager("444444444"));
        BranchShiftController.getInstance().AssignNewShift(DayOfShift,TimeOfShift,"444444444",BranchAddress,WorkersID,HourToStart,HourToEnd);
        assertEquals(true, BranchShiftController.getInstance().isExist(BranchAddress,DayOfShift,TimeOfShift));
        ConstraintDataMapper.getInstance().deleteConstraint("111111111",DayOfShift, TimeOfShift);
        ConstraintDataMapper.getInstance().deleteConstraint("555555555",DayOfShift, TimeOfShift);
        ConstraintDataMapper.getInstance().deleteConstraint("444444444",DayOfShift, TimeOfShift);

    }
    @Test
    @Order(4)
    void addWorkerToShift() throws Exception {
        // 4. Shows that worker isn't a part from a particular shift, then tries to add the worker to the shift with this specific method
        String ID = "1414141414";
        String FirstName = "Avi";
        String LastName = "Ron";
        String password = "1234";
        String username = "1414141414";
        int bankAccount = 3450;
        double wage = 45;
        String employmentCondition = "Student";
        LocalDate HireDate = LocalDate.of(2022, 3, 12);
        String BranchAddress = "Eqron City";
        SimpleWorkerType type = SimpleWorkerType.Cashier;
        WorkerController.getInstance().createNewWorker(BranchAddress, FirstName, LastName, ID, bankAccount, wage, HireDate, employmentCondition, type);        String TestedWorkerBranchAddress = "Eqron City";
        LocalDate DayOfShift = LocalDate.of(2023,9,25);
        ShiftType TimeOfShift = ShiftType.Evening;
        LocalDate DayForConstraint = LocalDate.of(2023,9,25);
        WorkerController.getInstance().AddConstraint(ID,DayForConstraint, ShiftType.Evening);
        assertEquals(false, BranchShiftController.getInstance().isWorkerInShift(TestedWorkerBranchAddress,ID,DayOfShift,TimeOfShift));
        BranchShiftController.getInstance().addWorkerToShift(TestedWorkerBranchAddress,ID,DayOfShift,TimeOfShift);
        assertEquals(true, BranchShiftController.getInstance().isWorkerInShift(TestedWorkerBranchAddress,ID,DayOfShift,TimeOfShift));
        ConstraintDataMapper.getInstance().deleteConstraint(ID,DayOfShift, TimeOfShift);
    }
    @Test
    @Order(5)
    void addCancelItem() throws Exception {
        // 5. First a regular worker will try to reach to the shift manager menu to add an item to a shift cancellation list,
        // but he will be rejected, then we will promote this worker to be a shift manager and then he will get a full access
        // to this menu, he will try to add an item to the cancellation list.
        String ID = "1414141414";
        String BranchAddress = "Eqron City";
        assertEquals(false, WorkerController.getInstance().isShiftManager(ID));
        WorkerController.getInstance().PromoteWorkerToShiftManager(ID);
        assertEquals(true, WorkerController.getInstance().isShiftManager(ID));
        LocalDate DayOfShift = LocalDate.of(2023,9,25);
        ShiftType TimeOfShift = ShiftType.Evening;
        BranchShiftController.getInstance().addCancelItem(BranchAddress,DayOfShift,TimeOfShift,"Banana Dawara");
        assertEquals(true, BranchShiftDataMapper.getInstance().getAllCancelledItems(BranchAddress,DayOfShift,TimeOfShift).keySet().contains("Banana Dawara"));
        BranchWorkerDataMapper.getInstance().deleteBranchWorker(ID);
        ConstraintDataMapper.getInstance().deleteConstraint(ID,DayOfShift, TimeOfShift);
        BranchShiftDataMapper.getInstance().deleteBranchShift(BranchAddress,DayOfShift,TimeOfShift);

    }
}


