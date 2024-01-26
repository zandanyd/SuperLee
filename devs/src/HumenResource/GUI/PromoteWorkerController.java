package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.WorkerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PromoteWorkerController implements ActionListener {
    private WorkerController workerController = WorkerController.getInstance();

    private WorkersModel model;
    private JTextField workerIDText;

    public PromoteWorkerController(WorkersModel wm, JTextField it)
    {
        model = wm;
        workerIDText = it;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
        String workerID = workerIDText.getText();
        try {
            if (workerController.isExist(workerID)) {
                if (!workerController.isShiftManager(workerID)) {
                    workerController.PromoteWorkerToShiftManager(workerID);
                    JOptionPane.showMessageDialog(null, workerController.findWorker(workerID).getFirstName() + " " + workerController.findWorker(workerID).getLastName() +" was promoted successfully to shift manager ", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "This worker is already shift manager", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else{
                JOptionPane.showMessageDialog(null, "cant find a worker with the following ID: " + workerID, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
    }
}
