package SuperLee.Transport.GUI;


import SuperLee.Transport.BusinessLayer.*;
import SuperLee.Transport.BusinessLayer.TransportsController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class redesignControllerView implements ActionListener{
    int transportID;
    JTable orderTable;
    public void setTable(JTable table){
        this.orderTable = table;
    }
    JButton removeButton;
    JButton replaceDestButton;
    JButton replaceButton;
    JButton removeProductsButton;
    JComboBox<String> destinationToRemove;
    JComboBox<String> destinationToReplace;
    JComboBox<String> destinationsByArea;
    JComboBox<String> areasBox;
    JComboBox<String> truckToReplace;
    JComboBox<String> orders;
    JComboBox<String> itemNameBox;
    JTextField itemAmount;
    DefaultListModel<String> ordersComboBox;

    TransportsController transportsController = TransportsController.getInstance();
    TransportManagment transportManagment = TransportManagment.getInstance();
    SiteController siteController = SiteController.getInstance();
    TruckController truckController = TruckController.getInstance();
    OrdersController ordersController = OrdersController.getInstance();
    JComboBox<String> replaceComboBox;
    JComboBox<String> areaComboBox;
    JComboBox<String> destinationComboBox;

    public redesignControllerView(int transportID, JComboBox<String> destinationToRemove, JComboBox<String> destinationToReplace,
                                  JComboBox<String> itemNameBox,
                                  JComboBox<String> orders, JComboBox<String> truckToReplace,
                                  DefaultListModel<String> ordersComboBox, JComboBox<String> areasBox, JTable table,
                                  JComboBox<String> replaceComboBox,JComboBox<String> areaComboBox,JComboBox<String> destinationComboBox ){
        super();
        this.areaComboBox = areaComboBox;
        this.destinationComboBox = destinationComboBox;
        this.replaceComboBox = replaceComboBox;
        this.orderTable = table;
        this.transportID = transportID;
        this.destinationToRemove = destinationToRemove;
        this.destinationToReplace = destinationToReplace;
        this.truckToReplace = truckToReplace;
        this.itemNameBox = itemNameBox;
        this.orders = orders;
        this.ordersComboBox = ordersComboBox;
        this.areasBox = areasBox;

        showAllDestination(destinationToRemove,transportID);
        showAllDestinationToReplace(destinationToReplace, transportID);
//        showAllSuitableDestByArea(destinationsByArea, areasBox);
        showAllSuitableTruck(truckToReplace,transportID);
        showAllOrders(orders, transportID);
        showAllItems(itemNameBox, orders);
//        showOrdersByDestination(ordersComboBox,"Beer Sheva");

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == removeButton){
            String addressToRemove = destinationToRemove.getSelectedItem().toString();
            transportManagment.removeDestinationOfTransport(transportID,addressToRemove);
        }
        else if(source == replaceDestButton){
            // Get the selected destination from the combo box
            String selectedReplaceDestination = (String) replaceComboBox.getSelectedItem();
            String selectedArea = (String) areaComboBox.getSelectedItem();
            String selectedDestination = (String) destinationComboBox.getSelectedItem();
//            ShippingArea selectedArea = (ShippingArea) areasBox.getSelectedItem();

            if (selectedReplaceDestination.equals("Select...") || selectedArea.equals("Select...") ||
                    selectedDestination.equals("Select...")) {
                JOptionPane.showMessageDialog(null, "Please fill all the fields.");
                return;
            }


            int[] selectedRows = orderTable.getSelectedRows();
            ArrayList<Integer> newOrders = new ArrayList<>();
            if (selectedRows.length == 0) {
                JOptionPane.showMessageDialog(null, "No orders selected.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Optionally, you can exit the method here
            }
            for (int row : selectedRows) {
                Object orderId = orderTable.getValueAt(row, 0);
                newOrders.add(Integer.parseInt(orderId.toString()));
            }
            transportManagment.replaceSitesOfTransport(transportID, destinationToReplace.getSelectedItem().toString(), newOrders);
            JOptionPane.showMessageDialog(null, "Selected replace destination: " + selectedReplaceDestination +
                    "\nSelected area: " + selectedArea +
                    "\nSelected destination: " + selectedDestination);
            transportGUI transportGUI = new transportGUI();
            transportGUI.setVisible(true); // Show the transportGUI frame
            return;

        } else if(source == replaceButton){
            String selectedLicense = truckToReplace.getSelectedItem().toString();
            if (selectedLicense != null && !selectedLicense.isEmpty()) {
                Integer licenseNumber = Integer.parseInt(selectedLicense);
                transportManagment.replaceTrucksDuringTransport(transportID, truckController.getTruckByLicense(licenseNumber));
            }
        }
        else if(source == removeProductsButton){
            int orderNumber = Integer.parseInt(orders.getSelectedItem().toString());
            String itemName = itemNameBox.getSelectedItem().toString();
            String[] itemNameParts = itemName.split(",", 2);  // Split at the first comma, maximum 2 parts
            String itemNameWithoutComma = itemNameParts[0];
            int itemAmountValue = Integer.parseInt(itemAmount.getText());
            transportManagment.removeItemsFromTransportByOrderNum(transportID, orderNumber, itemNameWithoutComma, itemAmountValue);
        }
    }

    // ------
    public void setOrderTable(JTable orderTable) {
        this.orderTable = orderTable;
    }
    public void setRemoveButton(JButton removeButton) {
        this.removeButton = removeButton;
    }
    public void setReplaceDestButton(JButton replaceDestButton) { this.replaceDestButton = replaceDestButton; }
    public void setReplaceTruckButton(JButton replaceButton) {
        this.replaceButton = replaceButton;
    }
    public void setRemoveProductsButton(JButton removeProductsButton) {
        this.removeProductsButton = removeProductsButton;
    }

    // ------
    public void showAllDestination(JComboBox<String> destinationRemoveBox, int transportID){
        ArrayList<Site> destinationToRemove = transportsController.getTransportById(transportID).getTransportDocument().getDestinationList();
        for (Site site : destinationToRemove){
            destinationRemoveBox.addItem(site.getAddress());
        }
    }
    public void showAllDestinationToReplace(JComboBox<String> destinationReplaceBox, int transportID) {
        ArrayList<Site> destinationToReplace = transportsController.getTransportById(transportID).getTransportDocument().getDestinationList();
        for (Site site : destinationToReplace) {
            destinationReplaceBox.addItem(site.getAddress());
        }
    }
    public void showAllSuitableDestByArea(JComboBox<String> destinationsByArea,JComboBox<String> areasBox ){
        if (areasBox.getSelectedItem().toString().equals("SOUTH")){
            ArrayList<Site> destsByArea =  siteController.getSitesByArea(ShippingArea.SOUTH);
            for(Site site : destsByArea){
                destinationsByArea.addItem(site.getAddress());
            }
            return;
        }
        else if (areasBox.getSelectedItem().toString().equals("CENTRAL")){
            ArrayList<Site> destsByArea =  siteController.getSitesByArea(ShippingArea.CENTRAL);
            for(Site site : destsByArea){
                destinationsByArea.addItem(site.getAddress());
            }
            return;
        }
        else{
            ArrayList<Site> destsByArea =  siteController.getSitesByArea(ShippingArea.NORTH);
            for(Site site : destsByArea){
                destinationsByArea.addItem(site.getAddress());
            }
        }
    }
    public ArrayList<TransportOrder> showOrdersByDestination(int transportID, String selectedDestination) {
        ArrayList<String> siteSelected = new ArrayList<>();
        siteSelected.add(siteController.getSiteByAddress(selectedDestination).getAddress());

        try {
            ArrayList<TransportOrder> transportOrders = ordersController.getOrdersAfterWorkersController(transportsController.getTransportById(transportID).getTrainingRequired(), siteSelected);

            if (transportOrders != null) {
                return transportOrders;
            } else {
                // Handle the case when transportOrders is null
                System.out.println("No transport orders available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the exception accordingly
        }
        return null;
    }

    public void showAllSuitableTruck(JComboBox<String> truckToReplace, int transportID){
        ArrayList<Truck> trucks = transportManagment.getExistTrucksDuringTransport(transportID, transportsController.getTransportById(transportID).getCurrentTruckWeight());
        if(trucks != null) {
            for (Truck truck : trucks) {
                truckToReplace.addItem(String.valueOf(truck.getLicenseNumber()));
            }
        }
//        ComboBoxHelper.fillComboBox(truckToReplace, transportManagment.getExistTrucksDuringTransport(transportID, transportsController.getTransportById(transportID).getCurrentTruckWeight()));
    }
    public void showAllOrders(JComboBox<String> orders, int transportID){
        ArrayList <TransportOrder> ordersOfTransport = transportsController.getTransportById(transportID).getTransportDocument().getOrders();
        for(TransportOrder order : ordersOfTransport){
            orders.addItem(String.valueOf(order.getOrderNumber()));
        }
    }
    public void showAllItems(JComboBox<String> items, JComboBox<String> ordersComboBox){
        TransportOrder transportOrder = transportManagment.getOrderByNumDuringTransport(transportID, Integer.parseInt(ordersComboBox.getSelectedItem().toString()));
        ArrayList<Item> itemsOfOrder = transportOrder.getItems();
        for(Item item : itemsOfOrder){
            items.addItem(item.getName() + ", total amount: " + item.getAmount());
        }
    }
    public void setItemAmount(JTextField itemAmount) {
        this.itemAmount = itemAmount;
    }
    // -----------
    private class ComboBoxHelper {
        public static <T> void fillComboBox(JComboBox<T> comboBox, ArrayList<T> list) {
            comboBox.removeAllItems();
            for (T item : list) {
                comboBox.addItem(item);
            }
        }
    }

}
