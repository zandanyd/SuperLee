package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.DataLayer.BranchWorkerDataMapper;
import SuperLee.HumenResource.DataLayer.ConstraintDataMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class WorkerController {
    private static WorkerController instance = null;
    BranchWorkerDataMapper BranchWorkerMapper = BranchWorkerDataMapper.getInstance();
    ConstraintDataMapper constraintMapper = ConstraintDataMapper.getInstance();
    // map of all the workers in the company

    private WorkerController()  {
    }

    public static synchronized WorkerController getInstance() {
        if (instance == null) {
            instance = new WorkerController();
        }
        return instance;
    }

    public void createNewWorker(String NewWorkerBranch, String NewWorkerFirstName, String NewWorkerLastName, String NewWorkerID, int NewWorkerBankAccount, double NewWorkerWage, LocalDate NewWorkerHireDate, String NewWorkerEmploymentCondition, SimpleWorkerType NewWorkerType) throws SQLException {
        SimpleWorker worker = new SimpleWorker(NewWorkerFirstName, NewWorkerLastName, NewWorkerID, generateRandomPassword(4), NewWorkerBranch, NewWorkerBankAccount, NewWorkerWage, NewWorkerHireDate, NewWorkerEmploymentCondition, NewWorkerType);
        BranchWorkerMapper.insertSimpleWorker(worker);

    }

    public BranchWorker SearchWorker(String id) throws SQLException {
        return BranchWorkerMapper.getWorker(id);
    }

    public void PrintWorker(String id) throws SQLException {
        System.out.println(BranchWorkerMapper.getWorker(id));
    }

    public void ShowAllWorkers() throws SQLException {
        ArrayList<BranchWorker> workers = BranchWorkerMapper.getAllWorkers();
        for (BranchWorker worker : workers) {
            System.out.println(worker + "\n");
        }
    }
    public ArrayList<BranchWorker> getAllWorkers() throws SQLException {
        return BranchWorkerMapper.getAllWorkers();
    }
    public boolean isTrainingExist(SimpleWorkerType type, String id) throws SQLException {
        return BranchWorkerMapper.haveTraining(type,id);
    }

    public ShiftManager PromoteWorkerToShiftManager(String id) throws SQLException {
        if (id == null || !BranchWorkerMapper.isInDataBase(id)) {
            System.out.println("wrong id");
            return null;
        }
        BranchWorker worker = BranchWorkerMapper.getWorker(id);
        String branchAddress = worker.getBranchAddress();
        ShiftManager shiftManager = new ShiftManager(worker.getFirstName(), worker.getLastName(), worker.getID(), worker.getPassword(),
                worker.getBranchAddress(), worker.getBankAccount(), worker.getWage(), worker.getHireDate(), worker.getEmploymentCondition());
        BranchWorkerMapper.deleteBranchWorker(id);
        BranchWorkerMapper.insertShiftManager(shiftManager, branchAddress);
        return (ShiftManager) BranchWorkerMapper.getWorker(shiftManager.getID());
    }

    public void EndWeek() throws SQLException {
        for (BranchWorker worker : BranchWorkerMapper.getAllWorkers()) {
            BranchWorkerMapper.updateNumberOfShiftsPerWeek(worker.getID(), 0);
        }
    }

    // this method prints for each worker his salary for the current month (without bonuses) and resets his work hours field
    public void EndMonth(ArrayList<String> RewardedWorkersID, ArrayList<Double> Bonuses) throws SQLException {
        int index = 0;
        for (BranchWorker worker : BranchWorkerMapper.getAllWorkers()) {
            if (!RewardedWorkersID.contains(worker.getID())) {
                System.out.println("Name: " + worker.getFirstName() + " " + worker.getLastName() + "\n" + "this month payment is - " + calculateWage(worker.getID(), 0, false));
                BranchWorkerMapper.updateWorkHours(worker.getID(), 0);
            } else {
                index = RewardedWorkersID.indexOf(worker.getID());
                System.out.println(worker.getFirstName() + " " + worker.getLastName() + "\n" + " this month payment after the bonus is " + calculateWage(worker.getID(), Bonuses.get(index), true));
            }
        }
    }
    public void resetWorkHours() throws SQLException {
        for (BranchWorker worker : BranchWorkerMapper.getAllWorkers()) {
                BranchWorkerMapper.updateWorkHours(worker.getID(), 0);
            }
    }

    public boolean isExist(String id) throws SQLException {
        return BranchWorkerMapper.isInDataBase(id);
    }

    public boolean VerifyId(String id, String password) throws SQLException {
        return Objects.equals(BranchWorkerMapper.getWorker(id).getPassword(), password);
    }

    public BranchWorker findWorker(String id) throws SQLException {
        if (!BranchWorkerMapper.isInDataBase(id)) {
            System.out.println("There is not a worker with this ID");
            return null;
        }
        return BranchWorkerMapper.getWorker(id);
    }

    public String getWorkerFirstName(String id) throws SQLException {
        return findWorker(id).getFirstName();
    }

    public String getWorkerLastName(String id) throws SQLException {
        return findWorker(id).getLastName();
    }

    public void setWorkerPassword(String id, String password) throws SQLException {
        BranchWorkerMapper.updatePassword(id, password);
    }

    public void setEmploymentCondition(String id, String text) throws SQLException {
        BranchWorkerMapper.updateEmploymentCondition(id, text);
    }

    public void setWage(String id, double wage) throws SQLException {
        BranchWorkerMapper.updateWage(id, wage);
    }

    public void AddConstraint(String id, LocalDate localDate, ShiftType shiftType) throws SQLException {
        if (constraintMapper.isInDataBase(id, localDate, shiftType)) {
            System.out.println("This worker already has this constraint!");
            return;
        }
        Constraints NewConstraint = new Constraints(localDate, shiftType, id);
        constraintMapper.insert(NewConstraint);
    }

    public boolean isShiftManager(String id) throws SQLException {
        return BranchWorkerMapper.isManager(id);
    }

    public double calculateWage(String id, double bonus, boolean rewarded) throws SQLException {
        if (rewarded)
            return findWorker(id).CalculateMonthlyWage() + bonus;
        else
            return findWorker(id).CalculateMonthlyWage();

    }

    public boolean MoreShiftThen(String ID, int NumOfShift) throws SQLException {
        return findWorker(ID).getNumberOfShiftsPerWeek() >= NumOfShift;
    }

    public boolean LessShiftThen(String ID, int NumOfShift) throws SQLException {
        return findWorker(ID).getNumberOfShiftsPerWeek() < NumOfShift;
    }


    public void showWorkerAvailability(String branchAddress, LocalDate date, ShiftType type) throws SQLException {
        ArrayList<BranchWorker> list = BranchWorkerDataMapper.getInstance().getWorkersByBranch(branchAddress);
        LinkedList<BranchWorker> ShiftManagers = new LinkedList<BranchWorker>();
        LinkedList<BranchWorker> Cashiers = new LinkedList<BranchWorker>();
        LinkedList<BranchWorker> StockKeepers = new LinkedList<BranchWorker>();
        LinkedList<BranchWorker> GeneralWorkers = new LinkedList<BranchWorker>();
        LinkedList<BranchWorker> Cleaners = new LinkedList<BranchWorker>();
        LinkedList<BranchWorker> Ushers = new LinkedList<BranchWorker>();
        for (BranchWorker worker : list) {
            Constraints constraints = constraintMapper.get(worker.getID(), date, type);
            if ((constraints != null) && worker.getClass() != ShiftManager.class) {
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
            } else if (constraints != null)
                ShiftManagers.add(worker);
        }
        System.out.println("All the shift managers that available for this shift are : ");
        for (GenericWorker i : ShiftManagers) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("All the cashiers that available for this shift are : ");
        for (GenericWorker i : Cashiers) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("All the stock keepers that available for this shift are : ");
        for (GenericWorker i : StockKeepers) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("All the general workers that available for this shift are : ");
        for (GenericWorker i : GeneralWorkers) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("All the cleaners that available for this shift are : ");
        for (GenericWorker i : Cleaners) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
        System.out.println("All the ushers that available for this shift are : ");
        for (GenericWorker i : Ushers) {
            System.out.println("Full name: " + i.getFirstName() + " " + i.getLastName());
            System.out.println("ID: " + i.getID());
        }
    }

    public void showBranchWorkersByBranch(String branchAddress) throws SQLException {
        for (BranchWorker worker : BranchWorkerMapper.getWorkersByBranch(branchAddress)) {
            System.out.println("name: " + worker.getFirstName() + " " + worker.getLastName() + "\n" + "ID: " + worker.getID() + "\n");

        }
    }

    public void addTraining(String ID, SimpleWorkerType type) throws SQLException {
        SimpleWorker simpleWorker = (SimpleWorker) this.findWorker(ID);
        BranchWorkerMapper.insertTraining(simpleWorker, type);
    }

    public boolean isStockKeeper(String ID) throws SQLException {
        if (BranchWorkerMapper.isManager(ID))
            return false;
        else {
            SimpleWorker worker = (SimpleWorker) BranchWorkerMapper.getWorker(ID);
            return worker.getTraining().contains(SimpleWorkerType.StockKeeper);
        }
    }

    public ArrayList<BranchWorker> getAllWorkerFromShift(String branchAddress, LocalDate date, ShiftType type) throws SQLException {
        return BranchWorkerMapper.getWorkersFromShift(branchAddress, date, type);
    }

    public boolean isAvailable(String workerID, LocalDate date, ShiftType type) throws SQLException {
        return constraintMapper.isInDataBase(workerID, date, type);
    }

    public ArrayList<BranchWorker> getAllWorkerFromBranch(String branchAddress) throws SQLException {
        return BranchWorkerMapper.getWorkersByBranch(branchAddress);
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
}