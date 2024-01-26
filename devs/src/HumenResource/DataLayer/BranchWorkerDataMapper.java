package SuperLee.HumenResource.DataLayer;
import SuperLee.HumenResource.BusinessLayer.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class BranchWorkerDataMapper {
    private static BranchWorkerDataMapper instance = null;
    private Map<String, BranchWorker> cache = new HashMap<>();

    private BranchWorkerDataMapper() {
    }

    public static synchronized BranchWorkerDataMapper getInstance()  {
        if (instance == null) {
            instance = new BranchWorkerDataMapper();
        }
        return instance;
    }

    public BranchWorker getWorker(String id) throws SQLException {
        Connection conn = DataBase.connect();;
        BranchWorker worker = cache.get(id);
        if (worker != null) {
            conn.close();;
            return worker;
        }
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT BranchAddress, FirstName, LastName, Password, UserName, BankAccount, Wage, " +
                        "EmploymentCondition, HireDate, NumberOfShiftsPerWeek, WorkHours, " +
                        "IsShiftManager " +
                        "FROM BranchWorker " +
                        "WHERE ID = ? "
        );
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            String BranchAddress = rs.getString("BranchAddress");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            String password = rs.getString("Password");
            String username = rs.getString("UserName");
            int bankAccount = rs.getInt("BankAccount");
            double wage = rs.getDouble("Wage");
            String employmentCondition = rs.getString("EmploymentCondition");
            String hireDateString = rs.getString("HireDate");
            int numberOfShiftsPerWeek = rs.getInt("NumberOfShiftsPerWeek");
            double workHours = rs.getDouble("WorkHours");
            boolean isShiftManager = rs.getInt("IsShiftManager") == 0;
            String[] splitList = hireDateString.split("-", 3);
            LocalDate hireDate = LocalDate.of(Integer.parseInt(splitList[0]), Integer.parseInt(splitList[1]), Integer.parseInt(splitList[2]));
            if (isShiftManager) {
                worker = new ShiftManager(firstName, lastName, id, password, BranchAddress, bankAccount, wage,
                        hireDate, employmentCondition);
                worker.setWorkHours(workHours);
                cache.put(id, worker);
                conn.close();;
                return worker;
            }
            String sql = "SELECT Training FROM SimpleWorkerTrainings WHERE WorkerID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            ArrayList<SimpleWorkerType> TrainingList = new ArrayList<>();
            // Process the result set
            while (resultSet.next()) {
                String training = resultSet.getString("Training");

                if (training.equals("Cashier")) {
                    TrainingList.add(SimpleWorkerType.Cashier);
                }
                if (training.equals("StockKeeper")) {
                    TrainingList.add(SimpleWorkerType.StockKeeper);
                }
                if (training.equals("GeneralWorker")) {
                    TrainingList.add(SimpleWorkerType.GeneralWorker);
                }
                if (training.equals("Cleaner")) {
                    TrainingList.add(SimpleWorkerType.Cleaner);
                }
                if (training.equals("Usher")) {
                    TrainingList.add(SimpleWorkerType.Usher);
                }
            }
            SimpleWorker simpleWorker = new SimpleWorker(firstName, lastName, id, password, BranchAddress, bankAccount, wage,
                    hireDate, employmentCondition, TrainingList.get(0));
            simpleWorker.setWorkHours(workHours);
            for (int i = 1; i < TrainingList.size(); i++) {
                simpleWorker.addTraining(TrainingList.get(i));
            }

            cache.put(id, simpleWorker);
            conn.close();;
            return simpleWorker;
        }
        conn.close();;
        return null;

    }

    public boolean isManager(String id) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "SELECT IsShiftManager FROM BranchWorker WHERE ID = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();
        int isManager = rs.getInt("IsShiftManager");
        if(isManager == 0) {
            conn.close();;
            return true;
        }
        else{
            conn.close();;
            return false;
        }

    }

    public void insertSimpleWorker(SimpleWorker worker) throws SQLException {
        Connection conn = DataBase.connect();;
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR REPLACE INTO BranchWorker " +
                        "(ID, BranchAddress, FirstName, LastName, Password, UserName, BankAccount, Wage, " +
                        "EmploymentCondition, HireDate, NumberOfShiftsPerWeek, WorkHours, IsShiftManager) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        stmt.setString(1, worker.getID());
        stmt.setString(2, worker.getBranchAddress());
        stmt.setString(3, worker.getFirstName());
        stmt.setString(4, worker.getLastName());
        stmt.setString(5, worker.getPassword());
        stmt.setString(6, worker.getID());
        stmt.setInt(7, worker.getBankAccount());
        stmt.setDouble(8, worker.getWage());
        stmt.setString(9, worker.getEmploymentCondition());
        stmt.setString(10, worker.getHireDate().toString());
        stmt.setInt(11, worker.getNumberOfShiftsPerWeek());
        stmt.setDouble(12, worker.getWorkHours());
        stmt.setInt(13,  1);
        stmt.executeUpdate();
        String sql = "INSERT INTO SimpleWorkerTrainings (WorkerID, Training) VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, worker.getID());
        statement.setString(2, worker.getTraining().get(0).toString());
        statement.executeUpdate();
        cache.put(worker.getID(), worker);
        conn.close();;
        System.out.println("hvfgfxdfzschj");
    }
    public void insertShiftManager(ShiftManager worker, String BranchAddress) throws SQLException {
        Connection conn = DataBase.connect();;
        PreparedStatement stmt = conn.prepareStatement(
                "INSERT OR REPLACE INTO BranchWorker " +
                        "(ID, BranchAddress, FirstName, LastName, Password, UserName, BankAccount, Wage, " +
                        "EmploymentCondition, HireDate, NumberOfShiftsPerWeek, WorkHours, IsShiftManager) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
        );
        stmt.setString(1, worker.getID());
        stmt.setString(2, BranchAddress);
        stmt.setString(3, worker.getFirstName());
        stmt.setString(4, worker.getLastName());
        stmt.setString(5, worker.getPassword());
        stmt.setString(6, worker.getID());
        stmt.setInt(7, worker.getBankAccount());
        stmt.setDouble(8, worker.getWage());
        stmt.setString(9, worker.getEmploymentCondition());
        stmt.setString(10, worker.getHireDate().toString());
        stmt.setInt(11, worker.getNumberOfShiftsPerWeek());
        stmt.setDouble(12, worker.getWorkHours());
        stmt.setInt(13,  0);
        stmt.executeUpdate();
        cache.put(worker.getID() , worker);
        conn.close();;
    }

    public void deleteBranchWorker(String workerID) throws SQLException {
        Connection conn = DataBase.connect();;
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM BranchWorker " +
                        "WHERE ID = ?")) {
            statement.setString(1, workerID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM SimpleWorkerTrainings " +
                        "WHERE WorkerID = ?")) {
            statement.setString(1, workerID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        cache.remove(workerID);
        conn.close();;
    }

    public ArrayList<BranchWorker> getWorkersByBranch(String BranchAddress) throws SQLException {
        Connection conn = DataBase.connect();;
        ArrayList<BranchWorker> branchWorkers = new ArrayList<>();
        String query = "SELECT * FROM BranchWorker WHERE BranchAddress = ?";

        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, BranchAddress);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                branchWorkers.add(getWorker(id));
            }
            conn.close();;
            return branchWorkers;
        }
    }
    public void updateWage(String WorkerId, double newWage) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "UPDATE BranchWorker SET Wage = ? WHERE ID = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setDouble(1, newWage);
        pstmt.setString(2, WorkerId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(WorkerId)){
            cache.get(WorkerId).setWage(newWage);
        }
        conn.close();;
    }

    public void updatePassword(String WorkerId, String newPassword) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "UPDATE BranchWorker SET Password = ? WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, newPassword);
        pstmt.setString(2, WorkerId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(WorkerId)){
            cache.get(WorkerId).setPassword(newPassword);
        }
        conn.close();;
    }

    public void updateEmploymentCondition(String employeeId, String newCondition) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "UPDATE BranchWorker SET EmploymentCondition = ? WHERE ID = ?";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setString(1, newCondition);
        pstmt.setString(2, employeeId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(employeeId)){
            cache.get(employeeId).setEmploymentCondition(newCondition);
        }
        conn.close();;
    }

    public void updateNumberOfShiftsPerWeek(String employeeId, int newNumberOfShifts) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "UPDATE BranchWorker SET NumberOfShiftsPerWeek = ? WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setInt(1, newNumberOfShifts);
        pstmt.setString(2, employeeId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(employeeId)){
            cache.get(employeeId).setNumberOfShiftsPerWeek(newNumberOfShifts);
        }
        conn.close();;
    }
    public void updateWorkHours(String employeeId, double workHours) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "UPDATE BranchWorker SET WorkHours = ? WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setDouble(1, workHours );
        pstmt.setString(2, employeeId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(employeeId)){
            cache.get(employeeId).setWorkHours(workHours );
        }
        conn.close();;
    }

    public boolean isInDataBase(String employeeId) throws SQLException {
        Connection conn = DataBase.connect();;
        boolean idExists = false;
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM BranchWorker WHERE ID = '" + employeeId + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            idExists = true;
        }
        conn.close();
        return idExists;

    }
    public ArrayList<BranchWorker> getAllWorkers() throws SQLException {
        Connection conn = DataBase.connect();;
        // Create a statement and execute the query
        ArrayList<BranchWorker> branchWorkers = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT ID FROM BranchWorker " );

        // Iterate through the results and print the workers
        while (rs.next()) {
            String workerID = rs.getString("ID");
            branchWorkers.add(getWorker(workerID));
        }
        conn.close();;
        return branchWorkers;
    }
    public void insertTraining(SimpleWorker worker, SimpleWorkerType type) throws SQLException {
        Connection conn = DataBase.connect();;
        String sql = "INSERT INTO SimpleWorkerTrainings (WorkerID, Training) VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, worker.getID());
        statement.setString(2, type.toString());
        statement.executeUpdate();
        worker.addTraining(type);
        conn.close();
    }

    public ArrayList<BranchWorker> getWorkersFromShift( String BranchAddress, LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();;
        ArrayList<BranchWorker> workers = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT WorkerID, BranchAddress FROM WorkerInShift " +
                        "WHERE Date = ? AND ShiftType = ?  AND BranchAddress = ?")) {
            statement.setString(1, date.toString());
            statement.setString(2, shiftType.toString());
            statement.setString(3,BranchAddress);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String workerId = resultSet.getString("WorkerID");
                    workers.add(getWorker(workerId));
                }
            }
        }
        conn.close();;
        return workers;
    }
    public boolean haveTraining(SimpleWorkerType type, String id) throws SQLException {
        Connection conn = DataBase.connect();;
        boolean idExists = false;
        String query = "SELECT * FROM SimpleWorkerTrainings WHERE WorkerID = ? AND Training = ? ";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1,id);
        statement.setString(2, type.toString());
        ResultSet rs = statement.executeQuery();
        if (rs.next()) {
            idExists = true;
        }
        conn.close();
        return idExists;
    }



}
