package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWorker extends JPanel {
    private JComboBox<String> employmentConditionComboBox;
    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;
    private JComboBox branchCombo;


    public NewWorker() {
        // Set panel layout
        setLayout(new GridLayout(20, 5, 2, 20));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Add the form fields and labels
        Font labelFont = new Font("Arial", Font.BOLD, 12);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 12);

        // Add the form fields and labels
        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setFont(labelFont);
        JTextField firstNameField = new JTextField();
        firstNameField.setFont(textFieldFont);
        add(firstNameLabel);
        add(firstNameField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        JTextField lastNameField = new JTextField();
        add(lastNameLabel);
        add(lastNameField);

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();
        add(idLabel);
        add(idField);

        JLabel branchLabel = new JLabel("Branch:");
        add(branchLabel);

        String[] branches = NewWorkerController.getBranches();
        branchCombo = new JComboBox<>(branches);
        add(branchCombo);

        JLabel accountLabel = new JLabel("Bank Account:");
        JTextField accountField = new JTextField();
        add(accountLabel);
        add(accountField);

        JLabel wageLabel = new JLabel("Wage:");
        JTextField wageField = new JTextField();
        add(wageLabel);
        add(wageField);

        JLabel employmentConditionLabel = new JLabel("Employment Condition:");
        JTextField employmentConditionField = new JTextField();
        add(employmentConditionLabel);
        add(employmentConditionField);

        JLabel space1 = new JLabel("");
        JLabel hireDateLabel = new JLabel("Hire Date:");
        add(hireDateLabel);
        add(space1);
        // Year Scrollbar
        JLabel yearLabel = new JLabel("Year:");
        Integer[] years = new Integer[101];
        for (int i = 0; i < 101; i++) {
            years[i] = 2023 - i;
        }
        JComboBox<Integer> yearComboBox = new JComboBox<>(years);
        add(yearLabel);
        add(yearComboBox);

        // Month ComboBox
        JLabel monthLabel = new JLabel("Month:");
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        JComboBox<String> monthComboBox = new JComboBox<>(months);
        add(monthLabel);
        add(monthComboBox);

        // Day ComboBox
        JLabel dayLabel = new JLabel("Day:");
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        JComboBox<Integer> dayComboBox = new JComboBox<>(days);
        add(dayLabel);
        add(dayComboBox);

        JLabel JobLabel = new JLabel("job:");
        String[] Jobs = {"Cashier", "StockKeeper", "GeneralWorker", "Cleaner", "Usher", "Driver"};
        JComboBox<String> ComboBox = new JComboBox<>(Jobs);
        add(JobLabel);
        add(ComboBox);

        String[] Licences = {"Light(1000)", "Medium(2000)", "Heavy(5000)"};
        JComboBox<String> ComboBox1 = new JComboBox<>(Licences);
        add(ComboBox1);
        ComboBox1.setVisible(false);
        JLabel DriverLicence = new JLabel("licence:");
        DriverLicence.setVisible(false);
        add(DriverLicence);
        add(ComboBox1);

        String[] Trainings = {"REFRIGERATED", "FROZEN", "DRY"};
        JComboBox<String> ComboBox2 = new JComboBox<>(Trainings);
        add(ComboBox2);
        ComboBox2.setVisible(false);
        JLabel DriverTraining = new JLabel("Training:");
        DriverTraining.setVisible(false);
        add(DriverTraining);
        add(ComboBox2);
        JButton generateButton = new JButton("Submit");
        add(generateButton);
        NewWorkerController newWorkerController = new NewWorkerController(firstNameField, lastNameField, idField, branchCombo, accountField, wageField, employmentConditionField, yearComboBox, monthComboBox, dayComboBox, ComboBox, ComboBox1, ComboBox2);
        generateButton.addActionListener(newWorkerController);
        // Add ActionListener to the checkbox
        ComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) ComboBox.getSelectedItem();
                boolean showOptions = selectedOption.equals("Driver");
                DriverLicence.setVisible(showOptions);
                ComboBox1.setVisible(showOptions);
                DriverTraining.setVisible(showOptions);
                ComboBox2.setVisible(showOptions);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("New Worker Signup");
            frame.setSize(800, 800);
            frame.setLocationRelativeTo(null);

            NewWorker newWorker = new NewWorker();
            frame.getContentPane().add(newWorker);
            frame.setVisible(true);
        });
    }
}
