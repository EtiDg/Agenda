package agenda.process;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import agenda.process.sql.*;
public class Main {
	   /**
	    * @param args the command line arguments
	    */
	   public static void main(String[] args) {
		   SQLiteJDBCDriverConnection databaseDriver = new SQLiteJDBCDriverConnection();
		   databaseDriver.createTables();
	   }
	   
	   public static void createNewDatabase(String fileName) {
		   
	        String url = "jdbc:sqlite:database/agenda.db" + fileName;
	 
	        try (Connection conn = DriverManager.getConnection(url)) {
	            if (conn != null) {
	                DatabaseMetaData meta = conn.getMetaData();
	                System.out.println("The driver name is " + meta.getDriverName());
	                System.out.println("A new database has been created.");
	            }
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	    }
}
