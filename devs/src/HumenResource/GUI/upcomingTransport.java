package SuperLee.HumenResource.GUI;

import javax.swing.*;
import java.awt.*;

public class upcomingTransport extends JPanel {
    private JComboBox<String> branchCombo;
    private JComboBox<Integer> yearCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> dayCombo;
    private JTextArea info;


    private JPanel mainPanel;
    private JPanel panel;

    public upcomingTransport() {

        setLayout(new BorderLayout());



        mainPanel = new JPanel(new BorderLayout());
        panel = new JPanel(); // Create JPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add spacing around components


        JPanel topPanel = new JPanel(new FlowLayout());
        JPanel bottomPanel = new JPanel(new FlowLayout());
        // Create combo boxes
        JLabel yearLabel = new JLabel("Year:");
        Integer[] years = new Integer[101];
        for (int i = 0; i < 101; i++) {
            years[i] = 2023 - i;
        }
        yearCombo = new JComboBox<>(years);
        topPanel.add(yearLabel);
        topPanel.add(yearCombo);

        // Month ComboBox
        JLabel monthLabel = new JLabel("Month:");
        String[] months = new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        monthCombo = new JComboBox<>(months);
        topPanel.add(monthLabel);
        topPanel.add(monthCombo);

        // Day ComboBox
        JLabel dayLabel = new JLabel("Day:");
        Integer[] days = new Integer[31];
        for (int i = 0; i < 31; i++) {
            days[i] = i + 1;
        }
        dayCombo = new JComboBox<>(days);
        topPanel.add(dayLabel);
        topPanel.add(dayCombo);


        JLabel branchLabel = new JLabel("Branch:");
        String[] branches = upcomingTransportController.getBranches();
        assert branches != null;
        branchCombo = new JComboBox<>(branches);

        topPanel.add(branchLabel);
        topPanel.add(branchCombo);

        info = new JTextArea("");
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(info, gbc);

        upcomingTransportController controller = new upcomingTransportController(yearCombo, monthCombo, dayCombo, info, branchCombo);
        JButton startButton = new JButton("Search");
        startButton.addActionListener(controller);
        topPanel.add(startButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Upcoming Transport");
            frame.setSize(800, 900);
            frame.setLocationRelativeTo(null);

            upcomingTransport upcomingtransport = new upcomingTransport();
            frame.getContentPane().add(upcomingtransport);
            frame.setVisible(true);
        });
    }
}
