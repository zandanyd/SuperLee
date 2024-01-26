package SuperLee.HumenResource.GUI;

import SuperLee.HumenResource.BusinessLayer.BranchController;
import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.SimpleWorkerType;
import SuperLee.HumenResource.BusinessLayer.WorkerController;
import SuperLee.Transport.BusinessLayer.LicenseType;
import SuperLee.Transport.BusinessLayer.TrainingType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class NewWorkerController implements ActionListener {
    JTextField firstName;
    JTextField lastName;
    JTextField id;
    JComboBox branch;
    JTextField bank;
    JTextField wage;
    JTextField employmentCondition;
    JComboBox year;
    JComboBox day;
    JComboBox month;
    JComboBox job;
    JComboBox licence;
    JComboBox DriverTraining;
    public NewWorkerController(JTextField firstName, JTextField lastName, JTextField id, JComboBox branch, JTextField bank, JTextField wage, JTextField employmentCondition, JComboBox year, JComboBox day, JComboBox month, JComboBox job, JComboBox licence, JComboBox DriverTraining) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.branch = branch;
        this.id = id;
        this.job= job;
        this.year = year;
        this.month = month;
        this.day = day;
        this.bank = bank;
        this.DriverTraining = DriverTraining;
        this.licence = licence;
        this.employmentCondition = employmentCondition;
        this.wage = wage;
    }

    public static String[] getBranches() {
        String[] branches;
        try {
            return BranchController.getInstance().getAllBranchesAddress();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            if(WorkerController.getInstance().isExist(id.getText())){
                JOptionPane.showMessageDialog(null, "this worker already exist in the system", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        LocalDate date = LocalDate.of(2023 - year.getSelectedIndex(),  month.getSelectedIndex()+1, day.getSelectedIndex()+1);
        if (job.getSelectedItem().equals("Driver")) {
            LicenseType licenseType;
            switch (licence.getSelectedIndex()) {
                case 1 -> licenseType = LicenseType.Light;
                case 2 -> licenseType = LicenseType.Medium;
                case 3 -> licenseType = LicenseType.Heavy;
                default -> licenseType = LicenseType.Light;
            }
            TrainingType trainingType;
            switch (DriverTraining.getSelectedIndex()+1) {
                case 1 -> trainingType = TrainingType.REFRIGERATED;
                case 2 -> trainingType = TrainingType.FROZEN;
                case 3 -> trainingType = TrainingType.DRY;
                default -> trainingType = TrainingType.REFRIGERATED;
            }
            ArrayList<TrainingType> list = new ArrayList<>();
            list.add(trainingType);

            DriverController.getInstance().createNewDriver(firstName.getText(), lastName.getText(), id.getText(), Integer.parseInt(bank.getText()), Double.parseDouble(wage.getText()), date, employmentCondition.getText(), licenseType,list);

            }


         else {
            SimpleWorkerType NewWorkerType;
            switch (job.getSelectedIndex()) {
                case 1 -> NewWorkerType = SimpleWorkerType.Cashier;
                case 2 -> NewWorkerType = SimpleWorkerType.StockKeeper;
                case 3 -> NewWorkerType = SimpleWorkerType.GeneralWorker;
                case 4 -> NewWorkerType = SimpleWorkerType.Cleaner;
                case 5 -> NewWorkerType = SimpleWorkerType.Usher;
                default -> NewWorkerType= SimpleWorkerType.GeneralWorker;
            }

                WorkerController.getInstance().createNewWorker((String) branch.getSelectedItem(), firstName.getText(), lastName.getText(), id.getText(), Integer.parseInt(bank.getText()), Double.parseDouble(wage.getText()), date, employmentCondition.getText(), NewWorkerType);
            }} catch (Exception exception) {
                JOptionPane.showMessageDialog(null, "something went wrong try again", "Error", JOptionPane.ERROR_MESSAGE);
            }
            JOptionPane.showMessageDialog(null, "Worker successfully added to the system ", "New Worker", JOptionPane.INFORMATION_MESSAGE);


    }
}
