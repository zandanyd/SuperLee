package SuperLee.HumenResource.BusinessLayer;
import SuperLee.HumenResource.DataLayer.DataBase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchShiftDataMapper {
    private static BranchShiftDataMapper instance =null;
    private static Map<String, BranchShift> cache = new HashMap<>();

    private BranchShiftDataMapper() {
    }
    public static synchronized BranchShiftDataMapper getInstance() {
        if(instance == null){
            instance =  new BranchShiftDataMapper();
        }
        return instance;
    }


    public BranchShift getById(LocalDate date, ShiftType shiftType, String branchAddress) throws SQLException {
        Connection conn = DataBase.connect();
        String key= date.toString() + shiftType.toString() + branchAddress;
        if(cache.containsKey(key)){
            conn.close();
            return cache.get(key);
        }
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT ShiftManager, StartHour, EndHour FROM BranchShift " +
                        "WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?")) {
            statement.setString(1, date.toString());
            statement.setString(2, shiftType.toString());
            statement.setString(3, branchAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String shiftManager = resultSet.getString("ShiftManager");
                    String startHour = resultSet.getString("StartHour");
                    String[] arrOfStr = startHour.split(":", 2);
                    LocalTime start = LocalTime.of(Integer.parseInt(arrOfStr[0]),Integer.parseInt(arrOfStr[1]));
                    String endHour = resultSet.getString("EndHour");
                    String[] arrOfEnd = endHour.split(":", 2);
                    LocalTime end = LocalTime.of(Integer.parseInt(arrOfEnd[0]),Integer.parseInt(arrOfEnd[1]));
                    BranchShift shift= new BranchShift(date, shiftType, shiftManager, branchAddress);
                    shift.setStartHour(start);
                    shift.setFinishHour(end);
                    String key1 = date.toString() + shiftType + branchAddress;
                    cache.put(key1, shift);
                    conn.close();
                    return shift;
                } else {
                    conn.close();
                    return null;
                }
            }
        }
    }

    public void insert(ArrayList<String> workersId, BranchShift shift) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO BranchShift (Date, ShiftType, BranchAddress, ShiftManager, StartHour, EndHour) VALUES (?, ?, ?, ?, ?, ?)");
        insertStatement.setString(1, shift.getDate().toString());
        insertStatement.setString(2, shift.getType().toString());
        insertStatement.setString(3, shift.getBranchAddress());
        insertStatement.setString(4, shift.getManager());
        insertStatement.setString(5, shift.getStartHour().toString());
        insertStatement.setString(6, shift.getFinishHour().toString());
        insertStatement.executeUpdate();
        cache.put( shift.getDate().toString() + shift.getType().toString() + shift.getBranchAddress(), shift);
        for(String workerID : workersId) {
            insertStatement = conn.prepareStatement("INSERT INTO WorkerInShift (Date, ShiftType, WorkerID, BranchAddress) VALUES (?, ?, ?, ?)");
            insertStatement.setString(1, shift.getDate().toString());
            insertStatement.setString(2, shift.getType().toString());
            insertStatement.setString(3, workerID);
            insertStatement.setString(4, shift.getBranchAddress());
            insertStatement.executeUpdate();
        }
        conn.close();

    }

    public void deleteBranchWorkerFromShift(String branchAddress, String workerID, LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM WorkerInShift " +
                        "WHERE Date = ? AND ShiftType = ? AND WorkerID = ? AND BranchAddress = ?");
        statement.setString(1, date.toString());
        statement.setString(2, shiftType.toString());
        statement.setString(3, workerID);
        statement.setString(4, branchAddress);
        statement.executeUpdate();
        conn.close();
    }

    public void deleteBranchShift(String BranchAddress, LocalDate date, ShiftType type) throws SQLException {
        Connection conn = DataBase.connect();;
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM BranchShift " +
                        "WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?")) {
            statement.setString(1, date.toString());
            statement.setString(2, type.toString());
            statement.setString(3, BranchAddress);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        String mapKey = date.toString() + type + BranchAddress;
        cache.remove(mapKey);
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM WorkerInShift " +
                        "WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?")) {
            statement.setString(1, date.toString());
            statement.setString(2, type.toString());
            statement.setString(3, BranchAddress);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();;
    }

    public void insertBranchWorkerToShift(String workerId, String branchAddress, LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO WorkerInShift (Date, ShiftType, WorkerID, BranchAddress) VALUES (?, ?, ?, ?)");
        insertStatement.setString(1, date.toString());
        insertStatement.setString(2, shiftType.toString());
        insertStatement.setString(3, workerId);
        insertStatement.setString(4, branchAddress);
        insertStatement.executeUpdate();
        conn.close();
    }
    public boolean isBranchWorkerInShift(String BranchAddress, String WorkerID, LocalDate localDate, ShiftType shiftType)throws SQLException{
        Connection conn = DataBase.connect();
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT WorkerID FROM WorkerInShift WHERE WorkerID = ? AND Date = ? AND ShiftType = ? AND BranchAddress = ?")) {
            statement.setString(1, WorkerID);
            statement.setString(2, localDate.toString());
            statement.setString(3, shiftType.toString());
            statement.setString(4, BranchAddress);
            try (ResultSet resultSet = statement.executeQuery()) {
                conn.close();
                return resultSet.next();
            }
        }


    }

    public boolean isInDataBase(String BranchAddress, LocalDate localDate, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();

        try {
            String query = "SELECT * FROM BranchShift WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?" ;
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, localDate.toString());
            statement.setString(2, shiftType.toString());
            statement.setString(3, BranchAddress);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                //statement.close();
                conn.close();
                return true;
            }

        }
        catch (Exception exception){
            exception.printStackTrace();
            conn.close();
        }
        conn.close();
        return false;
    }

    public void updateBranchShiftStartHour(String BranchAddress, LocalDate date, ShiftType type, LocalTime startHour) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "UPDATE BranchShift SET StartHour = ? WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, startHour.toString());
        pstmt.setString(2, date.toString());
        pstmt.setString(3, type.toString());
        pstmt.setString(4, BranchAddress);
        // update
        pstmt.executeUpdate();
        String mapKey = date.toString() + type + BranchAddress;
        if(cache.containsKey(mapKey)){
            cache.get(mapKey).setStartHour(startHour);
        }
        conn.close();
    }

    public void updateBranchShiftFinishHour(String BranchAddress, LocalDate date, ShiftType type, LocalTime finishHour) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "UPDATE BranchShift SET EndHour = ? WHERE Date = ? AND ShiftType = ? AND BranchAddress = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, finishHour.toString());
        pstmt.setString(2, date.toString());
        pstmt.setString(3, type.toString());
        pstmt.setString(4, BranchAddress);
        // update
        pstmt.executeUpdate();
        String mapKey = date.toString() + type + BranchAddress;
        if(cache.containsKey(mapKey)){
            cache.get(mapKey).setFinishHour(finishHour);
        }
        conn.close();
    }

    public void insertItemToCancellationList(String BranchAddress, LocalDate date, ShiftType type, String item) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "SELECT * FROM CanceledItem WHERE BranchAddress = ? AND Date = ? AND ShiftType = ? AND Item = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, BranchAddress);
        stmt.setString(2, date.toString());
        stmt.setString(3, type.toString());
        stmt.setString(4,  item);

        // Execute the SELECT statement
        ResultSet rs = stmt.executeQuery();
        // Check if the result set is empty
        if (!rs.isBeforeFirst()) {
            PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO CanceledItem (BranchAddress, Date, ShiftType, Item, Amount) VALUES (?, ?, ?, ?, ?)");
            insertStatement.setString(1, BranchAddress);
            insertStatement.setString(2, date.toString());
            insertStatement.setString(3, type.toString());
            insertStatement.setString(4, item);
            insertStatement.setInt(5, 1);
            insertStatement.executeUpdate();
        } else {
            sql = "UPDATE CanceledItem SET Amount = Amount + 1 WHERE BranchAddress = ? AND Date = ? AND ShiftType = ? AND Item = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, BranchAddress);
            stmt.setString(2, date.toString());
            stmt.setString(3, type.toString());
            stmt.setString(4, item);
            stmt.executeUpdate();
        }
        conn.close();
    }

    public HashMap<String, Integer> getAllCancelledItems(String BranchAddress, LocalDate date, ShiftType type) throws SQLException {
        Connection conn = DataBase.connect();
        HashMap<String, Integer> AllCancelledItems = new HashMap<>();
        String sql = "SELECT * FROM CanceledItem WHERE BranchAddress = ? AND Date = ? AND ShiftType = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, BranchAddress);
        stmt.setString(2, date.toString());
        stmt.setString(3, type.toString());
        ResultSet rs = stmt.executeQuery();
        // Process the results
        while (rs.next()) {
            String item = rs.getString("Item");
            int amount = rs.getInt("Amount");
            AllCancelledItems.put(item, amount);
        }
        conn.close();
        return AllCancelledItems;
    }





//    public ArrayList<BranchShift> getBranchShiftHistoryByBranch(String branchID, LocalDate date, ShiftType type) throws SQLException{
//
//    }



}
