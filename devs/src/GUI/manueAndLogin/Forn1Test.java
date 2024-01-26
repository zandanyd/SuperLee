package manueAndLogin;


import SuperLee.Transport.GUI.truckControllerView;
import SuperLee.Transport.BusinessLayer.TruckController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Forn1Test extends JPanel {
    private JTextField licenseField;
    private JTextField modelField;
    private JTextField netWeightField;
    private JTextField maxWeightField;
    private JComboBox<String> truckTypeComboBox;
    private JButton addButton;

    public Forn1Test() {
        // Set up the JPanel
        setLayout(new BorderLayout());

        // Create the components
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        JPanel imagePanel = new JPanel(new FlowLayout());

        JLabel licenseLabel = new JLabel("License Number:");
        JLabel modelLabel = new JLabel("Model:");
        JLabel netWeightLabel = new JLabel("Net Weight:");
        JLabel maxWeightLabel = new JLabel("Maximum Weight:");
        JLabel truckTypeLabel = new JLabel("Truck Type:");
        JLabel truckImageLabel = new JLabel();

        // Load the truck image from the file
        ImageIcon truckIcon = new ImageIcon("C:\\Users\\tomerkatzav\\Desktop\\truckimage.jpg"); // Replace with the actual path to your image file
        Image truckImage = truckIcon.getImage();
        Image scaledTruckImage = truckImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon scaledTruckIcon = new ImageIcon(scaledTruckImage);
        truckImageLabel.setIcon(scaledTruckIcon);

        licenseField = new JTextField();
        modelField = new JTextField();
        netWeightField = new JTextField();
        maxWeightField = new JTextField();

        String[] truckTypes = {"Select..", "Dry", "Cold", "Frozen"};
        truckTypeComboBox = new JComboBox<>(truckTypes);

        addButton = new JButton("Add Truck");
        addButton.setEnabled(false); // Disable the button initially
        // Create an ActionListener to check if all fields are filled
        ActionListener fieldsCheckListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String licenseText = licenseField.getText();
                String modelText = modelField.getText();
                String netWeightText = netWeightField.getText();
                String maxWeightText = maxWeightField.getText();
                String truckType = (String) truckTypeComboBox.getSelectedItem();

                // Check if any of the fields is empty
                if (licenseText.isEmpty() || modelText.isEmpty() || netWeightText.isEmpty() || maxWeightText.isEmpty() || truckType.equals("Select..")) {
                    addButton.setEnabled(false); // Disable the button
                } else {
                    addButton.setEnabled(true); // Enable the button
                }
            }
        };

        // Add the ActionListener to each text field and combo box
        licenseField.addActionListener(fieldsCheckListener);
        modelField.addActionListener(fieldsCheckListener);
        netWeightField.addActionListener(fieldsCheckListener);
        maxWeightField.addActionListener(fieldsCheckListener);
        truckTypeComboBox.addActionListener(fieldsCheckListener);

        TruckController truckController = TruckController.getInstance();
        truckControllerView truckControllerView = new truckControllerView(licenseField, modelField, netWeightField, maxWeightField, truckTypeComboBox, truckController);
        addButton.addActionListener(truckControllerView);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // The code to handle adding a truck
                // ...
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                TransportMenu transportMenu = new TransportMenu();
//                transportMenu.setVisible(true);
//                SwingUtilities.getWindowAncestor(Forn1Test.this).dispose();
                setVisible(false);

            }
        });

        // Set up the layout for the details panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        detailsPanel.add(licenseLabel, gbc);
        gbc.gridy++;
        detailsPanel.add(modelLabel, gbc);
        gbc.gridy++;
        detailsPanel.add(netWeightLabel, gbc);
        gbc.gridy++;
        detailsPanel.add(maxWeightLabel, gbc);
        gbc.gridy++;
        detailsPanel.add(truckTypeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        detailsPanel.add(licenseField, gbc);
        gbc.gridy++;
        detailsPanel.add(modelField, gbc);
        gbc.gridy++;
        detailsPanel.add(netWeightField, gbc);
        gbc.gridy++;
        detailsPanel.add(maxWeightField, gbc);
        gbc.gridy++;
        detailsPanel.add(truckTypeComboBox, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        detailsPanel.add(addButton, gbc);

        // Set up the layout for the image panel
        imagePanel.add(truckImageLabel);

        // Add the panels to the main JPanel
        add(detailsPanel, BorderLayout.CENTER);
        add(imagePanel, BorderLayout.EAST);
        add(backButton, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("Truck Details");
                frame.setSize(500, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setContentPane(new Forn1Test());
                frame.setVisible(true);
            }
        });
    }
}
