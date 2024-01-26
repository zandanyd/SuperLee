package SuperLee.HumenResource.BusinessLayer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class BranchShift extends GenericShift {
    //List of items that been cancelled in this shift
    private String Manager;
    private String BranchID;
    BranchShift(LocalDate Date, ShiftType Type, String manager, String branchID){
        super(Date,Type);
        this.Manager = manager;
        BranchID = branchID;
    }
    public String getBranchAddress() {
        return BranchID;
    }
    public String getManager() {
        return Manager;
    }
    public void setManager(String manager) {
        Manager = manager;
    }

    public void EndShift(ArrayList<BranchWorker> branchShiftWorkers, ShiftManager manager) throws SQLException {
        double WorkHours = 0;
        double minutes = Math.abs(this.getFinishHour().getMinute() - this.getStartHour().getMinute()) / 60.0;
        double hours = this.getFinishHour().getHour() - this.getStartHour().getHour();
        WorkHours = hours + minutes;
        manager.setWorkHours(manager.getWorkHours() + WorkHours);
        for (BranchWorker worker : branchShiftWorkers) {
            worker.setWorkHours(worker.getWorkHours() + WorkHours);
        }
    }

}
