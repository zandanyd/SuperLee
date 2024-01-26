package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.Transport.BusinessLayer.TrainingType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DriverTrainingController implements ActionListener {
    private DriverController driverController = DriverController.getInstance();

    private JComboBox<String> trainingComboBox;
    private WorkersModel model;
    private JTextField driverIDText;

    public DriverTrainingController(WorkersModel wm, JComboBox<String> tc, JTextField id )
    {
        model = wm;
        trainingComboBox = tc;
        driverIDText = id;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
        String driverID = driverIDText.getText();
        try {
            if(!driverController.isExist(driverID)){
                JOptionPane.showMessageDialog(null, "This worker is not a driver", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (trainingComboBox.getSelectedItem().equals("DRY")) {
                if(!driverController.isTrainingExist(driverID, TrainingType.DRY)) {
                    driverController.addTraining(driverID, TrainingType.DRY);
                    JOptionPane.showMessageDialog(null, "DRY training added successfully to " + "\n" + "Driver's ID: " + driverID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This driver already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (trainingComboBox.getSelectedItem().equals("FROZEN")) {
                if(!driverController.isTrainingExist(driverID, TrainingType.FROZEN)) {
                    driverController.addTraining(driverID, TrainingType.FROZEN);
                    JOptionPane.showMessageDialog(null, "FROZEN training added successfully to " + "\n" + "Driver's ID: " + driverID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This driver already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (trainingComboBox.getSelectedItem().equals("REFRIGERATED")) {
                if(!driverController.isTrainingExist(driverID, TrainingType.REFRIGERATED)) {
                    driverController.addTraining(driverID, TrainingType.REFRIGERATED);
                    JOptionPane.showMessageDialog(null, "REFRIGERATED training added successfully to " + "\n" + "Driver's ID: " + driverID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
                }
                else{
                    JOptionPane.showMessageDialog(null, "This driver already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
                }                      }
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);

    }
}
