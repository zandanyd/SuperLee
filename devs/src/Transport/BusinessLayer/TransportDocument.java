package SuperLee.Transport.BusinessLayer;

import java.util.ArrayList;

public class TransportDocument {

    private int id;
    private ArrayList<Site> destinationList; // List of destinations in order
    private ArrayList<TransportOrder> orders; // list of all orders
    private String startTransportTime;
    private Site source;
    private String driverID;
    private int truckLicense;
    private OrderStatus status;
    private double currentTruckWeight;
    private TrainingType trainingRequired;

    public TransportDocument( Site source, String driverID, int truckLicense){
        this.orders = new ArrayList<>();
        this.destinationList = new ArrayList<>();
        this.driverID = driverID;
        this.truckLicense = truckLicense;
        this.source = source;
        status = OrderStatus.PENDING;
    }

    public TransportDocument(){}

    public void setDestinationList(ArrayList<Site> destinationList) {
        this.destinationList = destinationList;
    }

    public void setOrders(ArrayList<TransportOrder> orders) {
        this.orders = orders;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public void setTruckLicense(int truckLicense) {
        this.truckLicense = truckLicense;
    }

    public void setCurrentTruckWeight(double currentTruckWeight) {
        this.currentTruckWeight = currentTruckWeight;
    }

    public void setTrainingRequired(TrainingType trainingRequired) {
        this.trainingRequired = trainingRequired;
    }

    public ArrayList<Site> getDestinationList() {
        return destinationList;
    }

    public Site getSource() {
        return source;
    }

    public String getDriverID() {
        return driverID;
    }

    public int getTruckLicense() {
        return truckLicense;
    }

    public TrainingType getTrainingRequired() {
        return trainingRequired;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void addDestination(Site dest){
        if(destinationList.contains(dest)){
            return;
        }
        destinationList.add(dest);
    }

    public String getStartTransportTime() {
        return startTransportTime;
    }

    public void setStartTransportTime(String startTransportTime) {
        this.startTransportTime = startTransportTime;
    }

    public ArrayList<TransportOrder> getOrders(){
        return orders;
    }

    public void addOrder(TransportOrder transportOrder) {
        if (orders.size() == 0) {
            orders = new ArrayList<TransportOrder>();
        }
        orders.add(transportOrder);
    }

    public boolean removeDestination(Site dest){
        for(Site site : destinationList){
            if(site.getAddress().equals(dest.getAddress())){
                destinationList.remove(site);
                return true;
            }
        }
        return false;
    }

    public ArrayList<TransportOrder> getOrdersOfDestination(Site dest){
//        if(!destinationList.contains(dest)){
//            return null;
//        }
        ArrayList<TransportOrder> result = new ArrayList<>();
        for (TransportOrder order : orders){
            if(order.getAddress().equals(dest.getAddress())){
                result.add(order);
            }
        }
        return result;
    }

    public TransportOrder getOrderByNum(int orderNum){
        for (TransportOrder transportOrder : orders){
            if (transportOrder.getOrderNumber() == orderNum){
                return transportOrder;
            }
        }
        return null;
    }

    public void updateWeight(double weight){
        currentTruckWeight = weight;
    }

    public double getCurrentTruckWeight(){
        return currentTruckWeight;
    }

    public  int getId() {
        return id;
    }

    public void setID(int id){
        this.id = id;
    }

    public void setSource(Site source){
        this.source = source;
    }

    public boolean removeOrder(TransportOrder order){
        if (!orders.contains(order)) {
            return false;
        }
        orders.remove(order);
        return false;
    }

    public boolean destinationExist(Site dest){
        if(destinationList.contains(dest)){
            return true;
        }
        return false;
    }

    @Override


    public String toString() {
        String documentDetails = String.format("Transport Document Details:\n\tID: %d\n", id);
        String destinationListDetails = String.format("\tDESTINATION LIST (%d sites):\n", destinationList.size());
        String ordersDetails = String.format("\tORDERS (%d orders):\n", orders.size());
        String sourceDetails = "\tSOURCE:\n";
        String driverDetails = "\tDRIVER ID:\n\t" + driverID;
        String truckDetails = "\tTRUCK LICENSE:\n\t" + truckLicense;

        for (Site destination : destinationList) {
            destinationListDetails += "\t" + destination.toString() + "\n";
        }

        for (TransportOrder order : orders) {
            ordersDetails += "\t" + order.toString() + "\n";
        }

        sourceDetails += "\t" + source.toString() + "\n";
//        driverDetails += "\t" + driver.toString() + "\n";
//        truckDetails += "\t" + truck.toString() + "\n";

        return documentDetails +"\n"+ destinationListDetails + "\n" + ordersDetails + "\n"+ sourceDetails +"\n"+ driverDetails +"\n"+ truckDetails;
    }
}
