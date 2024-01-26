package SuperLee.Transport.BusinessLayer;
import SuperLee.Transport.DataLayer.OrdersDAO;

import java.util.ArrayList;

public class OrdersController { // Singleton class

    private OrdersDAO ordersDAO;

    private OrdersController() {
        ordersDAO = OrdersDAO.getInstance();
    }

    private static OrdersController single_instance = null;

    public static synchronized OrdersController getInstance() {
        if(single_instance == null){
            single_instance = new OrdersController();
        }
        return single_instance;
    }

    //----------------------- Order functions----------------------------------------------
    public void createTransportOrder(int orderNumber, String supplierName, int supplierNumber, String address, String date, String contact_phone, ArrayList<Item> items){
        TransportOrder transportOrder = new TransportOrder( orderNumber,  supplierName, supplierNumber, address,  date,  contact_phone, items);
        splitOrder(transportOrder);
    }

    public void addTransportOrder(TransportOrder order){
        ordersDAO.addTransportOrder(order);
    }


    // key = orderNumber and trainingType
    public TransportOrder getOrderByKey(int orderNumber, TrainingType type){
        return ordersDAO.getTransportOrderByIDAndTrainingType(orderNumber, type);
    }

    public ArrayList<TransportOrder> getOrdersByTrainingType(TrainingType type){
        return ordersDAO.getOrdersByTrainingType(type);
    }

    public void splitOrder(TransportOrder tOrder) {
//        originOrders.put(tOrder.getOrderNumber(), tOrder);
        ArrayList<Item> t_items = tOrder.getItems();
        int dry_items = 0;
        int cool_items = 0;
        int freeze_items = 0;
        for (Item item : t_items) {
            if (item.getStorageType().equals("Dry")) {
                dry_items++;
            } else if (item.getStorageType().equals("Cool")) {
                cool_items++;
            } else {
                freeze_items++;
            }
        }

        if (dry_items > 0) {
            ArrayList<Item> dry_items_list = new ArrayList<>();
            for (Item item : t_items) {
                if (item.getStorageType().equals("Dry")) {
                    dry_items_list.add(item);
                }
            }
            // Create a new dry order and insert it in the right place in the hash table.
            TransportOrder dryOrder = new TransportOrder(tOrder.getOrderNumber(), tOrder.getSupplierName(), tOrder.getSupplierNumber(),
                    tOrder.getAddress(), tOrder.getDate(), tOrder.getContactPhone(), dry_items_list);
            dryOrder.setTrainingType(TrainingType.DRY);
            ordersDAO.addTransportOrder(dryOrder);

        }
        if (cool_items > 0) {
            ArrayList<Item> cool_items_list = new ArrayList<>();
            for (Item item : t_items) {
                if (item.getStorageType().equals("Cool")) {
                    cool_items_list.add(item);
                }
            }
            TransportOrder coolOrder = new TransportOrder(tOrder.getOrderNumber(), tOrder.getSupplierName(), tOrder.getSupplierNumber(),
                    tOrder.getAddress(), tOrder.getDate(), tOrder.getContactPhone(), cool_items_list);
            coolOrder.setTrainingType(TrainingType.REFRIGERATED);
            ordersDAO.addTransportOrder(coolOrder);

        }
        if (freeze_items > 0) {
            ArrayList<Item> freeze_items_list = new ArrayList<>();
            for (Item item : t_items) {
                if (item.getStorageType().equals("Frozen")) {
                    freeze_items_list.add(item);
                }
            }
            TransportOrder freezeOrder = new TransportOrder(tOrder.getOrderNumber(), tOrder.getSupplierName(), tOrder.getSupplierNumber(),
                    tOrder.getAddress(), tOrder.getDate(), tOrder.getContactPhone(), freeze_items_list);
            freezeOrder.setTrainingType(TrainingType.FROZEN);
            ordersDAO.addTransportOrder(freezeOrder);

        }
    }

    // Move order from pending to selected if the transport manager decided to add it to transport document.
    public void moveOrderToSelected(int id, TrainingType type){
        TransportOrder transportOrder = ordersDAO.getTransportOrderByIDAndTrainingType(id, type);
        transportOrder.setStatus(OrderStatus.SELECTED_FOR_TRANSPORT);
        ordersDAO.updateOrder(transportOrder);
    }

    // receive list of order id and returns list of orders objects
    public ArrayList<TransportOrder> convertToOrdersByIDAndType(ArrayList<Integer> ordersID, TrainingType type){
        ArrayList<TransportOrder> result = new ArrayList<>();
        ArrayList<TransportOrder> ordersBYType = getOrdersByTrainingType(type);
        for(TransportOrder order : ordersBYType){
            if(ordersID.contains(order.getOrderNumber())){
                result.add(order);
            }
        }
        return result;
    }

//    public ArrayList<TransportOrder> getAllOrderBySite(Site site){
//        ArrayList<TransportOrder> result = new ArrayList<>();
//        ArrayList<TransportOrder> allOrders = new ArrayList<>();
//        allSubOrders.values().forEach(allOrders::addAll);
//        for(TransportOrder order : allOrders){
//            if(order.getAddress().equals(site.getAddress())){
//                result.add(order);
//            }
//        }
//        return result;
//    }

    public ArrayList<TransportOrder> getAvailableOrdersByType(TrainingType type){
//        if(subOrders.size() == 0){
//            return null;
//        }
        ArrayList<TransportOrder> result = new ArrayList<>();
        ArrayList<TransportOrder> ordersByType = getOrdersByTrainingType(type);
        for (TransportOrder order : ordersByType){
            if(order.getStatus() == OrderStatus.PENDING){
                result.add(order);
            }
        }
        return result;
    }

    public ArrayList<TransportOrder> getOrdersBy_Area_And_Type(TrainingType trainingType, ArrayList<Site>sites){
        ArrayList<TransportOrder> result = new ArrayList<>();
        ArrayList<TransportOrder> ordersByType = getOrdersByTrainingType(trainingType);
        for(TransportOrder transportOrder : ordersByType){
            for(Site site : sites){
                if (transportOrder.getAddress().equals(site.getAddress())){
                    result.add(transportOrder);
                }
            }
        }
        return result;
    }

    public ArrayList<Integer> getAvailableOrdersByAreaAndType(TrainingType trainingType, ArrayList<Site>sites){
        ArrayList<TransportOrder> orders = getOrdersByTrainingType(trainingType);
        ArrayList<Integer> result = new ArrayList<>();
        for (TransportOrder order : orders){
            if(order.getStatus() == OrderStatus.PENDING) {
                for (Site site : sites) {
                    if (order.getAddress().equals(site.getAddress())) {
                        result.add(order.getOrderNumber());
                    }
                }
            }

        }
        return result;
    }



    public boolean displayOrders(TrainingType type, ArrayList<String> sitesAddress) {
        int flag = 0;
        ArrayList<Site> sites = new ArrayList<>();
        for(String address : sitesAddress){
            Site site = SiteController.getInstance().getSiteByAddress(address);
            sites.add(site);
        }
        ArrayList<Integer> allOrders = getAvailableOrdersByAreaAndType(type, sites);
        for (int i = 0; i < allOrders.size(); i++) {
            System.out.println((i + 1) + ". " + ordersDAO.getTransportOrderByIDAndTrainingType(allOrders.get(i), type));
            flag = 1;
        }
        if(flag == 1){
            return true;
        }
        return false;
    }

    // -------------------For the DocumentGUI-----------------------------------
    public ArrayList<TransportOrder> getOrdersAfterWorkersController(TrainingType type, ArrayList<String> sitesAddress){
        ArrayList<TransportOrder> orders = new ArrayList<>();
        ArrayList<Site> sites = new ArrayList<>();

        for (String address : sitesAddress) {
            Site site = SiteController.getInstance().getSiteByAddress(address);
            sites.add(site);
        }

        ArrayList<Integer> allOrders = getAvailableOrdersByAreaAndType(type, sites);
        if(allOrders.isEmpty()){
            return null;
        }
        for (int i = 0; i < allOrders.size(); i++) {
            TransportOrder order = ordersDAO.getTransportOrderByIDAndTrainingType(allOrders.get(i), type);
            orders.add(order);
        }

        return orders;
    }


    public void printOrders(ArrayList<Integer> ordersSelected, TrainingType type, ArrayList<Site> sites){
        ArrayList<Integer> allOrdersID = getAvailableOrdersByAreaAndType(type, sites);
        ArrayList<TransportOrder> allOrders = convertToOrdersByIDAndType(allOrdersID, type);
        for (TransportOrder order : allOrders) {
            if(ordersSelected.contains(order.getOrderNumber()))
                System.out.println("- " + order);
        }
    }

    public ArrayList<Integer> getAvailableOrdersIDByType(TrainingType trainingType){
        ArrayList<TransportOrder> orders = getOrdersByTrainingType(trainingType);
        ArrayList<Integer> result = new ArrayList<>();
        for (TransportOrder order : orders){
            if(order.getStatus() == OrderStatus.PENDING){
                result.add(order.getOrderNumber());
            }

        }
        return result;
    }

    public void setOrderStatus(TransportOrder order, OrderStatus orderStatus){
        order.setStatus(orderStatus);
        ordersDAO.updateOrder(order);
    }

    public void update(TransportOrder order){
        ordersDAO.updateOrder(order);
    }
}
