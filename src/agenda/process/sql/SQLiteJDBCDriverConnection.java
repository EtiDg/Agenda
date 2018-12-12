package agenda.process.sql;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLiteJDBCDriverConnection {
	private static String url = "jdbc:sqlite:database/agenda.db"; 
	
	public SQLiteJDBCDriverConnection(){
		
	}
	
    /**
    * Connect to a sample database
    */
   public void connect() {
       Connection conn = null;
       try {
           // create a connection to the database
           conn = DriverManager.getConnection(url);
           
           System.out.println("Connection to SQLite has been established.");
           
       } catch (SQLException e) {
           System.out.println(e.getMessage());
       } finally {
           try {
               if (conn != null) {
                   conn.close();
               }
           } catch (SQLException ex) {
               System.out.println(ex.getMessage());
           }
       }
   }
   
   public void createTables(){       
       // SQL statement for creating new tables 
       String creationModeleDeReprise = "CREATE TABLE IF NOT EXISTS ModeleDeReprise (\n"
               + "	id integer PRIMARY KEY,\n"
               + "	nom text\n"
               + ");";
       
       String creationGroupe = "CREATE TABLE IF NOT EXISTS Groupe (\n"
               + "	nom text PRIMARY KEY,\n"
               + "	isVacances boolean,\n"
               + "  isTreve boolean,\n"
               + "  idMR integer,\n"
               + "  FOREIGN KEY (idMR) REFERENCES ModeleDeReprise(id)\n"
               + ");";
       
       String creationLieu = "CREATE TABLE IF NOT EXISTS Lieu (\n"
               + "	nom text PRIMARY KEY\n"
               + ");";
       
       String creationMonitrice = "CREATE TABLE IF NOT EXISTS Monitrice (\n"
               + "	nom text PRIMARY KEY\n"
               + ");";
       
       String creationJourFerie = "CREATE TABLE IF NOT EXISTS JourFerie (\n"
               + "	date date PRIMARY KEY,\n"
               + ");";
       
       String creationReprise = "CREATE TABLE IF NOT EXISTS Reprise (\n"
               + "	id integer PRIMARY KEY,\n"
               + "	nom text, \n"
               + "  heureDebut integer,\n"
               + "  duree integer,\n"
               + "  nomLieu text ,\n"
               + "  date date ,\n"
               + "  FOREIGN KEY  (nomLieu) REFERENCES Lieu(nom),\n"
               + "  FOREIGN KEY  (date) REFERENCES Date(date)\n"
               + ");";
       
       String creationCreneau = "CREATE TABLE IF NOT EXISTS Creneau (\n"
               + "	id integer PRIMARY KEY,\n"
               + "  heureDebut integer,\n"
               + "  duree integer,\n"
               + "  date date ,\n"
               + "  nomMonitrice text,\n"
               + "  FOREIGN KEY (date) REFERENCES Date(date),\n"
               + "  FOREIGN KEY (nomMonitrice) REFERENCES Monitrice(nom)\n"
               + ");";
       
       String creationVacances = "CREATE TABLE IF NOT EXISTS Vacances (\n"
               + "	nom text PRIMARY KEY,\n"
               + "  dateDebut date, \n"
               + "  dateFin date, \n"
               + "  FOREIGN KEY (dateDebut) REFERENCES Date(date),\n"
               + "  FOREIGN KEY (dateFin) REFERENCES Date(date)\n"
               + ");";
       
       String creationTreveHivernale = "CREATE TABLE IF NOT EXISTS TreveHivernale (\n"
               + "  dateDebut date,\n" 
               + "  dateFin date ,\n" 
               + "  FOREIGN KEY (dateDebut) REFERENCES Date(date),\n"
               + "  FOREIGN KEY (dateFin) REFERENCES Date(date)\n"
               + ");";
       
       String creationRepriseMonitrice = "CREATE TABLE IF NOT EXISTS RepriseMonitrice (\n"
    		   + " nomMonitrice text,\n"
    		   + " idReprise integer,\n"
    		   + " FOREIGN KEY (nomMonitrice) REFERENCES Monitrice(nom),\n"
    		   + " FOREIGN KEY (idReprise) REFERENCES Reprise(id)\n"
    		   + ");";
       
       try(Connection conn = DriverManager.getConnection(url);
    	   Statement stmt = conn.createStatement();
    		   ){
		           // create the tables
		    	   stmt.execute(creationModeleDeReprise);
		    	   stmt.execute(creationGroupe);
		    	   stmt.execute(creationLieu);
		    	   stmt.execute(creationMonitrice);
		    	   stmt.execute(creationJourFerie);
		    	   stmt.execute(creationReprise);
		    	   stmt.execute(creationCreneau);
		    	   stmt.execute(creationVacances);
		    	   stmt.execute(creationTreveHivernale);
		    	   stmt.execute(creationRepriseMonitrice);


	    	   }catch (SQLException e) {
	           System.out.println(e.getMessage());
       }
   }
}

