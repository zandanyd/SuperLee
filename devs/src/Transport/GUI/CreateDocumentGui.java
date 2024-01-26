package SuperLee.Transport.GUI;

import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.ShiftType;
import SuperLee.Transport.BusinessLayer.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CreateDocumentGui extends  JPanel {
    private JPanel mainPanel;
    private JComboBox<Truck> truckComboBox;
    private JComboBox<String> driverComboBox;
    private JComboBox<String> destinationComboBox;
    private JComboBox<Site> sourceComboBox;
    private JButton submitDateButton;
    private JButton finishButton;
    private JButton exitButton;

    private ButtonGroup regionButtonGroup;
    private JRadioButton southRadioButton;
    private JRadioButton centerRadioButton;
    private JRadioButton northRadioButton;

    private ButtonGroup typeGroup;
    private JRadioButton DRY;
    private JRadioButton COOL;
    private JRadioButton FROZEN;



    private JPanel orderTablePanel;
    private java.util.List<String> selectedOrders;

    private JButton submitOrdersButton;
    private JButton filterOrdersButton;
    private JTextField dateTimeField;
    private JTextField timeFieldText;

    private DocumentControllerView documentControllerView;

    private Boolean dateFilled = false;
    private Boolean typeFilled = false;

    private String dateTimeDocument;

    private TrainingType docType;
    private String dateInput;
    private String timeInput;

    private ShiftType shiftType;

    private JButton submitTruck;
    private JButton submitDriver;

    private JComboBox<String> shiftDriverTimeComboBox;

    private JTable table ;
    JButton sourceButton;

    private static final int ANIMATION_DELAY = 10; // Animation delay in milliseconds
    private static final int PANEL_WIDTH = 400; // Width of the panels
    private static final int PANEL_HEIGHT = 200; // Height of the panels

    public CreateDocumentGui() {
        TruckController truckController = TruckController.getInstance();
//        frame = new JFrame("Create Document");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLayout(new FlowLayout());
//        frame.setSize(400, 500);

        // Create the date and time panel
        mainPanel = new JPanel();
        JPanel dateTimePanel = new JPanel(new FlowLayout());
//        JPanel dateTimePanel = createAnimatedPanel(Color.WHITE);

        JLabel dateTimeLabel = new JLabel("TransportDate: ");
        dateTimeField = new JTextField(15);

        submitDateButton = new JButton("Submit Date"); // Check the date and time
        submitDateButton.addActionListener(e -> {
            dateInput = dateTimeField.getText();
            //timeInput = timeFieldText.getText();
            if (!dateInput.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(CreateDocumentGui.this, "Invalid date  format! Please enter the date in the format.");
                return;
            }

//            dateFilled = true;

            shiftDriverTimeComboBox.setEnabled(true);

        });


        submitOrdersButton = new JButton("Submit Orders");
        submitOrdersButton.setEnabled(false);

//        JLabel timeLabel = new JLabel("Transport Start Time: ");
//        timeFieldText = new JTextField(15);

        dateTimePanel.add(dateTimeLabel);
        dateTimePanel.add(dateTimeField);


//        dateTimePanel.add(timeLabel);
        //dateTimePanel.add(timeFieldText);

        dateTimePanel.add(submitDateButton);




        // Create the truck selection panel
        JPanel truckPanel = new JPanel(new FlowLayout());
        JLabel truckLabel = new JLabel("Truck Selection: ");
        submitTruck = new JButton("Submit Truck");

        truckComboBox = new JComboBox<>();
        truckComboBox.setEnabled(false);
        truckComboBox.setSelectedIndex(-1);
        truckPanel.add(truckLabel);
        truckPanel.add(truckComboBox);
        truckPanel.add(submitTruck);
        submitTruck.setEnabled(false);

        // Create the driver selection panel
        JPanel driverPanel = new JPanel(new FlowLayout());
        JLabel driverLabel = new JLabel("Driver Selection: ");
        submitDriver = new JButton("Submit Driver");

        driverComboBox = new JComboBox<>();
        driverPanel.add(driverLabel);
        driverPanel.add(driverComboBox);
        driverPanel.add(submitDriver);
        submitDriver.setEnabled(false);

        shiftDriverTimeComboBox = new JComboBox<>();
        JLabel shiftLabel = new JLabel("Select driver shift: ");
        shiftDriverTimeComboBox.addItem("Select Transport Shift...");
        shiftDriverTimeComboBox.addItem("Morning");
        shiftDriverTimeComboBox.addItem("Evening");
        driverPanel.add(shiftLabel);
        driverPanel.add(shiftDriverTimeComboBox);

        // Select Shift Driver
        shiftDriverTimeComboBox.addActionListener(e -> {
            String selectedTime = (String) shiftDriverTimeComboBox.getSelectedItem();
            if (selectedTime != null && !selectedTime.equals("Select...")) {
                shiftType = ShiftType.valueOf(selectedTime);
                if(shiftType.equals(ShiftType.Morning)) {
                    timeInput = "10:00:00";
                }
                else{
                    timeInput = "16:00:00";
                }
                dateTimeDocument = dateInput + " " + timeInput;
                documentControllerView.setDateTimeStr(dateTimeDocument);

            }
            DriverController driverController = DriverController.getInstance();
            if(documentControllerView.createDriverComboBox(driverComboBox, dateInput, shiftType) == null){
                JOptionPane.showMessageDialog(null, "No available drivers on this shift!");
                return;
            }
            //submitButton.setEnabled(true);
            enableAll();


        });




        ButtonGroup typeGroupSource = new ButtonGroup();
        DRY = new JRadioButton("South");
        COOL = new JRadioButton("Central");
        FROZEN = new JRadioButton("North");

        // Create the source selection panel
        JPanel sourcePanel = new JPanel(new FlowLayout());
        JLabel sourceLabel = new JLabel("Source Selection: ");
        sourceButton = new JButton("Submit Source");
        sourceButton.setEnabled(false);
        sourceComboBox = new JComboBox<>();
        sourcePanel.add(sourceLabel);
        sourcePanel.add(sourceComboBox);
        sourcePanel.add(sourceButton);

        // Create the order selection panel
        JPanel orderSelectionPanel = new JPanel(new FlowLayout());
        orderSelectionPanel.setLayout(new BoxLayout(orderSelectionPanel, BoxLayout.Y_AXIS));
        southRadioButton = new JRadioButton("South");
        centerRadioButton = new JRadioButton("Center");
        northRadioButton = new JRadioButton("North");
        filterOrdersButton = new JButton("Filter Orders");
        filterOrdersButton.setEnabled(false);
        regionButtonGroup = new ButtonGroup();
        regionButtonGroup.add(southRadioButton);
        regionButtonGroup.add(centerRadioButton);
        regionButtonGroup.add(northRadioButton);
        orderSelectionPanel.add(southRadioButton);
        orderSelectionPanel.add(centerRadioButton);
        orderSelectionPanel.add(northRadioButton);
        orderSelectionPanel.add(filterOrdersButton);

        southRadioButton.addActionListener(e->{
            documentControllerView.setArea(ShippingArea.SOUTH);
        });
        northRadioButton.addActionListener(e->{
            documentControllerView.setArea(ShippingArea.NORTH);
        });
        centerRadioButton.addActionListener(e->{
            documentControllerView.setArea(ShippingArea.CENTRAL);
        });




        typeGroup = new ButtonGroup();
        DRY = new JRadioButton("Dry");
        COOL = new JRadioButton("Cool");
        FROZEN = new JRadioButton("Frozen");

        DRY.addActionListener(e -> {
            docType = TrainingType.DRY;
            typeFilled = true;
            documentControllerView.setDocType(docType);
//            enableAll();
        });
        COOL.addActionListener(e -> {
            docType = TrainingType.REFRIGERATED;
            typeFilled = true;
            documentControllerView.setDocType(docType);

//            enableAll();
        });
        FROZEN.addActionListener(e -> {
            docType = TrainingType.FROZEN;
            typeFilled = true;
            documentControllerView.setDocType(docType);

//            enableAll();
        });

        typeGroup.add(DRY);
        typeGroup.add(COOL);
        typeGroup.add(FROZEN);
        JLabel typeLabel = new JLabel("Type Selection: ");
        JPanel typeSelectionPanel = new JPanel(new FlowLayout());
        typeSelectionPanel.setLayout(new BoxLayout(typeSelectionPanel, BoxLayout.Y_AXIS));
        typeSelectionPanel.add(typeLabel);
        typeSelectionPanel.add(DRY); typeSelectionPanel.add(COOL); typeSelectionPanel.add(FROZEN);
        mainPanel.add(typeSelectionPanel);

        // Create the order table panel
        orderTablePanel = new JPanel();
        orderTablePanel.setLayout(new GridLayout(0, 1));

        // Create the finish button panel
        JPanel finishPanel = new JPanel(new FlowLayout());
        finishButton = new JButton("Finish");
        exitButton = new JButton("Exit");
        finishButton.setEnabled(false);
        finishPanel.add(finishButton);
        finishPanel.add(exitButton);

//        // Populate the truckComboBox with truck details
//        truckComboBox.addItem("Select...");
//        truckComboBox.addItem("Truck 1 - License: ABC123");
//        truckComboBox.addItem("Truck 2 - License: DEF456");
//        truckComboBox.addItem("Truck 3 - License: GHI789");

        // Populate the driverComboBox with driver details
//        driverComboBox.addItem("Select...");
//        driverComboBox.addItem("Driver 1 - ID: 001");
//        driverComboBox.addItem("Driver 2 - ID: 002");
//        driverComboBox.addItem("Driver 3 - ID: 003");



        // Populate the sourceComboBox with source details
        // fill source combo
        ComboBoxHelper.fillComboBox(sourceComboBox, SiteController.getInstance().getAllSites());

//        // Add action listeners
//        submitDateButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Enable other components
//                truckComboBox.setEnabled(true);
//                driverComboBox.setEnabled(true);
//                //destinationComboBox.setEnabled(true);
//                sourceComboBox.setEnabled(true);
//            }
//        });

        ActionListener comboBoxListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean allFilled = !dateTimeField.getText().isEmpty() &&
//                        truckComboBox.getSelectedIndex() != 0 &&
                        driverComboBox.getSelectedIndex() != -1 &&
                        sourceComboBox.getSelectedIndex() != -1 &&
                        sourceComboBox.getSelectedIndex() != -1 &&
                        documentControllerView.getOrdersStatus();

                finishButton.setEnabled(allFilled);
                // Enable finish button if all combo boxes are filled
            }
        };


        submitOrdersButton.addActionListener(comboBoxListener);
//        truckComboBox.addActionListener(comboBoxListener);
        truckComboBox.addActionListener(e->{
            int idx = truckComboBox.getSelectedIndex();
            if(idx != -1 ){
                submitTruck.setEnabled(true);
            }
        });

        driverComboBox.addActionListener(e-> {
            int idx = driverComboBox.getSelectedIndex();
            if(idx != -1){
                submitDriver.setEnabled(true);
            }
        });

        sourceComboBox.addActionListener(e->{
            int idx = sourceComboBox.getSelectedIndex();
            if(idx != -1){
                sourceButton.setEnabled(true);
            }
        });

        driverComboBox.addActionListener(comboBoxListener);
        sourceComboBox.addActionListener(comboBoxListener);

//        filterOrdersButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                showOrderSelectionDialog();
//            }
//        });

        // Add panels to the frame
        mainPanel.add(dateTimePanel);
        mainPanel.add(truckPanel);
        mainPanel.add(driverPanel);
        mainPanel.add(sourcePanel);
//        frame.add(orderSelectionPanel);
        mainPanel.add(orderTablePanel);
        mainPanel.add(finishPanel);

        mainPanel.setVisible(true);
        // Set the view layout

        // Display orders table by area
        JTable ordersTable = new JTable();
        ordersTable.setVisible(false);
        JPanel ctrlPane = new JPanel();
        ctrlPane.add(filterOrdersButton);
        ctrlPane.add(southRadioButton);
        ctrlPane.add(centerRadioButton);
        ctrlPane.add(northRadioButton);
        ctrlPane.add(filterOrdersButton);


        // model
        ModelOrdersTable model = new ModelOrdersTable();
        ordersTable.setModel(model);
        ordersTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // Add the table to a scroll pane




        JScrollPane tableScrollPane = new JScrollPane(ordersTable);
        tableScrollPane.setPreferredSize(new Dimension(700, 182));
        tableScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Transport Orders",
                TitledBorder.CENTER, TitledBorder.TOP));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, ctrlPane, tableScrollPane);
        splitPane.setDividerLocation(35);
        splitPane.setEnabled(false);

        // Display it all in a scrolling window and make the window appear
//        JFrame ordersTableFrame = new JFrame("Orders Table");
//        ordersTableFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ordersTableFrame.add(splitPane);
        mainPanel.add(splitPane);
//        frame.add(orderSelectionPanel);
//        ordersTableFrame.pack();
//        ordersTableFrame.setLocationRelativeTo(null);
//        frame.setVisible(true);

        //controller
        documentControllerView = new DocumentControllerView(model, ordersTable, submitOrdersButton, filterOrdersButton,dateTimeDocument,
                docType,truckComboBox, driverComboBox, sourceComboBox);
        filterOrdersButton.addActionListener(documentControllerView);

        submitDriver.addActionListener(documentControllerView);
        submitTruck.addActionListener(documentControllerView);
        sourceButton.addActionListener(documentControllerView);
        finishButton.addActionListener(documentControllerView);

        exitButton.addActionListener(e-> {
            setVisible(false);
        });


        //JButton submitOrdersButton = new JButton("Submit Orders ");
        submitOrdersButton.addActionListener(documentControllerView);
        ctrlPane.add(submitOrdersButton);



        truckComboBox.setEnabled(false);
        driverComboBox.setEnabled(false);
        sourceComboBox.setEnabled(false);

//        submitButton.setEnabled(false);
        shiftDriverTimeComboBox.setEnabled(false);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add the dateTimePanel
        // ... Add components to the dateTimePanel
        mainPanel.add(dateTimePanel, gbc);

        // Add the truckPanel
        gbc.gridy++;
        // ... Add components to the truckPanel
        mainPanel.add(truckPanel, gbc);

        // Add the driverPanel
        gbc.gridy++;
        // ... Add components to the driverPanel
        mainPanel.add(driverPanel, gbc);

        // Add the sourcePanel
        gbc.gridy++;
        // ... Add components to the sourcePanel
        mainPanel.add(sourcePanel, gbc);

        // Add the orderTablePanel
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        JPanel orderTablePanel = new JPanel();
        // ... Add components to the orderTablePanel
        mainPanel.add(orderTablePanel, gbc);

        // Add the finishPanel
        gbc.gridy++;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        // ... Add components to the finishPanel
        mainPanel.add(finishPanel, gbc);

        // Add the splitPane
        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        // ... Set up the splitPane
        mainPanel.add(splitPane, gbc);



        add(mainPanel);
    }



    private void showOrderSelectionDialog() {
        JDialog orderDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Transportation Details", true);
        orderDialog.setLayout(new BorderLayout());
        orderDialog.setSize(300, 200);

        JPanel ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(ordersPanel, BoxLayout.Y_AXIS));

        if (southRadioButton.isSelected()) {
            ordersPanel.add(new JLabel("Order 1 - South"));
            ordersPanel.add(new JLabel("Order 2 - South"));
        } else if (centerRadioButton.isSelected()) {
            ordersPanel.add(new JLabel("Order 3 - Center"));
            ordersPanel.add(new JLabel("Order 4 - Center"));
        } else if (northRadioButton.isSelected()) {
            ordersPanel.add(new JLabel("Order 5 - North"));
            ordersPanel.add(new JLabel("Order 6 - North"));
        }

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedOrders = new java.util.ArrayList<>();
                Component[] components = ordersPanel.getComponents();
                for (Component component : components) {
                    if (component instanceof JLabel) {
                        JLabel orderLabel = (JLabel) component;
                        selectedOrders.add(orderLabel.getText());
                    }
                }
                orderDialog.dispose();
                displayOrderTable();
            }
        });

        orderDialog.add(ordersPanel, BorderLayout.CENTER);
        orderDialog.add(confirmButton, BorderLayout.SOUTH);

        orderDialog.setVisible(true);
    }

    private void displayOrderTable() {
        orderTablePanel.removeAll();
        orderTablePanel.revalidate();
        orderTablePanel.repaint();

        if (selectedOrders != null) {
            for (String order : selectedOrders) {
                JLabel orderLabel = new JLabel(order);
                orderTablePanel.add(orderLabel);
            }
        }
    }

    private void enableAll(){
//        if(!dateFilled || !typeFilled){
//            return;
//        }
        DRY.setEnabled(false); COOL.setEnabled(false); FROZEN.setEnabled(false);// Type
        dateTimeField.setEnabled(false); submitDateButton.setEnabled(false); // Date
        sourceComboBox.setEnabled(true);
//        sourceButton.setEnabled(true);
        filterOrdersButton.setEnabled(true);


        TruckController truckController = TruckController.getInstance();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(dateInput, formatter);
        if(truckController.getAvailableTrucksByType(docType, localDate) == null){
            JOptionPane.showMessageDialog(CreateDocumentGui.this, "There are no trucks available for this type of transport");
        }
        ComboBoxHelper.fillComboBox(truckComboBox, truckController.getAvailableTrucksByType(docType, localDate));
        truckComboBox.setSelectedIndex(-1);
        truckComboBox.setEnabled(true);
//        submitTruck.setEnabled(true);

        driverComboBox.setEnabled(true);


//        submitDriver.setEnabled(true);

        sourceComboBox.setEnabled(true);

    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
//                CreateDocumentGui createDocumentGui = new CreateDocumentGui();
//                JFrame frame = new JFrame();
//                frame = new JFrame("Create Document");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setLayout(new FlowLayout());
//                frame.setSize(400, 500);
//
//                frame.getContentPane().add(createDocumentGui);
//                frame.setVisible(true);
                JFrame frame = new JFrame("Create Document");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                CreateDocumentGui createDocumentGui = new CreateDocumentGui();
                frame.getContentPane().add(createDocumentGui);

                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public JButton getSubmitDriver() {
        return submitDriver;
    }

    public JButton getSubmitTruck() {
        return submitTruck;
    }



    private class ComboBoxHelper {
        public static <T> void fillComboBox(JComboBox<T> comboBox, ArrayList<T> list) {
            comboBox.removeAllItems();
            comboBox.addItem(null);
            for (T item : list) {
                comboBox.addItem(item);
            }
        }
    }


    public void fillSources(){

    }

    private static JPanel createAnimatedPanel(Color color) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        panel.setBackground(color);

        // Create the animation timer
        Timer timer = new Timer(ANIMATION_DELAY, new ActionListener() {
            private int alpha = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                alpha += 5;
                if (alpha > 255) {
                    alpha = 0;
                }
                panel.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                panel.repaint();
            }
        });

        // Start the animation
        timer.start();

        return panel;
    }
}