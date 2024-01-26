package SuperLee.Transport.BusinessLayer;

import SuperLee.Transport.DataLayer.TransportDAO;
import SuperLee.TransportWorkersController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class TransportsController { // Singleton

    private static  TransportsController single_instance = null;
//    private HashMap<Integer, Transport> allTransports;  // transports not done yet
    private TransportDAO transportDAO;

    public static synchronized TransportsController getInstance() {
        if(single_instance == null){
            single_instance = new TransportsController();
        }
        return single_instance;
    }

    private TransportsController() {
        transportDAO = TransportDAO.getInstance();
    }

    public Transport getTransportById(int id){
        return transportDAO.getTransportById(id);
    }


    public boolean finishTransport(int transportID)  {
        Transport transport = getTransportById(transportID);
        if (transport == null){
            return false;
        }
        TruckController truckController = null;
        TransportDocumentController documentController = null;
        OrdersController ordersController = null;
        truckController = TruckController.getInstance();
        documentController = TransportDocumentController.getInstance();
        ordersController = OrdersController.getInstance();
        truckController.setTruckAvailable(transport.getTruck()); // TODO not need it
        documentController.setDocumentStatus(transport.getTransportDocument() , OrderStatus.CLOSE);
        transport.setStatus(TransportStatus.DONE);


        // Specify the format of the string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Parse the string to LocalDateTime
        LocalDateTime dateTimeStart = LocalDateTime.parse(transport.getStartLocalDateTime(), formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(transport.getEndLocalDateTime(), formatter);
        TransportWorkersController.getInstance().UpdateDriverWorkHours(dateTimeStart, dateTimeEnd,transport.getDriver());
        transport.setEndLocalDateTime(LocalDateTime.now());

        transportDAO.updateTransport(transport);

        for(TransportOrder order : transport.getTransportDocument().getOrders()){
            ordersController.setOrderStatus(order, OrderStatus.CLOSE);
        }
        return true;
    }

    public void printAllTransportInProcess(){
        int i = 1;
        for (Transport transport : transportDAO.getAllInTransitTransports()){
            System.out.println("-------------------------"+i + "-------------------------\n" + transport.toString());
            i++;
        }
    }

    public ArrayList<Transport> getAllTransports(){
        return transportDAO.getAllTransports(); // TODO check it!!!
    }

    public boolean checkIfExistTransportInProcess() {
        return (transportDAO.getAllInTransitTransports().size() != 0);
    }

    public ArrayList<Transport> getAllInTransitTransports(){
        return transportDAO.getAllInTransitTransports(); // TODO check it!!!
    }

    public void addTransport(Transport transport){
        transportDAO.addTransport(transport);
    }

    public void updateTransport(Transport transport){
        TransportDocumentDAO documentDAO = TransportDocumentDAO.getInstance();
        documentDAO.updateDocument(transport.getTransportDocument());
        transportDAO.updateTransport(transport);
    }

    public void setTransportID(Transport transport){
        transport.setId(transportDAO.getNewTransportID());
    }

    public boolean updateCurrentTruckWeight(int transportID, double newWeight) {
        Transport transport = getTransportById(transportID);
        if (newWeight > transport.getTruck().getMaxWeight()) {
            transport.weighing(newWeight); // TODO if weight not good do not update!
            transportDAO.updateTransport(transport);
            return false;
        }
        transport.weighing(newWeight);
        transportDAO.updateTransport(transport);
        return true;
    }

}

