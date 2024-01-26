package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.BranchController;
import SuperLee.HumenResource.BusinessLayer.SimpleWorkerType;
import SuperLee.HumenResource.BusinessLayer.WorkerController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class WorkersActionListenerController implements ActionListener {

    private WorkerController workerController = WorkerController.getInstance();
    private BranchController branchController = BranchController.getInstance();
    private  WorkersModel model;
    private JButton submitButton;
    private JTable originalTable;
    private JComboBox<String> filterComboBox;
    private JComboBox<String> branchComboBox;
    private JComboBox<String> jobComboBox;
    private JTextField IDtext;

    public WorkersActionListenerController() {}

    public WorkersActionListenerController(WorkersModel tmodel, JTable table, JTextField id, JButton sButton , JComboBox<String> bcBox, JComboBox<String> jcBox, JComboBox<String> fcBox) {
        super();
        model = tmodel;
        branchComboBox = bcBox;
        jobComboBox = jcBox;
        filterComboBox = fcBox;
        IDtext = id;
        submitButton = sButton;
        originalTable = table;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if((filterComboBox.getSelectedItem()).equals("ID"))
        {
            model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
            String workerID = IDtext.getText();
            int counter = 0;
            for(int i = 0; i < model.getRowCount(); i++)
            {
                if(model.getValueAt(i,0).equals(workerID))
                {
                    counter++;
                }
            }
            Object[][] newData = new Object[counter][13];
            int idx = 0;
            for (int j = 0; j < model.getRowCount(); j++) {
                if (model.getValueAt(j,0).equals(workerID)) {
                    for(int k = 0; k < model.getColumnCount(); k++)
                    {
                        newData[idx][k] = model.getValueAt(j,k);
                    }
                    idx++;
                }
            }
            model.setDataVector(newData, WorkersTableContent.columnNames);
        }
        else if(((String)filterComboBox.getSelectedItem()).equals("Branch"))
        {
            model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
            String branch = (String) branchComboBox.getSelectedItem();
            int counter = 0;
            for(int i = 0; i < model.getRowCount(); i++)
            {
                if(model.getValueAt(i,1).equals(branch))
                {
                    counter++;
                }
            }
            Object[][] newData = new Object[counter][13];
            int idx = 0;
            for (int j = 0; j < model.getRowCount(); j++) {
                if (model.getValueAt(j,1).equals(branch)) {
                    for(int k = 0; k < model.getColumnCount(); k++)
                    {
                        newData[idx][k] = model.getValueAt(j,k);
                    }
                    idx++;
                }
            }
            model.setDataVector(newData, WorkersTableContent.columnNames);
        }
        else if(((String)filterComboBox.getSelectedItem()).equals("Job"))
        {
            model.setDataVector(WorkersTableContent.getInstance().getData(), WorkersTableContent.columnNames);
            String job = (String) jobComboBox.getSelectedItem();
            int counter = 0;
            for(int i = 0; i < model.getRowCount(); i++)
            {
                    if (model.getValueAt(i, 12).equals("Driver")) {
                        counter++;
                    }
                try {
                    if(workerController.isShiftManager((String) model.getValueAt(i,0)))
                    {
                        counter++;
                        continue;
                    }
                    if(!job.equals("Driver") && !job.equals("Shift Manager")) {
                        if (workerController.isTrainingExist(getType(job), (String) model.getValueAt(i, 0))) {
                            counter++;
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            Object[][] newData = new Object[counter][13];
            int idx = 0;
            for (int j = 0; j < model.getRowCount(); j++) {
                    if (model.getValueAt(j, 12).equals("Driver")) {
                        for (int k = 0; k < model.getColumnCount(); k++) {
                            newData[idx][k] = model.getValueAt(j, k);
                        }
                        idx++;
                }
                try {
                    if(workerController.isShiftManager((String) model.getValueAt(j,0)))
                    {
                        for(int k = 0; k < model.getColumnCount(); k++)
                        {
                            newData[idx][k] = model.getValueAt(j,k);
                        }
                        idx++;
                        continue;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                }
                try {
                    if(!job.equals("Driver") && !job.equals("Shift Manager")) {
                        if (workerController.isTrainingExist(getType(job), (String) model.getValueAt(j, 0))) {
                            for (int k = 0; k < model.getColumnCount(); k++) {
                                newData[idx][k] = model.getValueAt(j, k);
                            }
                            idx++;
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            model.setDataVector(newData, WorkersTableContent.columnNames);
        }else {
            JOptionPane.showMessageDialog(null, "Search term is empty", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public String[] getAllBranches()
    {
        try {
            return branchController.getAllBranchesAddress();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleWorkerType getType(String job)
    {
        if(job.equals("Usher"))
            return SimpleWorkerType.Usher;
        if(job.equals("Cleaner"))
            return SimpleWorkerType.Cleaner;
        if(job.equals("GeneralWorker"))
            return SimpleWorkerType.GeneralWorker;
        if(job.equals("Cashier"))
            return SimpleWorkerType.Cashier;
        if(job.equals("StockKeeper"))
            return SimpleWorkerType.StockKeeper;
        return null;
    }
}
