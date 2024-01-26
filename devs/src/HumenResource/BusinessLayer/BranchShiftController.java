package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.DataLayer.BranchWorkerDataMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class BranchShiftController {
    //    BranchController branchController = BranchController.getInstance();
//    WorkerController workerController = WorkerController.getInstance();
    BranchShiftDataMapper branchShiftMapper = BranchShiftDataMapper.getInstance();
    public BranchWorkerDataMapper branchSWorkerMapper = BranchWorkerDataMapper.getInstance();
    private ShiftManager shiftManager;
    private static BranchShiftController single_instance = null;
    // map of all the Branches in the company

    private BranchShiftController() {
    }
    public static synchronized BranchShiftController getInstance() {

        if (single_instance == null) {
            single_instance = new BranchShiftController();
        }
        return single_instance;
    }

    public BranchShift findShift(String BranchID , LocalDate date, ShiftType type) throws SQLException {
        return branchShiftMapper.getById(date, type, BranchID);
    }

    public boolean isExist(String BranchID , LocalDate date, ShiftType type) throws SQLException {
        return branchShiftMapper.isInDataBase(BranchID, date, type);
    }

    public void printAllShiftWorkers(String id, LocalDate date, ShiftType type) throws SQLException {
        findShift(id,date,type).PrintShiftDetails();
    }
    public void RemoveWorkerFromShift(String BranchID,String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        branchShiftMapper.deleteBranchWorkerFromShift(BranchID, workerID, localDate, shiftType);
        branchSWorkerMapper.updateNumberOfShiftsPerWeek(workerID, branchSWorkerMapper.getWorker(workerID).getNumberOfShiftsPerWeek() - 1);

    }
    public void addWorkerToShift(String BranchID,String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        branchShiftMapper.insertBranchWorkerToShift(workerID, BranchID, localDate, shiftType);
        branchSWorkerMapper.updateNumberOfShiftsPerWeek(workerID, branchSWorkerMapper.getWorker(workerID).getNumberOfShiftsPerWeek() + 1);

    }
    public boolean isWorkerInShift(String branchID, String workerID, LocalDate localDate, ShiftType shiftType) throws SQLException {
        return branchShiftMapper.isBranchWorkerInShift(branchID, workerID, localDate, shiftType);
    }

    public void printShift(String id, LocalDate localDate, ShiftType shiftType) throws SQLException {
        branchShiftMapper.getById(localDate, shiftType, id).PrintShiftDetails();
    }

    public void addCancelItem(String branchID, LocalDate date, ShiftType shiftType, String item) throws SQLException {
        branchShiftMapper.insertItemToCancellationList(branchID, date, shiftType, item);
    }

    public void AssignNewShift(LocalDate Date, ShiftType Type, String manager, String branchID, ArrayList<String> branchShiftWorkersID, LocalTime startTime, LocalTime finishTime) throws SQLException {
        int currentNumOfShifts = 0;
        BranchShift NewBranchShift = new BranchShift(Date, Type, manager, branchID);
        branchShiftWorkersID.add(manager);
        branchShiftMapper.updateBranchShiftStartHour(branchID, Date, Type, startTime);
        branchShiftMapper.updateBranchShiftFinishHour(branchID, Date, Type, finishTime);
        branchShiftMapper.insert(branchShiftWorkersID, NewBranchShift);
        for(String workerID : branchShiftWorkersID){
            currentNumOfShifts = branchSWorkerMapper.getWorker(workerID).getNumberOfShiftsPerWeek();
            branchSWorkerMapper.updateNumberOfShiftsPerWeek(workerID, currentNumOfShifts + 1);
        }

    }

    //    public void printBranchShiftHistoryByBranch(String branchID) throws SQLException {
//        int tomorrow;
//        LocalDate Now = LocalDate.now();
//        ArrayList<BranchShift> ArchiveBranchShifts = new ArrayList<>();
//        if(LocalTime.now().isAfter(branchShiftMapper.getById(Now, ShiftType.Morning, branchID).FinishHour) && LocalTime.now().isBefore(branchShiftMapper.getById(Now, ShiftType.Morning, branchID).FinishHour)){
//            ArchiveBranchShifts = branchShiftMapper.getBranchShiftHistoryByBranch(branchID, Now, ShiftType.Evening);
//            for(BranchShift shift : ArchiveBranchShifts){
//                shift.PrintShiftDetails();
//            }
//        }
//        if(LocalTime.now().isAfter(branchShiftMapper.getById(Now, ShiftType.Morning, branchID).FinishHour) && LocalTime.now().isAfter(branchShiftMapper.getById(Now, ShiftType.Morning, branchID).FinishHour)) {
//            if(Now.getDayOfMonth() + 1 > Now.lengthOfMonth())
//                tomorrow = 1;
//            else{
//                tomorrow = Now.getDayOfMonth() + 1;
//            }
//            LocalDate tomorrowDate = LocalDate.of(Now.getYear(), Now.getMonthValue(), tomorrow);
//            ArchiveBranchShifts = branchShiftMapper.getBranchShiftHistoryByBranch(branchID, tomorrowDate, ShiftType.Morning);
//            for(BranchShift shift : ArchiveBranchShifts){
//                shift.PrintShiftDetails();
//            }
//        }
//
//    }
    public void PrintShiftDetails(LocalDate date, ShiftType shiftType, String address) throws SQLException {
        BranchShift shift = findShift(address,date,shiftType);
        shift.PrintShiftDetails();
        LinkedList<GenericWorker> ShiftManagers = new LinkedList<GenericWorker>();
        LinkedList<GenericWorker> Cashiers = new LinkedList<GenericWorker>();
        LinkedList<GenericWorker> StockKeepers = new LinkedList<GenericWorker>();
        LinkedList<GenericWorker> GeneralWorkers = new LinkedList<GenericWorker>();
        LinkedList<GenericWorker> Cleaners = new LinkedList<GenericWorker>();
        LinkedList<GenericWorker> Ushers = new LinkedList<GenericWorker>();
        ArrayList<BranchWorker> ListOfWorkers = branchSWorkerMapper.getWorkersFromShift(address,date,shiftType);
        for (GenericWorker worker : ListOfWorkers) {
            if (((SimpleWorker) worker).getAllWorkerTypes().contains(SimpleWorkerType.Cashier))
                Cashiers.add(worker);
            if (((SimpleWorker) worker).getAllWorkerTypes().contains(SimpleWorkerType.StockKeeper))
                StockKeepers.add(worker);
            if (((SimpleWorker) worker).getAllWorkerTypes().contains(SimpleWorkerType.GeneralWorker))
                GeneralWorkers.add(worker);
            if (((SimpleWorker) worker).getAllWorkerTypes().contains(SimpleWorkerType.Cleaner))
                Cleaners.add(worker);
            if (((SimpleWorker) worker).getAllWorkerTypes().contains(SimpleWorkerType.Usher))
                Ushers.add(worker);
        }
        System.out.println("The Shift manager was : " + shift.getManager());

        System.out.println("Cashiers were : ");
        for(GenericWorker i : Cashiers ) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("Stock keepers were : ");
        for(GenericWorker i : StockKeepers ) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("General workers were : ");
        for(GenericWorker i : GeneralWorkers ) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("Cleaners were : ");
        for(GenericWorker i : Cleaners ) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("Ushers were : ");
        for(GenericWorker i : Ushers ) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        if(!branchShiftMapper.getAllCancelledItems(address, date, shiftType).isEmpty()){
            ShowCancellationList(address, date, shiftType);
        }
    }
    public void ShowCancellationList(String address,LocalDate date, ShiftType shiftType) throws SQLException {
        HashMap<String, Integer> CancelledItems = branchShiftMapper.getAllCancelledItems(address, date, shiftType);
        System.out.println("The cancelled items are: ");
        for(String item : CancelledItems.keySet()){
            System.out.println("Item description - " + item + " Amount - " + CancelledItems.get(item));
        }
    }

    public void EndShift(String branchAddress, LocalDate date, ShiftType type) throws SQLException {
        if (!branchShiftMapper.isInDataBase(branchAddress, date, type)) {
            System.out.println("Shift does not exist");
        }
        BranchShift shift = branchShiftMapper.getById(date, type, branchAddress);
        double WorkHours = 0;
        double minutes = Math.abs(shift.getFinishHour().getMinute() - shift.getStartHour().getMinute()) / 60.0;
        double hours = shift.getFinishHour().getHour() - shift.getStartHour().getHour();
        WorkHours = hours + minutes;
        branchSWorkerMapper.updateWorkHours(shift.getManager(),WorkHours+branchSWorkerMapper.getWorker(shift.getManager()).getWorkHours());
        for (BranchWorker worker : branchSWorkerMapper.getWorkersFromShift(branchAddress, date, type)) {
            branchSWorkerMapper.updateWorkHours(worker.getID(), WorkHours+worker.getWorkHours());
        }
    }


    public HashMap<String, Integer> getAllCanceledItems(String BranchAddress, LocalDate date, ShiftType type) throws SQLException {
        return branchShiftMapper.getAllCancelledItems(BranchAddress,date,type);
    }


}
