package agenda.process.object;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Vacances {
	
	private String nom;
	private LocalDate dateDebut;
	private LocalDate dateFin;
	
	public Vacances(String nom, LocalDate dateDebut, LocalDate dateFin){
		this.nom = nom;
		this.dateDebut = dateDebut;
		this.dateFin = dateFin; 
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}
	
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}


	public LocalDate getDateFin() {
		return dateFin;
	}
	
	public void setDateFin(LocalDate dateFin) {
		this.dateFin = dateFin;
	}
	
	public Date getSQLDateDebut() {
		return Date.valueOf(dateDebut);
	}

	public Date getSQLDateFin() {
		return Date.valueOf(dateFin);
	}
	
	@Override
	public String toString(){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		return nom + " : " + formatter.format(dateDebut) + " - " + formatter.format(dateFin);
	}
}
