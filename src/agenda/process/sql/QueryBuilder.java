package agenda.process.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import agenda.process.object.*;

public class QueryBuilder {
	
	// insertions
	private static String ajoutReprise = 
			"INSERT INTO Reprise(id, nom, heureDebut, duree, idLieu, date) \n"
			+ "VALUES (?,?,?,?,?,?);";
	private static String ajoutGroupe =
			"INSERT INTO Groupe(nom, isVacances, isTreve, idMR) \n"
			+ "VALUES (?,?,?,?);";
	private static String ajoutModeleDeReprise =
			"INSERT INTO ModeleDeReprise(id, nom) \n"
			+ "VALUES (?,?);";
	private static String ajoutLieu =
			"INSERT INTO Lieu(id, nom) \n"
			+ "VALUES (?,?);";
	private static String ajoutMonitrice =
			"INSERT INTO Monitrice(id, nom) \n"
			+ "VALUES (?,?);";
	private static String ajoutCreneau =
			"INSERT INTO Creneau(id, heureDebut, duree, date, idMonitrice) \n"
			+ "VALUES (?,?,?,?,?);";
	private static String ajoutJourFerie =
			"INSERT INTO JourFerie(date) \n"
			+ "VALUES (?);";
	private static String ajoutVacances =
			"INSERT INTO Vacances(nom, dateDebut, dateFin) \n"
			+ "VALUES (?,?,?);";
	private static String ajoutTreveHivernale =
			"INSERT INTO TreveHivernale(dateDebut, dateFin) \n"
			+ "VALUES (?,?);";
	private static String ajoutRepriseMonitrice=
			"INSERT INTO RepriseMonitrice(idMonitrice, idReprise) \n"
			+ "VALUES (?,?);";
	
	// suppressions
	private static String suppressionReprise = 
			"DELETE FROM Reprise \n"
			+ "WHERE id = ?";
	private static String suppressionGroupe = 
			"DELETE FROM Groupe \n"
			+ "WHERE nom = ?";
	private static String suppressionModeleDeReprise = 
			"DELETE FROM ModeleDeReprise \n"
			+ "WHERE id = ?";
	private static String suppressionLieu = 
			"DELETE FROM Lieu \n"
			+ "WHERE id = ?";
	private static String suppressionMonitrice = 
			"DELETE FROM Monitrice \n"
			+ "WHERE id = ?";
	private static String suppressionCreneau = 
			"DELETE FROM Creneau \n"
			+ "WHERE id = ?";
	private static String suppressionJourFerie = 
			"DELETE FROM JourFerie \n"
			+ "WHERE date = ?";
	private static String suppressionVacances = 
			"DELETE FROM Vacances\n"
			+ "WHERE nom = ?";

	//modification
	private static String modificationReprise = 
			"UPDATE Reprise \n"
			+ "SET nom = ?, heureDebut = ?, duree = ? , idLieu = ?, date = ? \n"
			+ "WHERE id = ?";
	private static String modificationGroupe = 
			"UPDATE Groupe \n"
			+ "SET nom = ?, isVacances = ?, isTreve = ? \n"
			+ "WHERE nom = ?";
	private static String modificationModeleDeReprise = 
			"UPDATE ModeleDeReprise \n"
			+ "SET  nom = ? \n"
			+ "WHERE id = ?";
	private static String modificationLieu = 
			"UPDATE Lieu \n"
			+ "SET nom = ?\n"
			+ "WHERE nom = ?";
	private static String modificationMonitrice = 
			"UPDATE Monitrice \n"
			+ "SET \n"
			+ "WHERE nom = ?";
	private static String modificationCreneau = 
			"UPDATE Creneau \n"
			+ "SET \n"
			+ "WHERE id = ?";
	private static String modificationJourFerie = 
			"UPDATE JourFerie \n"
			+ "SET \n"
			+ "WHERE date = ?";
	private static String modificationVacances = 
			"UPDATE Vacances\n"
			+ "SET \n"
			+ "WHERE nom = ?";
	
	
//*******************************************************************************
// Requetes d'ajout	
//*******************************************************************************	
	
	public static void ajoutReprise(Connection conn, PreparedStatement ps, Reprise reprise) throws SQLException{
		ps = conn.prepareStatement(ajoutReprise);
		ps.setInt(1,reprise.getId());
		ps.setString(2,reprise.getNom());
		ps.setInt(3,reprise.getHeureDebut());
		ps.setInt(4,reprise.getDuree());
		ps.setInt(5,reprise.getIdLieu());
		ps.setDate(6,reprise.getDate());
	}
	
	public static void ajoutGroupe(Connection conn, PreparedStatement ps, Groupe groupe) throws SQLException{
		ps = conn.prepareStatement(ajoutGroupe);
		ps.setString(1,groupe.getNom());
		ps.setBoolean(2,groupe.getIsVacances());
		ps.setBoolean(3,groupe.getIsTreveHivernale());
	}
	
	public static void ajoutModeleDeReprise(Connection conn, PreparedStatement ps, ModeleDeReprise modeleDeReprise) throws SQLException{
		ps = conn.prepareStatement(ajoutModeleDeReprise);
		ps.setInt(1,modeleDeReprise.getId());
		ps.setString(2,modeleDeReprise.getNom());
	}
	
	public static void ajoutLieu(Connection conn, PreparedStatement ps, Lieu lieu) throws SQLException{
		ps = conn.prepareStatement(ajoutLieu);
		ps.setInt(1, lieu.getId());
		ps.setString(2,lieu.getNom());
	}
	
	public static void ajoutMonitrice(Connection conn, PreparedStatement ps, Monitrice monitrice) throws SQLException{
		ps = conn.prepareStatement(ajoutMonitrice);
		ps.setInt(1, monitrice.getId());
		ps.setString(2,monitrice.getNom());
	}
	
	public static void ajoutCreneau(Connection conn, PreparedStatement ps, Creneau creneau) throws SQLException{
		ps = conn.prepareStatement(ajoutCreneau);
		ps.setInt(1,creneau.getId());
		ps.setInt(2,creneau.getHeureDebut());
		ps.setDate(3,creneau.getDate());
		ps.setInt(4,creneau.getDuree());
		ps.setInt(5,creneau.getIdMonitrice());
	}
	
	public static void ajoutJourFerie(Connection conn, PreparedStatement ps, JourFerie jourFerie) throws SQLException{
		ps = conn.prepareStatement(ajoutJourFerie);
		ps.setDate(1,jourFerie.getDate());
	}
	
	public static void ajoutVacances(Connection conn, PreparedStatement ps, Vacances vacances) throws SQLException{
		ps = conn.prepareStatement(ajoutVacances);
		ps.setString(1,vacances.getNom());
		ps.setDate(2,vacances.getDateDebut());
		ps.setDate(3,vacances.getDateFin());
	}
	
	public static void ajoutTreveHivernale(Connection conn, PreparedStatement ps, TreveHivernale treveHivernale) throws SQLException{
		ps = conn.prepareStatement(ajoutTreveHivernale);
		ps.setDate(1,treveHivernale.getDateDebut());
		ps.setDate(2,treveHivernale.getDateFin());
	}
	
	public static void ajoutRepriseMonitrice(Connection conn, PreparedStatement ps, int idMonitrice, int idReprise) throws SQLException{
		ps = conn.prepareStatement(ajoutRepriseMonitrice);
		ps.setInt(1,idMonitrice);
		ps.setInt(2,idReprise);
	}
	
//*******************************************************************************
// Requetes de suppression
//*******************************************************************************
	
	public static void suppressionReprise(Connection conn, PreparedStatement ps, int id) throws SQLException{
		ps = conn.prepareStatement(suppressionReprise);
		ps.setInt(1,id);
	}
	
	public static void suppressionGroupe(Connection conn, PreparedStatement ps, String nom) throws SQLException{
		ps = conn.prepareStatement(suppressionGroupe);
		ps.setString(1, nom);
	}
	
	public static void suppressionModeleDeReprise(Connection conn, PreparedStatement ps, int id) throws SQLException{
		ps = conn.prepareStatement(suppressionModeleDeReprise);
		ps.setInt(1, id);
	}
	
	public static void suppressionLieu(Connection conn, PreparedStatement ps, int id) throws SQLException{
		ps = conn.prepareStatement(suppressionLieu);
		ps.setInt(1, id);
	}
	
	public static void suppressionMonitrice(Connection conn, PreparedStatement ps, int id) throws SQLException{
		ps = conn.prepareStatement(suppressionMonitrice);
		ps.setInt(1, id);
	}
	
	public static void suppressionCreneau(Connection conn, PreparedStatement ps, int id) throws SQLException{
		ps = conn.prepareStatement(suppressionCreneau);
		ps.setInt(1, id);
	}
	
	public static void suppressionJourFerie(Connection conn, PreparedStatement ps, Date date) throws SQLException{
		ps = conn.prepareStatement(suppressionJourFerie);
		ps.setDate(1, date);
	}
	
	public static void suppressionVacances(Connection conn, PreparedStatement ps, String nom) throws SQLException{
		ps = conn.prepareStatement(suppressionVacances);
		ps.setString(1, nom);
	}
	

//*******************************************************************************
//Requetes de modification
//*******************************************************************************
			
	

	
	
	
	
	
}
