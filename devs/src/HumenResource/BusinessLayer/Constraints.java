package SuperLee.HumenResource.BusinessLayer;

import java.time.LocalDate;

public class Constraints {
    private LocalDate Date;
    private ShiftType shiftType;
    // list of all possible jobs that the worker submits per constraint
    private String WorkerID;
    public Constraints(LocalDate date, ShiftType shiftType, String workerID){
        this.Date = date;
        this.shiftType = shiftType;
        this.WorkerID = workerID;
    }

    public String getWorkerID() {
        return WorkerID;
    }

    public LocalDate getDate() {
        return Date;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public String toString(){
        return String.format("Date: " + Date + "Time: " + shiftType);
    }
}
