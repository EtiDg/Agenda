package agenda.process.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import agenda.process.object.*;

public class Queries {
	
	// insertions
	private static String ajoutReprise = 
			"INSERT INTO Reprise(id, nom, heureDebut, duree, nomLieu, date) \n"
			+ "VALUES (?,?,?,?,?,?);";
	private static String ajoutGroupe =
			"INSERT INTO Groupe(nom, isVacances, isTreve, idMR) \n"
			+ "VALUES (?,?,?,?);";
	private static String ajoutModeleDeReprise =
			"INSERT INTO ModeleDeReprise(id, nom) \n"
			+ "VALUES (?,?);";
	private static String ajoutLieu =
			"INSERT INTO Lieu(nom) \n"
			+ "VALUES (?);";
	private static String ajoutMonitrice =
			"INSERT INTO Monitrice(nom) \n"
			+ "VALUES (?);";
	private static String ajoutCreneau =
			"INSERT INTO Creneau(id, heureDebut, duree, date, nomMonitrice) \n"
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
	

	public void ajoutReprises(Connection conn, PreparedStatement ps, ArrayList<Reprise> reprises) throws SQLException{
		ps = conn.prepareStatement(ajoutReprise);
		for (int i = 0; i < reprises.size(); ++i){
			ps.setInt(1,reprises.get(i).getId());
			ps.setString(2,reprises.get(i).getNom());
			ps.setInt(3,reprises.get(i).getHeureDebut());
			ps.setInt(4,reprises.get(i).getDuree());
			ps.setString(5,reprises.get(i).getNomLieu());
			ps.setDate(6,reprises.get(i).getDate());
			ps.addBatch();
		}
	}
	
	public void ajoutGroupe(Connection conn, PreparedStatement ps, Groupe groupe) throws SQLException{
		ps = conn.prepareStatement(ajoutGroupe);
		ps.setString(1,groupe.getNom());
		ps.setBoolean(2,groupe.getIsVacances());
		ps.setBoolean(3,groupe.getIsTreveHivernale());
	}
	
	public void ajoutModeleDeReprise(Connection conn, PreparedStatement ps, ModeleDeReprise modeleDeReprise) throws SQLException{
		ps = conn.prepareStatement(ajoutModeleDeReprise);
		ps.setInt(1,modeleDeReprise.getId());
		ps.setString(2,modeleDeReprise.getNom());
	}
	
	public void ajoutLieu(Connection conn, PreparedStatement ps, Lieu lieu) throws SQLException{
		ps = conn.prepareStatement(ajoutLieu);
		ps.setString(1,lieu.getNom());
	}
	
	public void ajoutMonitrice(Connection conn, PreparedStatement ps, Monitrice monitrice) throws SQLException{
		ps = conn.prepareStatement(ajoutMonitrice);
		ps.setString(1,monitrice.getNom());
	}
	
	public void ajoutCreneau(Connection conn, PreparedStatement ps, Creneau creneau) throws SQLException{
		ps = conn.prepareStatement(ajoutCreneau);
		ps.setInt(1,creneau.getId());
		ps.setInt(2,creneau.getHeureDebut());
		ps.setDate(3,creneau.getDate());
		ps.setInt(4,creneau.getDuree());
		ps.setString(5,creneau.getNomMonitrice());
	}
	
	public void ajoutJourFerie(Connection conn, PreparedStatement ps, JourFerie jourFerie) throws SQLException{
		ps = conn.prepareStatement(ajoutJourFerie);
		ps.setDate(1,jourFerie.getDate());
	}
	
	public void ajoutVacances(Connection conn, PreparedStatement ps, Vacances vacances) throws SQLException{
		ps = conn.prepareStatement(ajoutVacances);
		ps.setString(1,vacances.getNom());
		ps.setDate(2,vacances.getDateDebut());
		ps.setDate(3,vacances.getDateFin());
	}
	
	public void ajoutTreveHivernale(Connection conn, PreparedStatement ps, TreveHivernale treveHivernale) throws SQLException{
		ps = conn.prepareStatement(ajoutTreveHivernale);
		ps.setDate(1,treveHivernale.getDateDebut());
		ps.setDate(2,treveHivernale.getDateFin());
	}
	
	
	
}
