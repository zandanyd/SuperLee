package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.GenericWorker;
import SuperLee.HumenResource.BusinessLayer.WorkerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class EndWeekMonthController implements ActionListener  {
    private JTextField ID;
    private JTextField Bonus;
    public EndWeekMonthController(){

    }
    public ArrayList<String[]> getData(){
        ArrayList<String[]> res = new ArrayList<>();
        try {
            for (GenericWorker worker: WorkerController.getInstance().getAllWorkers()){
                String[] array = new String[2];
                array[0] = worker.getID();
                array[1] = String.valueOf((worker.getWorkHours()*worker.getWorkHours()));
                res.add(array);

            }
            for (GenericWorker worker: DriverController.getInstance().getAllDrivers()){
                String[] array = new String[2];
                array[0] = worker.getID();
                array[1] = String.valueOf((worker.getWorkHours()*worker.getWorkHours()));
                res.add(array);

            }
            return res;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }
    public void endWeek(){
        try {

            WorkerController.getInstance().EndWeek();
            DriverController.getInstance().EndWeek();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Week end successfully", "End week", JOptionPane.INFORMATION_MESSAGE);

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            DriverController.getInstance().resetWorkHours();
            WorkerController.getInstance().resetWorkHours();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Month end successfully", "End Month", JOptionPane.INFORMATION_MESSAGE);

    }
}
