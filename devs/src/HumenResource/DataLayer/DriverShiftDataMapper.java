package SuperLee.HumenResource.DataLayer;
import SuperLee.HumenResource.BusinessLayer.GenericShift;
import SuperLee.HumenResource.BusinessLayer.ShiftType;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

public class DriverShiftDataMapper{
    private static DriverShiftDataMapper instance = null;
    private static Map<String, GenericShift> cache = new HashMap<>();
    private DriverShiftDataMapper()  {
    }
    public static synchronized DriverShiftDataMapper getInstance()  {
        if(instance == null){
            instance =  new DriverShiftDataMapper();
        }
        return instance;
    }
    public GenericShift get(LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        String key= date.toString() + shiftType.toString();
        if(cache.containsKey(key)){
            conn.close();;
            return cache.get(key);
        }
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT StartHour, EndHour FROM DriversShift " +
                        "WHERE Date = ? AND ShiftType = ? ")) {
            statement.setString(1, date.toString());
            statement.setString(2, shiftType.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String startHour = resultSet.getString("StartHour");
                    String[] arrOfStr = startHour.split(":", 2);
                    LocalTime start = LocalTime.of(Integer.parseInt(arrOfStr[0]),Integer.parseInt(arrOfStr[1]));
                    String endHour = resultSet.getString("EndHour");
                    String[] arrOfEnd = endHour.split(":", 2);
                    LocalTime end = LocalTime.of(Integer.parseInt(arrOfEnd[0]),Integer.parseInt(arrOfEnd[1]));
                    GenericShift shift= new GenericShift(date, shiftType);
                    shift.setStartHour(start);
                    shift.setFinishHour(end);
                    String key1 = date.toString() + shiftType;
                    cache.put(key1, shift);
                    conn.close();;
                    return shift;
                } else {
                    conn.close();;
                    return null;
                }
            }
        }
    }

    public void insert(String driversId, GenericShift shift) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO DriversShift (Date, ShiftType, StartHour, EndHour) VALUES (?, ?, ?, ?)");
        insertStatement.setString(1, shift.getDate().toString());
        insertStatement.setString(2, shift.getType().toString());
        insertStatement.setString(3, shift.getStartHour().toString());
        insertStatement.setString(4, shift.getFinishHour().toString());
        insertStatement.executeUpdate();
        cache.put( shift.getDate().toString()+shift.getType().toString(), shift);
        insertStatement = conn.prepareStatement("INSERT INTO DriverInShift (Date, ShiftType, DriverID) VALUES (?, ?, ?)");
        insertStatement.setString(1, shift.getDate().toString());
        insertStatement.setString(2, shift.getType().toString());
        insertStatement.setString(3, driversId);
        insertStatement.executeUpdate();
        conn.close();;
    }
    public void deleteDriverFromShift(String DriverID, LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM DriverInShift " +
                        "WHERE Date = ? AND ShiftType = ? AND DriverID = ? ");
        statement.setString(1, date.toString());
        statement.setString(2, shiftType.toString());
        statement.setString(3, DriverID);
        statement.executeUpdate();
        conn.close();;
    }
    public void insertDriverToShift(String DriverID, LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO DriverInShift (Date, ShiftType, DriverID) VALUES (?, ?, ?)");
        insertStatement.setString(1, date.toString());
        insertStatement.setString(2, shiftType.toString());
        insertStatement.setString(3, DriverID);
        insertStatement.executeUpdate();
        conn.close();;
    }
    public boolean isDriverInShift(String DriverID, LocalDate localDate, ShiftType shiftType)throws SQLException{
        Connection conn = DataBase.connect();
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT DriverID FROM DriverInShift WHERE DriverID = ? AND Date = ? AND ShiftType = ? ")) {
            statement.setString(1, DriverID);
            statement.setString(2, localDate.toString());
            statement.setString(3, shiftType.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                conn.close();;
                return resultSet.next();
            }
        }


    }

    public boolean isInDataBase(LocalDate localDate, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        try {
            String query = "SELECT * FROM GenericShift WHERE Date = ? AND ShiftType = ? " ;
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, localDate.toString());
            statement.setString(2, shiftType.toString());
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                //statement.close();
                conn.close();;
                return true;
            }
        }
        catch (Exception exception){
            exception.printStackTrace();
            conn.close();;
        }
        conn.close();;
        return false;
    }

    public void updateGenericShiftStartHour(LocalDate date, ShiftType type, LocalTime startHour) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "UPDATE GenericShift SET StartHour = ? WHERE Date = ? AND ShiftType = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, startHour.toString());
        pstmt.setString(2, date.toString());
        pstmt.setString(3, type.toString());
        // update
        pstmt.executeUpdate();
        String mapKey = date.toString() + type;
        if(cache.containsKey(mapKey)){
            cache.get(mapKey).setStartHour(startHour);
        }
        conn.close();;
    }

    public void updateFinishHour(LocalDate date, ShiftType type, LocalTime finishHour) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "UPDATE GenericShift SET EndHour = ? WHERE Date = ? AND ShiftType = ? ";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, finishHour.toString());
        pstmt.setString(2, date.toString());
        pstmt.setString(3, type.toString());
        // update
        pstmt.executeUpdate();
        String mapKey = date.toString() + type;
        if(cache.containsKey(mapKey)){
            cache.get(mapKey).setFinishHour(finishHour);
        }
        conn.close();;
    }
}
