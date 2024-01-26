package SuperLee.Transport.BusinessLayer;
import SuperLee.Transport.DataLayer.DataSource;
import SuperLee.Transport.DataLayer.OrdersDAO;
import SuperLee.Transport.DataLayer.SiteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportDocumentDAO { // Singleton

    private Connection conn;
    private static  TransportDocumentDAO single_instance = null;
    private HashMap<Integer, TransportDocument> DocumentCache;

    private TransportDocumentDAO(){
        DocumentCache= new HashMap<>();
    }

    public static synchronized TransportDocumentDAO getInstance(){
        if(single_instance == null){
            single_instance = new TransportDocumentDAO();
        }
        return single_instance;
    }

    // new id number for the transport document
    public synchronized int getNewDocumentID() {
        int newID = 2;
        try {
            conn = DataSource.openConnection();
            try (PreparedStatement idStatement = conn.prepareStatement("SELECT lastDocumentID FROM IDGenerator")) {
                ResultSet idResult = idStatement.executeQuery();
                if (idResult.next()) {
                    int lastID = idResult.getInt("lastDocumentID");
                    newID = lastID + 1;
                } else {
                    throw new SQLException("Failed to retrieve lastID from IDGenerator");
                }
            }
            try (PreparedStatement updateStatement = conn.prepareStatement("UPDATE IDGenerator SET lastDocumentID = ?")) {
                updateStatement.setInt(1, newID);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return newID;
    }

    public TransportDocument getTransportDocumentById(int transportID)  {
        if (DocumentCache.containsKey(transportID)) {
            return DocumentCache.get(transportID);
        }
        try {
            conn = DataSource.openConnection();
            // Prepare SQL statement to select the document by ID
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM TransportDocuments WHERE documentID = ?"
            );
            stmt.setInt(1, transportID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create a new TransportDocument object and set its properties based on the result set
                TransportDocument document = new TransportDocument();
                document.setID(rs.getInt("documentID"));
                document.setDriverID(rs.getString("driverID"));
                document.setTruckLicense(rs.getInt("truckLicense"));
                document.setStatus(OrderStatus.valueOf(rs.getString("status")));
                document.setCurrentTruckWeight(rs.getDouble("current_truck_weight"));
                document.setTrainingRequired(TrainingType.valueOf(rs.getString("trainingRequired")));
                document.setStartTransportTime(rs.getString("startTransportTime"));


                // set the Source to document
                Site source = SiteDAO.getInstance().getSiteByAddress(rs.getString("sourceAddress"));
                document.setSource(source);
                rs.close();
                stmt.close();
                // Retrieve the orders for this transport document
                PreparedStatement orderStmt = conn.prepareStatement(
                        "SELECT * FROM TransportOrders " +
                                "JOIN Orders_And_Documents ON TransportOrders.orderNumber = Orders_And_Documents.orderNumber " +
                                "WHERE Orders_And_Documents.documentID = ? AND TransportOrders.trainingType = ?");

                orderStmt.setInt(1, transportID);
                orderStmt.setString(2, document.getTrainingRequired().toString());
                ResultSet orderRs = orderStmt.executeQuery();
                ArrayList<TransportOrder> orders = new ArrayList<>();
                while (orderRs.next()) {
                    TransportOrder order = OrdersDAO.getInstance().getTransportOrderByIDAndTrainingType(orderRs.getInt("orderNumber"),document.getTrainingRequired());
                    orders.add(order);
                }
                document.setOrders(orders);
                orderRs.close();

                // Retrieve the destinations for this transport document
                PreparedStatement destStmt = conn.prepareStatement(
                        "SELECT * FROM Sites WHERE address IN " +
                                "(SELECT address FROM Destinations_And_Documents WHERE documentID = ?)"
                );
                destStmt.setInt(1, transportID);
                ResultSet destRs = destStmt.executeQuery();
                ArrayList<Site> destinations = new ArrayList<>();
                while (destRs.next()) {
                    // Create a new Site object and set its properties based on the result set
                    Site destination = SiteDAO.getInstance().getSiteByAddress(destRs.getString("address"));
                    destination.setContactName(destRs.getString("contactName"));
                    destinations.add(destination);
                }
                document.setDestinationList(destinations);
                destRs.close();

                // Add the document to the cache and return it
                DocumentCache.put(document.getId(), document);
                DataSource.closeConnection();
                return document;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
        DataSource.closeConnection();
        return null;
    }

    // TODO set arrivalTime field!
    public void addTransportDocument(TransportDocument document){
        try {
            if (DocumentCache.containsKey(document.getId()))
                return;
            conn = DataSource.openConnection();
            try {

                // Insert the document into the TransportDocuments table
                PreparedStatement documentStatement = conn.prepareStatement(
                        "INSERT INTO TransportDocuments (documentID, driverID, truckLicense, status, current_truck_weight, trainingRequired, sourceAddress, startTransportTime) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                documentStatement.setInt(1, document.getId());
                documentStatement.setString(2, document.getDriverID());
                documentStatement.setInt(3, document.getTruckLicense());
                documentStatement.setString(4, document.getStatus().toString());
                documentStatement.setDouble(5, document.getCurrentTruckWeight());
                documentStatement.setString(6, document.getTrainingRequired().toString());
                documentStatement.setString(7, document.getSource().getAddress());
                documentStatement.setString(8, document.getStartTransportTime());
                documentStatement.executeUpdate();
                documentStatement.close();

                // Insert the document's destinations into the Destinations_And_Documents table
                PreparedStatement destinationStatement = conn.prepareStatement(
                        "INSERT INTO Destinations_And_Documents (address, documentID) " +
                                "VALUES (?, ?)");
                for (Site destination : document.getDestinationList()) {
                    destinationStatement.setString(1, destination.getAddress());
                    destinationStatement.setInt(2, document.getId());
                    destinationStatement.executeUpdate();
                }
                destinationStatement.close();



                // Insert the document's orders into the Orders_And_Documents table
                PreparedStatement orderStatement = conn.prepareStatement(
                        "INSERT INTO Orders_And_Documents (documentID, orderNumber, trainingType) " +
                                "VALUES (?, ?, ?)");
                for (TransportOrder order : document.getOrders()) {
                    orderStatement.setInt(1, document.getId());
                    orderStatement.setInt(2, order.getOrderNumber());
                    orderStatement.setString(3, order.getTrainingRequired().toString());
                    orderStatement.executeUpdate();
                }
                orderStatement.close();



                // Commit the transaction if all statements executed successfully

                // Add the document to the cache
                DocumentCache.put(document.getId(), document);
                DataSource.closeConnection();
            } catch (SQLException e) {
                // Roll back the transaction if any statement failed
                conn.rollback();
                e.printStackTrace();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    public ArrayList<TransportDocument> getAllPendingDocuments() {
        ArrayList<TransportDocument> pendingDocuments = new ArrayList<>();

        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM TransportDocuments WHERE status = ?"
            );
            stmt.setString(1, OrderStatus.PENDING.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TransportDocument document = new TransportDocument();
                document.setID(rs.getInt("documentID"));
                document.setDriverID(rs.getString("driverID"));
                document.setTruckLicense(rs.getInt("truckLicense"));
                document.setStatus(OrderStatus.valueOf(rs.getString("status")));
                document.setCurrentTruckWeight(rs.getDouble("current_truck_weight"));
                document.setTrainingRequired(TrainingType.valueOf(rs.getString("trainingRequired")));
                document.setStartTransportTime(rs.getString("startTransportTime"));

                // Set the source for the transport document
                Site source = SiteDAO.getInstance().getSiteByAddress(rs.getString("sourceAddress"));
                document.setSource(source);
                // Retrieve the orders for this transport document
                PreparedStatement orderStmt = conn.prepareStatement(
                        "SELECT * FROM TransportOrders " +
                                "JOIN Orders_And_Documents " +
                                "ON TransportOrders.orderNumber = Orders_And_Documents.orderNumber " +
                                "AND TransportOrders.trainingType = Orders_And_Documents.trainingType " +
                                "WHERE Orders_And_Documents.documentID = ? " +
                                "AND TransportOrders.trainingType = ?");

                orderStmt.setInt(1, document.getId());
                orderStmt.setString(2, document.getTrainingRequired().toString());
                ResultSet orderRs = orderStmt.executeQuery();
                ArrayList<TransportOrder> orders = new ArrayList<>();
                while (orderRs.next()) {
                    TransportOrder order = OrdersDAO.getInstance().getTransportOrderByIDAndTrainingType(orderRs.getInt("orderNumber"), document.getTrainingRequired());
                    orders.add(order);
                }
                orderRs.close();
                orderStmt.close();
                document.setOrders(orders);

                // Retrieve the destinations for this transport document
                PreparedStatement destStmt = conn.prepareStatement(
                        "SELECT * FROM Sites WHERE address IN " +
                                "(SELECT address FROM Destinations_And_Documents WHERE documentID = ?)"
                );
                destStmt.setInt(1, document.getId());
                ResultSet destRs = destStmt.executeQuery();
                ArrayList<Site> destinations = new ArrayList<>();
                while (destRs.next()) {
                    Site destination = SiteDAO.getInstance().getSiteByAddress(destRs.getString("address"));
                    destination.setContactName(destRs.getString("contactName"));
                    destinations.add(destination);
                }
                destRs.close();
                destStmt.close();
                document.setDestinationList(destinations);
                pendingDocuments.add(document);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return pendingDocuments;
    }

    // TODO set arrivalTime field!
    public boolean checkIfExistPendingDocument() {
        for (TransportDocument document : DocumentCache.values()) {
            if (document.getStatus() == OrderStatus.PENDING) {
                return true;
            }
        }

        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT COUNT(*) FROM TransportDocuments WHERE status = ?"
            );
            stmt.setString(1, OrderStatus.PENDING.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                DataSource.closeConnection();
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return false;
    }

    public void updateDocument(TransportDocument transportDocument){
        try {
            conn = DataSource.openConnection();

            // update the TransportDocuments table
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE TransportDocuments " +
                            "SET driverID = ?, truckLicense = ?, status = ?, current_truck_weight = ?, trainingRequired = ?, sourceAddress = ?, startTransportTime = ? " +
                            "WHERE documentID = ?"
            );
            stmt.setString(1, transportDocument.getDriverID());
            stmt.setInt(2, transportDocument.getTruckLicense());
            stmt.setString(3, transportDocument.getStatus().toString());
            stmt.setDouble(4, transportDocument.getCurrentTruckWeight());
            stmt.setString(5, transportDocument.getTrainingRequired().toString());
            stmt.setString(6, transportDocument.getSource().getAddress());
            stmt.setString(7, transportDocument.getStartTransportTime());
            stmt.setInt(8, transportDocument.getId());
            stmt.executeUpdate();
            stmt.close();

            // Update Destinations_And_Documents table
            PreparedStatement deleteDestStmt = conn.prepareStatement(
                    "DELETE FROM Destinations_And_Documents WHERE documentID = ?"
            );
            deleteDestStmt.setInt(1, transportDocument.getId());
            deleteDestStmt.executeUpdate();
            deleteDestStmt.close();
            PreparedStatement insertDestStmt = conn.prepareStatement(
                    "INSERT INTO Destinations_And_Documents (address, documentID) VALUES (?, ?)"
            );
            for (Site destination : transportDocument.getDestinationList()) {
                insertDestStmt.setString(1, destination.getAddress());
                insertDestStmt.setInt(2, transportDocument.getId());
                insertDestStmt.executeUpdate();
            }
            insertDestStmt.close();

            // Update Orders_And_Documents table
            PreparedStatement deleteOrdersStmt = conn.prepareStatement(
                    "DELETE FROM Orders_And_Documents WHERE documentID = ?"
            );
            deleteOrdersStmt.setInt(1, transportDocument.getId());
            deleteOrdersStmt.executeUpdate();
            deleteOrdersStmt.close();
            PreparedStatement insertOrdersStmt = conn.prepareStatement(
                    "INSERT INTO Orders_And_Documents (documentID, orderNumber, trainingType) VALUES (?, ?, ?)"
            );
            for (TransportOrder order : transportDocument.getOrders()) {
                insertOrdersStmt.setInt(1, transportDocument.getId());
                insertOrdersStmt.setInt(2, order.getOrderNumber());
                insertOrdersStmt.setString(3, order.getTrainingRequired().toString());
                insertOrdersStmt.executeUpdate();
            }
            insertOrdersStmt.close();

            DataSource.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            try{
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }//


    public void deleteDocument(int ID){
        conn = DataSource.openConnection();


        try{
            String deleteQuery = "DELETE FROM TransportDocuments WHERE documentID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, ID);
            preparedStatement.executeUpdate();
            String deleteQuery2 = "DELETE FROM Destinations_And_Documents WHERE documentID = ?";
            PreparedStatement preparedStatement2 = conn.prepareStatement(deleteQuery2);
            preparedStatement2.setInt(1, ID);
            preparedStatement2.executeUpdate();
            String deleteQuery3 = "DELETE FROM Orders_And_Documents WHERE documentID = ?";
            PreparedStatement preparedStatement3 = conn.prepareStatement(deleteQuery3);
            preparedStatement3.setInt(1, ID);
            preparedStatement3.executeUpdate();
            conn.close();
        }
        catch (SQLException e){
            try {
                conn.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }

    }
}





