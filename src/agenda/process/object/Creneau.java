package agenda.process.object;

import java.sql.Date;
import java.time.LocalDate;

public class Creneau {
	
	private long id;
	private long idMonitrice;
	private LocalDate date;
	private int heureDebut;
	private int heureFin;
	
	
	public Creneau(long idMonitrice, LocalDate date, int heureDebut, int heureFin) {
		this.idMonitrice = idMonitrice;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
		id = IdGenerator.getId();
	}
	
	public Creneau(long id, long idMonitrice, LocalDate date, int heureDebut, int heureFin) {
		this.id = id;
		this.idMonitrice = idMonitrice;
		this.date = date;
		this.heureDebut = heureDebut;
		this.heureFin = heureFin;
	}
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId() {
		return id;
	}
	
	public long getIdMonitrice() {
		return idMonitrice;
	}
	
	public LocalDate getDate(){
		return date;
	}
	public Date getSQLDate() {
		return Date.valueOf(date);
	}
	
	public int getHeureDebut() {
		return heureDebut;
	}

	public int getHeureFin() {
		return heureFin;
	}
	
	
	
}
