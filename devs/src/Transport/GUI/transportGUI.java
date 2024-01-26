package SuperLee.Transport.GUI;


import SuperLee.Transport.BusinessLayer.Transport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class transportGUI extends JPanel {
    transportControllerView transportControllerView;
    public transportGUI() {
        transportControllerView = new transportControllerView(new JComboBox<String>(), new JComboBox<String>(), new JComboBox<String>());

        // TODO!!! just redisgn 4 button
        JLabel manageLabel = new JLabel("Choose a transport to manage:");
        JComboBox<String> manageComboBox = new JComboBox<>();
        manageComboBox.addItem("Select...");
        transportControllerView.showTransportToManageComboBox(manageComboBox);
        JButton manageButton = new JButton("Manage Transport");
        // TODO - ADD DETAILS OF SELECTED TRANSPORT
        manageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTransport = (String) manageComboBox.getSelectedItem();
                if (selectedTransport != null && !selectedTransport.equals("Select...")) {
                    // Create a new dialog for weight update
                    JDialog weightDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(transportGUI.this), "Update Truck Weight", true);
                    weightDialog.setSize(300, 150);
                    weightDialog.setLocationRelativeTo(transportGUI.this);

                    // Create components for weight update dialog
                    JLabel weightLabel = new JLabel("New Truck Weight:");
                    JTextField weightField = new JTextField(10);
                    JButton updateButton = new JButton("Update");

                    // Set up action listener for update button
                    updateButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            String newWeightString = weightField.getText();
                            double newWeight = Double.parseDouble(newWeightString);
                            int transportID = Integer.parseInt(selectedTransport.replace("transport ", ""));
                            if (!transportControllerView.transportsController.updateCurrentTruckWeight(transportID, newWeight)) {
                                // Update the current truck weight using transportsController
                                JOptionPane.showMessageDialog(transportGUI.this, "new weight exceeds maximum weight allowed, you must redesign your transport!");
                                weightDialog.dispose();
                                redesignGUI redesignGUI = new redesignGUI(transportID);
                                redesignGUI.setVisible(true);
                                transportGUI.this.setVisible(false);
                            } else {
                                JOptionPane.showMessageDialog(transportGUI.this, "update truck's weight successfully!");
                                weightDialog.dispose();
                            }
                        }
                    });

                    // Add components to the weight update dialog
                    JPanel weightPanel = new JPanel();
                    weightPanel.add(weightLabel);
                    weightPanel.add(weightField);
                    weightPanel.add(updateButton);
                    weightDialog.add(weightPanel);
                    weightDialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(transportGUI.this, "Please select a transport to manage.");
                }
            }
        });



        JButton endTransportButton = new JButton("Finish Transport");
        JLabel selectLabel = new JLabel("Choose a transport to finish:");
        JComboBox<String> transportComboBox = new JComboBox<>();
        transportComboBox.addItem("Select...");
        transportControllerView.setEndTransportButton(endTransportButton);
        transportControllerView.showTransportToFinishComboBox(transportComboBox);
        endTransportButton.addActionListener(transportControllerView);
        endTransportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTransport = (String) transportComboBox.getSelectedItem();
                if (selectedTransport != null && !selectedTransport.equals("Select...")) {
                    // Perform the action for ending the transport
                    JOptionPane.showMessageDialog(transportGUI.this, "Ending Transport: " + selectedTransport);
                } else {
                    JOptionPane.showMessageDialog(transportGUI.this, "Please select a transport to finish.");
                }
            }
        });

        JButton historyButton = new JButton("Transports Information");
        historyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showTransportHistory();
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
//                TransportMenu transportMenu = new TransportMenu();
//                transportMenu.setVisible(true);
//                transportGUI.this.setVisible(false); // Hide the TransportGUI frame
            }
        });

        JLabel chooseTransportLabel = new JLabel("Choose a transport to start:");
        JComboBox<String> startTransportComboBox = new JComboBox<>();
        JButton startTransportButton = new JButton("Start Transport");
        startTransportComboBox.addItem("Select...");
        transportControllerView.setStartTransportButton(startTransportButton);
        transportControllerView.showTransportToStartComboBox(startTransportComboBox);
        startTransportButton.addActionListener(transportControllerView);
        startTransportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedTransportDocument = (String) startTransportComboBox.getSelectedItem();
                if (selectedTransportDocument != null && !selectedTransportDocument.equals("Select...")) {
                    // Perform the action for starting the transport
                    JOptionPane.showMessageDialog(transportGUI.this, "Starting Transport: " + selectedTransportDocument);
                } else {
                    JOptionPane.showMessageDialog(transportGUI.this, "Please select a transport document to start.");
                }
            }
        });


        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 1));
//        panel.add(orderButton);

        JPanel chooseTransportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        chooseTransportPanel.add(chooseTransportLabel);
        chooseTransportPanel.add(startTransportComboBox);
        chooseTransportPanel.add(startTransportButton);
        panel.add(chooseTransportPanel);

        JPanel managePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        managePanel.add(manageLabel);
        managePanel.add(manageComboBox);
        managePanel.add(manageButton);
        panel.add(managePanel);

        JPanel endTransportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endTransportPanel.add(selectLabel);
        endTransportPanel.add(transportComboBox);
        endTransportPanel.add(endTransportButton);
        panel.add(endTransportPanel);

        panel.add(historyButton);
        panel.add(backButton);

        add(panel);
        setVisible(true);
    }
    private void showTransportHistory() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Transportation Details", true);
        dialog.setSize(900, 400);
        dialog.setLocationRelativeTo(this);

        // Create the table models
        DefaultTableModel transportModel = new DefaultTableModel();
        transportModel.addColumn("Transport ID");
        transportModel.addColumn("Start DateTime");
        transportModel.addColumn("End DateTime");
        transportModel.addColumn("Status");
        transportModel.addColumn("Current Truck Weight");
        transportModel.addColumn("Source Address");
        transportModel.addColumn("Driver ID");
        transportModel.addColumn("Truck License");
        transportModel.addColumn("Transport Document ID");

        DefaultTableModel driverModel = new DefaultTableModel();
        driverModel.addColumn("Driver ID");
        driverModel.addColumn("Driver Name");
        driverModel.addColumn("Driver License");

        DefaultTableModel truckModel = new DefaultTableModel();
        truckModel.addColumn("Truck License");
        truckModel.addColumn("Truck Model");
        truckModel.addColumn("Truck Type");
        truckModel.addColumn("Net Weight");
        truckModel.addColumn("Max Weight");

        ArrayList<Transport> allTransports = transportControllerView.transportsInfo();
        for (Transport transport : allTransports) {
            transportModel.addRow(new Object[]{
                    transport.getId(),
                    transport.getStartLocalDateTime(),
                    transport.getEndLocalDateTime(),
                    transport.getStatus(),
                    transport.getCurrentTruckWeight(),
                    transport.getSource().getAddress(),
                    transport.getDriver().getID(),
                    transport.getTruck().getLicenseNumber(),
                    transport.getTransportDocument().getId()
            });
        }

        JTable transportTable = new JTable(transportModel);
        transportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Enable horizontal scrolling

        // Create a TableRowSorter for each table
        TableRowSorter<TableModel> transportSorter = new TableRowSorter<>(transportTable.getModel());
        transportTable.setRowSorter(transportSorter);

        // Sort transport table by the Start DateTime column (column index 1)
        transportSorter.toggleSortOrder(1);

        // Create scroll panes for each table
        JScrollPane transportScrollPane = new JScrollPane(transportTable);
        transportScrollPane.setPreferredSize(new Dimension(300, 380));

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout(1, 3));
        tablePanel.add(transportScrollPane);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);

        dialog.add(tablePanel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            // Set up the JFrame
            frame.setTitle("Transport Operations Menu");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            transportGUI transportGUI = new transportGUI();
            frame.getContentPane().add(transportGUI);
            frame.setVisible(true);
        });
    }
}