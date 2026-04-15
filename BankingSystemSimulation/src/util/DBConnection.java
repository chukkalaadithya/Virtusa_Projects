package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/bank_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "system1234";

    public static Connection getConnection() {
        Connection con=null;

        try {
            con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error in DBconnection catch exception");
        }

        return con;
    }
    
}