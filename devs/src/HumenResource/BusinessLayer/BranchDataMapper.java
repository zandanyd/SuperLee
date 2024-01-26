package SuperLee.HumenResource.BusinessLayer;
import SuperLee.HumenResource.DataLayer.DataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BranchDataMapper  {
    private static BranchDataMapper instance = null;
    private static Map<String, Branch> cache = new HashMap<>();
    private static Statement stmt;

    private BranchDataMapper()  {

    }
    public static synchronized BranchDataMapper getInstance()  {
        if(instance == null){
            instance =  new BranchDataMapper();
        }
        return instance;
    }
    public void insert(Branch branch) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO Branch(address) VALUES (?)");
        preparedStatement.setString(1, branch.getAddress());
        preparedStatement.executeUpdate();
        for(int i = 1 ; i<8; i++){
            PreparedStatement MorningStatement = conn.prepareStatement("INSERT INTO Schedule(Day,ShiftType,StartHour,finishHour,BranchAddress) VALUES (?,?,?,?,?)");
            MorningStatement.setInt(1, i);
            MorningStatement.setInt(2, 0);
            MorningStatement.setString(3, "8:00");
            MorningStatement.setString(4, "15:00");
            MorningStatement.setString(5, branch.getAddress());
            MorningStatement.executeUpdate();
            MorningStatement.close();
            PreparedStatement EveningStatement = conn.prepareStatement("INSERT INTO Schedule(Day,ShiftType,StartHour,finishHour,BranchAddress) VALUES (?,?,?,?,?)");
            EveningStatement.setInt(1, i);
            EveningStatement.setInt(2, 1);
            EveningStatement.setString(3, "15:00");
            EveningStatement.setString(4, "21:00");
            EveningStatement.setString(5, branch.getAddress());

            EveningStatement.executeUpdate();
            EveningStatement.close();
        }
        conn.close();
    }

    public Branch get(String branchAddress) throws SQLException {
        Connection conn = DataBase.connect();
        if (cache.containsKey(branchAddress)) {
            return cache.get(branchAddress);
        }
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM Branch WHERE Address = ?");
        preparedStatement.setString(1, branchAddress);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String address = resultSet.getString("Address");
            Branch branch = new Branch(branchAddress);
            cache.put(branchAddress, branch);
            conn.close();
            return branch;
        }
        conn.close();
        throw new SQLException("No such branch");
    }
    public void updateStartHour(String branchAddress, int day, int shiftType, String startHour) throws SQLException {
        Connection conn = DataBase.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE Schedule SET StartHour = ? WHERE Day = ? AND ShiftType = ? AND BranchAddress = ?");
            preparedStatement.setString(1, startHour);
            preparedStatement.setInt(2, day);
            preparedStatement.setInt(3, shiftType);
            preparedStatement.setString(4, branchAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();

    }
    public void updateFinishHour(String branchAddress, int day, int shiftType, String finishHour) throws SQLException {
        Connection conn = DataBase.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("UPDATE Schedule SET FinishHour = ? WHERE Day = ? AND ShiftType = ? AND BranchAddress = ?");
            preparedStatement.setString(1, finishHour);
            preparedStatement.setInt(2, day);
            preparedStatement.setInt(3, shiftType);
            preparedStatement.setString(4, branchAddress);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
    }

    public String getStartHour(String branchAddress, int day, int shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT StartHour FROM Schedule WHERE Day = ? AND ShiftType = ? AND BranchAddress = ?");
            preparedStatement.setInt(1, day);
            preparedStatement.setInt(2, shiftType);
            preparedStatement.setString(3, branchAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String result = resultSet.getString("StartHour");
                conn.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return null;
    }

    public String getFinishHour(String branchAddress, int day, int shiftType) throws SQLException {
        Connection conn = DataBase.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT FinishHour FROM Schedule WHERE Day = ? AND ShiftType = ? AND BranchAddress = ?");
            preparedStatement.setInt(1, day);
            preparedStatement.setInt(2, shiftType);
            preparedStatement.setString(3, branchAddress);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String result = resultSet.getString("FinishHour");

                conn.close();
                return result;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return null;
    }


    public boolean isInDataBase(String address)throws SQLException{
        Connection conn = DataBase.connect();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM Branch WHERE Address = ?");
            preparedStatement.setString(1, address);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                conn.close();
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return false;

    }
    public ArrayList<Branch> getAllBranches() throws SQLException {
        Connection conn = DataBase.connect();
        // Create a statement and execute the query
        ArrayList<Branch> branches = new ArrayList<>();
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT Address FROM Branch ");
        // Iterate through the results and print the workers
        while (rs.next()) {
            String address = rs.getString("Address");
            branches.add(get(address));
        }

        conn.close();
        return branches;
    }

}
