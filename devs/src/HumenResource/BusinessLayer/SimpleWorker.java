package SuperLee.HumenResource.BusinessLayer;

import java.time.LocalDate;
import java.util.LinkedList;


public class SimpleWorker extends BranchWorker {
    // list of all the jobs that the worker can fulfill
    private LinkedList<SimpleWorkerType> Training ;
    public SimpleWorker(String firstName, String lastName, String id, String password, String branchID, int bankAccount, double wage, LocalDate hireDate, String employmentCondition, SimpleWorkerType type){
        super(firstName, lastName, id, password,branchID, bankAccount, wage, hireDate, employmentCondition);
        Training = new LinkedList<SimpleWorkerType>();
        Training.add(type);
    }
    public LinkedList<SimpleWorkerType> getAllWorkerTypes(){
        return this.Training;
    }
    public String toString(){
        return String.format("Simple Worker" + "\n" + "Training to do: " + Training + "\n" + super.toString()) ;
    }
    public void addTraining(SimpleWorkerType simpleWorkerType){
        Training.add(simpleWorkerType);
    }

    public LinkedList<SimpleWorkerType> getTraining() {
        return Training;
    }
}