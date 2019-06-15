package agenda.process.sql;

import java.sql.Statement;
import java.util.ArrayList;

import org.sqlite.SQLiteConfig;

import agenda.process.object.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryManager {
	//private static String url = "jdbc:sqlite:database/agenda.db"; 
	private static String url = "jdbc:sqlite:" + System.getenv("LOCALAPPDATA") + "/Agenda/database/agenda.db"; 
	public static final String DRIVER = "org.sqlite.JDBC";
	public static Connection getConnection() throws SQLException {
		//Class.forName(DRIVER);
		Connection conn = null;
		SQLiteConfig config = new SQLiteConfig();
		config.enforceForeignKeys(true);
		conn = DriverManager.getConnection(url,config.toProperties());

	    return conn;
	}

	/**
	 * Connect to a sample database
	 */
	public static void connect() {
		Connection conn = null;
		// create a connection to the database
		try {
			conn = getConnection();
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
		System.out.println("Connection to SQLite has been established.");
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
				+ "	date date PRIMARY KEY\n"
				+ ");";

		String creationReprise = "CREATE TABLE IF NOT EXISTS Reprise (\n"
				+ "	id integer PRIMARY KEY,\n"
				+ "	nom text, \n"
				+ "  heureDebut integer,\n"
				+ "  heureFin integer,\n"
				+ "  idMR integer, \n"
				+ "  idLieu integer,\n"
				+ "  date date,\n"
				+ "  FOREIGN KEY  (idLieu) REFERENCES Lieu(id) ON DELETE CASCADE,\n"
				+ "  FOREIGN KEY  (idMR) REFERENCES ModeleDeReprise(id) ON DELETE CASCADE\n"
				+ ");";

		String creationCreneau = "CREATE TABLE IF NOT EXISTS Creneau (\n"
				+ "	id integer PRIMARY KEY,\n"
				+ "  date date ,\n"
				+ "  heureDebut integer,\n"
				+ "  heureFin integer,\n"
				+ "  idMonitrice integer,\n"
				+ "  FOREIGN KEY (idMonitrice) REFERENCES Monitrice(id) ON DELETE CASCADE\n"
				+ ");";

		String creationVacances = "CREATE TABLE IF NOT EXISTS Vacances (\n"
				+ "	nom text PRIMARY KEY,\n"
				+ "  dateDebut date, \n"
				+ "  dateFin date \n"
				+ ");";

		String creationTreveHivernale = "CREATE TABLE IF NOT EXISTS TreveHivernale (\n"
				+ "  dateDebut date,\n" 
				+ "  dateFin date\n" 
				+ ");";

		String creationRepriseMonitrice = "CREATE TABLE IF NOT EXISTS RepriseMonitrice (\n"
				+ " idMonitrice integer,\n"
				+ " idReprise integer,\n"
				+ " FOREIGN KEY (idMonitrice) REFERENCES Monitrice(id) ON DELETE CASCADE,\n"
				+ " FOREIGN KEY (idReprise) REFERENCES Reprise(id) ON DELETE CASCADE\n"
				+ ");";

		try(Connection conn = getConnection();
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
			System.exit(0);
		}
	}
	
	public static void createTreveHivernale(){
		String query = "INSERT INTO TreveHivernale(dateDebut, dateFin) \n"
			+ "VALUES (?,?);";
		try(Connection conn = getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				){
			ps.setDate(1,new Date(0));
			ps.setDate(2,new Date(0));
			ps.executeUpdate();
		}catch(SQLException e){
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}

	//*******************************************************************************
	// Requetes d'ajout	
	//*******************************************************************************	


	public static void ajoutMonitricesAReprise(long idReprise, ArrayList<Monitrice> monitrices) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutRepriseMonitrice(conn);
				){
			for(int i = 0; i < monitrices.size(); i++){
				ps.setLong(1, idReprise);
				ps.setLong(2, monitrices.get(i).getId()); 
				ps.addBatch();
			}
			ps.executeBatch();
		}	
	}

	public static void ajoutReprisesAMonitrice(long idMonitrice, ArrayList<Reprise> reprises) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutRepriseMonitrice(conn);
				){
			for(int i = 0; i < reprises.size(); i++){
				ps.setLong(1, reprises.get(i).getId());
				ps.setLong(2, idMonitrice);
				ps.addBatch();
			}	
			ps.executeBatch();
		}	
	}

	public static void ajoutReprises(ArrayList<Reprise> reprises) throws SQLException{
		// on ajoute d'abord les reprises dans la table
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutReprise(conn);
				){
			for(int i = 0; i < reprises.size(); i++){
				ps.setLong(1,reprises.get(i).getId());
				ps.setString(2,reprises.get(i).getNom());
				ps.setInt(3,reprises.get(i).getHeureDebut());
				ps.setInt(4,reprises.get(i).getHeureFin());
				ps.setLong(5, reprises.get(i).getIdMR());
				ps.setLong(6,reprises.get(i).getLieu().getId());
				ps.setDate(7,reprises.get(i).getSQLDate());
				ps.addBatch();
			}	
			ps.executeBatch();
		}

		// on ajoute ensuite ses monitrices associées
		for(int i = 0; i < reprises.size(); i++){
			ajoutMonitricesAReprise(reprises.get(i).getId(), reprises.get(i).getMonitrices());	
		}
	}


	public static void ajoutGroupe(Groupe groupe) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutGroupe(conn);
				){
			ps.setString(1,groupe.getNom());
			ps.setBoolean(2,groupe.getIsVacances());
			ps.setBoolean(3,groupe.getIsTreveHivernale());
			ps.setLong(4, groupe.getModeleDeReprise().getId());
			ps.executeUpdate();
		}	
	}

	public static void ajoutModeleDeReprise(ModeleDeReprise modeleDeReprise) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutModeleDeReprise(conn);
				){
			ps.setLong(1,modeleDeReprise.getId());
			ps.setString(2,modeleDeReprise.getNom());
			ps.executeUpdate();
		}
	}

	public static void ajoutLieu(Lieu lieu) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutLieu(conn);
				){
			ps.setLong(1, lieu.getId());
			ps.setString(2,lieu.getNom());
			ps.executeUpdate();
		}
	}

	public static void ajoutMonitrice(Monitrice monitrice) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutMonitrice(conn);
				){
			ps.setLong(1, monitrice.getId());
			ps.setString(2,monitrice.getNom());
			ps.executeUpdate();
		}
	}

	public static void ajoutCreneau(ArrayList<Creneau> creneaux) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutCreneau(conn);
				){
			for(int i = 0; i < creneaux.size(); i++){
				ps.setLong(1,creneaux.get(i).getId());
				ps.setInt(2,creneaux.get(i).getHeureDebut());
				ps.setDate(3,creneaux.get(i).getSQLDate());
				ps.setInt(4,creneaux.get(i).getHeureFin());
				ps.setLong(5,creneaux.get(i).getIdMonitrice());
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	public static void ajoutJourFerie(JourFerie jourFerie) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutJourFerie(conn);
				){
			ps.setDate(1,jourFerie.getSQLDate());
			ps.executeUpdate();
		}
	}

	public static void ajoutVacances(Vacances vacances) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutVacances(conn);
				){    
			ps.setString(1,vacances.getNom());
			ps.setDate(2,vacances.getSQLDateDebut());
			ps.setDate(3,vacances.getSQLDateFin());
			ps.executeUpdate();
		}
	}

	public static void ajoutTreveHivernale(TreveHivernale treveHivernale) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.ajoutTreveHivernale(conn);
				){   
			ps.setDate(1,treveHivernale.getSQLDateDebut());
			ps.setDate(2,treveHivernale.getSQLDateFin());
			ps.executeUpdate();
		}
	}

	//*******************************************************************************
	// Requetes de suppression
	//*******************************************************************************

	public static void suppressionReprises(ArrayList<Reprise> reprises) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionReprise(conn);
				){
			for(int i = 0; i < reprises.size(); i++){
				ps.setLong(1,reprises.get(i).getId());
				ps.executeQuery();
				ps.addBatch();
			}
			ps.executeBatch();
		}
	}

	public static void suppressionGroupe(Groupe groupe) throws SQLException{
		// pour supprimer un groupe, on supprime son modele de reprise
		suppressionModeleDeReprise(groupe.getModeleDeReprise());
	}

	public static void suppressionModeleDeReprise(ModeleDeReprise modeleDeReprise) throws SQLException{
		// Les reprises associees  et le groupe éventuellement associe sont détruits en cascade
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionModeleDeReprise(conn);
				){   
			ps.setLong(1, modeleDeReprise.getId());
			ps.executeUpdate();
		}
	}

	public static void suppressionLieu(Lieu lieu) throws SQLException{
		// Les reprises associées sont detruites en cascade
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionLieu(conn);
				){   
			ps.setLong(1, lieu.getId());
			ps.executeUpdate();
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
	public static void suppressionMonitrice(Monitrice monitrice, int choix) throws SQLException, Exception{
		// Les creneaux associes sont détruits en cascade
		if (choix == 0){
			supprimerReprisesDeMonitrice(monitrice);
		}else if(choix == 1){
			supprimerReprisesDeMonitriceUnique(monitrice);
		}else if(choix == 2){
			supprimerRepriseMonitriceDeMonitrice(monitrice);
		}else{
			throw new Exception("choix non valide");
		}
		//on supprime maintenant la monitrice
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionMonitrice(conn);
				){   
			ps.setLong(1, monitrice.getId());
			ps.executeUpdate();
		}
	}

	public static void supprimerReprisesDeMonitrice(Monitrice monitrice) throws SQLException{
		// Les relations RepriseMonitrice associées sont détruites en cascade
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.supprimerReprisesDeMonitrice(conn);
				){   
			ps.setLong(1, monitrice.getId());
			ps.executeUpdate();
		}
	}


	public static void supprimerReprisesDeMonitriceUnique(Monitrice monitrice) throws SQLException{
		// Les relations RepriseMonitrice sont détruites en cascade
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.supprimerReprisesDeMonitriceUnique(conn);
				){   
			ps.setLong(1, monitrice.getId());
			ps.executeUpdate();
		}
	}

	public static void supprimerRepriseMonitriceDeMonitrice(Monitrice monitrice) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.supprimerRepriseMonitriceDeMonitrice(conn);
				){   
			ps.setLong(1, monitrice.getId());
			ps.executeUpdate();
		}
	}
	
	public static void supprimerRepriseMonitriceDeReprise(Reprise reprise) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.supprimerRepriseMonitriceDeReprise(conn);
				){   
			ps.setLong(1, reprise.getId());
			ps.executeUpdate();
		}
	}

	public static void suppressionCreneau(Creneau creneau) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionCreneau(conn);
				){   
			ps.setLong(1, creneau.getId());
			ps.executeUpdate();
		}
	}

	public static void suppressionJourFerie(JourFerie jourFerie) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionJourFerie(conn);
				){   
			ps.setDate(1, jourFerie.getSQLDate());
			ps.executeUpdate();
		}
	}

	public static void suppressionVacances(Vacances vacances) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.suppressionVacances(conn);
				){   
			ps.setString(1, vacances.getNom());
			ps.executeUpdate();
		}
	}


	//*******************************************************************************
	//Requetes de modification
	//*******************************************************************************

	public static void modificationReprise(Reprise reprise) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationReprise(conn);
				){   
			ps.setString(1,reprise.getNom());
			ps.setInt(2,reprise.getHeureDebut());
			ps.setInt(3,reprise.getHeureFin());
			ps.setLong(4,reprise.getLieu().getId());
			ps.setDate(5,reprise.getSQLDate());
			ps.setLong(6,reprise.getId());
			ps.executeUpdate();
		}
		//on met aussi à jour les monitrices
		supprimerRepriseMonitriceDeReprise(reprise);
		ajoutMonitricesAReprise(reprise.getId(), reprise.getMonitrices());	

	}

	public static void modificationGroupe(Groupe groupe, String ancienNom) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationGroupe(conn);
				){
			ps.setString(1,ancienNom);
			ps.setBoolean(2,groupe.getIsVacances());
			ps.setBoolean(3,groupe.getIsTreveHivernale());
			ps.setString(4,groupe.getNom());
			ps.executeUpdate();
		}
	}

	public static void modificationModeleDeReprise(ModeleDeReprise modeleDeReprise) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationModeleDeReprise(conn);
				){   
			ps.setString(1,modeleDeReprise.getNom());
			ps.setLong(2,modeleDeReprise.getId());
			ps.executeUpdate();
		}
	}


	public static void modificationLieu(Lieu lieu) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationLieu(conn);
				){   
			ps.setString(1,lieu.getNom());
			ps.setLong(2,lieu.getId());
			ps.executeUpdate();
		}
	}

	public static void modificationMonitrice(Monitrice monitrice) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationMonitrice(conn);
				){   
			ps.setString(1,monitrice.getNom());
			ps.setLong(2,monitrice.getId());
			ps.executeUpdate();
		}
	}

	public static void modificationCreneau(Creneau creneau) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationCreneau(conn);
				){   
			ps.setInt(1,creneau.getHeureDebut());
			ps.setInt(2,creneau.getHeureFin());
			ps.setDate(3,creneau.getSQLDate());
			ps.setLong(4,creneau.getId());
			ps.executeUpdate();
		}
	}

	public static void modificationTreveHivernale(TreveHivernale treveHivernale) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.modificationTreveHivernale(conn);
				){   
			ps.setDate(1,treveHivernale.getSQLDateDebut());
			ps.setDate(2,treveHivernale.getSQLDateFin());
			ps.executeUpdate();
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
		long idMR;
		String nomMR;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeGroupe(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				nomGroupe = rs.getString(1);
				isVacances = rs.getBoolean(2);
				isTreve = rs.getBoolean(3);
				idMR = rs.getLong(4);
				nomMR = rs.getString(5);
				groupes.add(new Groupe(nomGroupe, isVacances, isTreve, new ModeleDeReprise(idMR, nomMR)));
			}
		}
		return groupes;
	}

	public static ArrayList<ModeleDeReprise> selectListeMRParticulier() throws SQLException{
		ArrayList<ModeleDeReprise> modeleDeReprises= new ArrayList<ModeleDeReprise>();
		long id;
		String nom;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeMRParticulier(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
				nom = rs.getString(2);
				modeleDeReprises.add(new ModeleDeReprise(id, nom));
			}
		}
		return modeleDeReprises;
	}

	public static ArrayList<Monitrice> selectListeMonitrice() throws SQLException{
		ArrayList<Monitrice> monitrices = new ArrayList<Monitrice>();
		long id;
		String nom;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeMonitrice(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
				nom = rs.getString(2);
				monitrices.add(new Monitrice(id, nom));
			}
		}
		return monitrices;
	}

	public static ArrayList<Monitrice> selectMonitricesDeReprise(long idReprise) throws SQLException{
		ArrayList<Monitrice> monitrices = new ArrayList<Monitrice>();
		long id;
		String nom;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectMonitricesDeReprise(conn);
				){
			ps.setLong(1, idReprise);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
				nom = rs.getString(2);
				monitrices.add(new Monitrice(id, nom));
			}
		}
		return monitrices;
	}

	public static ArrayList<Lieu> selectListeLieu() throws SQLException{
		ArrayList<Lieu> lieux = new ArrayList<Lieu>();
		long id;
		String nom;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeLieu(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
				nom = rs.getString(2);
				lieux.add(new Lieu(id, nom));
			}
		}
		return lieux;
	}

	public static ArrayList<Creneau> selectCreneaux(long idMonitrice) throws SQLException{
		ArrayList<Creneau> creneaux = new ArrayList<Creneau>();
		long id;
		Date date;
		int heureDebut;
		int heureFin;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeLieu(conn);
				){
			ps.setLong(1, idMonitrice);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				id = rs.getLong(1);
				date = rs.getDate(2);
				heureDebut = rs.getInt(3);
				heureFin = rs.getInt(4);
				creneaux.add(new Creneau(id, idMonitrice, date.toLocalDate(), heureDebut, heureFin));
			}
		}
		return creneaux;
	}

	public static ArrayList<Vacances> selectListeVacances() throws SQLException{
		ArrayList<Vacances> vacances = new ArrayList<Vacances>();
		String nom;
		Date dateDebut;
		Date dateFin;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeVacances(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				nom = rs.getString(1);
				dateDebut = rs.getDate(2);
				dateFin = rs.getDate(3);
				vacances.add(new Vacances(nom, dateDebut.toLocalDate(), dateFin.toLocalDate()));
			}
		}
		return vacances;		
	}

	public static ArrayList<JourFerie> selectListeJoursFeries() throws SQLException{
		ArrayList<JourFerie> joursFeries = new ArrayList<JourFerie>();
		Date date;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectListeJoursFeries(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				date = rs.getDate(1);
				joursFeries.add(new JourFerie(date.toLocalDate()));
			}
		}
		return joursFeries;		
	}

	public static TreveHivernale selectTreveHivernale() throws SQLException{
		Date dateDebut = null;
		Date dateFin = null;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectTreveHivernale(conn);
				){
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				dateDebut = rs.getDate(1);
				dateFin= rs.getDate(2);
			}
		}
		return new TreveHivernale(dateDebut.toLocalDate(), dateFin.toLocalDate());
	}

	public static ArrayList<Reprise> selectReprisesDeModele(long idMR) throws SQLException{
		ArrayList<Reprise> reprises = new ArrayList<Reprise>();
		long idReprise;
		String nomReprise;
		Date date;
		int heureDebut;
		int heureFin;
		long idLieu;
		String nomLieu;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectReprisesDeModele(conn);
				){
			ps.setLong(1, idMR);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				idReprise = rs.getLong(1);
				nomReprise = rs.getString(2);
				date = rs.getDate(3);
				heureDebut = rs.getInt(4);
				heureFin = rs.getInt(5);
				idLieu = rs.getLong(6);
				nomLieu = rs.getString(7);
				reprises.add(new Reprise(idReprise, nomReprise, date.toLocalDate(), heureDebut, heureFin, idMR, new Lieu(idLieu, nomLieu)));
			}
		}
		return reprises;
	}

	public static ArrayList<Reprise> selectReprisesDeSemaine(String annee, String semaine) throws SQLException{
		ArrayList<Reprise> reprises = new ArrayList<Reprise>();
		long idReprise;
		String nomReprise;
		Date date;
		int heureDebut;
		int heureFin;
		long idMR;
		long idLieu;
		String nomLieu;
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.selectReprisesDeSemaine(conn);
				){
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				idReprise = rs.getLong(1);
				nomReprise = rs.getString(2);
				date = rs.getDate(3);
				heureDebut = rs.getInt(4);
				heureFin = rs.getInt(5);
				idMR = rs.getLong(6);
				idLieu = rs.getLong(7);
				nomLieu = rs.getString(8);
				reprises.add(new Reprise(idReprise, nomReprise, date.toLocalDate(), heureDebut, heureFin, idMR, new Lieu(idLieu, nomLieu)));
			}
		}
		return reprises;
	}


	//*******************************************************************************
	//Requetes de test
	//*******************************************************************************
	public static boolean testCreneauMonitrice(long idMonitrice, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.testCreneauMonitrice(conn);
				){
			ps.setLong(1,idMonitrice);
			ps.setDate(2,date);
			ps.setInt(3,heureFin);
			ps.setInt(4, heureDebut);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	public static boolean testConflitLieu(long idLieu, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.testConflitLieu(conn);
				){
			ps.setLong(1,idLieu);
			ps.setDate(2,date);
			ps.setInt(3,heureFin);
			ps.setInt(4, heureDebut);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}

	public static boolean testConflitMonitrice(long idMonitrice, Date date, int heureDebut, int heureFin) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement ps = QueryBuilder.testConflitMonitrice(conn);
				){
			ps.setLong(1,idMonitrice);
			ps.setDate(2,date);
			ps.setInt(3,heureFin);
			ps.setInt(4, heureDebut);
			ResultSet rs = ps.executeQuery();
			return rs.next();
		}
	}	


}


