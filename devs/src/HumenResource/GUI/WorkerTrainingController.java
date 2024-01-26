package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.SimpleWorkerType;
import SuperLee.HumenResource.BusinessLayer.WorkerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkerTrainingController implements ActionListener {
    
    private WorkerController workerController = WorkerController.getInstance();
    private WorkersModel model;
    private JTextField workerIDText;
    private JComboBox<String> workerTrainingsComboBox;
    
    public WorkerTrainingController(WorkersModel wm, JTextField it, JComboBox<String> tc)
    {
        model = wm;
        workerIDText = it;
        workerTrainingsComboBox = tc;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
        String workerID = workerIDText.getText();
        try {
            if(!workerController.isExist(workerID)){
                JOptionPane.showMessageDialog(null, "cant find a worker with the following ID: " + workerID, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (workerTrainingsComboBox.getSelectedItem().equals("Cashier")) {
                if(!workerController.isTrainingExist(SimpleWorkerType.Cashier, workerID)) {
                    workerController.addTraining(workerID, SimpleWorkerType.Cashier);
                    JOptionPane.showMessageDialog(null, "Cashier training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (workerTrainingsComboBox.getSelectedItem().equals("Cleaner")) {
                if(!workerController.isTrainingExist(SimpleWorkerType.Cleaner, workerID)) {
                    workerController.addTraining(workerID, SimpleWorkerType.Cleaner);
                    JOptionPane.showMessageDialog(null, "Cleaner training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (workerTrainingsComboBox.getSelectedItem().equals("Usher")) {
                if(!workerController.isTrainingExist(SimpleWorkerType.Usher, workerID)) {
                    workerController.addTraining(workerID, SimpleWorkerType.Usher);
                    JOptionPane.showMessageDialog(null, "Usher training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (workerTrainingsComboBox.getSelectedItem().equals("General Worker")) {
                if (!workerController.isTrainingExist(SimpleWorkerType.GeneralWorker, workerID)) {
                    workerController.addTraining(workerID, SimpleWorkerType.GeneralWorker);
                    JOptionPane.showMessageDialog(null, "General Worker training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (workerTrainingsComboBox.getSelectedItem().equals("Stock Keeper")) {
                if (!workerController.isTrainingExist(SimpleWorkerType.StockKeeper, workerID)) {
                    workerController.addTraining(workerID, SimpleWorkerType.StockKeeper);
                    JOptionPane.showMessageDialog(null, "Stock Keeper training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
    }
}
