package SuperLee.Transport.BusinessLayer;

import SuperLee.HumenResource.BusinessLayer.Driver;
import SuperLee.HumenResource.BusinessLayer.DriverController;
import SuperLee.TransportWorkersController;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TransportManagment { // Singleton

    private static TransportManagment single_instance = null;

    public static synchronized TransportManagment getInstance() {
        if (single_instance == null) {
            single_instance = new TransportManagment();
        }
        return single_instance;
    }

    //    public HashMap<TrainingType, ArrayList<Truck>> availableTrucksMap; // by type (cool..)
    private TruckController truckController;

    public ArrayList<Driver> allDrivers;
    private OrdersController ordersController;
    private DriverController driverController;

    //    public Map<String, Site> sitesByAddress; // all sites
    private SiteController siteController;

    //    public HashMap<Integer,TransportDocument> TransDocumentsList; //
    private TransportDocumentController transportDocumentController;

    //    private HashMap<Integer, Transport> allTransports;  // transports not done yet
    private TransportsController transportsController;

//    private TransportWorkersController transportWorkersController;


    // Shipping area service
//    public ArrayList<Transport> getAllTransport () { return (ArrayList<Transport>) allTransports. values();};

    private TransportManagment()  {

        this.truckController = TruckController.getInstance();
        this.siteController = SiteController.getInstance();
        this.ordersController = OrdersController.getInstance();
        this.transportDocumentController = TransportDocumentController.getInstance();
        this.driverController = DriverController.getInstance();

        this.transportsController = TransportsController.getInstance();
//        this.transportWorkersController = TransportWorkersController.getInstance();

        allDrivers = new ArrayList<>();

    }


    // TODO need to change the fields add to transportOrder -> (source , destination)
    public ArrayList<Site> getSitesFromOrders(ArrayList<TransportOrder> orders) {
        ArrayList<Site> sites = new ArrayList<>();
        for (TransportOrder transportOrder : orders) {
            Site s = siteController.getSiteByAddress(transportOrder.getAddress());
            sites.add(s); // TODO: check if address not exist
        }
        return sites;
    }

    public void prepareToTransportDocument(int truckLicense, String driverID, ArrayList<Integer> ordersID, TrainingType type) {
        Driver driver = null;
        try {
            driver = driverController.findDriver(driverID);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        Truck truck = truckController.getTruckByLicense(truckLicense);
        truckController.setTruckUnAvailable(truck);
//        truckController.addDateToTruck(tr);

        ArrayList<TransportOrder> orders = ordersController.convertToOrdersByIDAndType(ordersID, type);
        for (TransportOrder transportOrder : orders) {
            ordersController.setOrderStatus(transportOrder, OrderStatus.SELECTED_FOR_TRANSPORT);
        }
    }

//    public void displayTransportsByDateAndSite(Site site, LocalDateTime date){
//        for(Transport transport : transportsController.getAllTransports()){
//            if(transport.getDateTime().isEqual(date)) {
//                for (Site site1 : transport.getTransportDocument().getDestinationList()) {
//                    if (site.getAddress().equals(site1.getAddress())) {
//                        System.out.println(transport);
//                        break;
//                    }
//                }
//            }
//        }
//    }


    //--------------------- Replanning transport functions----------------------------------------------
    //--------------------- Replanning transport functions----------------------------------------------
    public void replaceSitesOfTransport(int transportID, String addressToRemove, ArrayList<Integer> ordersAdd1) {
        if (ordersAdd1.size() == 0) {
            return;
        }
        Transport transport = transportsController.getTransportById(transportID);
        Site siteToRemove = siteController.getSiteByAddress(addressToRemove);
        ArrayList<TransportOrder> ordersToRemove = transport.getOrdersOfDestination(siteToRemove);
        transport.removeDestination(siteToRemove);
        for (TransportOrder order : ordersToRemove) {
            transport.removeOrder(order);
            ordersController.setOrderStatus(order, OrderStatus.PENDING);
        }
        String addressNewSite = null;
        ArrayList<TransportOrder> ordersAdd = ordersController.convertToOrdersByIDAndType(ordersAdd1, transport.getTrainingRequired());
        for (TransportOrder order : ordersAdd) {
            transport.addOrder(order);
            ordersController.setOrderStatus(order, OrderStatus.SELECTED_FOR_TRANSPORT);
            addressNewSite = order.getAddress();
        }
        Site newSite = siteController.getSiteByAddress(addressNewSite);
        transport.addDestination(newSite);
        transportsController.updateTransport(transport);
    }

    public boolean checkAddressExistInTransport(int transportID, String addressDest) {
        Transport transport = transportsController.getTransportById(transportID);
        Site dest = siteController.getSiteByAddress(addressDest);
        if (dest == null) {
            return false;
        }
        return transport.destinationExist(dest);
    }


    // Removing a dest from transport while it is happening
    public void removeDestinationOfTransport(int transportID, String addressToRemove) {
        Transport transport = transportsController.getTransportById(transportID);
        Site siteToRemove = siteController.getSiteByAddress(addressToRemove);
        ArrayList<TransportOrder> ordersToRemove = transport.getOrdersOfDestination(siteToRemove);
        transport.removeDestination(siteToRemove);
        for (TransportOrder order : ordersToRemove) {
            transport.removeOrder(order);
            ordersController.setOrderStatus(order, OrderStatus.PENDING);
        }
        transportsController.updateTransport(transport);
    }


    // Function that print the orders for each site, according to the transport training-type and the chosen shipping area that the user chose.
    // and print it only if the order is "PENDING"
    public boolean printOrdersOfEachSite_ByTransportType(int transportID, ShippingArea area) throws SQLException{
        Transport transport = transportsController.getTransportById(transportID);
        ArrayList<Site> sitesByArea = siteController.getSitesByArea(area);
        ArrayList<TransportOrder> ordersByTraining_and_Area = ordersController.getOrdersBy_Area_And_Type(transport.getTruck().getTrainingType(), siteController.getSitesByArea(area));
        if (ordersByTraining_and_Area.size() == 0) {
            return false;
        }
        int flag = 0;
        boolean hasPendingOrders = false;
        for (Site site : sitesByArea) {
            hasPendingOrders = false;
            if(!TransportWorkersController.getInstance().getAvailableSiteByDate(transport.getTransportDocument().getStartTransportTime(), site.getAddress())){ // TODO the date will be dateTimeNow after the tests!
                continue;
            }
            if (checkAddressExistInTransport(transportID, site.getAddress() )) {
                continue;
            }
            // TODO check if sitesAddress is open
            for (TransportOrder order : ordersByTraining_and_Area) {
                if (order.getStatus().equals(OrderStatus.PENDING) && order.getAddress().equals(site.getAddress())) {
                    hasPendingOrders = true;
                    flag = 1;
                    break;
                }
            }
            if (hasPendingOrders) {
                System.out.println("Orders for " + site.getAddress() + ":");
                for (TransportOrder order : ordersByTraining_and_Area) {
                    if (order.getStatus() == OrderStatus.PENDING && order.getAddress().equals(site.getAddress())) {
                        System.out.println("- "  + order.toString());
                    }
                }
                System.out.println();
            }
        }
        if (flag == 0) {
            return false;
        }
        return true;
    }

    // Returns list of orders
    public ArrayList<Integer> getOrdersOfSiteBy_trainingTypeTransport(int transportID, ShippingArea area, String siteAddress) {
        Transport transport = transportsController.getTransportById(transportID);
        ArrayList<Integer> result = new ArrayList<>();
        Site site = siteController.getSiteByAddress(siteAddress);
        ArrayList<TransportOrder> ordersByTraining_and_Area = ordersController.getOrdersBy_Area_And_Type(transport.getTruck().getTrainingType(), siteController.getSitesByArea(area));
        for (TransportOrder order : ordersByTraining_and_Area) {
            if (order.getAddress().equals(site.getAddress())) {
                result.add(order.getOrderNumber());
            }
        }
        return result;
    }

    // Check if exist trucks thst suitble to this transport and return it.
    public ArrayList<Truck> getExistTrucksDuringTransport(int transportID, double weight) {
        Transport transport = transportsController.getTransportById(transportID);
        ArrayList<Truck> trucks = truckController.getAvailableTrucksByType(transport.getTrainingRequired(), LocalDate.now());
        if (trucks == null) {
            return null;
        }
        ArrayList<Truck> result = new ArrayList<>();
        for (Truck truck : trucks) {
            if (truck.getAvailable().equals(true) && truck.getMaxWeight() > weight) {
                result.add(truck);
            }
        }
        if (result.size() == 0) {
            return null;
        }
        return result;
    }

    public void printSuitableTrucksToTransport(int transportID, double weight) {
        ArrayList<Truck> trucks = getExistTrucksDuringTransport(transportID, weight);
        System.out.println("Suitable trucks for transport:");
        for (Truck truck : trucks) {
            System.out.println(truck.toString());
        }
    }

    public boolean replaceTrucksDuringTransport(int transportID, Truck newTruck) {
        Transport transport = transportsController.getTransportById(transportID);
        if (!checkLicenseDriverToTruck(transport.getDriver(), newTruck)) {
            return false; // The driver of this transport does not have an appropriate license for the truck you selected
        }
        Truck oldTruck = transport.getTruck();
        truckController.removeDateToTruck(LocalDate.now(), oldTruck);
        transport.setNewTruck(newTruck);
        truckController.addDateToTruck(LocalDate.now(), newTruck);
        transportsController.updateTransport(transport);

        return true;
    }

    // Functions for removing items while transport

    // Return order from specific transport orders list by it order number.
    public TransportOrder getOrderByNumDuringTransport(int transportID, int orderNum) {
        Transport transport = transportsController.getTransportById(transportID);
        TransportOrder order = transport.getTransportDocument().getOrderByNum(orderNum);
        if (order == null) {
            return null;
        }
        return order;
    }

    // Return 1 if orderNum not good, 2 for itemName, 3 for amount
    public int removeItemsFromTransportByOrderNum(int transportID, int orderNum, String itemName, int amount) {
        Transport transport = transportsController.getTransportById(transportID);
        TransportOrder order = getOrderByNumDuringTransport(transportID, orderNum);
        if (order == null) {
            return 1;
        }
        for (Item item : order.getItems()) {
            if (item.getName().equals(itemName)) {
                if ((item.getAmount() - amount) < 0) {
                    return 3;
                }
                if ((item.getAmount() - amount) == 0) { // if need to remove all the item amount.
                    if (order.getItems().size() == 1) { // if this is the only item on this specific order so remove all the order
                        transport.removeOrder(order);
                        ordersController.setOrderStatus(order, OrderStatus.PENDING);
                        return 10;
                    }
                    order.removeItem(item);
                    ordersController.update(order);
                }
                item.addToAmount(-amount);
                ordersController.update(order);
                Item newItem = new Item(item.getName(), item.getStorageType(), amount);
                ArrayList<Item> newItemslist = new ArrayList<>(); // Save this item order for next transports.
                newItemslist.add(newItem);
                TransportOrder newOrder = new TransportOrder(order.getOrderNumber(), order.getSupplierName(), order.getSupplierNumber(), order.getAddress(),
                        order.getDate(), order.getContactPhone(), newItemslist);
                return 10; // Completed successfully
            }
        }
        return 2; // Didn't find the item!
    }


    // ---------- Transport -----------------------------------------------------------------
    // ---------- Transport -----------------------------------------------------------------




    public Integer startTransport(int transportDocId) {
        TransportDocument transportDocument = transportDocumentController.getDocument(transportDocId);
        if (transportDocument == null) {
            return -1;
        }
        if (transportDocument.getStatus() == OrderStatus.SELECTED_FOR_TRANSPORT) {
            return -1;
        }
        Transport transport = new Transport();
        transportsController.setTransportID(transport);
        transportDocumentController.setDocumentStatus(transportDocument, OrderStatus.SELECTED_FOR_TRANSPORT);
        transport.setTransportDocument(transportDocument);

        try {
            transport.setDriver(driverController.findDriver(transportDocument.getDriverID()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        transport.setSource(transportDocument.getSource());

        Truck truck = truckController.getTruckByLicense(transportDocument.getTruckLicense());
        transport.setTruck(truck);

        transport.setStatus(TransportStatus.IN_TRANSIT);

        LocalDateTime dateTime = LocalDateTime.now();
        transport.setStartLocalDateTime(dateTime);

        transportsController.addTransport(transport);

        return transport.getId();
    }


    // Function that returns false if the new weight exceeds the maximum allowed weight on the truck
//    public boolean updateTruckWeight(int transportID, double newWeight) {
//        Transport transport = transportsController.getTransportById(transportID);
//        if (newWeight > transport.getTruck().getMaxWeight()) {
//            return false;
//        }
//        transport.weighing(newWeight); // TODO if weight not good do not update!
//
//        return true;
//    }


    public void printTransport(int transportID) {
        Transport transport = transportsController.getTransportById(transportID);
        System.out.println(transport.toString());
    }


    // Print order by transport number and order number (ID)
    public void printOrderByTransportID(int transportID, int orderNumber) {
        Transport transport = transportsController.getTransportById(transportID);
        for (TransportOrder order : transport.getTransportDocument().getOrders()) {
            if (order.getOrderNumber() == orderNumber) {
                System.out.println(order.toString());
            }
        }
    }


//    // Returns true if the driver has good license to this truck.
    public boolean checkLicenseDriverToTruck(Driver driver, Truck truck) {
        return (driver.getLicenseType().getWeightLimit() >= truck.getNetWeight());
    }



    public void fillDataTests() throws SQLException {
        System.out.println("implement after the last connection with the others module");
    }


}

