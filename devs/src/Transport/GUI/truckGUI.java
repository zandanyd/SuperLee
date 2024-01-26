package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.TruckController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class truckGUI extends JFrame {
    private JTextField licenseField;
    private JTextField modelField;
    private JTextField netWeightField;
    private JTextField maxWeightField;
    private JComboBox<String> truckTypeComboBox;
    private JButton addButton;

    public truckGUI() {
        // Set up the JFrame
        setTitle("Truck Details");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the components
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
                TransportMenu transportMenu = new TransportMenu();
                transportMenu.setVisible(true);
                dispose();
            }
        });

        // Set up the layout
        JPanel contentPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        contentPane.add(licenseLabel, gbc);
        gbc.gridy++;
        contentPane.add(modelLabel, gbc);
        gbc.gridy++;
        contentPane.add(netWeightLabel, gbc);
        gbc.gridy++;
        contentPane.add(maxWeightLabel, gbc);
        gbc.gridy++;
        contentPane.add(truckTypeLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        contentPane.add(licenseField, gbc);
        gbc.gridy++;
        contentPane.add(modelField, gbc);
        gbc.gridy++;
        contentPane.add(netWeightField, gbc);
        gbc.gridy++;
        contentPane.add(maxWeightField, gbc);
        gbc.gridy++;
        contentPane.add(truckTypeComboBox, gbc);
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(addButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.insets = new Insets(10, 10, 10, 0);
        contentPane.add(backButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        contentPane.add(truckImageLabel, gbc);

        setContentPane(contentPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                truckGUI truckGUI = new truckGUI();
            }
        });
    }
}
