package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class WatchShifts extends JPanel {

    private JTable table;
    private WatchShiftController controller;
    private JComboBox<String> branchCombo;
    private JComboBox<Integer> yearCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> dayCombo;
    private JComboBox<String> typeCombo;
    private JLabel startField;
    private JLabel endField;
    private DefaultListModel<String> cancelModel;
    private JPanel mainPanel;
    private JPanel mainPanel2;
    private JPanel panel; // Updated JPanel

    public WatchShifts() {
        // Set panel layout
        setLayout(new BorderLayout());

        controller = new WatchShiftController(yearCombo, monthCombo, dayCombo, typeCombo, branchCombo);

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());
        panel = new JPanel(); // Create JPanel

        // Create top panel for start button and combo boxes
        JPanel topPanel = new JPanel(new FlowLayout());

        // Create start button
        JButton startButton = new JButton("Search");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.isExist()) {
                    createTable();
                    startField.setText(controller.getStartHour());
                    endField.setText(controller.getFinishHour());
                    Switch();
                }
            }
        });

        topPanel.add(startButton);
        topPanel.add(new JLabel("         "));
        // Create combo boxes
        JLabel yearLabel = new JLabel("    Year:");
        Integer[] years = new Integer[101];
        for (int i = 0; i < 101; i++) {
            years[i] = 2023 - i;
        }
        yearCombo = new JComboBox<>(years);
        topPanel.add(yearLabel);
        topPanel.add(yearCombo);

        // Month ComboBox
        JLabel monthLabel = new JLabel("    Month:");
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        topPanel.add(monthLabel);
        topPanel.add(monthCombo);

        // Day ComboBox
        JLabel dayLabel = new JLabel("    Day:");
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayCombo = new JComboBox<>(days);
        topPanel.add(dayLabel);
        topPanel.add(dayCombo);

        JLabel shiftType = new JLabel("    Shift Type:");
        String[] Types = new String[]{
                "Morning", "Evening",
        };
        typeCombo = new JComboBox<>(Types);
        topPanel.add(shiftType);
        topPanel.add(typeCombo);

        JLabel branchLabel = new JLabel("    branch:");
        String[] branches = WatchShiftController.getBranches();
        assert branches != null;
        branchCombo = new JComboBox<>(branches);

        topPanel.add(branchLabel);
        topPanel.add(branchCombo);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create scrollable table (initially hidden)
        JScrollPane tableScrollPane = new JScrollPane();
        table = new JTable();
        tableScrollPane.setViewportView(table);
        tableScrollPane.setVisible(true);
        JLabel tableLabel = new JLabel("workers", SwingConstants.CENTER);

        mainPanel.add(tableLabel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.AFTER_LAST_LINE);

        // Add main panel to the panel
        add(mainPanel, BorderLayout.NORTH);
        mainPanel2 = new JPanel();
        mainPanel2.add(new JLabel("start hour:"));
        startField = new JLabel();

        mainPanel2.add(startField);
        mainPanel2.add(new JLabel("finish hour:"));
        endField = new JLabel();

        mainPanel2.add(endField);
        add(mainPanel2, BorderLayout.CENTER);
        controller = new WatchShiftController(yearCombo, monthCombo, dayCombo, typeCombo, branchCombo);
        startButton.addActionListener(controller);

        // Display the panel
        setVisible(true);
    }

    public void Switch() {
        JPanel switchPanel = new JPanel(new FlowLayout());
        switchPanel.add(new JLabel("        Worker1 ID:"));
        JTextField worker1 = new JTextField();
        worker1.setPreferredSize(new Dimension(200, 20));
        switchPanel.add(worker1);
        switchPanel.add(new JLabel("Worker2 ID:"));
        JTextField worker2 = new JTextField();
        worker2.setPreferredSize(new Dimension(200, 20));
        switchPanel.add(worker2);
        JButton switchButton = new JButton("Switch between two workers");
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.switchWorkers(worker1, worker2);
                int rowIndex = -1;
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 2).equals(worker1.getText())) {
                        rowIndex = i;
                        System.out.println(i);
                        break;
                    }
                }
                if (rowIndex != -1) {
                    // Add the bonus to the second column of the matching row
                    table.setValueAt(worker2.getText(), rowIndex, 2);
                    try {
                        table.setValueAt(controller.geFirstName(worker2.getText()), rowIndex, 0);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        table.setValueAt(controller.getLastName(worker2.getText()), rowIndex, 1);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        switchPanel.add(switchButton);
        mainPanel2.add(switchPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        // Set table properties
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("ID");
        ArrayList<String[]> list = controller.getWorkers();
        for (int i = 0; i < list.size(); i++) {
            model.addRow(list.get(i));
        }

        table.setModel(model);
        table.setVisible(true);
        JPanel cancelPanel = new JPanel(new BorderLayout());
        cancelModel = new DefaultListModel<>();
        cancelModel.addAll(controller.getCancelItems());
        JList<String> cancelList = new JList<>(cancelModel);
        JScrollPane CancelScrollPane = new JScrollPane(cancelList);
        cancelPanel.add(CancelScrollPane, BorderLayout.CENTER);
        add(cancelPanel, BorderLayout.AFTER_LAST_LINE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Watch Shifts");
            frame.setSize(800, 900);
            frame.setLocationRelativeTo(null);

            WatchShifts watchShifts = new WatchShifts();
            frame.getContentPane().add(watchShifts);
            frame.setVisible(true);
        });
    }
}
