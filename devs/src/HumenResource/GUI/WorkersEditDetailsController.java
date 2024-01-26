package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.WorkerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkersEditDetailsController implements ActionListener {

    private WorkerController workerController = WorkerController.getInstance();
    private DriverController driverController = DriverController.getInstance();
    private WorkersModel model;
    private JButton submitButton;
    private JComboBox editedDetail;
    private JTextField newText;
    private JTextField idText;


    public WorkersEditDetailsController(WorkersModel wm,JButton sb, JComboBox ed, JTextField nt, JTextField idt){
        model = wm;
        submitButton = sb;
        editedDetail = ed;
        newText = nt;
        idText = idt;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
        if ((editedDetail.getSelectedItem()).equals("Wage")) {
            try {
                double wage = Double.parseDouble(newText.getText());
                if (workerController.isExist(idText.getText())) {
                    workerController.setWage(idText.getText(), wage);
                }
                if (driverController.isExist(idText.getText())) {
                    driverController.setWage(idText.getText(), wage);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if ((editedDetail.getSelectedItem()).equals("Password")) {
            try {
                if (workerController.isExist(idText.getText())) {
                    if (!workerController.findWorker(idText.getText()).getPassword().equals(newText.getText())) {
                        workerController.setWorkerPassword(idText.getText(), newText.getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "The password you just entered is the same password as your current one! please enter a different one", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
                if (driverController.isExist(idText.getText())) {
                    if (!driverController.findDriver(idText.getText()).getPassword().equals(newText.getText())) {
                        driverController.setDriverPassword(idText.getText(), newText.getText());
                    } else {
                        JOptionPane.showMessageDialog(null, "The password you just entered is the same password as your current one! please enter a different one", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if ((editedDetail.getSelectedItem()).equals("Employment condition")) {
            try {
                if (workerController.isExist(idText.getText())) {
                    workerController.setEmploymentCondition(idText.getText(), newText.getText());
                }
                if (driverController.isExist(idText.getText())) {
                    driverController.setEmploymentCondition(idText.getText(), newText.getText());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
            model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
        } else {
            JOptionPane.showMessageDialog(null, "Search term is empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "System successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);

    }
}
