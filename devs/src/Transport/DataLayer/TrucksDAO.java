package SuperLee.Transport.DataLayer;
import SuperLee.Transport.BusinessLayer.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class TrucksDAO { // Singleton class
    private Connection conn;
    private static  TrucksDAO single_instance = null;
    private HashMap<Integer, Truck> truckCache;

    private TrucksDAO(){
        truckCache = new HashMap<>();
    }

    public static synchronized TrucksDAO getInstance() {
        if(single_instance == null){
            single_instance = new TrucksDAO();
        }
        return single_instance;
    }

    public Truck getTruckByLicense(int licenseTruck)  {
        try {
            conn = DataSource.openConnection();

            Truck truck = truckCache.get(licenseTruck);
            if (truck == null) {
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Trucks WHERE licenseNumber = ?");
                stmt.setInt(1, licenseTruck);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    String type = rs.getString("type_truck"); // TYPE_TRUCK = DRY/COOL/FROZEN
                    if (type.equals("COOL"))
                        truck = new CoolTruck(rs.getInt("licenseNumber"), rs.getString("model"), rs.getDouble("netWeight"), rs.getDouble("maxWeight"));
                    if (type.equals("DRY"))
                        truck = new DryTruck(rs.getInt("licenseNumber"), rs.getString("model"), rs.getDouble("netWeight"), rs.getDouble("maxWeight"));
                    if (type.equals("FROZEN"))
                        truck = new FrozenTruck(rs.getInt("licenseNumber"), rs.getString("model"), rs.getDouble("netWeight"), rs.getDouble("maxWeight"));
                    String[] unavailableDates = rs.getString("unavailableDates")
                            .substring(1, rs.getString("unavailableDates").length() - 1)
                            .split(", ");
                    ArrayList<LocalDate> unavailableDatesList = new ArrayList<>();
                    for (String date : unavailableDates) {
                        date.replace("[", "").replace("]", "");

                        LocalDate newDate = LocalDate.parse(date.replace("[", "").replace("]", ""));
                        unavailableDatesList.add(newDate);
                    }
                    truck.setUnAvailableDates(unavailableDatesList);
                    truckCache.put(truck.getLicenseNumber(), truck); // Add truck to the Identity Map
                }
            }
            DataSource.closeConnection();
            return truck;
        }
        catch (SQLException e) {
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

    public void addTruck(TrainingType type, Truck truck)  {
        String truckType = "DRY";
        if(type.equals(TrainingType.REFRIGERATED)){
            truckType = "COOL";
        }
        if(type.equals(TrainingType.FROZEN)){
            truckType = "FROZEN";
        }
        try{
            conn = DataSource.openConnection();
            String sql = "INSERT INTO Trucks (licenseNumber, model, netWeight, maxWeight, type_truck, unAvailableDates) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, truck.getLicenseNumber());
            stmt.setString(2, truck.getModel());
            stmt.setDouble(3, truck.getNetWeight());
            stmt.setDouble(4, truck.getMaxWeight());
            stmt.setString(5, truckType); // Assuming TrainingType can be converted to String
            stmt.setString(6, String.valueOf(truck.getUnAvailableDates()));
            stmt.executeUpdate();
            truckCache.put(truck.getLicenseNumber(), truck); // Add truck to the Identity Map
            DataSource.closeConnection();
        }
        catch (SQLException e){
            e.printStackTrace();
            try {
                conn.rollback();
            }catch(SQLException ex){
                ex.printStackTrace();
            }
        }
    }

    public void updateTruck(Truck truck) {
        try {
            conn = DataSource.openConnection();
            PreparedStatement stmt = conn.prepareStatement("UPDATE Trucks SET model = ?, netWeight = ?, maxWeight = ?, unavailableDates = ? WHERE licenseNumber = ?");

            stmt.setString(1, truck.getModel());
            stmt.setDouble(2, truck.getNetWeight());
            stmt.setDouble(3, truck.getMaxWeight());

            // Convert the list of unavailable dates to a string representation
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (LocalDate date : truck.getUnAvailableDates()) {
                sb.append(date.toString()).append(", ");
            }
            sb.setLength(sb.length() - 2);
            sb.append("]");
            String unavailableDates = sb.toString();

            stmt.setString(4, unavailableDates);
            stmt.setInt(5, truck.getLicenseNumber());

            stmt.executeUpdate();
            truckCache.put(truck.getLicenseNumber(), truck);
            DataSource.closeConnection();
            return;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        DataSource.closeConnection();
        }
    }

public ArrayList<Truck> getAvailableTrucksByType(TrainingType truckType, LocalDate currentDate) {
    String type = "DRY";
    if (truckType.equals(TrainingType.REFRIGERATED)) {
        type = "COOL";
    } else if (truckType.equals(TrainingType.FROZEN)) {
        type = "FROZEN";
    }

    ArrayList<Truck> availableTrucks = new ArrayList<>();

    try (Connection conn = DataSource.openConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Trucks WHERE type_truck = ? AND (unavailableDates IS NULL OR unavailableDates NOT LIKE ?)")) {

        stmt.setString(1, type);
        stmt.setString(2, "%[" + currentDate.toString() + "]%");
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int licenseNumber = rs.getInt("licenseNumber");
            String model = rs.getString("model");
            double netWeight = rs.getDouble("netWeight");
            double maxWeight = rs.getDouble("maxWeight");
            String[] unavailableDates = rs.getString("unavailableDates")
                    .substring(1, rs.getString("unavailableDates").length() - 1)
                    .split(", ");
            ArrayList<LocalDate> unavailableDatesList = new ArrayList<>();
            for (String date : unavailableDates) {
                LocalDate newDate = LocalDate.parse(date.replace("[", "").replace("]", ""));
                unavailableDatesList.add(newDate);
            }

            Truck truck;
            if (type.equals("COOL")) {
                truck = new CoolTruck(licenseNumber, model, netWeight, maxWeight);
            } else if (type.equals("FROZEN")) {
                truck = new FrozenTruck(licenseNumber, model, netWeight, maxWeight);
            } else {
                truck = new DryTruck(licenseNumber, model, netWeight, maxWeight);
            }
            if (truck != null) {
                truck.setUnAvailableDates(unavailableDatesList);
                if (!truck.getUnAvailableDates().contains(currentDate)) {
                    availableTrucks.add(truck);
                    truckCache.put(licenseNumber, truck);
                }
            }

        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return availableTrucks;
}


    public boolean checkIfExistTrucks(){
        if(truckCache.size() != 0)
            return true;
        try {
            conn = DataSource.openConnection();

            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Trucks");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
            DataSource.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        DataSource.closeConnection();
        return false;
    }

    public void deleteTruck(Truck truck) {
        try {
            conn = DataSource.openConnection();

            // Delete the truck from the Trucks table
            PreparedStatement deleteTruckStmt = conn.prepareStatement("DELETE FROM Trucks WHERE licenseNumber = ?");
            deleteTruckStmt.setInt(1, truck.getLicenseNumber());

            // Execute the delete statement
            int rowsAffected = deleteTruckStmt.executeUpdate();
            if (rowsAffected > 0) {
                // Remove the truck from the cache
                truckCache.remove(truck.getLicenseNumber());
                System.out.println("Truck deleted successfully.");
            } else {
                System.out.println("Truck not found.");
            }

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

