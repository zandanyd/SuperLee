package SuperLee.HumenResource.BusinessLayer;

import java.time.LocalDate;

public abstract class  BranchWorker extends GenericWorker {
    private String BranchAddress;
    BranchWorker(String firstName, String lastName, String id, String password, String BranchAddress, int bankAccount, double wage, LocalDate hireDate, String employmentCondition) {
        super(firstName, lastName, id, password, bankAccount, wage, hireDate, employmentCondition);
        this.BranchAddress = BranchAddress;
    }

    public String getBranchAddress() {
        return BranchAddress;
    }

    public void setBranchAddress(String branchID) {
        this.BranchAddress = branchID;
    }


}
