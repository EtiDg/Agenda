package agenda.process.object;

import java.sql.Date;
import java.util.Calendar;

public class Creneau {
	
	private int id;
	private String nomMonitrice;
	private Calendar date;
	private int heureDebut;
	private int duree;
	
	
	public Creneau(String nomMonitrice, Date date, int heureDebut, int duree) {
		super();
		this.nomMonitrice = nomMonitrice;
		this.date.setTime(date);
		this.heureDebut = heureDebut;
		this.duree = duree;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNomMonitrice() {
		return nomMonitrice;
	}
	
	public Date getDate() {
		return new java.sql.Date(date.getTimeInMillis());
	}
	
	public int getHeureDebut() {
		return heureDebut;
	}

	public int getDuree() {
		return duree;
	}
	
	
	
}
