package SuperLee.HumenResource.DataLayer;
import SuperLee.HumenResource.BusinessLayer.Driver;
import SuperLee.HumenResource.BusinessLayer.ShiftType;
import SuperLee.Transport.BusinessLayer.LicenseType;
import SuperLee.Transport.BusinessLayer.TrainingType;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DriverDataMapper {
    private static DriverDataMapper instance = null;
    private static Map<String, SuperLee.HumenResource.BusinessLayer.Driver> cache = new HashMap<>();

    private DriverDataMapper() {

    }
    public static synchronized DriverDataMapper getInstance() {
        if(instance == null){
            instance =  new DriverDataMapper();
        }
        return instance;
    }
    public void insert(SuperLee.HumenResource.BusinessLayer.Driver driver) throws SQLException {
        Connection conn = DataBase.connect();
        if(cache.containsValue(driver)){
            System.out.println("This driver is already inside your data base");
            conn.close();
            return;
        }
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Drivers (ID, FirstName, LastName, Password, UserName, BankAccount, Wage, EmploymentCondition, HireDate, NumberOfShiftsPerWeek, WorkHours, LicenseType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,? ,?)");
        stmt.setString(1, driver.getID());
        stmt.setString(2, driver.getFirstName());
        stmt.setString(3, driver.getLastName());
        stmt.setString(4, driver.getPassword());
        stmt.setString(5, driver.getID());
        stmt.setInt(6, driver.getBankAccount());
        stmt.setDouble(7, driver.getWage());
        stmt.setString(8, driver.getEmploymentCondition() );
        stmt.setString(9, driver.getHireDate().toString());
        stmt.setInt(10, driver.getNumberOfShiftsPerWeek());
        stmt.setDouble(11, driver.getWorkHours());
        stmt.setString(12, driver.getLicenseType().toString());
        for(TrainingType type : driver.getTrainings()) {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO DriverTrainings (DriverID, Training) VALUES (?, ?)");
            statement.setString(1, driver.getID());
            statement.setString(2, type.toString());
            statement.executeUpdate();
        }
        stmt.executeUpdate();
        cache.put(driver.getID(), driver);
        conn.close();;
    }
    public void delete(String DriverID) throws SQLException {
        Connection conn = DataBase.connect();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Drivers WHERE ID = ?");
        stmt.setString(1, DriverID);
        stmt.executeUpdate();
        try (PreparedStatement statement = conn.prepareStatement(
                "DELETE FROM DriverTrainings " +
                        "WHERE DriverID = ?")) {
            statement.setString(1, DriverID);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(cache.containsKey(DriverID)) {
            cache.remove(DriverID);
        }

        conn.close();;
    }
    public SuperLee.HumenResource.BusinessLayer.Driver getDriver(String id) throws SQLException {
        Connection conn = DataBase.connect();
        SuperLee.HumenResource.BusinessLayer.Driver driver = cache.get(id);
        if (driver != null) {
            conn.close();
            return driver;
        }
        PreparedStatement stmt = conn.prepareStatement(
                "SELECT ID, FirstName, LastName, Password, UserName, BankAccount, Wage, " +
                        "EmploymentCondition, HireDate, NumberOfShiftsPerWeek, WorkHours, LicenseType " +
                        "FROM Drivers " +
                        "WHERE ID = ? "
        );
        stmt.setString(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
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
            String[] splitList = hireDateString.split("-", 3);
            LocalDate hireDate = LocalDate.of(Integer.parseInt(splitList[0]), Integer.parseInt(splitList[1]), Integer.parseInt(splitList[2]));
            LicenseType NewLicense = null;
            String licenseType = rs.getString("LicenseType");
            if (licenseType.equals("Light")) {
                NewLicense = LicenseType.Light;
            }
            if (licenseType.equals("Medium")) {
                NewLicense = LicenseType.Medium;
            }
            if (licenseType.equals("Heavy")) {
                NewLicense = LicenseType.Heavy;
            }
            String sql = "SELECT Training FROM DriverTrainings WHERE DriverID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, id);
            // Execute the query
            ResultSet resultSet = statement.executeQuery();
            ArrayList<TrainingType> TrainingList = new ArrayList<>();
            // Process the result set
            while (resultSet.next()) {
                String training = resultSet.getString("Training");
                if (training.equals("DRY")) {
                    TrainingList.add(TrainingType.DRY);
                }
                if (training.equals("FROZEN")) {
                    TrainingList.add(TrainingType.FROZEN);
                }
                if (training.equals("REFRIGERATED")) {
                    TrainingList.add(TrainingType.REFRIGERATED);
                }
            }
            SuperLee.HumenResource.BusinessLayer.Driver newDriver = new SuperLee.HumenResource.BusinessLayer.Driver(firstName, lastName, id, password, bankAccount, wage,
                    hireDate, employmentCondition, NewLicense, TrainingList);
            cache.put(id, newDriver);
            conn.close();;
            return newDriver;
        }
        conn.close();;
        return null;

    }

    public void updateWage(String WorkerId, double newWage) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "UPDATE Drivers SET Wage = ? WHERE ID = ?";

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
        Connection conn = DataBase.connect();
        String sql = "UPDATE Drivers SET Password = ? WHERE ID = ?";
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
        Connection conn = DataBase.connect();
        String sql = "UPDATE Drivers SET EmploymentCondition = ? WHERE ID = ?";

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
        Connection conn = DataBase.connect();
        String sql = "UPDATE Drivers SET NumberOfShiftsPerWeek = ? WHERE ID = ?";
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
        Connection conn = DataBase.connect();
        String sql = "UPDATE Drivers SET WorkHours = ? WHERE ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        // set the corresponding parameters
        pstmt.setDouble(1,workHours);
        pstmt.setString(2, employeeId);
        // update
        pstmt.executeUpdate();
        if(cache.containsKey(employeeId)){
            cache.get(employeeId).setWorkHours(getDriver(employeeId).getWorkHours() + workHours);
        }
        conn.close();;
    }

    public boolean isInDataBase(String employeeId) throws SQLException {
        Connection conn = DataBase.connect();
        boolean idExists = false;
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM Drivers WHERE ID = '" + employeeId + "'";
        ResultSet rs = stmt.executeQuery(query);
        if (rs.next()) {
            idExists = true;
        }
        conn.close();;
        return idExists;

    }
    public ArrayList<SuperLee.HumenResource.BusinessLayer.Driver> getAllDrivers() throws SQLException {
        Connection conn = DataBase.connect();
        // Create a statement and execute the query
        ArrayList<SuperLee.HumenResource.BusinessLayer.Driver> Drivers = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT ID FROM Drivers " );

        // Iterate through the results and print the workers
        while (rs.next()) {
            String workerID = rs.getString("ID");
            Drivers.add(getDriver(workerID));
        }
        conn.close();;
        return Drivers;
    }
    public void insertTraining(SuperLee.HumenResource.BusinessLayer.Driver driver, TrainingType type) throws SQLException {
        Connection conn = DataBase.connect();
        String sql = "INSERT INTO DriverTrainings (DriverID, Training) VALUES (?, ?)";
        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, driver.getID());
        statement.setString(2, type.toString());
        statement.executeUpdate();
        driver.setTrainings(type);
        conn.close();;
    }
    public ArrayList<SuperLee.HumenResource.BusinessLayer.Driver> getDriversFromShift(LocalDate date, ShiftType shiftType) throws SQLException {
        Connection conn = DataBase.connect();;
        ArrayList<Driver> Drivers = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(
                "SELECT DriverID FROM DriverInShift " +
                        "WHERE Date = ? AND ShiftType = ? ")) {
            statement.setString(1, date.toString());
            statement.setString(2, shiftType.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String DriverId = resultSet.getString("DriverID");
                    Drivers.add(getDriver(DriverId));
                }
            }
        }
        conn.close();;
        return Drivers;
    }

    public boolean isTrainingExist(String employeeId, TrainingType type) throws SQLException {
        Connection conn = DataBase.connect();
        boolean idExists = false;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM DriverTrainings WHERE DriverID = ? AND Training = ? ");
        stmt.setString(1, employeeId);
        stmt.setString(2, type.toString());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            idExists = true;
        }
        conn.close();;
        return idExists;
    }
}

