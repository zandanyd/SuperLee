package SuperLee.Transport.DataLayer;

import SuperLee.HumenResource.BusinessLayer.Driver;
import SuperLee.HumenResource.DataLayer.DriverDataMapper;
import SuperLee.Transport.BusinessLayer.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

public class TransportDAO { // Singleton class

    private Connection conn;
    private static TransportDAO single_instance = null;
    //    private HashMap<Integer, Transport> allTransports;  // transports not done yet
    private HashMap<Integer, Transport> transportCache;  // transports not done yet




    private TransportDAO() {
        transportCache = new HashMap<>();
    }

    public static TransportDAO getInstance() {
        if (single_instance == null) {
            single_instance = new TransportDAO();
        }
        return single_instance;
    }

    public synchronized int getNewTransportID() {
        int newID = 3;
        try {
            conn = DataSource.openConnection();
            try (PreparedStatement idStatement = conn.prepareStatement("SELECT lastTransportID FROM IDGenerator")) {
                ResultSet idResult = idStatement.executeQuery();
                if (idResult.next()) {
                    int lastID = idResult.getInt("lastTransportID");
                    newID = lastID + 1;
                } else {
                    throw new SQLException("Failed to retrieve lastID from IDGenerator");
                }
            }
            try (PreparedStatement updateStatement = conn.prepareStatement("UPDATE IDGenerator SET lastTransportID = ?")) {
                updateStatement.setInt(1, newID);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return newID;
    }

    public void updateTransport(Transport transport) {
        TransportDocumentDAO transportDocumentDAO = TransportDocumentDAO.getInstance();
        SiteDAO siteDAO = SiteDAO.getInstance();
        TrucksDAO trucksDAO = TrucksDAO.getInstance();


        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Transports SET startLocalDateTime = ?, endLocalDateTime = ?, " +
                            "currentTruckWeight = ?, transportDocument_id = ?, status = ?, sourceAddress = ?, " +
                            "driver_id = ?, truckLicense = ? WHERE transport_id = ?"
            );
            stmt.setString(1, transport.getStartLocalDateTime());
            stmt.setString(2, transport.getEndLocalDateTime());
            stmt.setDouble(3, transport.getCurrentTruckWeight());
            stmt.setInt(4, transport.getTransportDocument().getId());
            stmt.setString(5, transport.getStatus().toString());
            stmt.setString(6, transport.getSource().getAddress());
            stmt.setString(7, transport.getDriver().getID());
            stmt.setInt(8, transport.getTruck().getLicenseNumber());
            stmt.setInt(9, transport.getId());
            stmt.executeUpdate();
//            DataSource.closeConnection();

            // Update the associated objects if needed
            transportDocumentDAO.updateDocument(transport.getTransportDocument());
            trucksDAO.updateTruck(transport.getTruck());

            DataSource.closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ArrayList<Transport> getAllTransports() {
        ArrayList<Transport> allTransports = new ArrayList<>();
        TransportDocumentDAO transportDocumentDAO = TransportDocumentDAO.getInstance();
        SiteDAO siteDAO = SiteDAO.getInstance();
        TrucksDAO trucksDAO = TrucksDAO.getInstance();



        try {
            conn = DataSource.openConnection();
            DriverDataMapper DriverDAO = DriverDataMapper.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transports");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Transport transport = new Transport();
                transport.setId(rs.getInt("transport_id"));
                String datetimeStart = rs.getString("startLocalDateTime");
                LocalDateTime dateTimeStart = LocalDateTime.parse(datetimeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setStartLocalDateTime(dateTimeStart);
                String datetimeEnd = rs.getString("endLocalDateTime");
                LocalDateTime dateTimeEnd = LocalDateTime.parse(datetimeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setEndLocalDateTime(dateTimeEnd);
                transport.setCurrentTruckWeight(rs.getDouble("currentTruckWeight"));

                // Retrieve and set the associated TransportDocument object
                int documentID = rs.getInt("transportDocument_id");
                TransportDocument document = transportDocumentDAO.getTransportDocumentById(documentID);
                transport.setTransportDocument(document);

                // Retrieve and set the associated TransportStatus object
                String statusString = rs.getString("status");
                TransportStatus status = TransportStatus.valueOf(statusString.toUpperCase());
                transport.setStatus(status);

                // Retrieve and set the associated Site object
                String siteAddress = rs.getString("sourceAddress");
                Site source = siteDAO.getSiteByAddress(siteAddress);
                transport.setSource(source);

                // Retrieve and set the associated Driver object
                String driverID = rs.getString("driver_id");
                Driver driver = DriverDAO.getDriver(driverID);
                transport.setDriver(driver);

                // Retrieve and set the associated Truck object
                int truckLicense = rs.getInt("truckLicense");
                Truck truck = trucksDAO.getTruckByLicense(truckLicense);
                transport.setTruck(truck);

                transportCache.put(transport.getId(), transport);
                allTransports.add(transport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        DataSource.closeConnection();
        return allTransports;
    }

    public Transport getTransportById(int transportID) {
        TransportDocumentDAO transportDocumentDAO = TransportDocumentDAO.getInstance();
        SiteDAO siteDAO = SiteDAO.getInstance();
        TrucksDAO trucksDAO = TrucksDAO.getInstance();

        if (transportCache.containsKey(transportID)) {
            return transportCache.get(transportID);
        }
        Transport transport = null;
        try {
            conn = DataSource.openConnection();
            DriverDataMapper DriverDAO = DriverDataMapper.getInstance();
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transports WHERE transport_id = ?");
            stmt.setInt(1, transportID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                transport = new Transport();
                transport.setId(rs.getInt("transport_id"));
                String datetimeStart = rs.getString("startLocalDateTime");
                LocalDateTime dateTimeStart = LocalDateTime.parse(datetimeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setStartLocalDateTime(dateTimeStart);
                String datetimeEnd = rs.getString("endLocalDateTime");
                LocalDateTime dateTimeEnd = LocalDateTime.parse(datetimeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setEndLocalDateTime(dateTimeEnd);
                transport.setCurrentTruckWeight(rs.getDouble("currentTruckWeight"));

                // Retrieve and set the associated TransportDocument object
                int documentID = rs.getInt("transportDocument_id");
                TransportDocument document = transportDocumentDAO.getTransportDocumentById(documentID);
                transport.setTransportDocument(document);

                // Retrieve and set the associated TransportStatus object
                String statusString = rs.getString("status");
                TransportStatus status = TransportStatus.valueOf(statusString.toUpperCase());
                transport.setStatus(status);

                // Retrieve and set the associated Site object
                String siteAddress = rs.getString("sourceAddress");
                Site source = siteDAO.getSiteByAddress(siteAddress);
                transport.setSource(source);

                // Retrieve and set the associated Driver object
                String driverID = rs.getString("driver_id");
                Driver driver = DriverDAO.getDriver(driverID);
                transport.setDriver(driver);

                // Retrieve and set the associated Truck object
                int truckLicense = rs.getInt("truckLicense");
                Truck truck = trucksDAO.getTruckByLicense(truckLicense);
                transport.setTruck(truck);

                transportCache.put(transport.getId(), transport);
            }


        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        DataSource.closeConnection();
        return transport;
    }


    public ArrayList<Transport> getAllInTransitTransports() {
        ArrayList<Transport> inTransitTransports = new ArrayList<>();
        TransportDocumentDAO transportDocumentDAO = TransportDocumentDAO.getInstance();
        SiteDAO siteDAO = SiteDAO.getInstance();
        TrucksDAO trucksDAO = TrucksDAO.getInstance();
        for(Transport transport : transportCache.values()){
            if(transport.getStatus().equals(TransportStatus.IN_TRANSIT)){
                inTransitTransports.add(transport);
            }
        }
        try {
            conn = DataSource.openConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Transports WHERE status='IN_TRANSIT'");
            ResultSet rs = stmt.executeQuery();
            DriverDataMapper DriverDAO = DriverDataMapper.getInstance();
            while (rs.next()) {
                int id = rs.getInt("transport_id");
                if(transportCache.containsKey(id)){
                    continue;
                }
                Transport transport = new Transport();
                transport.setId(id);
                String datetimeStart = rs.getString("startLocalDateTime");
                LocalDateTime dateTimeStart = LocalDateTime.parse(datetimeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setStartLocalDateTime(dateTimeStart);
                String datetimeEnd = rs.getString("endLocalDateTime");
                LocalDateTime dateTimeEnd = LocalDateTime.parse(datetimeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                transport.setEndLocalDateTime(dateTimeEnd);
                transport.setCurrentTruckWeight(rs.getDouble("currentTruckWeight"));

                // Retrieve and set the associated TransportDocument object
                int documentID = rs.getInt("transportDocument_id");
                TransportDocument document = transportDocumentDAO.getTransportDocumentById(documentID);
                transport.setTransportDocument(document);

                // Retrieve and set the associated TransportStatus object
                String statusString = rs.getString("status");
                TransportStatus status = TransportStatus.valueOf(statusString.toUpperCase());
                transport.setStatus(status);

                // Retrieve and set the associated Site object
                String siteAddress = rs.getString("sourceAddress");
                Site source = siteDAO.getSiteByAddress(siteAddress);
                transport.setSource(source);

                // Retrieve and set the associated Driver object
                String driverID = rs.getString("driver_id");
                Driver driver = DriverDAO.getDriver(driverID);
                transport.setDriver(driver);

                // Retrieve and set the associated Truck object
                int truckLicense = rs.getInt("truckLicense");
                Truck truck = trucksDAO.getTruckByLicense(truckLicense);
                transport.setTruck(truck);

                transportCache.put(transport.getId(), transport);
                inTransitTransports.add(transport);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        DataSource.closeConnection();
        return inTransitTransports;
    }




    public void addTransport(Transport transport) {
        TransportDocumentDAO transportDocumentDAO = TransportDocumentDAO.getInstance();
        SiteDAO siteDAO = SiteDAO.getInstance();
        TrucksDAO trucksDAO = TrucksDAO.getInstance();

        try {
            conn = DataSource.openConnection();

            // Insert the transport record into the Transports table
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Transports (transport_id, startLocalDateTime, " +
                            "currentTruckWeight, transportDocument_id, status, sourceAddress, driver_id, truckLicense) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            stmt.setInt(1, transport.getId());
            stmt.setString(2, transport.getStartLocalDateTime());
            stmt.setDouble(3, transport.getCurrentTruckWeight());
            stmt.setInt(4, transport.getTransportDocument().getId());
            stmt.setString(5, transport.getStatus().toString());
            stmt.setString(6, transport.getSource().getAddress());
            stmt.setString(7, transport.getDriver().getID());
            stmt.setInt(8, transport.getTruck().getLicenseNumber());
            stmt.executeUpdate();

            // Update the associated objects if needed
            transportDocumentDAO.updateDocument(transport.getTransportDocument());
            siteDAO.updateSite(transport.getSource());
            trucksDAO.updateTruck(transport.getTruck());

            // Add the transport to the cache
            transportCache.put(transport.getId(), transport);

            DataSource.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }


}
