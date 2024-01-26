//package Components;//import java.awt.Color;
//import javax.swing.*;
//
//public class Forn1Test extends JPanel {
//   public Forn1Test(){
//       initComponents();
//
//   }
//    private void initComponents() {
//
////        JLabel jLabel1 = new JLabel();
//
//        setBackground(new java.awt.Color(242, 242, 242));
//
////        jLabel1.setFont(new java.awt.Font("sansserif", 0, 36)); // NOI18N
////        jLabel1.setForeground(new java.awt.Color(106, 106, 106));
////        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
////        jLabel1.setText("Form 1");
//
//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                .addContainerGap()
////                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
//                                .addContainerGap())
//        );
//        layout.setVerticalGroup(
//                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                                .addGap(128, 128, 128)
////                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                                .addGap(125, 125, 125))
//        );
//    }// </editor-fold>//GEN-END:initComponents
//
//}
////import java.awt.*;
////import java.awt.event.ActionEvent;
////import java.awt.event.ActionListener;
////import javax.swing.*;
////
////
////public class Forn1Test extends JPanel {
////    private JTextField licenseField;
////    private JTextField modelField;
////    private JTextField netWeightField;
////    private JTextField maxWeightField;
////    private JComboBox<String> truckTypeComboBox;
////    private JButton addButton;
////    public Forn1Test() {
////        initComponents();
////    }
////
////    private void initComponents() {
////        setBackground(new Color(242, 242, 242));
////
////        JLabel licenseLabel = new JLabel("License Number:");
////        JLabel modelLabel = new JLabel("Model:");
////        JLabel netWeightLabel = new JLabel("Net Weight:");
////        JLabel maxWeightLabel = new JLabel("Maximum Weight:");
////        JLabel truckTypeLabel = new JLabel("Truck Type:");
////        JLabel truckImageLabel = new JLabel();
//////
//////        ImageIcon truckIcon = new ImageIcon("C:\\Users\\tomerkatzav\\Desktop\\truckimage.jpg"); // Replace with the actual path to your image file
//////        Image truckImage = truckIcon.getImage();
//////        Image scaledTruckImage = truckImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
//////        ImageIcon scaledTruckIcon = new ImageIcon(scaledTruckImage);
//////        truckImageLabel.setIcon(scaledTruckIcon);
////
////        licenseField = new JTextField();
////        modelField = new JTextField();
////        netWeightField = new JTextField();
////        maxWeightField = new JTextField();
////
////        String[] truckTypes = {"Select..", "Dry", "Cold", "Frozen"};
////        truckTypeComboBox = new JComboBox<>(truckTypes);
////
////        addButton = new JButton("Add Truck");
////        addButton.setEnabled(false);
////
////        ActionListener fieldsCheckListener = new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                String licenseText = licenseField.getText();
////                String modelText = modelField.getText();
////                String netWeightText = netWeightField.getText();
////                String maxWeightText = maxWeightField.getText();
////                String truckType = (String) truckTypeComboBox.getSelectedItem();
////
////                if (licenseText.isEmpty() || modelText.isEmpty() || netWeightText.isEmpty() || maxWeightText.isEmpty() || truckType.equals("Select..")) {
////                    addButton.setEnabled(false);
////                } else {
////                    addButton.setEnabled(true);
////                }
////            }
////        };
////
////        licenseField.addActionListener(fieldsCheckListener);
////        modelField.addActionListener(fieldsCheckListener);
////        netWeightField.addActionListener(fieldsCheckListener);
////        maxWeightField.addActionListener(fieldsCheckListener);
////        truckTypeComboBox.addActionListener(fieldsCheckListener);
////
////
//////        TruckController truckController = TruckController.getInstance();
////        SuperLee.GUI.truckControllerView truckControllerVie = new SuperLee.GUI.truckControllerView(licenseField, modelField, netWeightField, maxWeightField, truckTypeComboBox);
////        addButton.addActionListener(SuperLee.GUI.truckControllerView);
////
////
////        addButton.addActionListener(new ActionListener() {
////            @Override
////            public void actionPerformed(ActionEvent e) {
////                // The code to handle adding a truck
////                // ...
////            }
////        });
////
////        JButton backButton = new JButton("Back");
////        backButton.addActionListener(new ActionListener() {
////            public void actionPerformed(ActionEvent e) {
//////                TransportMenu transportMenu = new TransportMenu();
//////                transportMenu.setVisible(true);
////                // dispose();
////            }
////        });
////
////        GroupLayout layout = new GroupLayout(this);
////        setLayout(layout);
////        layout.setHorizontalGroup(
////                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(licenseLabel)
////                                .addContainerGap(340, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(modelLabel)
////                                .addContainerGap(363, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(netWeightLabel)
////                                .addContainerGap(319, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(maxWeightLabel)
////                                .addContainerGap(300, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(truckTypeLabel)
////                                .addContainerGap(335, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addGap(150, 150, 150)
////                                .addComponent(addButton)
////                                .addContainerGap(182, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(backButton)
////                                .addContainerGap(366, Short.MAX_VALUE))
////                        .addGroup(layout.createSequentialGroup()
////                                .addGap(138, 138, 138)
////                                .addComponent(truckImageLabel)
////                                .addContainerGap(161, Short.MAX_VALUE))
////        );
////        layout.setVerticalGroup(
////                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
////                        .addGroup(layout.createSequentialGroup()
////                                .addContainerGap()
////                                .addComponent(licenseLabel)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                                .addComponent(modelLabel)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                                .addComponent(netWeightLabel)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                                .addComponent(maxWeightLabel)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                                .addComponent(truckTypeLabel)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
////                                .addComponent(addButton)
////                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 55, Short.MAX_VALUE)
////                                .addComponent(backButton)
////                                .addGap(18, 18, 18)
////                                .addComponent(truckImageLabel)
////                                .addContainerGap())
////        );
////    }
////}



package SuperLee.Transport.GUI;

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
