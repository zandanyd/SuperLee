package SuperLee.Transport.BusinessLayer;

import SuperLee.HumenResource.BusinessLayer.Driver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Transport {
    private int id;
    private String startLocalDateTime;
    private String endLocalDateTime;

    private TransportDocument transportDocument;
    private TransportStatus status;
    private Site source;
    private Driver driver;
    private Truck truck;
    private double currentTruckWeight;


    public Transport() {
        LocalDateTime temp = LocalDateTime.of(0, 1, 1, 0, 0, 0); // Initialization with zeros
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format the date and time using the defined format
        endLocalDateTime = temp.format(formatter);
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public void setSource(Site source) {
        this.source = source;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void setStatus(TransportStatus status) {
        this.status = status;
    }

    public TransportStatus getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartLocalDateTime() {
        return startLocalDateTime;
    }

    public String getEndLocalDateTime() {
        return endLocalDateTime;
    }


    public void setTransportDocument(TransportDocument transportDocument) {
        this.transportDocument = transportDocument;
    }

    public void setCurrentTruckWeight(double currentTruckWeight) {
        this.currentTruckWeight = currentTruckWeight;
    }

    public int getId() {
        return id;
    }

    public void setEndLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.endLocalDateTime =  localDateTime.format(formatter);
    }

    public void setStartLocalDateTime(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.startLocalDateTime = localDateTime.format(formatter);
    }

    public Truck getTruck() {
        return truck;
    }

    public Driver getDriver() {
        return driver;
    }

    public Site getSource() {
        return source;
    }

    public TransportDocument getTransportDocument() {
        return transportDocument;
    }

    public ArrayList<TransportOrder> getOrdersOfDestination(Site dest){
        return transportDocument.getOrdersOfDestination(dest);
    }

    public TrainingType getTrainingRequired(){
        return transportDocument.getTrainingRequired();
    }

    public void setNewTruck(Truck newTruck){
        this.truck = newTruck;
        transportDocument.setTruckLicense(newTruck.getLicenseNumber());
        this.transportDocument.setTrainingRequired(truck.getTrainingType());
    }

    public boolean removeDestination(Site dest){
        return transportDocument.removeDestination(dest);
    }

    public boolean removeOrder(TransportOrder order){
        return transportDocument.removeOrder(order);
    }

    public void weighing(double weight){
        transportDocument.updateWeight(weight);
    }

    public double getCurrentTruckWeight(){
        return transportDocument.getCurrentTruckWeight();
    }

    public void addOrder(TransportOrder transportOrder){
        transportDocument.addOrder(transportOrder);
    }

    public void addDestination(Site site){
        transportDocument.addDestination(site);
    }

    public boolean destinationExist(Site dest){
        return transportDocument.destinationExist(dest);
    }

    @Override

    public String toString() { // TODO check it again
        String driverDetails = "\tDRIVER:\n";
        String truckDetails = "\tTRUCK:\n";
        driverDetails += "\t" + driver.toString() + "\n";
        truckDetails += "\t" + truck.toString() + "\n";


        return "Transport id: " + id + "\n" +
                this.transportDocument.toString() + "\n" +
                "\tSTART TIME:\n" + "\t\t" + startLocalDateTime + "\n" +
                "\tEND TIME:\n" + "\t\t" + endLocalDateTime + "\n" +
                "\tCURRENT TRUCK WIGHT:\n" + "\t" + transportDocument.getCurrentTruckWeight() + "\n\n" +
                "\tTRANSPORT STATUS:\n" + "\t" + this.getStatus().toString() + "\n" +
                driverDetails + "\n" + truckDetails;
    }

}
