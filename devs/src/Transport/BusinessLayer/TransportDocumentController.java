package SuperLee.Transport.BusinessLayer;

import java.util.ArrayList;

public class TransportDocumentController { // Singleton class

    private TransportDocumentDAO transportDocumentDAO;

    private static TransportDocumentController instance;

    private TransportDocumentController()  {
        transportDocumentDAO = TransportDocumentDAO.getInstance();
    }

    public static TransportDocumentController getInstance()  {
        if (instance == null) {
            instance = new TransportDocumentController();
        }
        return instance;
    }

    public Integer addTransportDocument(int truckLicense, String driverID, ArrayList<TransportOrder> orders, Site source, ArrayList<Site> sites, TrainingType type, String startTime){
        TransportDocument transportDocument = new TransportDocument(source, driverID, truckLicense);
        for (TransportOrder transportOrder : orders){
            transportDocument.addOrder(transportOrder);
            OrdersController.getInstance().setOrderStatus(transportOrder, OrderStatus.SELECTED_FOR_TRANSPORT);
        }
        for(Site site : sites){ // TODO check if site has not already exist
            transportDocument.addDestination(site);
        }
        transportDocument.setTrainingRequired(type);
        int newID = transportDocumentDAO.getNewDocumentID();
        transportDocument.setID(newID);
        transportDocument.setStartTransportTime(startTime);
        transportDocumentDAO.addTransportDocument(transportDocument);
        return transportDocument.getId();
    }

    public void printTransportDoc(int docID){
        TransportDocument transportDocument = getDocument(docID);
        System.out.println(transportDocument.toString());
        System.out.println("Transport destination arrival times: ");
        ArrayList<String> destAddress = new ArrayList<>();
        for(Site site : transportDocument.getDestinationList()){
            destAddress.add(site.getAddress());
        }
        SiteController.getInstance().displayArrivalTimes(transportDocument.getSource().getAddress(), destAddress, transportDocument.getStartTransportTime());
    }

    public void printAllPendingTransportDoc(){
        ArrayList<TransportDocument> allDocuments = transportDocumentDAO.getAllPendingDocuments();
        for (TransportDocument transportDocument : allDocuments){
                System.out.println(transportDocument.toString());
        }

    }

    public void deleteDocument(int id){
        transportDocumentDAO.deleteDocument(id);
    }
    public boolean checkIfExistPendingDocument(){
        return transportDocumentDAO.checkIfExistPendingDocument();
    }

    public TransportDocument getDocument(int documentID){
        return transportDocumentDAO.getTransportDocumentById(documentID);
    }

    public void setDocumentStatus(TransportDocument transportDocument, OrderStatus status){
        transportDocument.setStatus(status);
        transportDocumentDAO.updateDocument(transportDocument);
    }

    // new func to GUI
    public ArrayList<TransportDocument> getAllPendingDocuments(){
        return transportDocumentDAO.getAllPendingDocuments();
    }

}
