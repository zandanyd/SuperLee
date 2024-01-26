package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.BranchShift;
import SuperLee.HumenResource.BusinessLayer.BranchShiftController;
import SuperLee.HumenResource.BusinessLayer.DriverShiftController;
import SuperLee.HumenResource.BusinessLayer.ShiftType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddToCancellationList implements ActionListener {
    private JTextField item;
    private JTextField amount;
    JComboBox year;
    JComboBox month;
    JComboBox day;
    JComboBox shiftType;
    JComboBox branch;
    public AddToCancellationList(JTextField Item, JTextField Amount, JComboBox year, JComboBox month, JComboBox day, JComboBox shiftType, JComboBox branch){
        item = Item;
        amount=Amount;
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
        BranchShift shift = null;
        if(shiftType.getSelectedIndex() == 0){
            st = ShiftType.Morning;
        }
        else {
            st = ShiftType.Evening;
        }
        try {
            shift = BranchShiftController.getInstance().findShift(branch.getSelectedItem().toString(), date,st);
            if(shift == null){
                JOptionPane.showMessageDialog(null, "This Shift does not exist in the System", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            else {
                for (int i =0; i< Integer.parseInt(amount.getText());i++){
                    BranchShiftController.getInstance().addCancelItem(branch.getSelectedItem().toString(), date,st,item.getText());
                }
            }
            JOptionPane.showMessageDialog(null, "Item Canceled", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Something Went Wrong Please Try Again", "Error", JOptionPane.ERROR_MESSAGE);

        }

    }
}
