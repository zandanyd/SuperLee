package SuperLee.Transport.GUI;

import SuperLee.HumenResource.BusinessLayer.Driver;
import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.HumenResource.BusinessLayer.ShiftType;
import SuperLee.Transport.BusinessLayer.*;
import SuperLee.TransportWorkersController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentControllerView extends Component implements ActionListener {
    TransportsController transportsController = TransportsController.getInstance();
    TransportDocumentController transportDocumentController = TransportDocumentController.getInstance();
    TransportManagment transportManagment = TransportManagment.getInstance();
    JComboBox<String> transportComboBox;
    JComboBox<String> startTransportComboBox;
    JComboBox<String > manageTransportComboBox;
    JButton endTransportButton;
    JButton startTransportButton;

    private JButton submitOrdersButton;
    private JButton filterOrdersButton;
    private String dateTimeStr;
   private ButtonGroup areaGroup;

    public void setDocType(TrainingType docType) {
        this.docType = docType;
    }

    private TrainingType docType;
   private Boolean finish = false;

    private Driver driver;
    private Truck truck;
    private Site source;

    private JComboBox<Truck> truckComboBox;
    private JComboBox<String> driverComboBox;


    private JComboBox<Site> sourceComboBox;
    private ArrayList<TransportOrder> ordersList;

    ModelOrdersTable model;
    JTable table;
    JFrame ordersTableFrame;

    public void setSourcesArea(ShippingArea sourcesArea) {
        this.sourcesArea = sourcesArea;
    }

    ShippingArea sourcesArea;

    public void setArea(ShippingArea area) {
        this.area = area;
    }

    ShippingArea area;

    public boolean getTruckStatus() {
        return truckStatus;
    }

    boolean truckStatus;

    public boolean getOrdersStatus() {
        return ordersStatus;
    }

    boolean ordersStatus;
    boolean sourceStatus;

    public boolean getFinishStatus() {
        return finishStatus;
    }

    boolean finishStatus;


    public DocumentControllerView(ModelOrdersTable model, JTable table, JButton submitBTN, JButton filterBTN,String dateTimeStr, TrainingType docType,
                                  JComboBox<Truck> truckComboBox, JComboBox<String> driverComboBox, JComboBox<Site> sourceComboBox){
        this.model = model;
        this.table = table;
        this.ordersTableFrame = ordersTableFrame;
        this.submitOrdersButton = submitBTN;
        this.filterOrdersButton = filterBTN;
        this.areaGroup = areaGroup;
        this.docType = docType;
        this.dateTimeStr = dateTimeStr;
       this.truckComboBox = truckComboBox;
       this.driverComboBox = driverComboBox;
       this.sourceComboBox = sourceComboBox;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OrdersController ordersController = OrdersController.getInstance();
        TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();
        SiteController siteController = SiteController.getInstance();
        ArrayList<TransportOrder> orders = new ArrayList<>();

        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String strFilter = "Filter Orders";
            if (buttonText.equalsIgnoreCase(strFilter)) {
                if (area == null) {
                    JOptionPane.showMessageDialog(null, "Please select an area option.");
                    return;
                }


                String dateStr = dateTimeStr;
//                String dateStr = "2023-21-09";
                try {
                    ArrayList<String> adresses = transportWorkersController.getAvailableSitesByDate(dateStr); // sites by date
                    ArrayList<String> adressesByArea = siteController.filterSitesAdressByArea(adresses, area); // sites bt area
                    orders = ordersController.getOrdersAfterWorkersController(docType, adressesByArea); // all orders to these sites
                    if (orders == null) {
                        JOptionPane.showMessageDialog(null, "No pending orders in the system!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                submitOrdersButton.setEnabled(true);
                model.populateTable(orders);
                table.setVisible(true);
            }


//            //  else the button is "submit"
//            //model.setDataVector(SuperLee.GUI.ModelOrdersTable.Constants.DATA, SuperLee.GUI.ModelOrdersTable.Constants.TABLE_HEADER);
//            submitOrdersButton.setEnabled(true);
//            model.populateTable(orders);
//            table.setVisible(true);


        }


        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String str = "Submit Orders";

            if (Objects.equals(str, buttonText)) {
                int[] selectedRows = table.getSelectedRows();
                ordersList = new ArrayList<>();
                if (selectedRows.length == 0) {
                    JOptionPane.showMessageDialog(null, "No orders selected.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Optionally, you can exit the method here
                }
                // Process the selected rows
                for (int row : selectedRows) {
                    // Access the data in the selected row
                    // Create a new TransportOrder object
                    Object orderId = table.getValueAt(row, 0);
                    Object supplierName = table.getValueAt(row, 1);
                    Object supplierNumber = table.getValueAt(row, 2);
                    Object address = table.getValueAt(row, 3);
                    Object date = table.getValueAt(row, 4);
                    Object contactPhone = table.getValueAt(row, 5);

                    Object typeObject = table.getValueAt(row, 7);
                    TrainingType type = TrainingType.valueOf(typeObject.toString());
                    int idInt = Integer.parseInt(orderId.toString());

                    ArrayList<Item> items = ordersController.getOrderByKey(idInt, type).getItems();
                    // Create a new TransportOrder object
                    TransportOrder transportOrder = new TransportOrder(
                            (int) orderId,
                            supplierName.toString(),
                            (int) supplierNumber,
                            address.toString(),
                            date.toString(),
                            contactPhone.toString(),
                            items
                    );
                    transportOrder.setTrainingType(docType);

                    ordersList.add(transportOrder);
                }
                ordersStatus = true;

            }
        }

        // Submit Truck
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String str = "Submit Truck";

            if (Objects.equals(buttonText, str) == true) {
                Truck selectedTruck = (Truck) truckComboBox.getSelectedItem();
                if (selectedTruck == null) {
                    JOptionPane.showMessageDialog(null, "Please select a Truck.");
                    return;
                }
                if (driver != null) {
                    //check driver to truck
                    TruckController truckController = TruckController.getInstance();
                    if (transportWorkersController.checkDriverToTruck(selectedTruck, driver) == 1) {
                        JOptionPane.showMessageDialog(null, "This driver does not have appropriate training for the selected truck!");
                        return;

                    } else if (transportWorkersController.checkDriverToTruck(selectedTruck, driver) == 2) {
                        JOptionPane.showMessageDialog(null, "The driver you selected does not have a suitable license to drive the selected truck!");
                        return;
                    }
                }

                truck = selectedTruck;
                truckStatus = true;
            }
        }


        // Submit Source
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String str = "Submit Source";

            if (Objects.equals(buttonText, str)) {
                Site selectedSource = (Site) sourceComboBox.getSelectedItem();
                if (selectedSource == null) {
                    JOptionPane.showMessageDialog(null, "Please select a Source.");
                    return;
                }

                source = selectedSource;
                sourceStatus = true;
            }
        }


        // Submit Driver
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String str = "Submit Driver";

            if (Objects.equals(buttonText, str) == true) {
                String selectedDriverStr = driverComboBox.getSelectedItem().toString();

                if (selectedDriverStr == null) {
                    JOptionPane.showMessageDialog(null, "Please select a Driver.");
                    return;
                }
                Pattern pattern = Pattern.compile("ID : (\\d+)");
                Matcher matcher = pattern.matcher(selectedDriverStr);
                String idSubstring = null;
                if (matcher.find()) {
                    idSubstring = matcher.group(1);
                } else {
                    return;
                }

                Driver selectedDriver = null;
                try {
                    selectedDriver = DriverController.getInstance().findDriver(idSubstring);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("Failed find driver");
                }

                if (truck != null) {
                    //check driver to truck
                    TruckController truckController = TruckController.getInstance();
                    if (transportWorkersController.checkDriverToTruck(truck, selectedDriver) == 1) {
                        JOptionPane.showMessageDialog(null, "This driver does not have appropriate training for the selected truck.", "Error", JOptionPane.ERROR_MESSAGE);

                        return;

                    } else if (transportWorkersController.checkDriverToTruck(truck, selectedDriver) == 2) {
                        JOptionPane.showMessageDialog(null, "The driver you selected does not have a suitable license to drive the selected truck.", "Error", JOptionPane.ERROR_MESSAGE);

                        return;
                    }
                }
                driver = selectedDriver;

            }
        }

        // Finish button
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            String buttonText = button.getText();
            String str = "Finish";

            if (Objects.equals(str, buttonText)) {
                ArrayList<Site> dests = new ArrayList<>();
                HashSet<String> uniqueAddresses = new HashSet<>();

                for (TransportOrder order : ordersList) {
                    String address = order.getAddress();

                    // Check if the address is already added to the HashSet
                    if (!uniqueAddresses.contains(address)) {
                        uniqueAddresses.add(address);

                        // Retrieve the site using the address
                        Site site = SiteController.getInstance().getSiteByAddress(address);

                        // Add the site to the destination list
                        dests.add(site);
                    }
                }
                int id = transportDocumentController.addTransportDocument(truck.getLicenseNumber(), driver.getID(), ordersList, source, dests, docType, dateTimeStr);
                TransportDocument document = transportDocumentController.getDocument(id);
                String docDetails = document.toString();

                JOptionPane.showMessageDialog(null,
                        docDetails,
                        "Order Completed",
                        JOptionPane.INFORMATION_MESSAGE);
                finishStatus = true;
            }

        }



    }

    public Boolean getStatus(){
        return finish;
    }

    public JComboBox<String> createDriverComboBox(JComboBox<String> boxString, String dateInput, ShiftType shiftType) {
        DriverController driverController = DriverController.getInstance();
        TransportWorkersController transportWorkersController = TransportWorkersController.getInstance();
        ArrayList<Driver> drivers = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateInput, formatter);
            drivers = driverController.getAvailableDriversByDate(localDate, shiftType);
            if(drivers == null){
                JOptionPane.showMessageDialog(null, "No available drivers on this shift!");
                return null;

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boxString.addItem(null);
        for (Driver driver : drivers) {
            String driverInfo = driver.toStringMethod_2();
            boxString.addItem(driverInfo);
        }
        return boxString;
    }

    public void setDateTimeStr(String dateTimeDocument) {
        this.dateTimeStr = dateTimeDocument;
    }
}
