//package SuperLee.GUI;
//
//import SuperLee.Transport.BusinessLayer.Transport;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import javax.swing.table.TableModel;
//import javax.swing.table.TableRowSorter;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//public class TransportsInfo extends JPanel {
//    transportControllerView transportControllerView;
//    public TransportsInfo(){
//        transportControllerView = new transportControllerView(new JComboBox<String>(), new JComboBox<String>(), new JComboBox<String>());
//        showTransportHistory();
//    }
//
//    private void showTransportHistory() {
//        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Transportation Details", true);
//        dialog.setSize(900, 400);
//        dialog.setLocationRelativeTo(this);
//
//        // Create the table models
//        DefaultTableModel transportModel = new DefaultTableModel();
//        transportModel.addColumn("Transport ID");
//        transportModel.addColumn("Start DateTime");
//        transportModel.addColumn("End DateTime");
//        transportModel.addColumn("Status");
//        transportModel.addColumn("Current Truck Weight");
//        transportModel.addColumn("Source Address");
//        transportModel.addColumn("Driver ID");
//        transportModel.addColumn("Truck License");
//        transportModel.addColumn("Transport Document ID");
//
//        DefaultTableModel driverModel = new DefaultTableModel();
//        driverModel.addColumn("Driver ID");
//        driverModel.addColumn("Driver Name");
//        driverModel.addColumn("Driver License");
//
//        DefaultTableModel truckModel = new DefaultTableModel();
//        truckModel.addColumn("Truck License");
//        truckModel.addColumn("Truck Model");
//        truckModel.addColumn("Truck Type");
//        truckModel.addColumn("Net Weight");
//        truckModel.addColumn("Max Weight");
//
//        ArrayList<Transport> allTransports = transportControllerView.transportsInfo();
//        for (Transport transport : allTransports) {
//            transportModel.addRow(new Object[]{
//                    transport.getId(),
//                    transport.getStartLocalDateTime(),
//                    transport.getEndLocalDateTime(),
//                    transport.getStatus(),
//                    transport.getCurrentTruckWeight(),
//                    transport.getSource().getAddress(),
//                    transport.getDriver().getID(),
//                    transport.getTruck().getLicenseNumber(),
//                    transport.getTransportDocument().getId()
//            });
//        }
//
//        JTable transportTable = new JTable(transportModel);
//        transportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Enable horizontal scrolling
//
//        // Create a TableRowSorter for each table
//        TableRowSorter<TableModel> transportSorter = new TableRowSorter<>(transportTable.getModel());
//        transportTable.setRowSorter(transportSorter);
//
//        // Sort transport table by the Start DateTime column (column index 1)
//        transportSorter.toggleSortOrder(1);
//
//        // Create scroll panes for each table
//        JScrollPane transportScrollPane = new JScrollPane(transportTable);
//        transportScrollPane.setPreferredSize(new Dimension(300, 380));
//
//        JPanel tablePanel = new JPanel();
//        tablePanel.setLayout(new GridLayout(1, 3));
//        tablePanel.add(transportScrollPane);
//
//        JButton backButton = new JButton("Back");
//        backButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                dialog.dispose();
//            }
//        });
//
//        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(backButton);
//
//        dialog.add(tablePanel);
//        dialog.add(buttonPanel, BorderLayout.SOUTH);
//
//        dialog.setVisible(true);
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame();
//            // Set up the JFrame
//            frame.setTitle("Transport Information Menu");
//            frame.setSize(500, 400);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setLocationRelativeTo(null);
//
//            TransportsInfo transportGUI = new TransportsInfo();
//            frame.getContentPane().add(transportGUI);
//            frame.setVisible(true);
//        });
//    }
//}






package SuperLee.Transport.GUI;

import SuperLee.Transport.BusinessLayer.Transport;
import SuperLee.Transport.BusinessLayer.TransportStatus;
import SuperLee.Transport.BusinessLayer.TransportsController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TransportsInfo extends JPanel {
    private JComboBox<String> statusComboBox;
    private JTable transportTable;
    JRadioButton in_TransitRadioButton;
    JRadioButton closeRadioButton;
    JButton applyButton;
    TransportStatus selectedStatus;
    public TransportsInfo() {
        statusComboBox = new JComboBox<>();

        setLayout(new BorderLayout());
        add(createFilterPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());

        JLabel statusLabel = new JLabel("Transport Status:");
        filterPanel.add(statusLabel);

        // Create radio buttons for transport status
        in_TransitRadioButton = new JRadioButton("In Transit");
        closeRadioButton = new JRadioButton("Close");

        in_TransitRadioButton.addActionListener(e->{
            selectedStatus = TransportStatus.IN_TRANSIT;
            applyButton.setEnabled(true);
        });

//        if (in_TransitRadioButton.isSelected()) {
//            selectedStatus = TransportStatus.IN_TRANSIT;
//            applyButton.setEnabled(true);

        closeRadioButton.addActionListener(e-> {
            selectedStatus = TransportStatus.DONE;
            applyButton.setEnabled(true);
        });


        // Create a button group and add radio buttons to it
        ButtonGroup statusButtonGroup = new ButtonGroup();
        statusButtonGroup.add(in_TransitRadioButton);
        statusButtonGroup.add(closeRadioButton);

        filterPanel.add(in_TransitRadioButton);
        filterPanel.add(closeRadioButton);

        applyButton = new JButton("Apply Filters");
        applyButton.setEnabled(false);
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilters();
            }
        });
        filterPanel.add(applyButton);

        return filterPanel;
    }



    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

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

        transportTable = new JTable(transportModel);
        transportTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Enable horizontal scrolling

        // Create a TableRowSorter for the table
        TableRowSorter<TableModel> transportSorter = new TableRowSorter<>(transportTable.getModel());
        transportTable.setRowSorter(transportSorter);

        // Sort the transport table by the Start DateTime column (column index 1) initially
        transportSorter.toggleSortOrder(1);

        JScrollPane transportScrollPane = new JScrollPane(transportTable);
        transportScrollPane.setPreferredSize(new Dimension(300, 380));

        tablePanel.add(transportScrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void applyFilters() {



        // Retrieve all transports from the database (or any other data source)
        TransportsController transportsController = TransportsController.getInstance();
        ArrayList<Transport> allTransports = transportsController.getAllTransports();

        // Filter transports based on the selected status
        ArrayList<Transport> filteredTransports = new ArrayList<>();
        for (Transport transport : allTransports) {
            if (transport.getStatus().equals(selectedStatus)) {
                filteredTransports.add(transport);
            }
        }

        // Update the transport table model with the filtered transports
        DefaultTableModel transportModel = (DefaultTableModel) transportTable.getModel();
        transportModel.setRowCount(0); // Clear existing data

        for (Transport transport : filteredTransports) {
            transportModel.addRow(new Object[]{
                    transport.getId(),
                    transport.getStartLocalDateTime(),
                    transport.getEndLocalDateTime(),
                    transport.getStatus(),
                    transport.getCurrentTruckWeight(),
                    transport.getSource().toString(),
                    transport.getDriver(),
                    transport.getTruck(),
                    transport.getTransportDocument().getId()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            // Set up the JFrame
            frame.setTitle("Transport Information Menu");
            frame.setSize(500, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            TransportsInfo transportGUI = new TransportsInfo();
            frame.getContentPane().add(transportGUI);
            frame.setVisible(true);
        });
    }
}
