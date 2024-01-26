package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class NewShiftController implements ActionListener {
    JComboBox year;
    JComboBox month;
    JComboBox day;
    JComboBox shiftType;
    JComboBox branch;
    LocalDate date;
    ShiftType st;

    public NewShiftController(JComboBox year, JComboBox month, JComboBox day, JComboBox shiftType, JComboBox branch){
        this.shiftType = shiftType;
        this.day = day;
        this.branch = branch;
        this.year =year;
        this.month = month;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    public ArrayList<String[]> getAvailableWorkers(){
        try {
            ArrayList<String[]> res = new ArrayList<>();
            date = LocalDate.of(2023 - year.getSelectedIndex(),  month.getSelectedIndex()+1, day.getSelectedIndex()+1);
            st = ShiftType.valueOf((String) shiftType.getSelectedItem());
            ArrayList<BranchWorker> workers = WorkerController.getInstance().getAllWorkerFromBranch((String) branch.getSelectedItem());
            for(BranchWorker worker : workers){
                if(WorkerController.getInstance().isAvailable(worker.getID(),date,st)){
                    String[] list = new String[3];
                    list[0] = worker.getFirstName()+" "+worker.getLastName();
                    list[1] = worker.getID();
                    if(WorkerController.getInstance().isShiftManager(worker.getID())){
                        list[2] = "Shift Manager";
                    }
                    else {
                        String trainings = "";
                        for(SimpleWorkerType type: ((SimpleWorker) worker).getAllWorkerTypes()){
                            trainings = trainings + type.toString() +" | ";
                        }
                        list[2] = trainings;
                    }
                    res.add(list);

                }
            }
            return res;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "This shift already exist", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public boolean isExist() {
        return branch != null;
    }

    public void createNewShift(ArrayList<String> selectedWorkers, String manager, JComboBox startHour,JComboBox startMinutes, JComboBox endHour,JComboBox endMinutes) {
        try {
            LocalTime start = LocalTime.of(Integer.parseInt((String)  startHour.getSelectedItem()),Integer.parseInt((String)startMinutes.getSelectedItem()));
            LocalTime end = LocalTime.of(Integer.parseInt((String)endHour.getSelectedItem()),Integer.parseInt((String)endMinutes.getSelectedItem()));
            BranchShiftController.getInstance().AssignNewShift(date,st, manager,(String) branch.getSelectedItem(),selectedWorkers,start,end);
            JOptionPane.showMessageDialog(null, "Shift successfully added to the system", "success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "This shift already exist", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
