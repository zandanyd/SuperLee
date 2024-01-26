package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NewShiftArrangement extends JPanel {

    private JTable table;
    private NewShiftController controller;
    private JComboBox<String> branchCombo;
    private JComboBox<Integer> yearCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> dayCombo;
    private JComboBox<String> typeCombo;
    private DefaultListModel<String> cancelModel;
    private JPanel mainPanel;

    public NewShiftArrangement() {
        setLayout(new BorderLayout());

        // Create main panel
        mainPanel = new JPanel(new BorderLayout());

        // Create top panel for start button and combo boxes
        JPanel topPanel = new JPanel(new FlowLayout());

        // Create start button
        JButton startButton = new JButton("Show available workers");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller.isExist()) {
                    createTable();
                }
            }
        });

        topPanel.add(startButton);

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
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.NORTH);

        // Add main panel to the frame's content pane
        controller = new NewShiftController(yearCombo, monthCombo, dayCombo, typeCombo, branchCombo);

        // Create submit button


        // Create combo boxes
        JLabel hoursLabel = new JLabel("    start hour:");
        String[] hours = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
        JComboBox<String> hourCombo = new JComboBox<>(hours);
        // Month ComboBox
        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) {
            String minute = String.format("%02d", i); // Format minutes with leading zeros
            minutes[i] = minute;
        }
        JComboBox<String> minutesCombo = new JComboBox<>(minutes);
        // Create combo boxes
        JLabel hoursLabel2 = new JLabel("finish hour:");
        JComboBox<String> hourCombo2 = new JComboBox<>(hours);
        // Month ComboBox
        JComboBox<String> minutesCombo2 = new JComboBox<>(minutes);
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retrieve selected checkboxes
                ArrayList<String> selectedWorkers = new ArrayList<>();
                String manager = null;
                boolean haveManager = false;
                for (int i = 0; i < table.getRowCount(); i++) {
                    boolean isSelected = (boolean) table.getValueAt(i, 0);
                    if (isSelected) {
                        String workerID = (String) table.getValueAt(i, 2);
                        if (((String) table.getValueAt(i, 3)).equals("Shift Manager") && !haveManager) {
                            haveManager = true;
                            manager = workerID;
                        } else {
                            selectedWorkers.add(workerID);
                        }
                    }
                }
                if (haveManager) {
                    controller.createNewShift(selectedWorkers, manager, hourCombo, minutesCombo, hourCombo2, minutesCombo2);
                } else {
                    JOptionPane.showMessageDialog(null, "Shift must have a Shift manager", "Error", JOptionPane.ERROR_MESSAGE);
                }
                // Process the selected workers
                // Add your code here to handle the selected workers
            }
        });
        JPanel panel = new JPanel();
        panel.add(new JLabel("         "));
        panel.add(hoursLabel);
        panel.add(hourCombo);
        panel.add(minutesCombo);
        panel.add(new JLabel("         "));
        panel.add(hoursLabel2);
        panel.add(hourCombo2);
        panel.add(minutesCombo2);
        add(panel, BorderLayout.CENTER);

        add(submitButton, BorderLayout.PAGE_END);
    }

    private void createTable() {
        CheckboxTableModel model = new CheckboxTableModel();
        model.addColumn(""); // Empty header for checkboxes
        model.addColumn("Name");
        model.addColumn("ID");
        model.addColumn("Trainings");

        ArrayList<String[]> list = controller.getAvailableWorkers();
        for (String[] row : list) {
            model.addRow(new Object[]{false, row[0], row[1], row[2]});
        }

        table.setModel(model);
        table.getColumn("").setMaxWidth(20); // Adjust checkbox column width

        // Add checkbox renderer and editor
        TableColumn checkboxColumn = table.getColumnModel().getColumn(0);
        checkboxColumn.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        checkboxColumn.setCellEditor(table.getDefaultEditor(Boolean.class));

        table.setVisible(true);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    class CheckboxTableModel extends DefaultTableModel {
        Class<?>[] columnTypes = new Class[]{Boolean.class, String.class, String.class, String.class};

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnTypes[columnIndex];
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // Only the first column (checkbox) is editable
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("New Shift Arrangement");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 800);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(new NewShiftArrangement());
            frame.setVisible(true);
        });
    }
}
