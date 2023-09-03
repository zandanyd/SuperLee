package Tests;

import SuperLee.Transport.BusinessLayer.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class transportTests {


    @Test
    void getSitesByArea() {
        ArrayList<Site> allSites = SiteController.getInstance().getAllSites();
        for (Site site : allSites) {
            System.out.println(site.toString());
        }

        ArrayList<Site> sitesByArea = SiteController.getInstance().getSitesByArea(ShippingArea.SOUTH);
        if (sitesByArea == null) {
            System.out.println("-- Test failed --");
            System.out.println("There are no sites in the SOUTH area.");
        } else {
            System.out.println("-- Test passed --");
            System.out.println("All sites from the SOUTH area:");
            for (Site site : sitesByArea) {
                System.out.println(site.toString());
            }
        }

        // Assert that the returned sites match the expected sites
        ArrayList<Site> expectedSites = SiteController.getInstance().getSitesByArea(ShippingArea.SOUTH);
        assertNotNull(expectedSites);
        assertEquals(expectedSites, sitesByArea);
    }


    @Test
    void convertOrderToSelectedTest() {
        TrainingType type = TrainingType.REFRIGERATED;
        ArrayList<TransportOrder> orders = OrdersController.getInstance().getOrdersByTrainingType(type);

        // Convert orders to selected for transport
        for (TransportOrder order : orders) {
            System.out.println("Order status before conversion: " + order.getStatus());
            OrdersController.getInstance().setOrderStatus(order, OrderStatus.SELECTED_FOR_TRANSPORT);
        }

        // Check the status after conversion
        for (TransportOrder order : orders) {
            System.out.println("Order status after convert: " + order.getStatus());
            assertEquals(OrderStatus.SELECTED_FOR_TRANSPORT, order.getStatus());
        }

        // RETURN TO THE INITIALLIZE TIME
        ArrayList<TransportOrder> orders2 = OrdersController.getInstance().getOrdersByTrainingType(type);

        // Convert orders to selected for transport
        for (TransportOrder order : orders) {
            System.out.println("Order status before conversion: " + order.getStatus());
            OrdersController.getInstance().setOrderStatus(order, OrderStatus.PENDING);
        }

    }

    @Test
    void createTruckTest() {
        int licenseNumber = 2000;
        String model = "Porsche";
        int netWeight = 4000;
        int maxWeight = 5000;
        String type_truck = "DRY";

        // Check if the truck with license number 10 does not exist before the test
        boolean existBefore = TruckController.getInstance().truckExist(licenseNumber);
        assertFalse(existBefore);

        // Create the truck
        TruckController.getInstance().createTruck(licenseNumber, model, netWeight, maxWeight, type_truck);

        // Get the created truck by license number
        Truck truck = TruckController.getInstance().getTruckByLicense(licenseNumber);

        // Check if the truck exists after creation
        assertNotNull(truck);

        // Check if the truck's attributes match the provided values
        assertEquals(licenseNumber, truck.getLicenseNumber());
        assertEquals(model, truck.getModel());
        assertEquals(netWeight, truck.getNetWeight());
        assertEquals(maxWeight, truck.getMaxWeight());
        assertEquals(type_truck, truck.getTrainingType().toString());

        // Print test result
        boolean testPassed = licenseNumber == truck.getLicenseNumber() &&
                model.equals(truck.getModel()) &&
                netWeight == truck.getNetWeight() &&
                maxWeight == truck.getMaxWeight() &&
                type_truck.equals(truck.getTrainingType().toString());
        System.out.println("Test passed: " + testPassed);
        TruckController.getInstance().deleteTruck(truck);
    }

    @Test
    void createDocument(){ // two test: prepere + addTransportDoc
        String driverId = "2";
        int truck_license = 44;
        ArrayList<Integer> orders = new ArrayList<>();
        orders.add(55);
        orders.add(56);

        TransportManagment transportManagment = TransportManagment.getInstance();
        transportManagment.prepareToTransportDocument(truck_license, driverId, orders, TrainingType.DRY);

        TransportDocumentController transportDocumentController = TransportDocumentController.getInstance();
        OrdersController ordersController = OrdersController.getInstance();
        SiteController siteController = SiteController.getInstance();

        // Verify that the transport document was added successfully
        int docID = transportDocumentController.addTransportDocument(
                truck_license,
                driverId,
                ordersController.convertToOrdersByIDAndType(orders, TrainingType.DRY),
                siteController.getSiteByAddress("Beer Sheva"),
                transportManagment.getSitesFromOrders(ordersController.convertToOrdersByIDAndType(orders, TrainingType.DRY)),
                TrainingType.DRY,
                "2023-12-12 11:11"
        );
        System.out.println(TransportDocumentController.getInstance().getDocument(docID));
        assert docID > 0 : "Transport document ID should be greater than 0";


        boolean flag =true;
        if(TransportDocumentController.getInstance().getDocument(docID) == null) {
            flag = false;
            System.out.println("Test Faild");
        }
        assertEquals(true, flag);

        //BACK TO THE START POSITION
        TransportDocumentController.getInstance().deleteDocument(docID);
        // INITALLIZE THE ORDERS STATUS
        TransportOrder addedOrder = OrdersController.getInstance().getOrderByKey(55, TrainingType.DRY);
        TransportOrder addedOrder2 = OrdersController.getInstance().getOrderByKey(56, TrainingType.DRY);
        ordersController.setOrderStatus(addedOrder, OrderStatus.PENDING);
        ordersController.setOrderStatus(addedOrder2, OrderStatus.PENDING);

    }



}