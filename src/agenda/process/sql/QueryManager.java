package agenda.process.sql;

import java.sql.Statement;
import java.util.ArrayList;

import agenda.process.object.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryManager {
	private static String url = "jdbc:sqlite:database/agenda.db"; 
	
	public QueryManager(){
		
	}
	
    /**
    * Connect to a sample database
    */
   public static void connect() {
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
   
   public static void createTables(){       
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
               + "  FOREIGN KEY (idMR) REFERENCES ModeleDeReprise(id) ON DELETE CASCADE\n"
               + ");";
       
       String creationLieu = "CREATE TABLE IF NOT EXISTS Lieu (\n"
    		   + "  id integer PRIMARY KEY, \n"
               + "	nom text \n"
               + ");";
       
       String creationMonitrice = "CREATE TABLE IF NOT EXISTS Monitrice (\n"
    		   + "  id integer PRIMARY KEY, \n"
               + "	nom text \n"
               + ");";
       
       String creationJourFerie = "CREATE TABLE IF NOT EXISTS JourFerie (\n"
               + "	date date PRIMARY KEY,\n"
               + ");";
       
       String creationReprise = "CREATE TABLE IF NOT EXISTS Reprise (\n"
               + "	id integer PRIMARY KEY,\n"
               + "	nom text, \n"
               + "  heureDebut integer,\n"
               + "  duree integer,\n"
               + "  idMR integer, \n"
               + "  idLieu integer,\n"
               + "  FOREIGN KEY  (idLieu) REFERENCES Lieu(id) ON DELETE CASCADE,\n"
               + "  FOREIGN KEY  (idMR) REFERENCES ModeleDeReprise(id) ON DELETE CASCADE\n"
               + ");";
       
       String creationCreneau = "CREATE TABLE IF NOT EXISTS Creneau (\n"
               + "	id integer PRIMARY KEY,\n"
               + "  date date ,\n"
               + "  heureDebut integer,\n"
               + "  duree integer,\n"
               + "  idMonitrice integer,\n"
               + "  FOREIGN KEY (idMonitrice) REFERENCES Monitrice(id) ON DELETE CASCADE\n"
               + ");";
       
       String creationVacances = "CREATE TABLE IF NOT EXISTS Vacances (\n"
               + "	nom text PRIMARY KEY,\n"
               + "  dateDebut date, \n"
               + "  dateFin date, \n"
               + ");";
       
       String creationTreveHivernale = "CREATE TABLE IF NOT EXISTS TreveHivernale (\n"
               + "  dateDebut date,\n" 
               + "  dateFin date ,\n" 
               + ");";
       
       String creationRepriseMonitrice = "CREATE TABLE IF NOT EXISTS RepriseMonitrice (\n"
    		   + " idMonitrice integer,\n"
    		   + " idReprise integer,\n"
    		   + " FOREIGN KEY (idMonitrice) REFERENCES Monitrice(id) ON DELETE CASCADE,\n"
    		   + " FOREIGN KEY (idReprise) REFERENCES Reprise(id) ON DELETE CASCADE\n"
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

//*******************************************************************************
// Requetes d'ajout	
//*******************************************************************************	
	

	public static void ajouterMonitricesAReprise(int idReprise, ArrayList<Monitrice> monitrices) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutRepriseMonitrice(conn);
		    ){
			for(int i = 0; i < monitrices.size(); i++){
				ps.setInt(1, idReprise);
				ps.setInt(2, monitrices.get(i).getId()); 
				ps.addBatch();
			}
			ps.executeBatch();
		}	
	}
	
	public static void ajouterReprisesAMonitrice(int idMonitrice, ArrayList<Reprise> reprises) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutRepriseMonitrice(conn);
		    ){
			for(int i = 0; i < reprises.size(); i++){
				ps.setInt(1, reprises.get(i).getId());
				ps.setInt(2, idMonitrice);
				ps.addBatch();
			}	
			ps.executeBatch();
		}	
	}
	
	public static void ajoutReprises(ArrayList<Reprise> reprises) throws SQLException{
		// on ajoute d'abord les reprises dans la table
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutReprise(conn);
		    ){
			for(int i = 0; i < reprises.size(); i++){
				ps.setInt(1,reprises.get(i).getId());
				ps.setString(2,reprises.get(i).getNom());
				ps.setInt(3,reprises.get(i).getHeureDebut());
				ps.setInt(4,reprises.get(i).getDuree());
				ps.setInt(5,reprises.get(i).getLieu().getId());
				ps.setDate(6,reprises.get(i).getDate());
				ps.addBatch();
			}	
			ps.executeBatch();
		}
		
		// on ajoute ensuite ses monitrices associées
		for(int i = 0; i < reprises.size(); i++){
			ajouterMonitricesAReprise(reprises.get(i).getId(), reprises.get(i).getMonitrices());	
		}
	}
	
	
	public static void ajoutGroupe(Groupe groupe) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutGroupe(conn);
			){
			ps.setString(1,groupe.getNom());
			ps.setBoolean(2,groupe.getIsVacances());
			ps.setBoolean(3,groupe.getIsTreveHivernale());
			ps.executeQuery();
		}	
	}
	
	public static void ajoutModeleDeReprise(ModeleDeReprise modeleDeReprise) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutModeleDeReprise(conn);
			){
			ps.setInt(1,modeleDeReprise.getId());
			ps.setString(2,modeleDeReprise.getNom());
			ps.executeQuery();
		}
	}
	
	public static void ajoutLieu(Lieu lieu) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutLieu(conn);
			){
			ps.setInt(1, lieu.getId());
			ps.setString(2,lieu.getNom());
			ps.executeQuery();
		}
	}
	
	public static void ajoutMonitrice(Monitrice monitrice) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutMonitrice(conn);
			){
			ps.setInt(1, monitrice.getId());
			ps.setString(2,monitrice.getNom());
			ps.executeQuery();
		}
	}
	
	public static void ajoutCreneau(ArrayList<Creneau> creneaux) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutCreneau(conn);
		    ){
			for(int i = 0; i < creneaux.size(); i++){
				ps.setInt(1,creneaux.get(i).getId());
				ps.setInt(2,creneaux.get(i).getHeureDebut());
				ps.setDate(3,creneaux.get(i).getDate());
				ps.setInt(4,creneaux.get(i).getDuree());
				ps.setInt(5,creneaux.get(i).getIdMonitrice());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}
	
	public static void ajoutJourFerie(JourFerie jourFerie) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutJourFerie(conn);
		    ){
			ps.setDate(1,jourFerie.getDate());
			ps.executeQuery();
		}
	}
	
	public static void ajoutVacances(Vacances vacances) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutVacances(conn);
			){    
			ps.setString(1,vacances.getNom());
			ps.setDate(2,vacances.getDateDebut());
			ps.setDate(3,vacances.getDateFin());
			ps.executeQuery();
		}
	}
	
	public static void ajoutTreveHivernale(TreveHivernale treveHivernale) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.ajoutTreveHivernale(conn);
			){   
			ps.setDate(1,treveHivernale.getDateDebut());
			ps.setDate(2,treveHivernale.getDateFin());
			ps.executeQuery();
		}
	}
	
//*******************************************************************************
// Requetes de suppression
//*******************************************************************************
	
	public static void suppressionReprise(int id) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionReprise(conn);
			){   
			ps.setInt(1,id);
			ps.executeQuery();
		}
	}
	
	public static void suppressionGroupe(Groupe groupe) throws SQLException{
		// pour supprimer un groupe, on supprime son modele de reprise
		suppressionModeleDeReprise(groupe.getModeleDeReprise().getId());
	}
	
	public static void suppressionModeleDeReprise(int id) throws SQLException{
		// Les reprises associees  et le groupe éventuellement associe sont détruits en cascade
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionModeleDeReprise(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}
	
	public static void suppressionLieu(int id) throws SQLException{
		// Les reprises associées sont detruites en cascade
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionLieu(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}
	
	/**
	 * Supprime une monitrice étant donné son id
	 * choix = 0 : on supprime toutes les reprises associées à la monitrice
	 * choix = 1 : on supprime toutes les reprises associées uniquement à cette monitrice
	 * choix = 2 : on ne supprime aucune reprise (une reprise peut se retrouver sans monitrice)
	 * @param id
	 * @param choice
	 * @throws SQLException
	 */
	public static void suppressionMonitrice(int id, int choix) throws SQLException, Exception{
		// Les creneaux associes sont détruits en cascade
		if (choix == 0){
			supprimerReprisesDeMonitrice(id);
		}else if(choix == 1){
			supprimerReprisesDeMonitriceUnique(id);
		}else if(choix == 2){
			supprimerRepriseMonitrice(id);
		}else{
			throw new Exception("choix non valide");
		}
	}
	
	public static void supprimerReprisesDeMonitrice(int id) throws SQLException, Exception{
		// Les relations RepriseMonitrice associées sont détruites en cascade
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.supprimerReprisesDeMonitrice(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}

	
	public static void supprimerReprisesDeMonitriceUnique(int id) throws SQLException, Exception{
		// Les relations RepriseMonitrice sont détruites en cascade
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.supprimerReprisesDeMonitriceUnique(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}
	
	public static void supprimerRepriseMonitrice(int id) throws SQLException, Exception{
	try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.supprimerRepriseMonitrice(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}
	
	public static void suppressionCreneau(int id) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionCreneau(conn);
			){   
			ps.setInt(1, id);
			ps.executeQuery();
		}
	}
	
	public static void suppressionJourFerie(Date date) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionJourFerie(conn);
			){   
			ps.setDate(1, date);
			ps.executeQuery();
		}
	}
	
	public static void suppressionVacances(String nom) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.suppressionVacances(conn);
			){   
			ps.setString(1, nom);
			ps.executeQuery();
		}
	}
	

//*******************************************************************************
//Requetes de modification
//*******************************************************************************
	
	public static void modificationReprise(Reprise reprise) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationReprise(conn);
			){   
			ps.setString(1,reprise.getNom());
			ps.setInt(2,reprise.getHeureDebut());
			ps.setInt(3,reprise.getDuree());
			ps.setInt(4,reprise.getLieu().getId());
			ps.setDate(5,reprise.getDate());
			ps.setInt(6,reprise.getId());
			ps.executeQuery();
		}
	}
	
	public static void modificationGroupe(Groupe groupe, String ancienNom) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationGroupe(conn);
			){
			ps.setString(1,ancienNom);
			ps.setBoolean(2,groupe.getIsVacances());
			ps.setBoolean(3,groupe.getIsTreveHivernale());
			ps.setString(4,groupe.getNom());
			ps.executeQuery();
		}
	}
	
	public static void modificationModeleDeReprise(ModeleDeReprise modeleDeReprise) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationModeleDeReprise(conn);
			){   
				ps.setString(1,modeleDeReprise.getNom());
				ps.setInt(2,modeleDeReprise.getId());
				ps.executeQuery();
		}
	}
	

	public static void modificationLieu(Lieu lieu) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationLieu(conn);
			){   
			ps.setString(1,lieu.getNom());
			ps.setInt(2,lieu.getId());
			ps.executeQuery();
		}
	}
	
	public static void modificationMonitrice(Monitrice monitrice) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationMonitrice(conn);
			){   
			ps.setString(1,monitrice.getNom());
			ps.setInt(2,monitrice.getId());
			ps.executeQuery();
		}
	}
	
	public static void modificationCreneau(Creneau creneau) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationCreneau(conn);
			){   
			ps.setInt(1,creneau.getHeureDebut());
			ps.setInt(2,creneau.getDuree());
			ps.setDate(3,creneau.getDate());
			ps.setInt(4,creneau.getId());
			ps.executeQuery();
		}
	}
	
	public static void modificationTreveHivernale(TreveHivernale treveHivernale) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.modificationTreveHivernale(conn);
			){   
			ps.setDate(1,treveHivernale.getDateDebut());
			ps.setDate(2,treveHivernale.getDateFin());
			ps.executeQuery();
		}
	}
	
	

//*******************************************************************************
//Requetes de remontee d'infos
//*******************************************************************************
	
	public static ArrayList<Groupe> selectListeGroupe() throws SQLException{
		ArrayList<Groupe> groupes = new ArrayList<Groupe>();
		String nomGroupe;
		boolean isVacances;
		boolean isTreve;
		int idMR;
		String nomMR;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeGroupe(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				nomGroupe = rs.getString(1);
				isVacances = rs.getBoolean(2);
				isTreve = rs.getBoolean(3);
				idMR = rs.getInt(4);
				nomMR = rs.getString(5);
				groupes.add(new Groupe(nomGroupe, isVacances, isTreve, new ModeleDeReprise(idMR, nomMR)));
			}
		}
		return groupes;
	}
	
	public static ArrayList<ModeleDeReprise> selectListeMRParticulier() throws SQLException{
		ArrayList<ModeleDeReprise> modeleDeReprises= new ArrayList<ModeleDeReprise>();
		int id;
		String nom;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeMRParticulier(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
				nom = rs.getString(2);
				modeleDeReprises.add(new ModeleDeReprise(id, nom));
			}
		}
		return modeleDeReprises;
	}
	
	public static ArrayList<Monitrice> selectListeMonitrice() throws SQLException{
		ArrayList<Monitrice> monitrices = new ArrayList<Monitrice>();
		int id;
		String nom;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeMonitrice(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
				nom = rs.getString(2);
				monitrices.add(new Monitrice(id, nom));
			}
		}
		return monitrices;
	}
	
	public static ArrayList<Monitrice> selectMonitricesDeReprise(int idReprise) throws SQLException{
		ArrayList<Monitrice> monitrices = new ArrayList<Monitrice>();
		int id;
		String nom;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectMonitricesDeReprise(conn);
			){
			ps.setInt(1, idReprise);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
				nom = rs.getString(2);
				monitrices.add(new Monitrice(id, nom));
			}
		}
		return monitrices;
	}
	
	public static ArrayList<Lieu> selectListeLieu() throws SQLException{
		ArrayList<Lieu> lieux = new ArrayList<Lieu>();
		int id;
		String nom;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeLieu(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
				nom = rs.getString(2);
				lieux.add(new Lieu(id, nom));
			}
		}
		return lieux;
	}
	
	public static ArrayList<Creneau> selectCreneaux(int idMonitrice) throws SQLException{
		ArrayList<Creneau> creneaux = new ArrayList<Creneau>();
		int id;
		Date date;
		int heureDebut;
		int duree;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeLieu(conn);
			){
			ps.setInt(1, idMonitrice);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getInt(1);
				date = rs.getDate(2);
				heureDebut = rs.getInt(3);
				duree = rs.getInt(4);
				creneaux.add(new Creneau(id, date, heureDebut, duree));
			}
		}
		return creneaux;
	}
	
	public static JoursSpeciaux selectJoursSpeciaux() throws SQLException{
		TreveHivernale treveHivernale = selectTreveHivernale();
		ArrayList<Vacances> vacances = selectListeVacances();
		ArrayList<JourFerie> joursFeries = selectListeJoursFeries();
		return new JoursSpeciaux(treveHivernale, vacances, joursFeries);
	}
	
	public static ArrayList<Vacances> selectListeVacances() throws SQLException{
		ArrayList<Vacances> vacances = new ArrayList<Vacances>();
		String nom;
		Date dateDebut;
		Date dateFin;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeVacances(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				nom = rs.getString(1);
				dateDebut = rs.getDate(2);
				dateFin = rs.getDate(3);
				vacances.add(new Vacances(nom, dateDebut, dateFin));
			}
		}
		return vacances;		
	}
	
	public static ArrayList<JourFerie> selectListeJoursFeries() throws SQLException{
		ArrayList<JourFerie> joursFeries = new ArrayList<JourFerie>();
		Date date;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectListeJoursFeries(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				date = rs.getDate(1);
				joursFeries.add(new JourFerie(date));
			}
		}
		return joursFeries;		
	}
	
	public static TreveHivernale selectTreveHivernale() throws SQLException{
		Date dateDebut = null;
		Date dateFin = null;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectTreveHivernale(conn);
			){
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				dateDebut = rs.getDate(1);
				dateFin= rs.getDate(1);
			}
		}
		return new TreveHivernale(dateDebut, dateFin);
	}
	
	public static ArrayList<Reprise> selectReprisesDeModele(int idMR) throws SQLException{
		ArrayList<Reprise> reprises = new ArrayList<Reprise>();
		int idReprise;
		String nomReprise;
		Date date;
		int heureDebut;
		int duree;
		int idLieu;
		String nomLieu;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectReprisesDeModele(conn);
			){
			ps.setInt(1,  idMR);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				idReprise = rs.getInt(1);
				nomReprise = rs.getString(2);
				date = rs.getDate(3);
				heureDebut = rs.getInt(4);
				duree = rs.getInt(5);
				idLieu = rs.getInt(6);
				nomLieu = rs.getString(7);
				reprises.add(new Reprise(idReprise, nomReprise, date, heureDebut, duree, idMR, new Lieu(idLieu, nomLieu)));
			}
		}
		return reprises;
	}
	
	public static ArrayList<Reprise> selectReprisesDeSemaine(String annee, String semaine) throws SQLException{
		ArrayList<Reprise> reprises = new ArrayList<Reprise>();
		int idReprise;
		String nomReprise;
		Date date;
		int heureDebut;
		int duree;
		int idMR;
		int idLieu;
		String nomLieu;
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.selectReprisesDeSemaine(conn);
			){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				idReprise = rs.getInt(1);
				nomReprise = rs.getString(2);
				date = rs.getDate(3);
				heureDebut = rs.getInt(4);
				duree = rs.getInt(5);
				idMR = rs.getInt(6);
				idLieu = rs.getInt(7);
				nomLieu = rs.getString(8);
				reprises.add(new Reprise(idReprise, nomReprise, date, heureDebut, duree, idMR, new Lieu(idLieu, nomLieu)));
			}
		}
		return reprises;
	}
	
	
//*******************************************************************************
//Requetes de test
//*******************************************************************************
	public static void testCreneauMonitrice(int idMonitrice, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.testCreneauMonitrice(conn);
			){
			ps.setInt(1,idMonitrice);
			ps.setDate(2,date);
			ps.setInt(3,heureDebut);
			ps.setInt(4, heureFin);
			ResultSet rs = ps.executeQuery();
		}
	}
	
	public static void testConflitLieu(int idLieu, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.testConflitLieu(conn);
			){
			ps.setInt(1,idLieu);
			ps.setDate(2,date);
			ps.setInt(3,heureDebut);
			ps.setInt(4, heureFin);
			ResultSet rs = ps.executeQuery();
		}
	}
	
	public static void testConflitMonitrice(int idMonitrice, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = DriverManager.getConnection(url);
			PreparedStatement ps = QueryBuilder.testConflitMonitrice(conn);
			){
			ps.setInt(1,idMonitrice);
			ps.setDate(2,date);
			ps.setInt(3,heureDebut);
			ps.setInt(4, heureFin);
			ResultSet rs = ps.executeQuery();
		}
	}	


}


