package SuperLee.HumenResource.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EndWeekMonth extends JPanel {
    private JTextField textField1;
    private JTextField textField2;
    private JTable table;
    private EndWeekMonthController controller;

    public EndWeekMonth() {
        // Create main panel
        JPanel mainPanel = new JPanel();

        // Create button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton addBonus = new JButton("Add bonus");
        addBonus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBonus();
            }
        });
        buttonPanel.add(addBonus);

        // Create data panel
        JPanel dataPanel = new JPanel();
        JLabel label1 = new JLabel("Worker id:");
        textField1 = new JTextField("              ");
        JLabel label2 = new JLabel("bonus:");
        textField2 = new JTextField("               ");
        dataPanel.add(label1);
        dataPanel.add(textField1);
        dataPanel.add(label2);
        dataPanel.add(textField2);

        // Create table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Salary");
        controller = new EndWeekMonthController();

        // Clear text fields
        ArrayList<String[]> data = controller.getData();
        for (int i = 0; i < data.size(); i++) {
            tableModel.addRow(data.get(i));
        }

        table = new JTable(tableModel);

        // Create table panel
        JPanel tablePanel = new JPanel();
        tablePanel.add(new JScrollPane(table));

        // Add panels to the main panel
        mainPanel.add(buttonPanel);
        mainPanel.add(dataPanel);
        mainPanel.add(tablePanel);
        JPanel endMonthPanel = new JPanel(new BorderLayout());
        JButton endMonthButton = new JButton("End Month");
        JPanel endWeekPanel = new JPanel(new BorderLayout());
        JButton endWeekButton = new JButton("End Week");
        endWeekPanel.add(endWeekButton);
        endWeekButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.endWeek();
            }
        });
        endMonthButton.addActionListener(controller);
        endMonthPanel.add(endMonthButton);
        mainPanel.add(endMonthPanel);
        mainPanel.add(endWeekPanel, BorderLayout.SOUTH);
        // Add main panel to this JPanel
        add(mainPanel);
    }

    private void addBonus() {
        String id = textField1.getText().trim();
        String bonus = textField2.getText().trim();

        // Find the row index for the given ID
        int rowIndex = -1;
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 0).equals(id)) {
                rowIndex = i;
                break;
            }
        }

        if (rowIndex != -1) {
            double bo = Double.parseDouble(bonus);
            double salary = Double.parseDouble((String) table.getValueAt(rowIndex, 1));
            salary += bo;
            // Add the bonus to the second column of the matching row
            table.setValueAt(String.valueOf(salary), rowIndex, 1);
        } else {
            JOptionPane.showMessageDialog(null, "no such worker in the system", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("End Week/Month");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(700, 500);
            frame.setLocationRelativeTo(null);
            frame.getContentPane().add(new EndWeekMonth());
            frame.setVisible(true);
        });
    }
}