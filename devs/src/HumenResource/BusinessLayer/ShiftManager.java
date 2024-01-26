package SuperLee.HumenResource.BusinessLayer;

import SuperLee.HumenResource.BusinessLayer.BranchWorker;

import java.time.LocalDate;

public class ShiftManager extends BranchWorker {
    public ShiftManager(String firstName, String lastName, String id, String password, String branchID, int bankAccount, double wage, LocalDate hireDate, String employmentCondition){
        super(firstName, lastName, id, password,branchID, bankAccount, wage, hireDate, employmentCondition);
    }
    public String toString(){
        return String.format("Shift Manager" + "\n" +  super.toString());
    }

}
