package agenda.process.object;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TreveHivernale {

	private LocalDate dateDebut;
	private LocalDate dateFin;
	
	public TreveHivernale(LocalDate dateDebut, LocalDate dateFin){
		this.dateDebut = dateDebut;
		this.dateFin = dateFin;
	}

	public LocalDate getDateDebut() {
		return dateDebut;
	}

	public LocalDate getDateFin() {
		return dateFin;
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
		return formatter.format(dateDebut) + " - " + formatter.format(dateFin);
	}
	
}
