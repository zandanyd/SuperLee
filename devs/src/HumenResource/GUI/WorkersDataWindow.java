package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkersDataWindow extends JPanel {
    private JTable table;
    private WorkersModel tableModel;
    private JComboBox<String> filterComboBox;
    private JLabel filterValueLabel;
    private JTextField filterTextField;
    private JButton searchButton;
    private JLabel branchLabel;
    private JComboBox<String> branchComboBox;
    private JLabel jobLabel;
    private JLabel IDValueLabel;
    private JTextField newIDTextField;
    private JComboBox<String> jobComboBox;
    private JLabel editDetailsLabel;
    private JComboBox<String> editDetailsComboBox;
    private JLabel newValueLabel;
    private JTextField newValueTextField;
    private JButton submitButton;
    private JLabel driversTrainingLabel;
    private JComboBox<String> driversTrainingComboBox;
    private JButton addButton;
    private JLabel driverIDLabel;
    private JTextField driverIDTextField;
    private JButton promoteButton;
    private JLabel workerIDLabel;
    private JTextField workerIDTextField;
    private JLabel addTrainingWorkerIDLabel;
    private JTextField addTrainingWorkerIDTextField;
    private JComboBox<String> trainingToAddComboBox;
    private JLabel trainingsDetailsLabel;
    private JButton addTrainingButton;


    public WorkersDataWindow() {

        setLayout(new BorderLayout());
        tableModel = new WorkersModel();
        table = new JTable();
        table.setModel(tableModel);

        int desiredWidth = 400;
        TableColumn column = table.getColumnModel().getColumn(12);
        column.setPreferredWidth(desiredWidth);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        table.setModel(tableModel);

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; // Align components to the top-left corner
        gbc.insets = new Insets(10, 10, 10, 10); // Add spacing around components


        // Filter options
        JLabel filterLabel = new JLabel("Filter By:");
        String[] filters = {"Worker ID", "Branch", "Job"};
        filterComboBox = new JComboBox<>(filters);
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedFilter = (String) filterComboBox.getSelectedItem();
                switch (selectedFilter) {
                    case "Worker ID":
                        showWorkerIdFilterOptions();
                        break;
                    case "Branch":
                        showBranchFilterOptions();
                        break;
                    case "Job":
                        showJobFilterOptions();
                        break;
                }
            }
        });

        filterValueLabel = new JLabel();
        filterValueLabel.setVisible(false);
        filterTextField = new JTextField(10);
        filterTextField.setVisible(false);

        gbc.gridx = 1;
        gbc.gridy = 0;
        optionsPanel.add(filterLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        optionsPanel.add(filterComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        optionsPanel.add(filterValueLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        optionsPanel.add(filterTextField, gbc);

        branchLabel = new JLabel("Select Branch:");
        branchLabel.setVisible(false);
        branchComboBox = new JComboBox<>(new WorkersActionListenerController().getAllBranches());
        branchComboBox.setVisible(false);

        gbc.gridx = 1;
        gbc.gridy = 4;
        optionsPanel.add(branchLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        optionsPanel.add(branchComboBox, gbc);

        jobLabel = new JLabel("Select Job:");
        jobLabel.setVisible(false);
        jobComboBox = new JComboBox<>(new String[]{"Driver", "Shift Manager", "Cleaner", "Usher", "Stockkeeper", "General Worker", "Cashier"});
        jobComboBox.setVisible(false);

        gbc.gridx = 1;
        gbc.gridy = 4;
        optionsPanel.add(jobLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        optionsPanel.add(jobComboBox, gbc);

        searchButton = new JButton("Search");
        WorkersActionListenerController controller = new WorkersActionListenerController(tableModel, table, filterTextField, searchButton, branchComboBox, jobComboBox, filterComboBox);
        searchButton.addActionListener(controller);

        gbc.gridx = 2;
        gbc.gridy = 5;
        optionsPanel.add(searchButton, gbc);

        add(optionsPanel, BorderLayout.WEST);


        // Edit details options
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcEditPane = new GridBagConstraints();
        gbcEditPane.anchor = GridBagConstraints.NORTHWEST; // Align components to the top-left corner
        gbcEditPane.insets = new Insets(10, 10, 10, 5); // Add spacing around components


        editDetailsLabel = new JLabel("Select Details to Edit:");
        editDetailsComboBox = new JComboBox<>(new String[]{"Wage", "Password", "Employment condition"});
        editDetailsComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) editDetailsComboBox.getSelectedItem();
                switch (selectedOption) {
                    case "Wage":
                        showWageEditOptions();
                        break;
                    case "Password":
                        showPasswordEditOptions();
                        break;
                    case "Employment condition":
                        showECEditOptions();
                        break;
                }
            }
        });
        IDValueLabel = new JLabel("Enter worker's ID: ");
        newIDTextField = new JTextField(10);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 0;
        editPanel.add(IDValueLabel, gbcEditPane);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 0;
        editPanel.add(newIDTextField, gbcEditPane);


        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 1;
        editPanel.add(editDetailsLabel, gbcEditPane);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 1;
        editPanel.add(editDetailsComboBox, gbcEditPane);

        newValueLabel = new JLabel("");
        newValueTextField = new JTextField(10);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 2;
        editPanel.add(newValueLabel, gbcEditPane);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 2;
        editPanel.add(newValueTextField, gbcEditPane);

        submitButton = new JButton("Submit");
        WorkersEditDetailsController edController = new WorkersEditDetailsController(tableModel,submitButton,editDetailsComboBox,newValueTextField,newIDTextField);
        submitButton.addActionListener(edController);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 3;
        gbcEditPane.gridwidth = 2;
        editPanel.add(submitButton, gbcEditPane);

        //Drivers add trainings
        driversTrainingLabel = new JLabel("Select a driver's training to add: ");
        driversTrainingComboBox = new JComboBox<>(new String[]{"FROZEN", "DRY", "REFRIGERATED"});
        driversTrainingComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTrainingOption = (String) driversTrainingComboBox.getSelectedItem();
                switch (selectedTrainingOption) {
                    case "FROZEN":
                        break;
                    case "DRY":
                        break;
                    case "REFRIGERATED":
                        break;
                }
            }
        });
        driverIDLabel = new JLabel("Enter driver's ID: ");
        driverIDTextField = new JTextField(10);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 10;
        editPanel.add(driverIDLabel, gbcEditPane);

        gbcEditPane.gridx = 3;
        gbcEditPane.gridy = 10;
        editPanel.add(driverIDTextField, gbcEditPane);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 12;
        editPanel.add(driversTrainingLabel, gbcEditPane);

        gbcEditPane.gridx = 3;
        gbcEditPane.gridy = 12;
        editPanel.add(driversTrainingComboBox, gbcEditPane);

        addButton = new JButton("Add");
        DriverTrainingController dtController = new DriverTrainingController(tableModel,driversTrainingComboBox, driverIDTextField);
        addButton.addActionListener(dtController);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 13;
        gbcEditPane.gridwidth = 2;
        editPanel.add(addButton, gbcEditPane);

        //Worker add trainings
        trainingsDetailsLabel = new JLabel("Select training to add: ");
        trainingToAddComboBox = new JComboBox<>(new String[]{"Cleaner", "Usher", "Stock Keeper", "General Worker", "Cashier"});
//        trainingToAddComboBox.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String selectedTrainingOption = (String) trainingToAddComboBox.getSelectedItem();
//                switch (selectedTrainingOption) {
//                    case "FROZEN":
//                        break;
//                    case "DRY":
//                        break;
//                    case "REFRIGERATED":
//                        break;
//                }
//            }
//        });
        addTrainingWorkerIDLabel = new JLabel("Enter worker's ID:");
        addTrainingWorkerIDTextField = new JTextField(10);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 14;
        editPanel.add(addTrainingWorkerIDLabel, gbcEditPane);

        gbcEditPane.gridx = 3;
        gbcEditPane.gridy = 14;
        editPanel.add(addTrainingWorkerIDTextField, gbcEditPane);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 15;
        editPanel.add(trainingsDetailsLabel, gbcEditPane);

        gbcEditPane.gridx = 3;
        gbcEditPane.gridy = 15;
        editPanel.add(trainingToAddComboBox, gbcEditPane);

        addTrainingButton = new JButton("Add");
        WorkerTrainingController wtController = new WorkerTrainingController(tableModel, addTrainingWorkerIDTextField, trainingToAddComboBox);
        addTrainingButton.addActionListener(wtController);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 16;
        gbcEditPane.gridwidth = 2;
        editPanel.add(addTrainingButton, gbcEditPane);

        //Shift manager promotion
        workerIDLabel = new JLabel("Enter the ID of the worker that you want to promote : ");
        workerIDTextField = new JTextField(10);

        gbcEditPane.gridx = 0;
        gbcEditPane.gridy = 17;
        editPanel.add(workerIDLabel, gbcEditPane);

        gbcEditPane.gridx = 3;
        gbcEditPane.gridy = 17;
        editPanel.add(workerIDTextField, gbcEditPane);


        promoteButton = new JButton("Promote");
        PromoteWorkerController pwController = new PromoteWorkerController(tableModel, workerIDTextField);
        promoteButton.addActionListener(pwController);

        gbcEditPane.gridx = 1;
        gbcEditPane.gridy = 18;
        gbcEditPane.gridwidth = 2;
        editPanel.add(promoteButton, gbcEditPane);

        add(editPanel, BorderLayout.EAST);



        setSize(800, 600);
        setVisible(true);
    }

    public void showWorkerIdFilterOptions() {
        filterValueLabel.setText("Enter Worker ID:");
        filterValueLabel.setVisible(true);
        filterTextField.setVisible(true);
        branchLabel.setVisible(false);
        branchComboBox.setVisible(false);
        jobLabel.setVisible(false);
        jobComboBox.setVisible(false);
    }

    public void showBranchFilterOptions() {
        filterValueLabel.setText("Select Branch:");
        filterValueLabel.setVisible(true);
        filterTextField.setVisible(false);
        branchLabel.setVisible(true);
        branchComboBox.setVisible(true);
        jobLabel.setVisible(false);
        jobComboBox.setVisible(false);
    }

    public void showJobFilterOptions() {
        filterValueLabel.setText("Select Job:");
        filterValueLabel.setVisible(true);
        filterTextField.setVisible(false);
        branchLabel.setVisible(false);
        branchComboBox.setVisible(false);
        jobLabel.setVisible(true);
        jobComboBox.setVisible(true);
    }

    public void showWageEditOptions() {
        newValueLabel.setText("Enter new wage:");
        newValueTextField.setText("");
        newValueLabel.setVisible(true);
        newValueTextField.setVisible(true);
    }

    public void showPasswordEditOptions() {
        newValueLabel.setText("Enter new password:");
        newValueTextField.setText("");
        newValueLabel.setVisible(true);
        newValueTextField.setVisible(true);
    }

    public void showECEditOptions() {
        newValueLabel.setText("Enter new Employment condition:");
        newValueTextField.setText("");
        newValueLabel.setVisible(true);
        newValueTextField.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame("Company's Employees");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setLocationRelativeTo(null);
                frame.getContentPane().add(new WorkersDataWindow());
                frame.setVisible(true);
            }
        });
    }
}
