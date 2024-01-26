package SuperLee.Transport.BusinessLayer;

import SuperLee.Transport.BusinessLayer.Item;
import SuperLee.Transport.BusinessLayer.OrderStatus;
import SuperLee.Transport.BusinessLayer.TrainingType;

import java.util.ArrayList;

public class TransportOrder {
    private int orderNumber;
    private String supplierName;
    private int supplierNumber;
    private String address;
    private String date;
    private String contact_phone;
    private ArrayList<Item> items;
    private OrderStatus status;
    private TrainingType trainingRequired; // The training required for the order item type

    public TransportOrder(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TransportOrder(int orderNumber, String supplierName, int supplierNumber, String address, String date, String contact_phone, ArrayList<Item> items) {
        this.orderNumber = orderNumber;
        this.supplierName = supplierName;
        this.supplierNumber = supplierNumber;
        this.address = address;
        this.date = date;
        this.contact_phone = contact_phone;
        this.items = items;
        this.status = OrderStatus.PENDING;
    }

    public OrderStatus getStatus() {return status;}

    public void setStatus(OrderStatus status) {this.status = status;}

//    public void removeItemAmount(String itemName, int amount){
//        for (int i = 0;)
//    }

    public void removeItem(Item item){
        if(items.contains(item)){
            items.remove(item);
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getContactPhone() {
        return contact_phone;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public TrainingType getStorageTypeOrder() { return trainingRequired; }

    public void setTrainingType(TrainingType typeOrder){
        this.trainingRequired = typeOrder;
    }

    public TrainingType getTrainingRequired() {
        return trainingRequired;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void setTrainingRequired(TrainingType trainingRequired) {
        this.trainingRequired = trainingRequired;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TransportOrder{")
                .append("orderNumber:").append(orderNumber)
                .append(", supplierName:'").append(supplierName).append('\'')
                .append(", supplierNumber:").append(supplierNumber)
                .append(", address:'").append(address).append('\'')
                .append(", date:'").append(date).append('\'')
                .append(", contactPhone:'").append(contact_phone).append('\'')
                .append(", items:[");

        for (int i = 0; i < items.size(); i++) {
            sb.append("{name:'").append(items.get(i).getName()).append("', amount:").append(items.get(i).getAmount()).append("}");
            if (i < items.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("], status:").append(status)
                .append('}');
        return sb.toString();
    }


}
