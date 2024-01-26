package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.BranchController;
import SuperLee.TransportWorkersController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class upcomingTransportController implements ActionListener {
    private TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> branchComboBox;
    private JTextArea transportInfo;



    public upcomingTransportController(JComboBox<Integer> yearCombo,JComboBox<String> monthCombo, JComboBox<Integer> dayCombo, JTextArea info, JComboBox<String> branchCombo)
    {
        yearComboBox = yearCombo;
        monthComboBox = monthCombo;
        dayComboBox = dayCombo;
        branchComboBox = branchCombo;
        transportInfo = info;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            LocalDate date = LocalDate.of(2023 - yearComboBox.getSelectedIndex(), monthComboBox.getSelectedIndex() + 1, dayComboBox.getSelectedIndex() + 1);
            String dateInString = date.toString();
            String branch = branchComboBox.getSelectedItem().toString();
            String[] arrOfDetails = transportWorkersController.displayAllTransportsBySiteAndDate(branch, dateInString).split(",");

            transportInfo.setText(String.join("",arrOfDetails));

        }
        catch (Exception exception){
            JOptionPane.showMessageDialog(null, "No Upcoming Transports", "No Upcoming Transports", JOptionPane.INFORMATION_MESSAGE);
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

}
