package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class WorkersTableContent {
    private WorkerController workerController = WorkerController.getInstance();
    private DriverController driverController = DriverController.getInstance();
    private BranchController branchController = BranchController.getInstance();
    public static String[] columnNames = {"ID", "Branch", "First Name", "Last Name", "Password", "User Name", "Bank Account", "Wage", "Employment Condition", "Hire Date", "Number Of Shifts Per Week", "Work Hours", "List Of Jobs"};
    private static WorkersTableContent instance =null;
    private WorkersTableContent() {

    }
    public static synchronized WorkersTableContent getInstance() {
        if(instance == null){
            instance =  new WorkersTableContent();
        }
        return instance;
    }
    public Object[][] getData()
    {
        ArrayList<BranchWorker> data = new ArrayList<>();
        ArrayList<Driver> driverList = new ArrayList<>();
        ArrayList<Branch> allBranches;
        try {
            allBranches = branchController.getListOfAllBranches();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for(Branch branch : allBranches)
        {
            try {
                for(BranchWorker worker : workerController.getAllWorkerFromBranch(branch.getAddress()))
                {
                    data.add(worker);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            for(Driver driver : driverController.getAllDrivers())
            {
                driverList.add(driver);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int k = 0;
        Object[][] allData = new Object[data.size() + driverList.size()][13];
        for(int i = 0; i < data.size(); i++)
        {
            ArrayList<String> arrJobs = new ArrayList<>();
            String[] allJobs;
            int counter = 0;
            allData[i][0] = data.get(i).getID();
            allData[i][1] = data.get(i).getBranchAddress();
            allData[i][2] = data.get(i).getFirstName();
            allData[i][3] = data.get(i).getLastName();
            allData[i][4] = data.get(i).getPassword();
            allData[i][5] = data.get(i).getID();
            allData[i][6] = data.get(i).getBankAccount();
            allData[i][7] = data.get(i).getWage();
            allData[i][8] = data.get(i).getEmploymentCondition();
            allData[i][9] = data.get(i).getHireDate();
            allData[i][10] = data.get(i).getNumberOfShiftsPerWeek();
            allData[i][11] = data.get(i).getWorkHours();
            try {
                if(workerController.isShiftManager(data.get(i).getID()))
                {
                    allData[i][12] = "Shift manager";
                }
                else {
                    SimpleWorker simpleWorker = (SimpleWorker) data.get(i);
                    String trainings = "";
                    for(SimpleWorkerType type : simpleWorker.getAllWorkerTypes())
                    {
                        trainings = trainings + " " + type.toString();
                    }
                    allData[i][12] = trainings;
                }
//                allJobs = new String[arrJobs.size()];
//                for(String job : arrJobs)
//                {
//                    allJobs[counter] = job;
//                    counter++;
//                }
//                allData[i][12] = allJobs;

             } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            k++;
        }
        for(int i = 0; i < driverList.size(); i++)
        {
            ArrayList<String> arrJobs = new ArrayList<>();
            String[] allJobs;
            int counter = 0;
            allData[k][0] = driverList.get(i).getID();
            allData[k][1] = "SuperLee";
            allData[k][2] = driverList.get(i).getFirstName();
            allData[k][3] = driverList.get(i).getLastName();
            allData[k][4] = driverList.get(i).getPassword();
            allData[k][5] = driverList.get(i).getID();
            allData[k][6] = driverList.get(i).getBankAccount();
            allData[k][7] = driverList.get(i).getWage();
            allData[k][8] = driverList.get(i).getEmploymentCondition();
            allData[k][9] = driverList.get(i).getHireDate();
            allData[k][10] = driverList.get(i).getNumberOfShiftsPerWeek();
            allData[k][11] = driverList.get(i).getWorkHours();
            allData[k][12] = "Driver";
            k++;
        }
        return allData;
    }
}
