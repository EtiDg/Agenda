package agenda.process;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import agenda.process.sql.*;
public class MainProcess {

	static boolean isDelete = true;

	public static void main(String[] args) {
		createNewDatabase();
		//QueryManager.connect();
		QueryManager.createTables();
//		try {
//			QueryManager.ajoutLieu(new Lieu(0,"salut"));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		QueryManager.createTreveHivernale();
	}

	public static void createNewDatabase() {

		//String url = "jdbc:sqlite:database/agenda.db";
		String url = "jdbc:sqlite:" + System.getenv("LOCALAPPDATA") + "/Agenda/database/agenda.db";
		
		if (isDelete){
			File file = new File(System.getenv("LOCALAPPDATA") + "/Agenda/database/agenda.db");
			file.delete();	
		}
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
