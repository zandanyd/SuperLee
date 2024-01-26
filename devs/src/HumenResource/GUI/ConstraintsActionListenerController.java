package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.ShiftType;
import SuperLee.HumenResource.BusinessLayer.WorkerController;
import manueAndLogin.CurrentUserController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class ConstraintsActionListenerController implements ActionListener {

    private WorkerController workerController = WorkerController.getInstance();
    private  ConstraintsModel model;
    private JButton submitButton;
    private LocalDate[] allDates = new LocalDate[7];
    private String id ;
    public ConstraintsActionListenerController(ConstraintsModel table, LocalDate[] alldates, JButton sButton) {
        super();
        model = table;
        allDates = alldates;
        submitButton = sButton;
        id = CurrentUserController.getInstance().getId();
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        for (int i = 0; i < model.getRowCount(); i++)
        {
            for(int j = 1; j < 7; j++)
            {
                if ((Boolean) model.getValueAt(i, j) && i == 0)
                {
                    try {
                        if(!workerController.isAvailable(id, allDates[j - 1], ShiftType.Morning)) {
                            workerController.AddConstraint(id, allDates[j - 1], ShiftType.Morning);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "You already have this constraint!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }                    } catch (Exception ex) {
                        ;                            JOptionPane.showMessageDialog(null, "You already have this constraint!", "Error", JOptionPane.ERROR_MESSAGE);

                    }
                }
                if ((Boolean) model.getValueAt(i, j) && i == 1)
                {
                    try {
                        if(!workerController.isAvailable(id, allDates[j], ShiftType.Evening)) {
                            workerController.AddConstraint(id, allDates[j], ShiftType.Evening);
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "You already have this constraint!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
}
