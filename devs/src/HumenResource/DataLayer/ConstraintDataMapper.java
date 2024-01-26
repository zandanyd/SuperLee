package SuperLee.HumenResource.DataLayer;

import SuperLee.HumenResource.BusinessLayer.Constraints;
import SuperLee.HumenResource.BusinessLayer.ShiftType;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ConstraintDataMapper{
    private static ConstraintDataMapper instance = null;
    private static Map<String, Constraints> cache = new HashMap<>();

    private ConstraintDataMapper()  {
    }
    public static synchronized ConstraintDataMapper getInstance() {
        if(instance == null){
            instance =  new ConstraintDataMapper();
        }
        return instance;
    }
    public void insert( Constraints constraint) throws SQLException {
        Connection conn = DataBase.connect();;
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO ConstraintManagement (Date, ShiftType, WorkerID) VALUES (?, ?, ?)");
        insertStatement.setString(1, constraint.getDate().toString());
        insertStatement.setString(2, constraint.getShiftType().toString());
        insertStatement.setString(3, constraint.getWorkerID());
        insertStatement.executeUpdate();
        cache.put( constraint.getDate().toString() + constraint.getShiftType().toString() + constraint.getWorkerID(), constraint);
        conn.close();;
    }

    public Constraints get(String id, LocalDate localDate, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();;
        String key= localDate.toString() + shiftType.toString() + id;
        if(cache.containsKey(key)){
            conn.close();;
            return cache.get(key);
        }
        Constraints constraints = new Constraints(localDate, shiftType, id);
        cache.put(key,constraints);
        conn.close();;
        return constraints;
    }

    public boolean isInDataBase(String id, LocalDate localDate, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        boolean idExists = false;

        try {
            String query = "SELECT * FROM ConstraintManagement WHERE Date = ?  And ShiftType = ? And WorkerID = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1,localDate.toString());
            statement.setString(2, shiftType.toString());
            statement.setString(3, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                idExists = true;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            conn.close();;
            return idExists;
        }
    }
    public void deleteConstraint(String WorkerID, LocalDate date, ShiftType type) throws SQLException {
        Connection conn = DataBase.connect();;
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM ConstraintManagement " +
                        "WHERE Date = ? AND ShiftType = ? AND WorkerID = ?")) {
            statement.setString(1, date.toString());
            statement.setString(2, type.toString());
            statement.setString(3, WorkerID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String mapKey = date.toString() + type + WorkerID;
        cache.remove(mapKey);
        conn.close();;
    }
}
