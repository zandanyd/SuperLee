package SuperLee.Transport.DataLayer;
import SuperLee.Transport.BusinessLayer.ShippingArea;
import SuperLee.Transport.BusinessLayer.Site;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;


public class SiteDAO {
    private Connection conn;
    private static SiteDAO instance = null;
    private HashMap<String, Site> sitesCache;

    private SiteDAO() {
        sitesCache = new HashMap<>();
    }

    public static SiteDAO getInstance() {
        if (instance == null) {
            instance = new SiteDAO();
        }
        return instance;
    }

    public boolean checkSiteExist(String address) {
        if (sitesCache.containsKey(address))
            return true;
        return false;
    }

    // TODO check done!
    public Site getSiteByAddress(String address) {
        if (sitesCache.containsKey(address)) {
            return sitesCache.get(address);
        }

        try {
            conn = DataSource.openConnection();

            String query = "SELECT phoneNumber, contactName, shippingArea FROM Sites WHERE address=?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, address);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String phoneNumber = rs.getString("phoneNumber");
                    String contactName = rs.getString("contactName");
                    String shippingAreaString = rs.getString("shippingArea");
                    ShippingArea shippingArea = ShippingArea.valueOf(shippingAreaString.toUpperCase());
                    Site newSite = new Site(address, phoneNumber, contactName, shippingArea);
                    sitesCache.put(address, newSite);
                    DataSource.closeConnection();
                    return newSite;
                }
            }
        }catch (SQLException e) {
                e.printStackTrace();
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
        }
        DataSource.closeConnection();
        return null;
    }


    //TODO CHECK DONE!
    public ArrayList<Site> getSitesByArea(ShippingArea area) {
        ArrayList<Site> sites = new ArrayList<>();
        for (Site site : sitesCache.values()) {
            if (site.getShippingArea().equals(area)) {
                sites.add(site);
            }
        }
        try {
            conn = DataSource.openConnection();

            try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Sites WHERE shippingArea=?")) {
                stmt.setString(1, area.toString());
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String address = rs.getString("address");
                    if (!sitesCache.containsKey(address)) {
                        String phoneNumber = rs.getString("phoneNumber");
                        String contactName = rs.getString("contactName");
                        Site newSite = new Site(address, phoneNumber, contactName, area);

                        sites.add(newSite);
                        sitesCache.put(address, newSite);
                    }
                }
                DataSource.closeConnection();
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
        return sites;
    }

    ///---------------
    // TODO not need!
    public void updateSite(Site site) {
        try {
            conn = DataSource.openConnection();

            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Sites SET phoneNumber=?, contactName=?, shippingArea=? WHERE address=?"
            );
            stmt.setString(1, site.getPhoneNumber());
            stmt.setString(2, site.getContactName());
            stmt.setString(3, site.getShippingArea().toString());
            stmt.setString(4, site.getAddress());
            stmt.executeUpdate();

            // Update the corresponding record in the Destinations_And_Documents table
            PreparedStatement destStmt = conn.prepareStatement(
                    "UPDATE Destinations_And_Documents SET address = ?"
            );
            destStmt.setString(1, site.getAddress());
            destStmt.executeUpdate();
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

    // TODO check Done!
    public void addSite(Site site) {
        try {
            conn = DataSource.openConnection();

            // Insert the site record into the Sites table
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO Sites (address, phoneNumber, contactName, shippingArea) VALUES (?, ?, ?, ?)"
            );
            stmt.setString(1, site.getAddress());
            stmt.setString(2, site.getPhoneNumber());
            stmt.setString(3, site.getContactName());
            stmt.setString(4, site.getShippingArea().toString());
            stmt.executeUpdate();

            // Insert the site record into the Destinations_And_Documents table
            PreparedStatement destStmt = conn.prepareStatement(
                    "INSERT INTO Destinations_And_Documents (address) VALUES (?)"
            );
            destStmt.setString(1, site.getAddress());
            destStmt.executeUpdate();

            sitesCache.put(site.getAddress(), site);
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

    public ArrayList<Site> getAllSites() {
        ArrayList<Site> sites = new ArrayList<>();
        try {
            conn = DataSource.openConnection();

            String query = "SELECT address, phoneNumber, contactName, shippingArea FROM Sites";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    String address = rs.getString("address");
                    String phoneNumber = rs.getString("phoneNumber");
                    String contactName = rs.getString("contactName");
                    String shippingAreaString = rs.getString("shippingArea");
                    ShippingArea shippingArea = ShippingArea.valueOf(shippingAreaString.toUpperCase());
                    Site site = new Site(address, phoneNumber, contactName, shippingArea);
                    sites.add(site);
                    sitesCache.put(address, site);
                }
                DataSource.closeConnection();
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
        return sites;
    }


    public HashMap<String, String> getArrivalTimes(String sourceAddress, ArrayList<String> destinations, String departureTimeStr) {
        String inputFormat = "yyyy-MM-dd HH:mm:ss";
        String outputFormat = "HH:mm";

        // Parse the input string into a LocalDateTime object
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputFormat);
        LocalDateTime dateTime = LocalDateTime.parse(departureTimeStr, inputFormatter);

        // Format the LocalDateTime object to the desired format
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);
        String outputString = dateTime.format(outputFormatter);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        HashMap<String, String> arrivalTimes = new HashMap<>();
        try {
            conn = DataSource.openConnection();
            LocalTime departureTime = LocalTime.parse(outputString, timeFormatter);
            double distance = 0;
            for (int i = 0; i < destinations.size(); i++) {
                String destination = destinations.get(i);
                if (i == 0) {
                    distance = getDistance(conn, sourceAddress, destination);
                } else {
                    String prevDestination = destinations.get(i-1);
                    distance = getDistance(conn, prevDestination, destination);
                }
                double travelTime = distance / 50; // assume average speed of 50 miles per hour
                double arrivalTimeDouble = departureTime.toSecondOfDay() + (int) (travelTime * 3600);
                LocalTime arrivalTime = LocalTime.ofSecondOfDay((long) arrivalTimeDouble);
                arrivalTimes.put(destination, arrivalTime.format(timeFormatter));
                departureTime = arrivalTime;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        DataSource.closeConnection();
        return arrivalTimes;
    }

    private double getDistance(Connection conn, String location1, String location2) throws SQLException  {
        String sql = "SELECT distance FROM distance_table WHERE location1 = ? AND location2 = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, location1);
            stmt.setString(2, location2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("distance");
                } else {
                    throw new SQLException("Distance not found for " + location1 + " to " + location2);
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }


}