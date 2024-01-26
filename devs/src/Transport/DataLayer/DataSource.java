package SuperLee.Transport.DataLayer;
import java.sql.*;

public class DataSource{
    private static final String DB_URL = "jdbc:sqlite::resource:SuperLee.db";
    private static Connection conn;

    public static Connection openConnection(){
        try{
            conn = DriverManager.getConnection(DB_URL);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try{
            if (conn != null && !conn.isClosed()){
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}