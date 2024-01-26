package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.Item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class orderGUI extends JPanel {
    private JTextField orderNumberField;
    private JTextField supplierNameField;
    private JTextField supplierNumberField;
    private JTextField addressField;
    private JTextField contactNumberField;
    private JComboBox<String> yearComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<String> dayComboBox;
    private JButton submitButton;
    private JButton addItemButton;

    private ArrayList<Item> items = new ArrayList<>();
    private boolean itemAdded = false;

    public orderGUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel orderNumberLabel = new JLabel("Order Number:");
        formPanel.add(orderNumberLabel, gbc);

        gbc.gridy++;
        JLabel supplierNameLabel = new JLabel("Supplier Name:");
        formPanel.add(supplierNameLabel, gbc);

        gbc.gridy++;
        JLabel supplierNumberLabel = new JLabel("Supplier Number:");
        formPanel.add(supplierNumberLabel, gbc);

        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address:");
        formPanel.add(addressLabel, gbc);

        gbc.gridy++;
        JLabel contactNumberLabel = new JLabel("Contact Phone Number:");
        formPanel.add(contactNumberLabel, gbc);

        gbc.gridy++;
        JLabel dateLabel = new JLabel("Date:");
        formPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        orderNumberField = new JTextField();
        formPanel.add(orderNumberField, gbc);

        gbc.gridy++;
        supplierNameField = new JTextField();
        formPanel.add(supplierNameField, gbc);

        gbc.gridy++;
        supplierNumberField = new JTextField();
        formPanel.add(supplierNumberField, gbc);

        gbc.gridy++;
        addressField = new JTextField();
        formPanel.add(addressField, gbc);

        gbc.gridy++;
        contactNumberField = new JTextField();
        formPanel.add(contactNumberField, gbc);

        gbc.gridy++;
        JPanel datePanel = new JPanel();
        yearComboBox = new JComboBox<>(new String[]{"Years", "2023", "2024", "2025"});
        monthComboBox = new JComboBox<>(new String[]{"Months", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        dayComboBox = new JComboBox<>(new String[]{"Days", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"});
        datePanel.add(yearComboBox);
        datePanel.add(monthComboBox);
        datePanel.add(dayComboBox);
        formPanel.add(datePanel, gbc);

        addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the popup window for adding a new item
                JFrame popupFrame = new JFrame("Add Item");
                popupFrame.setSize(300, 200);
                popupFrame.setLocationRelativeTo(null);
                popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Create components for the popup window
                JLabel nameLabel = new JLabel("Name:");
                JLabel typeLabel = new JLabel("Type:");
                JLabel quantityLabel = new JLabel("Quantity:");
                JTextField nameField = new JTextField();
                JComboBox<String> typeComboBox = new JComboBox<>();
                typeComboBox.addItem("Select.."); // Add the "Select.." option
                typeComboBox.addItem("Dry");
                typeComboBox.addItem("Frozen");
                typeComboBox.addItem("Cool");
                JTextField quantityField = new JTextField();
                JButton saveButton = new JButton("Save");

                // Set up the layout for the popup window
                JPanel popupContent = new JPanel(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                gbc.insets = new Insets(5, 5, 5, 5);

                popupContent.add(nameLabel, gbc);
                gbc.gridy++;
                popupContent.add(typeLabel, gbc);
                gbc.gridy++;
                popupContent.add(quantityLabel, gbc);
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                popupContent.add(nameField, gbc);
                gbc.gridy++;
                popupContent.add(typeComboBox, gbc);
                gbc.gridy++;
                popupContent.add(quantityField, gbc);
                gbc.gridy++;
                gbc.anchor = GridBagConstraints.EAST;
                popupContent.add(saveButton, gbc);

                popupFrame.setContentPane(popupContent);
                popupFrame.setVisible(true);

                saveButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String name = nameField.getText();
                        String type = (String) typeComboBox.getSelectedItem();
                        String quantity = quantityField.getText();

                        // Do something with the entered item details, such as storing them or displaying them
                        System.out.println("Name: " + name);
                        System.out.println("Storage type: " + type);
                        System.out.println("Quantity: " + quantity);

                        Item newItem = new Item(name, type, Integer.parseInt(quantity));
                        addItemToList(newItem);

                        itemAdded = true;
                        submitButton.setEnabled(true); // Enable the Submit button
                        popupFrame.dispose(); // Close the popup window
                    }
                });
            }
        });
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(addItemButton, gbc);

        submitButton = new JButton("Submit");
        submitButton.setEnabled(false); // Disable the Submit button initially
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (itemAdded) {
                    String orderNumber = orderNumberField.getText();
                    String supplierName = supplierNameField.getText();
                    String supplierNumber = supplierNumberField.getText();
                    String address = addressField.getText();
                    String contactNumber = contactNumberField.getText();
                    String year = (String) yearComboBox.getSelectedItem();
                    String month = (String) monthComboBox.getSelectedItem();
                    String day = (String) dayComboBox.getSelectedItem();

                    // Do something with the entered order details, such as storing them or displaying them
                    System.out.println("Order Number: " + orderNumber);
                    System.out.println("Supplier Name: " + supplierName);
                    System.out.println("Supplier Number: " + supplierNumber);
                    System.out.println("Address: " + address);
                    System.out.println("Contact Phone Number: " + contactNumber);
                    System.out.println("Date: " + year + "-" + month + "-" + day);

                    // Show success message
                    JOptionPane.showMessageDialog(orderGUI.this, "Order created successfully!");

                    // Reset item added flag
                    itemAdded = false;
                } else {
                    JOptionPane.showMessageDialog(orderGUI.this, "Please add an item first.");
                }
            }
        });
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the current OrderGUI frame
                setVisible(false);

//                // Create and show the transportMenu frame
//                SwingUtilities.invokeLater(new Runnable() {
//                    public void run() {
//                        TransportMenu transportMenu = new TransportMenu();
//                    }
//                });
            }
        });
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(backButton, gbc);

        // Set up the layout for the main panel
        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
    }

    public void addItemToList(Item item) {
        orderControllerView.addItemTolist(item);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                orderGUI orderGUI = new orderGUI();
                JFrame frame = new JFrame();
                frame.setTitle("Order Details");
                frame.setSize(500, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);

                frame.getContentPane().add(orderGUI);
                frame.setVisible(true);
            }
        });
    }
}