package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.TransportOrder;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class redesignGUI extends JFrame {
    private static int transportID;
    private JTable orderTable;
    private ModelOrdersTable model;
    private redesignControllerView redesignControllerView;
    JComboBox<String> replaceComboBox ;
    JComboBox<String> areaComboBox ;
    JComboBox<String> destinationComboBox;
    public redesignGUI(int transportID) {
        // Set up the JFrame
        setTitle("Redesign action");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // table orders
        JPanel ctrlPane = new JPanel();
        JTable ordersTable = new JTable();
        ordersTable.setVisible(false);
        ModelOrdersTable model = new ModelOrdersTable();
        ordersTable.setModel(model);
        ordersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 182));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Transport Orders",
                TitledBorder.CENTER, TitledBorder.TOP));
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ctrlPane, tableScrollPane);
        splitPane.setDividerLocation(35);


        // Create the buttons
        JButton replaceDestinationButton = new JButton("Replace Destination");
        JButton removeDestinationButton = new JButton("Remove Destination");
        JButton replaceTruckButton = new JButton("Replace Truck");
        JButton removeProductsButton = new JButton("Remove Products from Truck");
        JButton backButton = new JButton("Back");
        this.transportID = transportID;
        // Create the combo boxes for replacing and removing destinations
        replaceComboBox = new JComboBox<>();
        areaComboBox = new JComboBox<>();
        destinationComboBox = new JComboBox<>();
        redesignControllerView = new redesignControllerView(transportID, new JComboBox<String>(),
                new JComboBox<String>(),new JComboBox<String>(),new JComboBox<String>(),new JComboBox<String>(), new DefaultListModel<>(),
                new JComboBox<String>(), new JTable(),replaceComboBox,areaComboBox,destinationComboBox);
        redesignControllerView.setOrderTable(ordersTable);


        replaceDestinationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel replacePanel = new JPanel();
                replacePanel.setLayout(new GridLayout(7, 1));



                // Add a placeholder item to the replace combo box
                replaceComboBox.addItem("Select...");
                redesignControllerView.showAllDestinationToReplace(replaceComboBox, transportID);
                // Set initial states of combo boxes
                areaComboBox.setEnabled(false);
                destinationComboBox.setEnabled(false);

                replaceComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Enable/disable areaComboBox based on the selection
                        String selectedReplaceDestination = (String) replaceComboBox.getSelectedItem();
                        if (!selectedReplaceDestination.equals("Select...")) {
                            areaComboBox.addItem("Select...");
                            areaComboBox.setEnabled(true);
                            areaComboBox.addItem("SOUTH");
                            areaComboBox.addItem("CENTRAL");
                            areaComboBox.addItem("NORTH");
                        } else {
                            areaComboBox.setEnabled(false);
                        }
                    }
                });

                areaComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Enable/disable destinationComboBox based on the selection
                        String selectedArea = (String) areaComboBox.getSelectedItem();
                        if (!selectedArea.equals("Select...")) {
                            areaComboBox.setEnabled(false);
                            destinationComboBox.addItem("Select...");
                            destinationComboBox.setEnabled(true);
                            redesignControllerView.showAllSuitableDestByArea(destinationComboBox, areaComboBox);
                        } else {
                            destinationComboBox.setEnabled(false);
                        }
                    }
                });

                destinationComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String selectedDestination = (String) destinationComboBox.getSelectedItem();
                        if (!selectedDestination.equals("Select...")) {
                            // Enable the orders list box and populate it with orders based on the selected destination
                            ArrayList<TransportOrder> orders = redesignControllerView.showOrdersByDestination(transportID, selectedDestination);
                            if (orders != null && !orders.isEmpty()) {
                                model.populateTable(orders);
                                ordersTable.setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(redesignGUI.this, "Orders do not exist for this destination.");
                            }
                        }
                    }
                });

                JButton replaceDestButton = new JButton("Replace");
//                replaceDestButton.setEnabled(false);
                redesignControllerView.setReplaceDestButton(replaceDestButton);
                replaceDestButton.addActionListener(redesignControllerView);


                replacePanel.add(new JLabel("Select destination to replace:"));
                replacePanel.add(replaceComboBox);
                replacePanel.add(new JLabel("Select an area:"));
                replacePanel.add(areaComboBox);
                replacePanel.add(new JLabel("Select a new destination:"));
                replacePanel.add(destinationComboBox);
                replacePanel.add(replaceDestButton);

                JPanel mainPanel = new JPanel();
                mainPanel.setLayout(new BorderLayout());
                mainPanel.add(replacePanel, BorderLayout.NORTH);
                mainPanel.add(splitPane, BorderLayout.CENTER);

                JOptionPane.showMessageDialog(redesignGUI.this, mainPanel, "Replace Destination", JOptionPane.PLAIN_MESSAGE);
            }
        });
        //TODO - DONE + check
        removeDestinationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a new dialog for removing the destination
                JDialog removeDialog = new JDialog(redesignGUI.this, "Remove Destination", true);
                removeDialog.setSize(600, 150);
                removeDialog.setLocationRelativeTo(redesignGUI.this);

                // Create the label and combo box
                JLabel addressLabel = new JLabel("Enter the destination address you want to remove:");
                JComboBox<String> destinationComboBox = new JComboBox<>();

                // Add a placeholder item to the combo box
                destinationComboBox.addItem("Select...");
                JButton removeButton = new JButton("Remove");
                redesignControllerView.setRemoveButton(removeButton);
                redesignControllerView.showAllDestination(destinationComboBox,transportID);
                removeButton.addActionListener(redesignControllerView);
                removeButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Get the selected destination from the combo box
                        String selectedDestination = (String) destinationComboBox.getSelectedItem();
                        if (!selectedDestination.equals("Select...")) {
                            JOptionPane.showMessageDialog(redesignGUI.this, "Selected destination to remove: " + selectedDestination);
                            removeDialog.dispose();
                            transportGUI transportGUI = new transportGUI();
                            transportGUI.setVisible(true); // Show the transportGUI frame
                            redesignGUI.this.dispose(); // Close the redesignGUI frame
                        } else {
                            JOptionPane.showMessageDialog(redesignGUI.this, "Please select a destination to remove.");
                        }
                    }
                });

                // Add components to the remove destination dialog
                JPanel removePanel = new JPanel();
                removePanel.add(addressLabel);
                removePanel.add(destinationComboBox);
                removePanel.add(removeButton);
                removeDialog.add(removePanel);
                removeDialog.setVisible(true);
            }
        });

        //TODO - DONE + check
        replaceTruckButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog replaceTruckDialog = new JDialog(redesignGUI.this, "Replace Truck", true);
                replaceTruckDialog.setSize(600, 150);
                replaceTruckDialog.setLocationRelativeTo(redesignGUI.this);

                JLabel truckLabel = new JLabel("Enter the license number of the truck you want to select: ");
                JComboBox<String> truckComboBox = new JComboBox<>();

                truckComboBox.addItem("Select...");
                JButton replaceButton = new JButton("Replace");
                redesignControllerView.setReplaceTruckButton(replaceButton);
                redesignControllerView.showAllSuitableTruck(truckComboBox,transportID);
                replaceButton.addActionListener(redesignControllerView);
                replaceButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // Get the selected truck license number from the combo box
                        String selectedTruck = (String) truckComboBox.getSelectedItem();
                        if (!selectedTruck.equals("Select...")) {
                            JOptionPane.showMessageDialog(redesignGUI.this, "Selected truck license number: " + selectedTruck);
                            replaceTruckDialog.dispose();
                            transportGUI transportGUI = new transportGUI();
                            transportGUI.setVisible(true); // Show the transportGUI frame
                            redesignGUI.this.dispose(); // Close the redesignGUI frame
                        } else {
                            JOptionPane.showMessageDialog(redesignGUI.this, "Please select a truck license number.");
                        }
                    }
                });

                // Add components to the replace truck dialog
                JPanel replaceTruckPanel = new JPanel();
                replaceTruckPanel.add(truckLabel);
                replaceTruckPanel.add(truckComboBox);
                replaceTruckPanel.add(replaceButton);
                replaceTruckDialog.add(replaceTruckPanel);
                replaceTruckDialog.setVisible(true);
            }
        });

        //TODO - DONE + check
        removeProductsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog removeProductsDialog = new JDialog(redesignGUI.this, "Remove Products from Truck", true);
                removeProductsDialog.setSize(600, 200);
                removeProductsDialog.setLocationRelativeTo(redesignGUI.this);

                JLabel orderLabel = new JLabel("Enter order number:");
                JComboBox<String> orderComboBox = new JComboBox<>();
                orderComboBox.addItem("Select...");
                redesignControllerView.showAllOrders(orderComboBox, transportID);

                JLabel itemLabel = new JLabel("Select item:");
                JComboBox<String> itemComboBox = new JComboBox<>();
                itemComboBox.setEnabled(false);
                itemComboBox.addItem("Select...");

                JLabel amountLabel = new JLabel("Item amount:");
                JTextField amountTextField = new JTextField(10);
                redesignControllerView.setItemAmount(amountTextField);

                orderComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String selectedOrder = (String) orderComboBox.getSelectedItem();
                        if (!selectedOrder.equals("Select...")) {
                            itemComboBox.setEnabled(true);
                            itemComboBox.removeAllItems(); // Clear previous items
                            itemComboBox.addItem("Select...");
                            // Populate itemComboBox with items based on the selected order
                            redesignControllerView.showAllItems(itemComboBox, orderComboBox);
                        } else {
                            itemComboBox.setEnabled(false);
                            itemComboBox.setSelectedItem("Select...");
                        }
                    }
                });

                JButton removeProductsButton = new JButton("Remove Products");
                redesignControllerView.setRemoveProductsButton(removeProductsButton);
                removeProductsButton.addActionListener(redesignControllerView);
                removeProductsButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String selectedOrder = (String) orderComboBox.getSelectedItem();
                        String selectedItem = (String) itemComboBox.getSelectedItem();
                        String amount = amountTextField.getText();

                        if (!selectedOrder.equals("Select...") && !selectedItem.equals("Select...") && !amount.isEmpty()) {
                            JOptionPane.showMessageDialog(redesignGUI.this,
                                    "Selected order: " + selectedOrder +
                                            "\nSelected item: " + selectedItem +
                                            "\nItem amount: " + amount);
                            removeProductsDialog.dispose();
                            transportGUI transportGUI = new transportGUI();
                            transportGUI.setVisible(true); // Show the transportGUI frame
                            redesignGUI.this.dispose(); // Close the redesignGUI frame
                        } else {
                            JOptionPane.showMessageDialog(redesignGUI.this,
                                    "Please select an order, an item, and enter the item amount.");
                        }
                    }
                });

                // Add components to the remove products dialog
                JPanel removeProductsPanel = new JPanel();
                removeProductsPanel.setLayout(new GridLayout(4, 2));
                removeProductsPanel.add(orderLabel);
                removeProductsPanel.add(orderComboBox);
                removeProductsPanel.add(itemLabel);
                removeProductsPanel.add(itemComboBox);
                removeProductsPanel.add(amountLabel);
                removeProductsPanel.add(amountTextField);
                removeProductsPanel.add(removeProductsButton);
                removeProductsDialog.add(removeProductsPanel);
                removeProductsDialog.setVisible(true);
            }
        });

        // ------------------
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transportGUI transportGUI = new transportGUI();
                transportGUI.setVisible(true); // Show the transportGUI frame
                redesignGUI.this.dispose(); // Close the redesignGUI frame
            }
        });

        // Create the panel and add components
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        panel.add(replaceDestinationButton, c);

        c.gridy = 1;
        panel.add(removeDestinationButton, c);

        c.gridy = 2;
        panel.add(replaceTruckButton, c);

        c.gridy = 3;
        panel.add(removeProductsButton, c);

        c.gridy = 4;
        panel.add(backButton, c);

        // Add the panel to the JFrame
        add(panel);



    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            redesignGUI redesignGUI = new redesignGUI(transportID);
            redesignGUI.setVisible(true); // Show the redesignGUI frame
        });
    }

}