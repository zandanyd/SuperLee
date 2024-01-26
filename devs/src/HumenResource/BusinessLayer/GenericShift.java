package SuperLee.HumenResource.BusinessLayer;

import java.time.LocalDate;
import java.time.LocalTime;

public  class GenericShift {
    protected LocalDate Date;
    protected ShiftType Type;
    // List of the shift workers organized by the type of worker
//    protected LinkedList<GenericWorker> ListOfWorkers;
    protected LocalTime StartHour;
    protected LocalTime FinishHour;
    public GenericShift(LocalDate Date,ShiftType Type){
        this.Date = Date;
        this.Type = Type;
//        this.ListOfWorkers = new LinkedList<GenericWorker>();
        //Default start and finish hours
        LocalTime DefaultMorningStartHour = LocalTime.of(8,0);
        LocalTime DefaultNightStartHour = LocalTime.of(15,0);
        LocalTime DefaultMorningFinishHour = LocalTime.of(15,0);
        LocalTime DefaultNightFinishHour = LocalTime.of(22,0);
        if(this.Type == ShiftType.Morning){
            this.StartHour = DefaultMorningStartHour;
            this.FinishHour = DefaultMorningFinishHour;
        }
        else {
            this.StartHour = DefaultNightStartHour;
            this.FinishHour = DefaultNightFinishHour;
        }
    }
    public LocalDate getDate() {
        return Date;
    }
    public ShiftType getType() {
        return Type;
    }
    public LocalTime getStartHour(){
        return this.StartHour;
    }
    public LocalTime getFinishHour(){
        return this.FinishHour;
    }
    //    public LinkedList<GenericWorker>getListOfWorkers() {
//        return ListOfWorkers;
//    }
    public void setDate(LocalDate date) {
        Date = date;
    }
    public void setType(ShiftType type) {
        Type = type;
    }

    public void setStartHour(LocalTime time){
        this.StartHour = time;
    }
    public void setFinishHour(LocalTime time){
        this.FinishHour = time;
    }
    public void PrintShiftDetails(){
        System.out.println("Date:" + this.getDate() + "\n" + "Shift Time: " + this.getType() + " Started at:" + this.getStartHour() + " Ended at:" + this.getFinishHour());
    }
}
