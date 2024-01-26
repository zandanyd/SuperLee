package SuperLee.HumenResource.GUI;

import manueAndLogin.CurrentUserController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShiftManagerAndStockKeeper extends JPanel {
    private boolean isShiftManager = false;
    private JComboBox<String> branchCombo;
    private JComboBox<Integer> yearCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> dayCombo;
    private JComboBox<String> typeCombo;
    private JPanel mainPanel;


    public ShiftManagerAndStockKeeper() {
        // Set panel properties
        setLayout(new BorderLayout());
        if(CurrentUserController.getInstance().getUserJob().equals("Shift Manager")){
            isShiftManager = true;
        }
        else{
            isShiftManager = false;
        }
        // Create main panel
        mainPanel = new JPanel(new BorderLayout());

        // Create top panel for start button and combo boxes
        JPanel topPanel = new JPanel(new FlowLayout());

        // Create start button
        JButton startButton = new JButton("Show upcoming transports");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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
        String[] newArray = new String[branches.length - 1];
        System.arraycopy(branches, 1, newArray, 0, newArray.length);
        branchCombo = new JComboBox<>(newArray);

        topPanel.add(branchLabel);
        topPanel.add(branchCombo);

        if (isShiftManager) {
            JPanel centerPanel = new JPanel();
            JButton AddItem = new JButton("Add Item To Cancel List");
            centerPanel.add(AddItem, BorderLayout.SOUTH);
            JLabel CancelList = new JLabel("Item:");
            JTextField itemField = new JTextField();
            itemField.setPreferredSize(new Dimension(200, 20));
            JLabel amountLabel = new JLabel("Amount:");
            JTextField amountField = new JTextField();
            amountField.setPreferredSize(new Dimension(200, 20));
            centerPanel.add(CancelList);
            centerPanel.add(itemField);
            centerPanel.add(amountLabel);
            centerPanel.add(amountField);
            mainPanel.add(centerPanel, BorderLayout.SOUTH);
            AddItem.addActionListener(new AddToCancellationList(itemField, amountField, yearCombo, monthCombo, dayCombo, typeCombo, branchCombo));
        }
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add main panel to this panel
        add(mainPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new ShiftManagerAndStockKeeper());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
