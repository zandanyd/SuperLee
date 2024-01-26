package SuperLee.HumenResource.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class constraintsAssignments extends JPanel {
    private JTable table;
    public ConstraintsActionListenerController Acontroller;
    private JButton submitButton;
    private JLabel weekDatesLabel;
    private LocalDate[] allWeekDates = new LocalDate[7];
    final DefaultListModel<JCheckBox> allCheckBoxes = new DefaultListModel<>();
    public constraintsAssignments() {

        table = new JTable();

        ConstraintsModel model = new ConstraintsModel();
        table.setModel(model);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        add(submitButton, BorderLayout.SOUTH);

        weekDatesLabel = new JLabel();
        add(weekDatesLabel, BorderLayout.NORTH);

        // Calculate and set week dates label
        LocalDate today = LocalDate.now();
        LocalDate saturday = today.with(DayOfWeek.MONDAY);
        LocalDate sunday = today.with(java.time.DayOfWeek.SUNDAY);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String weekDates = saturday.format(formatter) + " - " + sunday.format(formatter);
        weekDatesLabel.setText(weekDates);
        for(int i = 0; i < allWeekDates.length; i++)
        {
            if(i == 0) {
                allWeekDates[i] = sunday;
                continue;
            }
            else if(i != 0 && i != allWeekDates.length - 1){
                allWeekDates[i] = LocalDate.of(saturday.getYear(), saturday.getMonthValue(), sunday.getDayOfMonth() + i);
            }
            if(i == allWeekDates.length - 1){
                allWeekDates[i] = saturday;
                continue;
            }
        }

        for(int i = 0; i < model.getRowCount(); i++)
        {
            for(int j = 1; j < 7; j++)
            {
                model.setValueAt(false, i, j);
            }
        }
        Acontroller = new ConstraintsActionListenerController(model, allWeekDates, submitButton);
        submitButton.addActionListener(Acontroller);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new constraintsAssignments());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}
