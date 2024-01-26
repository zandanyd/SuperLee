package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.DataLayer.BranchWorkerDataMapper;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class BranchController {

    private static BranchController single_instance = null;
    // map of all the Branches in the company
    private BranchDataMapper branchMapper = BranchDataMapper.getInstance();
    private BranchWorkerDataMapper branchWorkerMapper = BranchWorkerDataMapper.getInstance();
    private BranchShiftDataMapper branchShiftMapper = BranchShiftDataMapper.getInstance();
    private BranchController() {
    }
    public static synchronized BranchController getInstance() {
        if(single_instance == null){
            single_instance =  new BranchController();
        }
        return single_instance;
    }

    public ArrayList<Branch> getListOfAllBranches() throws SQLException {
        return branchMapper.getAllBranches();
    }
    public String[] getAllBranchesAddress() throws SQLException {
        ArrayList<Branch> branches = branchMapper.getAllBranches();
        String[] list = new String[branches.size()];
        int i =0;
        for(Branch branch : branches){
            list[i] = branch.getAddress();
            i++;
        }
        return list;
    }
    public void ShowAllBranches() throws SQLException {
        for(Branch branch : branchMapper.getAllBranches()){
            System.out.println(branch);
        }
    }
    //    public ArrayList<Branch> getBranches() throws SQLException {
//        ArrayList<Branch> AllBranches = new ArrayList<>();
//        if(AllBranches == null){
//            AllBranches = branchMapper.getAllBranches();
//        }
//        return AllBranches;
//    }
    public void createBranch(String address) throws Exception {
        if (branchMapper.isInDataBase(address)){
            throw new Exception("This Branch already exist");
        }
        Branch branch = new Branch(address);
        branchMapper.insert(branch);
    }

    public String getBranchIdByWorker(String id) throws SQLException {
        return branchWorkerMapper.getWorker(id).getBranchAddress();
    }
    public Branch findBranch(String id) throws  Exception {
        if(!branchMapper.isInDataBase(id)){
            throw new Exception("could not find branch");
        }
        return branchMapper.get(id);
    }
    public boolean isShiftInArrangement(String branchID, LocalDate date, ShiftType type) throws Exception {
        if(branchShiftMapper.getById(date,type, branchID) == null){
            return false;
        }
        return true;
    }


    public boolean isExist(String address) throws SQLException {
        return branchMapper.isInDataBase(address);
    }
    public void ShowSchedule(String address) throws SQLException {
        Formatter fmt = new Formatter();
        fmt.format("%15s %15s %15s %15s %15s %15s %15s %15s\n", "Shift Type", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
        fmt.format("%15s %15s %15s %15s %15s %15s %15s %15s\n", "Morning", branchMapper.getStartHour(address,1,0) +"-"+ branchMapper.getFinishHour(address,1,0), branchMapper.getStartHour(address,2,0)+"-"+ branchMapper.getFinishHour(address,2,0) ,  branchMapper.getStartHour(address,3,0)+"-"+ branchMapper.getFinishHour(address,3,0),  branchMapper.getStartHour(address,4,0)+"-"+ branchMapper.getFinishHour(address,4,0), branchMapper.getStartHour(address,5,0)+"-"+ branchMapper.getFinishHour(address,5,0),  branchMapper.getStartHour(address,6,0)+"-"+ branchMapper.getFinishHour(address,6,0), branchMapper.getStartHour(address,7,0)+"-"+ branchMapper.getFinishHour(address,7,0));
        fmt.format("%15s %15s %15s %15s %15s %15s %15s %15s\n", "Evening", branchMapper.getStartHour(address,1,1)+"-"+ branchMapper.getFinishHour(address,1,1) , branchMapper.getStartHour(address,2,1)+"-"+ branchMapper.getFinishHour(address,2,1) ,  branchMapper.getStartHour(address,3,1)+"-"+ branchMapper.getFinishHour(address,3,1),  branchMapper.getStartHour(address,4,1)+"-"+ branchMapper.getFinishHour(address,4,1),  branchMapper.getStartHour(address,5,1)+"-"+ branchMapper.getFinishHour(address,5,1),  branchMapper.getStartHour(address,6,1)+"-"+ branchMapper.getFinishHour(address,6,1),  branchMapper.getStartHour(address,7,1)+"-"+ branchMapper.getFinishHour(address,7,1));
        System.out.println(fmt);
    }
    public void setStartHour(String address, int day, int shiftType, String startHour) throws SQLException {
        branchMapper.updateStartHour(address,day,shiftType,startHour);
    }
    public void setFinishHour(String address, int day, int shiftType, String finishHour) throws SQLException {
        branchMapper.updateFinishHour(address,day,shiftType,finishHour);
    }

    public String getStartHour(String address, int day, int shiftType) throws SQLException {
        return branchMapper.getStartHour(address,day,shiftType);
    }
    public String getFinishHour(String address, int day, int shiftType) throws SQLException {
        return branchMapper.getFinishHour(address,day,shiftType);
    }

}
