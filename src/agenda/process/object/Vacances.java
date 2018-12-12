package agenda.process.object;

import java.sql.Date;
import java.util.Calendar;

public class Vacances {
	
	private String nom;
	private Calendar dateDebut;
	private Calendar dateFin;
	
	public Vacances(String nom, Date dateDebut, Date dateFin){
		this.nom = nom;
		this.dateDebut.setTime(dateDebut);
		this.dateFin.setTime(dateFin); 
	}

	public String getNom() {
		return nom;
	}

	public Date getDateDebut() {
		return new java.sql.Date(dateDebut.getTimeInMillis());
	}

	public Date getDateFin() {
		return new java.sql.Date(dateFin.getTimeInMillis());
	}
}
