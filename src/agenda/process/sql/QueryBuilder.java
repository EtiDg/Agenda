package agenda.process.sql;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

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
			"INSERT INTO RepriseMonitrice(idReprise, idMonitrice) \n"
			+ "VALUES (?,?);";
	
	// suppressions
	private static String suppressionRepriseMonitrice = 
			"DELETE FROM RepriseMonitrice"
			+ "WHERE idMonitrice = ?";
	private static String suppressionReprise = 
			"DELETE FROM Reprise \n"
			+ "WHERE id = ?";
	private static String suppressionReprisesDeMonitrice = 
			"DELETE FROM Reprise"
			+ "WHERE id IN ("
			+ "SELECT idReprise FROM RepriseMonitrice"
			+ "WHERE idMonitrice = ?)";
	private static String suppressionReprisesDeMonitriceUnique =
			"DELETE FROM Reprise"
			+ "WHERE id IN ("
			+ "SELECT idReprise FROM RepriseMonitrice"
		    + "GROUP BY idReprise"
		    + "HAVING count(idMonitrice) = 1)"
			+ "AND id IN ("
			+ "SELECT id FROM Monitrice";
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

	// modification
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
			+ "WHERE id = ?";
	private static String modificationMonitrice = 
			"UPDATE Monitrice \n"
			+ "SET nom = ?\n"
			+ "WHERE id = ?";
	private static String modificationCreneau = 
			"UPDATE Creneau \n"
			+ "SET heureDebut = ?, duree = ?, date = ? \n"
			+ "WHERE id = ?";
	private static String modificationVacances = 
			"UPDATE Vacances\n"
			+ "SET dateDebut = ?, dateFin = ?\n"
			+ "WHERE nom = ?";
	private static String modificationTreveHivernale =
			"UPDATE Vacances\n"
			+ "SET dateDebut = ?, dateFin = ?\n";
	
	// remontee d'infos
	private static String selectListeGroupe =
			"SELECT Groupe.nom , Groupe.isVacances, Groupe.isTreve, ModeleDeReprise.id, ModeleDeReprise.nom FROM Groupe \n"
			+ "JOIN ModeleDeReprise ON Groupe.idMR = ModeleDeReprise.id";
	private static String selectListeMRParticulier =
			"SELECT ModeleDeReprise.id, ModeleDeReprise.nom FROM ModeleDeReprise \n"
			+ "LEFT JOIN Groupe ON Groupe.idMR = ModeleDeReprise.id \n"
			+ "WHERE Groupe.idMR IS NULL";
	private static String selectListeMonitrice = 
			"SELECT id, nom  FROM Monitrice";
	private static String selectMonitricesDeReprise =
			"SELECT Monitrice.id, Monitrice.nom  FROM Monitrice"
			+ "JOIN RepriseMonitrice ON Monitrice.id = RepriseMonitrice.idMonitrice"
			+ "WHERE RepriseMonitrice.idReprise = ?";
	private static String selectListeLieu = 
			"SELECT id, nom  FROM Monitrice"; 
	private static String selectListeVacances =
			"SELECT nom, dateDebut, dateFin FROM Vacances";
	private static String selectCreneaux =
			"SELECT id, date, heureDebut, duree FROM Creneau"
			+ "WHERE idMonitrice = ?";
	private static String selectListeJoursFeries =
			"SELECT date FROM JoursFeries";
	private static String selectTreveHivernale =
			"SELECT  dateDebut, dateFin FROM TreveHivernale";
	private static String selectReprisesDeModele = 
			"SELECT Reprise.id, Reprise.nom, Reprise.date, Reprise.heureDebut, Reprise.duree, Lieu.id, Lieu.nom FROM Reprise"
			+ "JOIN Lieu ON Lieu.id = Reprise.idLieu"
			+ "WHERE Reprise.idMR = ?";
	private static String selectReprisesDeSemaine = 
			"SELECT Reprise.id, Reprise.nom, Reprise.date, Reprise.heureDebut, Reprise.duree, Reprise.idMR, Lieu.id, Lieu.nom  FROM Reprise"
			+ "JOIN Lieu ON Lieu.id = Reprise.idLieu"
			+ "WHERE strftime('%Y', Reprise.date) = ?"
			+ "AND strftime('%W', Reprise.date) = ?"
			+ "GROUP BY Reprise.id";

	
	// tests
	private static String testCreneauMonitrice =
			"SELECT id FROM Creneau \n"
			+ "WHERE idMonitrice = ?"
			+ "AND date = ?"
			+ "AND heureDebut < ?\n"
			+ "AND heureDebut + duree > ?";
	private static String testConflitLieu =
			"SELECT id FROM Reprise"
			+ "WHERE idLieu = ?"
			+ "AND date = ?"
			+ "AND heureDebut < ?\n"
			+ "AND heureDebut + duree > ?\n";
	private static String testConflitMonitrice = 
			"SELECT Reprise.id FROM Reprise \n"
			+ "JOIN RepriseMonitrice ON Reprise.id = RepriseMonitrice.idMonitrice"
			+ "WHERE Monitrice.idMonitrice = ?"
			+ "AND Reprise.date = ?"
			+ "AND Reprise.heureDebut < ?\n"
			+ "AND Reprise.heureDebut + Reprise.duree > ?\n";

	

//*******************************************************************************
// Requetes d'ajout	
//*******************************************************************************	
	
	public static PreparedStatement ajoutReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutReprise);
	}
	
	public static PreparedStatement ajoutGroupe(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutGroupe);
	}
	
	public static PreparedStatement ajoutModeleDeReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutModeleDeReprise);

	}
	
	public static PreparedStatement ajoutLieu(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutLieu);
	}
	
	public static PreparedStatement ajoutMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutMonitrice);
	}
	
	public static PreparedStatement ajoutCreneau(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutCreneau);
	}
	
	public static PreparedStatement ajoutJourFerie(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutJourFerie);
	}
	
	public static PreparedStatement ajoutVacances(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutVacances);
	}
	
	public static PreparedStatement ajoutTreveHivernale(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutTreveHivernale);
	}
	
	public static PreparedStatement ajoutRepriseMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(ajoutRepriseMonitrice);
	}
	
//*******************************************************************************
// Requetes de suppression
//*******************************************************************************
	
	public static PreparedStatement supprimerRepriseMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionRepriseMonitrice);
	}
	
	public static PreparedStatement suppressionReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionReprise);
	}
	
	public static PreparedStatement supprimerReprisesDeMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionReprisesDeMonitrice);
	}
	
	public static PreparedStatement supprimerReprisesDeMonitriceUnique(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionReprisesDeMonitriceUnique);
	}
	
	public static PreparedStatement suppressionModeleDeReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionModeleDeReprise);
	}
	
	public static PreparedStatement suppressionLieu(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionLieu);
	}
	
	public static PreparedStatement suppressionMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionMonitrice);
	}
	
	public static PreparedStatement suppressionCreneau(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionCreneau);
	}
	
	public static PreparedStatement suppressionJourFerie(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionJourFerie);
	}
	
	public static PreparedStatement suppressionVacances(Connection conn) throws SQLException{
		return conn.prepareStatement(suppressionVacances);
	}
	

//*******************************************************************************
//Requetes de modification
//*******************************************************************************
	
	public static PreparedStatement modificationReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationReprise);
	}
	
	public static PreparedStatement modificationGroupe(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationGroupe);
	}
	
	public static PreparedStatement modificationModeleDeReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationModeleDeReprise);
	}
	

	public static PreparedStatement modificationLieu(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationLieu);
	}
	
	public static PreparedStatement modificationMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationMonitrice);
	}
	
	public static PreparedStatement modificationCreneau(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationCreneau);
	}
	
	public static PreparedStatement modificationVacances(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationVacances);
	}
	
	public static PreparedStatement modificationTreveHivernale(Connection conn) throws SQLException{
		return conn.prepareStatement(modificationTreveHivernale);
	}
	
	

//*******************************************************************************
//Requetes de remontee d'infos
//*******************************************************************************
	public static PreparedStatement selectListeGroupe(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeGroupe);
	}
	
	public static PreparedStatement selectListeMRParticulier(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeMRParticulier);
	}
	
	public static PreparedStatement selectListeMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeMonitrice);
	}
	
	public static PreparedStatement selectMonitricesDeReprise(Connection conn) throws SQLException{
		return conn.prepareStatement(selectMonitricesDeReprise);
	}
	
	public static PreparedStatement selectListeLieu(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeLieu);
	}
	
	public static PreparedStatement selectCreneaux(Connection conn) throws SQLException{
		return conn.prepareStatement(selectCreneaux);
	}
	
	public static PreparedStatement selectListeVacances(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeVacances);
	}
	
	public static PreparedStatement selectListeJoursFeries(Connection conn) throws SQLException{
		return conn.prepareStatement(selectListeJoursFeries);
	}
	
	public static PreparedStatement selectTreveHivernale(Connection conn) throws SQLException{
		return conn.prepareStatement(selectTreveHivernale);
	}
	
	public static PreparedStatement selectReprisesDeModele(Connection conn) throws SQLException{
		return conn.prepareStatement(selectReprisesDeModele);
	}
	
	public static PreparedStatement selectReprisesDeSemaine(Connection conn) throws SQLException{
		return conn.prepareStatement(selectReprisesDeSemaine);
	}
	
	
//*******************************************************************************
//Requetes de test
//*******************************************************************************
	public static PreparedStatement testCreneauMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(testCreneauMonitrice);
	}
	
	public static PreparedStatement testConflitLieu(Connection conn) throws SQLException{
		return conn.prepareStatement(testConflitLieu);
	}
	
	public static PreparedStatement testConflitMonitrice(Connection conn) throws SQLException{
		return conn.prepareStatement(testConflitMonitrice);
	}
	
	
}
