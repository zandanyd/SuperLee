package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.Branch;
import SuperLee.HumenResource.BusinessLayer.BranchController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddBranchController implements ActionListener {
    private JTextField branch;
    private boolean Exist;
    public AddBranchController(JTextField branch){
        this.branch = branch;


    }
    @Override
    public void actionPerformed(ActionEvent e) {
        Exist = false;
        try {
            if(BranchController.getInstance().isExist(branch.getText())){
                JOptionPane.showMessageDialog(null, "This branch already exist", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BranchController.getInstance().createBranch(branch.getText());
        } catch (Exception ex) {

            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Branch successfully added ", "New Branch", JOptionPane.INFORMATION_MESSAGE);

    }
    public ArrayList<String> getAllBranches(){
        ArrayList<String> addresses = new ArrayList<>();
        try {
            for(Branch b: BranchController.getInstance().getListOfAllBranches()){
                addresses.add(b.getAddress());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return addresses;

    }
}
//if (workerTrainingsComboBox.getSelectedItem().equals("Cashier")) {
//                if(!workerController.isTrainingExist(SimpleWorkerType.Cashier, workerID)) {
//                    workerController.addTraining(workerID, SimpleWorkerType.Cashier);
//                    JOptionPane.showMessageDialog(null, "Cashier training added successfully to " + "\n" + "Worker's ID: " + workerID, "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
//                }
//                else{
//                    JOptionPane.showMessageDialog(null, "This worker already has this training type", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }