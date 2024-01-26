package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WatchShiftController implements ActionListener {
    JComboBox year;
    JComboBox month;
    JComboBox day;
    JComboBox shiftType;
    JComboBox branch;
    private GenericShift shift;
    public WatchShiftController(JComboBox year, JComboBox month, JComboBox day, JComboBox shiftType, JComboBox branch){
        this.shiftType = shiftType;
        this.day = day;
        this.branch = branch;
        this.year =year;
        this.month = month;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        LocalDate date = LocalDate.of(2023 - year.getSelectedIndex(),  month.getSelectedIndex()+1, day.getSelectedIndex()+1);
        ShiftType st ;
        if(shiftType.getSelectedIndex() == 0){
            st = ShiftType.Morning;
        }
        else {
            st = ShiftType.Evening;
        }

        if(branch.getSelectedIndex() == 0){
            try {

                shift = DriverShiftController.getInstance().findShift(date,st);
                if(shift == null){
                    JOptionPane.showMessageDialog(null, "This Shift does not exist in the System", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else {
            try {
                shift = BranchShiftController.getInstance().findShift(branch.getSelectedItem().toString(), date,st);
                if(shift == null){
                    JOptionPane.showMessageDialog(null, "This Shift does not exist in the System", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public static String[] getBranches(){
        String[] branches;
        try {
            branches = BranchController.getInstance().getAllBranchesAddress();
            String[] res = new String[branches.length + 1];
            res[0] = "Drivers Shift";
            System.arraycopy(branches, 0, res, 1, branches.length);
            return res;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public String getStartHour(){
        return shift.getStartHour().toString();
    }
    public String getFinishHour(){
        return shift.getFinishHour().toString();
    }
    public ArrayList<String[]> getWorkers(){
        ArrayList<Driver> Drivers;
        ArrayList<BranchWorker> workers;
        ArrayList<String[]> res = new ArrayList<>();
        if(branch.getSelectedIndex() == 0){
            try {
                Drivers = DriverController.getInstance().getAllDrivers();
                for(Driver d: Drivers){
                    String[] list = new String[3];
                    list[0] = d.getFirstName();
                    list[1] = d.getLastName();
                    list[2] = d.getID();
                    res.add(list);
                }
                return res;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        else{
            try {
                workers = BranchShiftController.getInstance().branchSWorkerMapper.getWorkersFromShift((String) branch.getSelectedItem(), shift.getDate(),shift.getType());
                for(BranchWorker d: workers){
                    String[] list = new String[3];
                    list[0] = d.getFirstName();
                    list[1] = d.getLastName();
                    list[2] = d.getID();
                    res.add(list);
                }
                return res;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
    }

    public Collection<String> getCancelItems() {
        try {
            ArrayList<String> res = new ArrayList<>();
            HashMap<String, Integer> map = BranchShiftController.getInstance().getAllCanceledItems((String) branch.getSelectedItem(), shift.getDate(), shift.getType());
            for(String key: map.keySet()){
                res.add(key +" amount: "+ map.get(key));
            }
            return res;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    public void switchWorkers(JTextField id1, JTextField id2){
        try {
            BranchShiftController.getInstance().RemoveWorkerFromShift((String) branch.getSelectedItem(),id1.getText(), shift.getDate(), shift.getType());
            BranchShiftController.getInstance().addWorkerToShift((String) branch.getSelectedItem(),id2.getText(), shift.getDate(), shift.getType());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant perform this kind of switch", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String geFirstName(String id) throws SQLException {
        return WorkerController.getInstance().findWorker(id).getFirstName();

    }
    public String getLastName(String id) throws SQLException {
        return WorkerController.getInstance().findWorker(id).getLastName();
    }
    public boolean isExist(){
        return (shift != null);
    }
}
