package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class OrdersInfo extends JPanel {
    private ModelOrdersTable model;
    private JTable table;
    private JRadioButton southRadioButton;
    private JRadioButton centralRadioButton;
    private JRadioButton northRadioButton;
    private JRadioButton dryRadioButton;
    private JRadioButton coolRadioButton;
    private JRadioButton frozenRadioButton;
    private JLabel ordersInfo;

    public OrdersInfo() {
        // Create an instance of ModelOrdersTable
        model = new ModelOrdersTable();

        // Create a JTable using the model
        table = new JTable(model);

        // Set table properties
        table.setFillsViewportHeight(true);

        // Create a JScrollPane and add the table to it
        JScrollPane scrollPane = new JScrollPane(table);

        // Create radio buttons for region selection
        southRadioButton = new JRadioButton("South");
        centralRadioButton = new JRadioButton("Central");
        northRadioButton = new JRadioButton("North");

        // Create radio buttons for type selection
        dryRadioButton = new JRadioButton("Dry");
        coolRadioButton = new JRadioButton("Cool");
        frozenRadioButton = new JRadioButton("Frozen");

        // Create a button group for region selection
        ButtonGroup regionButtonGroup = new ButtonGroup();
        regionButtonGroup.add(southRadioButton);
        regionButtonGroup.add(centralRadioButton);
        regionButtonGroup.add(northRadioButton);

        // Create a button group for type selection
        ButtonGroup typeButtonGroup = new ButtonGroup();
        typeButtonGroup.add(dryRadioButton);
        typeButtonGroup.add(coolRadioButton);
        typeButtonGroup.add(frozenRadioButton);

        // Create a panel to hold the radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout(2, 3));
        radioPanel.add(new JLabel("Region:"));
        radioPanel.add(southRadioButton);
        radioPanel.add(centralRadioButton);
        radioPanel.add(northRadioButton);
        radioPanel.add(new JLabel("Type:"));
        radioPanel.add(dryRadioButton);
        radioPanel.add(coolRadioButton);
        radioPanel.add(frozenRadioButton);

        // Create a label for informative messages
        ordersInfo = new JLabel("Please select a region and a type.");

        // Add action listeners to the radio buttons
        southRadioButton.addActionListener(new RadioActionListener());
        centralRadioButton.addActionListener(new RadioActionListener());
        northRadioButton.addActionListener(new RadioActionListener());
        dryRadioButton.addActionListener(new RadioActionListener());
        coolRadioButton.addActionListener(new RadioActionListener());
        frozenRadioButton.addActionListener(new RadioActionListener());

        // Set layout manager for the panel
        setLayout(new BorderLayout());

        // Add the radio panel to the north of the panel
        add(radioPanel, BorderLayout.NORTH);

        // Add the scroll pane to the center of the panel
        add(scrollPane, BorderLayout.CENTER);

        // Add the orders info label to the south of the panel
        add(ordersInfo, BorderLayout.SOUTH);
    }

    public void populateTable() {
        // Retrieve orders from the database (or any other data source)
        // For this example, let's assume you have an ArrayList<TransportOrder> called 'orders'

        // Check if both a region and a type are selected
        if (southRadioButton.isSelected() || centralRadioButton.isSelected() || northRadioButton.isSelected()) {
            if (dryRadioButton.isSelected() || coolRadioButton.isSelected() || frozenRadioButton.isSelected()) {
                // Apply filters based on selected radio buttons
                ShippingArea area = null;
                // Check the selected region

                if (southRadioButton.isSelected()){
                    area = ShippingArea.SOUTH;
                }
                if (centralRadioButton.isSelected()) {
                    area = ShippingArea.CENTRAL;

                } else if (northRadioButton.isSelected()) {
                    area = ShippingArea.NORTH;
                }

                // Check the selected type
                TrainingType type = null;

                if(dryRadioButton.isSelected()){
                    type = TrainingType.DRY;
                }
                 if (coolRadioButton.isSelected()) {
                    type = TrainingType.REFRIGERATED;

                } else if (frozenRadioButton.isSelected()) {
                    type = TrainingType.FROZEN;
                }

                // Check if there are orders matching the selected filters
                if (area == null || type == null) {
                    ordersInfo.setText("No orders found for the selected filters.");
                } else {
                    // Populate the table with the filtered orders
                    OrdersController ordersController = OrdersController.getInstance();
                    SiteController siteController = SiteController.getInstance();
                    ArrayList<Site>sites = siteController.getSitesByArea(area);
                    ArrayList<Integer> filteredOrders = ordersController.getAvailableOrdersByAreaAndType(type, sites);
                    ArrayList<TransportOrder> orders = new ArrayList<>();
                    for(int id : filteredOrders){
                        TransportOrder order = ordersController.getOrderByKey(id, type);
                        orders.add(order);
                    }
                    model.populateTable(orders);
                    ordersInfo.setText("Displaying orders for the selected filters.");
                }
            } else {
                ordersInfo.setText("Please select a type.");
            }
        } else {
            ordersInfo.setText("Please select a region.");
        }
    }

    private class RadioActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Update the table and informative messages when a radio button is selected
            populateTable();
        }
    }

    public static void main(String[] args) {
        // Create a JFrame to hold the OrdersPanel
        JFrame frame = new JFrame("Orders Table");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create an instance of OrdersPanel
        OrdersInfo ordersPanel = new OrdersInfo();

        // Populate the table with data
        ordersPanel.populateTable();

        // Add the OrdersPanel to the JFrame
        frame.add(ordersPanel);

        // Pack and display the JFrame
        frame.pack();
        frame.setVisible(true);
    }
}
